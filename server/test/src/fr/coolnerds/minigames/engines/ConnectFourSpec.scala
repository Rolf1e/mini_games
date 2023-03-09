package fr.coolnerds.minigames.engines

import fr.coolnerds.minigames.common.{CheaterPlayer, TestPlayer}
import fr.coolnerds.minigames.domain.impl.connectfour.*
import fr.coolnerds.minigames.domain.impl.connectfour.ConnectFourConstants.Cell
import utest.{TestSuite, Tests, assert, test}

object ConnectFourSpec extends TestSuite {

  val tests: Tests = Tests {

    test("Build empty board") {
      val yellow = TestPlayer[Cell](Seq.empty, Yellow.ponValue)
      val red = TestPlayer[Cell](Seq.empty, Red.ponValue)
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
      val yellow = TestPlayer[Cell](Seq(AddPon(Yellow, 0)), Yellow.ponValue)
      val red = TestPlayer[Cell](Seq.empty, Red.ponValue)
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
      val yellow = TestPlayer[Cell](Seq(AddPon(Yellow, 1)), Yellow.ponValue)
      val red = TestPlayer[Cell](Seq(AddPon(Red, 1)), Red.ponValue)
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
      val yellow = CheaterPlayer[Cell](Seq(AddPon(Yellow, 0), AddPon(Yellow, 1)), Yellow.ponValue)
      val red = TestPlayer[Cell](Seq(), Red.ponValue)
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
