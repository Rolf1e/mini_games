// build.sc
import mill._, scalalib._

object server extends ScalaModule {
  def scalaVersion = "2.13.8"

  object test extends Tests {
    override def ivyDeps = Agg(ivy"com.lihaoyi::utest:0.8.1")

    override def testFramework = "utest.runner.Framework"
  }
}
