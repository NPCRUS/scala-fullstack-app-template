package logic

import rpc.{TestApi, TestInput}
import io.getquill._
import db.ctx
import ctx._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object TestApiImpl extends TestApi {
  case class Test(id: Long, name: String)

  override def createFlat(entity: TestInput): Future[Int] =
    Future.successful(entity.id + 1)

  override def testCount: Future[Int] = Future {
    ctx.run(quote(query[Test].size)).toInt
  }
}
