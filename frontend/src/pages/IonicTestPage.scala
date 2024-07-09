package pages

import com.raquo.laminar.api.L.{*, given}
import com.raquo.laminar.nodes.ReactiveHtmlElement
import org.scalajs.dom
import org.scalajs.dom.HTMLDivElement
import pages.IonicTestPage.counter

object IonicTestPage {

  def ButtonWithCounter(counter: Var[Int]) =
    ionic.Button(
      _.onClick.mapTo(counter.now() + 1) --> counter,
      _.expand := "block",
      _.slots.inner(
        span(child.text <-- counter.signal.map(int => s"Hello: $int"))
      ),
    )


  val counter = Var(0)

  def render: ReactiveHtmlElement[HTMLDivElement] = div(
    ButtonWithCounter(counter = counter),
    p("counter:", b(child.text <-- counter.signal)),
  )
}
