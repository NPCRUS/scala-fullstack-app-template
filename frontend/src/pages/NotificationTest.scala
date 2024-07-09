package pages

import scalajs.js
import com.raquo.laminar.api.L.{*, given}
import org.scalajs.dom.{MouseEvent, WebSocket}
import typings.capacitorDialog.mod.Dialog
import typings.capacitorDialog.distEsmDefinitionsMod.*
import typings.capacitorLocalNotifications.distEsmDefinitionsMod.*
import typings.capacitorLocalNotifications.mod.*

object NotificationTest {

  var inc = 1
  private val hasPermissions = EventStream.fromJsPromise(LocalNotifications.requestPermissions())
  private val websocketConnection = new WebSocket("https://echo.websocket.org/")
  websocketConnection.onopen = _ => {
    // connection opened
  }
  websocketConnection.onmessage = ev => {
    println(s"message type: ${ev.`type`}")
    println(s"received: ${ev.data}")
    // Dialog.alert(AlertOptions(ev.data.asInstanceOf[String]))
    LocalNotifications.schedule(
      ScheduleOptions(js.Array(LocalNotificationSchema.apply(ev.data.asInstanceOf[String], inc, "new notification")))
    )
    inc = inc + 1
  }

  hasPermissions.tapEach(println)

  private val inputStr = Var("")
  private val onSendNotificationObserver = Observer[MouseEvent](onNext => {
    if(websocketConnection.readyState == WebSocket.OPEN) {
      websocketConnection.send(inputStr.now())
    }
  })

  def render = div(
    input(
      value <-- inputStr,
      onInput.mapToValue --> inputStr
    ),
    button(
      "send notification",
      onClick --> onSendNotificationObserver
    )
  )

}
