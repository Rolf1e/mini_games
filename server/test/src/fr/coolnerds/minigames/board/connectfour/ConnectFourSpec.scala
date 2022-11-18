package fr.coolnerds.minigames.board.connectfour

import fr.coolnerds.minigames.board.connectfour.Common.fail
import fr.coolnerds.minigames.boards.Point2D
import fr.coolnerds.minigames.boards.connectfour.ConnectFourConstants.{Case, redPon, yellowPon}
import fr.coolnerds.minigames.boards.connectfour._
import utest._

object ConnectFourSpec extends TestSuite {

  val tests: Tests = Tests {

    test("Build empty board") {
      val yellow = TestPlayer[Case, ConnectFourAction, ConnectFourState](Seq.empty)
      val red = TestPlayer[Case, ConnectFourAction, ConnectFourState](Seq.empty)
      val game = ConnectFour(yellow, red)
      val expected = """ |ConnectFour(E: 0 Y: 1 R: 2)
                       ||0|0|0|0|0|0|0|
                       ||0|0|0|0|0|0|0|
                       ||0|0|0|0|0|0|0|
                       ||0|0|0|0|0|0|0|
                       ||0|0|0|0|0|0|0|
                       ||0|0|0|0|0|0|0|
                       |""".stripMargin
      val actual = game.draw()
      assert(expected == actual)
    }

    test("Yellow plays col 0") {
      val yellow = TestPlayer[Case, ConnectFourAction, ConnectFourState](
        Seq(
          AddPonYellow(0)
        ),
        yellowPon
      )
      val red = TestPlayer[Case, ConnectFourAction, ConnectFourState](Seq.empty)
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
      val actual = game.draw()
      assert(expected == actual)
    }

    test("Yellow and Red play") {
      val yellow =
        TestPlayer[Case, ConnectFourAction, ConnectFourState](Seq(AddPonYellow(0)), yellowPon)
      val red = TestPlayer[Case, ConnectFourAction, ConnectFourState](Seq(AddPonRed(0)), redPon)
      val game = ConnectFour(yellow, red)
      game.askAndPlayAction()

      println(game.draw())

      game.askAndPlayAction()

      val expected = """ |ConnectFour(E: 0 Y: 1 R: 2)
                       ||0|0|0|0|0|0|0|
                       ||0|0|0|0|0|0|0|
                       ||0|0|0|0|0|0|0|
                       ||0|0|0|0|0|0|0|
                       ||2|0|0|0|0|0|0|
                       ||1|0|0|0|0|0|0|
                       |""".stripMargin
      val actual = game.draw()
      assert(expected == actual)
    }

    test("Yellow is a cheater, he is trying to play two times !") {
      val yellow =
        CheaterPlayer[Case, ConnectFourAction, ConnectFourState](
          Seq(AddPonYellow(0), AddPonYellow(1)),
          yellowPon
        )
      val red = TestPlayer[Case, ConnectFourAction, ConnectFourState](Seq(), redPon)
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
      val actual = game.draw()
      assert(expected == actual)

    }

  }
}
