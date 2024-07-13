package pages

import scalajs.js
import com.raquo.laminar.api.L.{*, given}
import org.scalajs.dom.{MouseEvent, WebSocket}
import typings.capacitorDialog.mod.Dialog
import typings.capacitorDialog.distEsmDefinitionsMod.*
import typings.capacitorPushNotifications.mod.PushNotifications
import typings.capacitorPushNotifications.distEsmDefinitionsMod.Channel
import typings.capacitorPushNotifications.capacitorPushNotificationsStrings as cpns
import typings.std.{PermissionState, stdStrings}

import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object NotificationTest {

  var inc = 1

  val setupFuture = for {
    permissions <- PushNotifications.checkPermissions().toFuture
    _ <- permissions.receive.asInstanceOf[String] match
      case "denied" =>
        println("permissions denied")
        Future.successful(())
      case "prompt" =>
        PushNotifications.requestPermissions().toFuture.map { value =>
          println(s"permissions: ${value.receive}")
        }.map(_ => ())
      case "granted" =>
        println("have permissions")
        Future.successful(())
    _ <- PushNotifications.createChannel(Channel("1337", "test")).toFuture
    _ <- PushNotifications.register().toFuture
  } yield ()

  PushNotifications.addListener_registration(cpns.registration, token => {
    // Use this token in FCM messaging console to send test message
    println(s"token: ${token.value}")
  })
  PushNotifications.addListener_registrationError(cpns.registrationError, error => {
    println(s"error: ${error.error}")
  })
  PushNotifications.addListener_pushNotificationReceived(cpns.pushNotificationReceived, notification => {
    println(s"notification received: ${notification.title}: ${notification.body}")
    println(s"channel: ${notification.notification}")
  })

  private val inputStr = Var("")
  private val onSendNotificationObserver = Observer[MouseEvent](onNext => {
    ???
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
