package rpc

import io.circe.Codec

import scala.concurrent.Future

case class TestInput(id: Int, title: String) derives Codec

trait TestApi {
  def createFlat(entity: TestInput): Future[Int]

  def testCount: Future[Int]
}
