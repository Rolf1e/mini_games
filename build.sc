// build.sc
import mill._, scalalib._, scalafmt._

object server extends ScalaModule with ScalafmtModule {
  val globalScalaVersion = "2.13.8"
  def scalaVersion = globalScalaVersion

  object core extends ScalaModule with ScalafmtModule {
    def scalaVersion = globalScalaVersion
  }

  object engine_connectfour extends ScalaModule with ScalafmtModule {
    def scalaVersion = globalScalaVersion
    override def mainClass = Some("connectfour.Main")
    override def moduleDeps = Seq(core)
  }

  object test extends Tests {
    override def ivyDeps = Agg(ivy"com.lihaoyi::utest:0.8.1")

    override def moduleDeps = Seq(core)
    override def testFramework = "utest.runner.Framework"
  }

}
