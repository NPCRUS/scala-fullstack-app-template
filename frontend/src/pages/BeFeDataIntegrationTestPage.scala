package pages

import com.raquo.laminar.api.L.{*, given}
import models.TestEntity

object BeFeDataIntegrationTestPage {

  val test = TestEntity(1)
  
  def render = div(
    test.toString
  )
}
