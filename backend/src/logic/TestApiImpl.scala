package logic

import rpc.{TestApi, TestInput}

import scala.concurrent.Future

object TestApiImpl extends TestApi {
  override def createFlat(entity: TestInput): Future[Int] =
    Future.successful(entity.id + 1)
}
