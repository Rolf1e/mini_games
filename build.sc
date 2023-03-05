// build.sc
import mill._, scalalib._, scalafmt._

object server extends ScalaModule with ScalafmtModule {
  val globalScalaVersion = "3.2.1"
  def scalaVersion = globalScalaVersion

  object core extends ScalaModule with ScalafmtModule {
    def scalaVersion = globalScalaVersion
    override def ivyDeps = Agg(
      ivy"org.postgresql:postgresql:42.5.1",
    )
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
