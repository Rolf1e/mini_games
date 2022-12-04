package fr.coolnerds.minigames.engine

import fr.coolnerds.minigames.common.{CheaterPlayer, TestPlayer}
import fr.coolnerds.minigames.engine.ConnectFourConstants.{Case, redPon, yellowPon}
import utest.{TestSuite, Tests, assert, test}

object ConnectFourSpec extends TestSuite {

  val tests: Tests = Tests {

    test("Build empty board") {
      val yellow = TestPlayer[Case](Seq.empty)
      val red = TestPlayer[Case](Seq.empty)
      val game = ConnectFour(yellow, red)
      val expected = """ |ConnectFour(E: 0 Y: 1 R: 2)
                       ||0|0|0|0|0|0|0|
                       ||0|0|0|0|0|0|0|
                       ||0|0|0|0|0|0|0|
                       ||0|0|0|0|0|0|0|
                       ||0|0|0|0|0|0|0|
                       ||0|0|0|0|0|0|0|
                       |""".stripMargin
      val actual = game.draw
      assert(expected == actual)
    }

    test("Yellow plays col 0") {
      val yellow = TestPlayer[Case](Seq(AddPonYellow(0)), yellowPon)
      val red = TestPlayer[Case](Seq.empty)
      val game = ConnectFour(yellow, red)
      game.askAndPlayAction()
      val expected = """ |ConnectFour(E: 0 Y: 1 R: 2)
                       ||0|0|0|0|0|0|0|
                       ||0|0|0|0|0|0|0|
                       ||0|0|0|0|0|0|0|
                       ||0|0|0|0|0|0|0|
                       ||0|0|0|0|0|0|0|
                       ||1|0|0|0|0|0|0|
                       |""".stripMargin
      val actual = game.draw
      assert(expected == actual)
    }

    test("Yellow and Red play") {
      val yellow = TestPlayer[Case](Seq(AddPonYellow(1)), yellowPon)
      val red = TestPlayer[Case](Seq(AddPonRed(1)), redPon)
      val game = ConnectFour(yellow, red)
      game.askAndPlayAction()

      println(game.draw)

      game.askAndPlayAction()

      val expected = """ |ConnectFour(E: 0 Y: 1 R: 2)
                       ||0|0|0|0|0|0|0|
                       ||0|0|0|0|0|0|0|
                       ||0|0|0|0|0|0|0|
                       ||0|0|0|0|0|0|0|
                       ||0|2|0|0|0|0|0|
                       ||0|1|0|0|0|0|0|
                       |""".stripMargin
      val actual = game.draw
      assert(expected == actual)
    }

    test("Yellow is a cheater, he is trying to play two times !") {
      val yellow = CheaterPlayer[Case](Seq(AddPonYellow(0), AddPonYellow(1)), yellowPon)
      val red = TestPlayer[Case](Seq(), redPon)
      val game = ConnectFour(yellow, red)

      game.askAndPlayAction()

      val expected = """ |ConnectFour(E: 0 Y: 1 R: 2)
                       ||0|0|0|0|0|0|0|
                       ||0|0|0|0|0|0|0|
                       ||0|0|0|0|0|0|0|
                       ||0|0|0|0|0|0|0|
                       ||0|0|0|0|0|0|0|
                       ||0|0|0|0|0|0|0|
                       |""".stripMargin
      val actual = game.draw
      assert(expected == actual)
    }

  }
}
