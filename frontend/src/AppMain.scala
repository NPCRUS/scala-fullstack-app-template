import org.scalajs.dom
import com.raquo.laminar.api.L.{*, given}
import com.raquo.laminar.tags.HtmlTag
import com.raquo.waypoint.SplitRender
import pages.{BeFeDataIntegrationTestPage, IonicTestPage}
import typings.capacitorDialog.distEsmDefinitionsMod.AlertOptions
import typings.capacitorDialog.mod.Dialog
import typings.capacitorLocalNotifications.distEsmDefinitionsMod.{LocalNotificationSchema, ScheduleOptions, ScheduleResult}
import typings.capacitorLocalNotifications.mod.*
import Router.*

import scala.scalajs.js.annotation.JSExportTopLevel
import scalajs.js

object AppMain extends App {

  val appContainer: dom.Element = dom.document.body

  val mainPage = div(
    h1("Main Page"),
    button(
      onClick --> (_ => router.pushState(Page.BeFeDataTest)),
      "BE-FE data test"
    ),
    button(
      onClick --> (_ => router.pushState(Page.IonicTest)),
      "ionic test"
    )
  )

  val splitter = SplitRender[Page, HtmlElement](router.currentPageSignal)
    .collectStatic(Page.Main)(mainPage)
    .collectStatic(Page.BeFeDataTest)(BeFeDataIntegrationTestPage.render)
    .collectStatic(Page.IonicTest)(IonicTestPage.render)

  val appElement: Div = div(
    display := "flex",
    flexDirection := "column",
    child <-- splitter.signal
  )

  @JSExportTopLevel("main")
  def main(): Unit = {
    renderOnDomContentLoaded(dom.document.body, appElement)
//    Dialog.alert(AlertOptions(
//      message = "alert successful"
//    ))
  }
}