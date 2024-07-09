package ionic

import org.scalajs.dom
import com.raquo.laminar.api.L.{*, given}
import com.raquo.laminar.codecs.*
import com.raquo.laminar.nodes.ReactiveHtmlElement
import com.raquo.laminar.tags.HtmlTag
import com.raquo.laminar.keys.HtmlAttr

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

// inspiration:
// https://github.com/raquo/Laminar/tree/master/websiteJS/src/main/scala/website/webcomponents/material
// https://github.com/uosis/laminar-web-components/blob/master/material/src/main/scala/material.scala
// https://github.com/sherpal/LaminarSAPUI5Bindings
object Button {

  @js.native
  trait RawElement extends js.Object {

    def expand: String

  }

  @js.native
  @JSImport("@ionic/core/components/ion-button", JSImport.Default)
  object RawImport extends js.Object {}
  RawImport

  type Ref         = dom.html.Element & RawElement
  type El          = ReactiveHtmlElement[Ref]
  type ModFunction = Button.type => Mod[El]

  private val tag = new HtmlTag[Ref]("ion-button", void = false)

  val expand = new HtmlAttr[String]("expand", StringAsIsCodec)

  val onClick = new EventProp[dom.MouseEvent]("click")

  object slots {
    def inner(el: HtmlElement): HtmlElement = el.amend()
  }

  def apply(mods: ModFunction*): El = {
    tag(mods.map(_(Button))*)
  }

}
