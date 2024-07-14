import org.scalajs.dom
import com.raquo.laminar.api.L.{*, given}
import com.raquo.laminar.tags.HtmlTag
import com.raquo.waypoint.SplitRender
import pages.*
import Router.*
import org.scalajs.dom.{MessageEvent, WebSocket}
import typings.capacitorDialog.distEsmDefinitionsMod.AlertOptions
import typings.capacitorDialog.mod.Dialog
import typings.capacitorApp.mod.App as CapacitorApp
import typings.capacitorApp.capacitorAppStrings.backButton

import scala.scalajs.js.Object
import scala.scalajs.js.annotation.JSExportTopLevel
import scalajs.js

object AppMain extends App {

  val appContainer: dom.Element = dom.document.body

  val mainPage = div(
    h1("Main Page"),
    div(
      display := "flex",
      flexDirection := "column",
      button(
        padding := "5px",
        margin := "5px",
        onClick --> (_ => router.pushState(Page.BeFeDataTest)),
        "BE-FE data test"
      ),
      button(
        padding := "5px",
        margin := "5px",
        onClick --> (_ => router.pushState(Page.IonicTest)),
        "ionic test"
      ),
      button(
        padding := "5px",
        margin := "5px",
        onClick --> (_ => router.pushState(Page.NotificationTest)),
        "Notification test"
      ),
      button(
        padding := "5px",
        margin := "5px",
        onClick --> (_ => router.pushState(Page.RpcTest)),
        "Rpc test"
      )
    )
  )

  val splitter = SplitRender[Page, HtmlElement](router.currentPageSignal)
    .collectStatic(Page.Main)(mainPage)
    .collectStatic(Page.BeFeDataTest)(BeFeDataIntegrationTestPage.render)
    .collectStatic(Page.IonicTest)(IonicTestPage.render)
    .collectStatic(Page.NotificationTest)(NotificationTest.render)
    .collectStatic(Page.RpcTest)(RpcTest.render)

  val appElement = ionic.App(
    _.slots.inner(
      div(
        display := "flex",
        flexDirection := "column",
        child <-- splitter.signal
      )
    )
  )

  CapacitorApp.addListener_backButton(backButton, _ => dom.window.history.back())

  @JSExportTopLevel("main")
  def main(): Unit = {
    renderOnDomContentLoaded(dom.document.body, appElement)
  }
}