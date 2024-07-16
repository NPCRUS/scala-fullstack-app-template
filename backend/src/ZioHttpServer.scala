import io.circe.{Json, ParsingFailure}
import zio.http.{HandlerAspect, *}
import zio.{*, given}
import io.circe.parser.*
import chameleon.ext.circe.{*, given}
import rpc.TestApi
import sloth.Router
import zio.http.Middleware.{CorsConfig, cors}
import logic.*

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object ZioHttpServer extends ZIOAppDefault {
  val corsConfig = CorsConfig(
    allowedOrigin = _ => Some(Header.AccessControlAllowOrigin.All)
  )

  val rpcRouter = Router[Json, Future].route[TestApi](TestApiImpl)

  val routes =
    Routes(
      Method.GET / Root / "health" -> Handler.text("alive"),
      Method.POST / Root / "rpc" / string("api") / string ("method") ->
        handler { (api: String, method: String, request: Request) =>
          for {
            entityStr <- request.body.asString(Charsets.Utf8)
            json <- ZIO.fromEither(parse(entityStr))
            result <- ZIO.fromFuture { context =>
              rpcRouter(sloth.Request(sloth.Method(api, method), json)) match
                case Left(error) =>
                  Future.successful(Left(error))
                case Right(value) =>
                  value.map(Right.apply)
            }.absolve
          } yield Response.json(result.spaces2)
        }.catchAll {
          case e: ParsingFailure =>
            Handler.badRequest(s"cannot parse json: ${e.message}")
          case e: sloth.ServerFailure.DeserializerError =>
            Handler.badRequest(e.ex.getMessage)
          case e: sloth.ServerFailure.MethodNotFound =>
            handler(Response.notFound(s"method not found: ${e.path}"))
          case e: sloth.ServerFailure.HandlerError =>
            handler(Response.internalServerError(e.ex.getMessage))
          case e: Exception =>
            Handler.internalServerError(e.getMessage)
          case e: Throwable =>
            handler(Response.internalServerError(e.getMessage))
        }
    ) @@ cors(corsConfig)

  def run = Server.serve(routes).provide(
    Server.defaultWith(_.port(3500).logWarningOnFatalError(true))
  )
}
