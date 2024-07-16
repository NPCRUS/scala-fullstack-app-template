import mill._
import mill.scalajslib._
import mill.scalalib._
import $file.scalablytyped
import mill.scalajslib.api.{ModuleKind, ModuleSplitStyle}

object shared extends Module {
    def scalaVersion = "3.4.2"
    def deps = Agg(
        ivy"com.github.cornerman::sloth::0.8.0",
        ivy"io.circe::circe-core::0.14.9",
        ivy"io.circe::circe-parser::0.14.9"
    )

    object jvm extends PlatformScalaModule {
        def scalaVersion = "3.4.2"
        override def ivyDeps = deps
    }

    object js extends PlatformScalaModule with ScalaJSModule {
        def scalaVersion = "3.4.2"
        def scalaJSVersion = "1.16.0"

        override def ivyDeps = deps
    }
}

object backend extends ScalaModule {
    def scalaVersion = "3.4.2"
    override def moduleDeps = Seq(shared.jvm)
    override def ivyDeps = Agg(
        ivy"dev.zio::zio-http::3.0.0-RC7",
        ivy"org.postgresql:postgresql::42.2.8",
        ivy"io.getquill::quill-jdbc::4.8.5",
    )
}

object frontend extends ScalaJSModule {
    def scalaVersion = "3.4.2"
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