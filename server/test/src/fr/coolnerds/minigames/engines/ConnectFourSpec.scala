package fr.coolnerds.minigames.engines

import fr.coolnerds.minigames.common.{CheaterPlayer, TestPlayer}
import fr.coolnerds.minigames.domain.impl.connectfour.{AddPonRed, AddPonYellow, ConnectFourEngine}
import fr.coolnerds.minigames.domain.impl.connectfour.ConnectFourConstants.{Cell, redPon, yellowPon}
import utest.{TestSuite, Tests, assert, test}

object ConnectFourSpec extends TestSuite {

  val tests: Tests = Tests {

    test("Build empty board") {
      val yellow = TestPlayer[Cell](Seq.empty)
      val red = TestPlayer[Cell](Seq.empty)
      val game = ConnectFourEngine(yellow, red)
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
      val yellow = TestPlayer[Cell](Seq(AddPonYellow(0)), yellowPon)
      val red = TestPlayer[Cell](Seq.empty)
      val game = ConnectFourEngine(yellow, red)
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
      val yellow = TestPlayer[Cell](Seq(AddPonYellow(1)), yellowPon)
      val red = TestPlayer[Cell](Seq(AddPonRed(1)), redPon)
      val game = ConnectFourEngine(yellow, red)
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
      val yellow = CheaterPlayer[Cell](Seq(AddPonYellow(0), AddPonYellow(1)), yellowPon)
      val red = TestPlayer[Cell](Seq(), redPon)
      val game = ConnectFourEngine(yellow, red)

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
