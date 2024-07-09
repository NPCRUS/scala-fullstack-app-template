import com.raquo.waypoint.*
import com.raquo.laminar.api.*

object Router {

  sealed trait Page
  object Page {
    case object Main extends Page
    case object IonicTest extends Page
    case object BeFeDataTest extends Page
    case object NotificationTest extends Page
  }
  import Page.*

  private val mainRoute = Route.static(Main, root)
  private val ionicTestRoute = Route.static(IonicTest, root / "ionicTest")
  private val beFeDataIntegrationTestRoute =
    Route.static(BeFeDataTest, root / "beFeDataTest")
  private val notificationTest =
    Route.static(NotificationTest, root / "notificationTest")

  val router = new Router[Page](
    routes = List(mainRoute, ionicTestRoute, beFeDataIntegrationTestRoute, notificationTest),
    getPageTitle = _.toString,
    serializePage = _.toString,
    deserializePage = {
      case "Main" => Main
      case "IonicTest" => IonicTest
      case "BeFeDataTest" => BeFeDataTest
      case "NotificationTest" => NotificationTest
    }
  )(
    popStateEvents = L.windowEvents(_.onPopState),
    owner = L.unsafeWindowOwner
  )
}
