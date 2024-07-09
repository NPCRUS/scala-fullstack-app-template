import mill._
import mill.scalajslib._
import mill.scalalib._
import $file.scalablytyped
import mill.scalajslib.api.{ModuleKind, ModuleSplitStyle}

object shared extends Module {
    def scalaVersion = "3.4.1"

    object jvm extends PlatformScalaModule {
        def scalaVersion = "3.4.1"
    }
    object js extends PlatformScalaModule with ScalaJSModule {
        def scalaVersion = "3.4.1"
        def scalaJSVersion = "1.16.0"
    }
}

object backend extends ScalaModule {
    def scalaVersion = "3.4.1"
    override def moduleDeps = Seq(shared.jvm)
}

object frontend extends ScalaJSModule {
    def scalaVersion = "3.4.1"
    override def moduleDeps = Seq(
        shared.js,
        scalablytyped.`scalablytyped-module`
    )
    def scalaJSVersion: T[String] = "1.16.0"
    override def ivyDeps = Agg(
        ivy"org.scala-js::scalajs-dom::2.8.0",
        ivy"com.raquo::laminar::17.0.0",
        ivy"com.raquo::waypoint::8.0.0"
    )
    override def moduleKind: Target[ModuleKind] = ModuleKind.ESModule

    @deprecated
    def link: Target[PathRef] = T {
        val result = fastLinkJS()
        val indexHtmlPath = resources().head.path / "index.html"
        os.copy(
            indexHtmlPath,
            result.dest.path / "index.html",
            replaceExisting = true
        )
        result.dest
    }
}