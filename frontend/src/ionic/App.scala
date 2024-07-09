package ionic

import org.scalajs.dom
import com.raquo.laminar.api.L.{*, given}
import com.raquo.laminar.codecs.*
import com.raquo.laminar.nodes.ReactiveHtmlElement
import com.raquo.laminar.tags.HtmlTag
import com.raquo.laminar.keys.HtmlAttr

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

object App {

  @js.native
  trait RawElement extends js.Object

  @js.native
  @JSImport("@ionic/core/components/ion-app", JSImport.Default)
  object RawImport extends js.Object {}
  RawImport

  type Ref         = dom.html.Element & RawElement
  type El          = ReactiveHtmlElement[Ref]
  type ModFunction = Button.type => Mod[El]

  private val tag = new HtmlTag[Ref]("ion-app", void = false)

  object slots {
    def inner(el: HtmlElement): HtmlElement = el.amend()
  }

  def apply(mods: ModFunction*): El = {
    tag(mods.map(_(Button))*)
  }

}

