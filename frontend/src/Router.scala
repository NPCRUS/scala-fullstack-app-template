import com.raquo.waypoint.*
import com.raquo.laminar.api.*

object Router {

  sealed trait Page
  object Page {
    case object Main extends Page
    case object IonicTest extends Page
    case object BeFeDataTest extends Page
  }
  import Page.*

  private val mainRoute = Route.static(Main, root)
  private val ionicTestRoute = Route.static(IonicTest, root / "ionicTest")
  private val beFeDataIntegrationTestRoute =
    Route.static(BeFeDataTest, root / "beFeDataTest")

  val router = new Router[Page](
    routes = List(mainRoute, ionicTestRoute, beFeDataIntegrationTestRoute),
    getPageTitle = _.toString,
    serializePage = _.toString,
    deserializePage = {
      case "Main" => Main
      case "IonicTest" => IonicTest
      case "BeFeDataTest" => BeFeDataTest
    }
  )(
    popStateEvents = L.windowEvents(_.onPopState),
    owner = L.unsafeWindowOwner
  )
}
