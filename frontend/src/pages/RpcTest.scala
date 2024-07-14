package pages

import scalajs.js
import org.scalajs.dom
import com.raquo.laminar.api.L.{*, given}
import io.circe.Json
import io.circe.parser.*
import chameleon.ext.circe.{*, given}
import org.scalajs.dom.{Headers, HttpMethod, MouseEvent, RequestInit, RequestMode}
import rpc.{TestApi, TestInput}
import sloth.{ClientCo, LogHandler, Request, RequestTransport}
import sloth.ext.jsdom.client.*

import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object RpcTest {

  object Transport extends RequestTransport[Json, Future] {
    override def apply(request: Request[Json]): Future[Json] =
      dom.fetch(
        s"http://localhost:3500/api/${request.method.traitName}/${request.method.methodName}",
        new RequestInit {
          method = HttpMethod.POST
          body = request.payload.noSpaces
          // headers = Headers(js.Array(js.Array("Access-Control-Allow-Origin", "*")))
          // mode = RequestMode.`no-cors`
        }
      ).toFuture
        .flatMap { response =>
          response.text().toFuture
        }.map { utf8String =>
          parse(utf8String) match {
            case Left(ex) =>
              println(s"cannot parse: ${ex.message}")
              throw ex
            case Right(value) =>
              value
          }
        }
  }
  val client = new ClientCo[Json, Future](Transport, LogHandler.empty[Future])
  val testApi = client.wire[TestApi]

  val inc = Var.apply(0)
  val clickBus = new EventBus[dom.MouseEvent]
  val responseStream = clickBus.events.flatMapMerge(_ => EventStream.fromFuture(testApi.createFlat(TestInput(inc.now(), "hello"))))

  def render = div(
    div(
      "inc: ",
      input(
        `type` := "number",
        value <-- inc.signal.map(_.toString),
        onInput.mapToValue.map(_.toInt) --> inc.writer
      )
    ),
    button(
      "send data",
      onClick --> clickBus.writer
    ),
    div(
      "response: ", span(child.text <-- responseStream)
    )
  )
}
