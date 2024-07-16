import io.getquill._
import db.ctx

object QuillTest {
  import ctx._

  case class Test(id: Long, name: String)

  val id = ctx.run(quote(
    query[Test].insert(_.name -> lift("hello")).returning(_.id)
  ))

  val result = ctx.run(quote(
    query[Test]
  ))

  println(result)
}
