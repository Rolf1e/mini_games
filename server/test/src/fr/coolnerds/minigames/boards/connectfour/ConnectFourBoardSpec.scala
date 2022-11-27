package fr.coolnerds.minigames.boards.connectfour

import fr.coolnerds.minigames.common.Common
import utest.{TestSuite, Tests, assert, test}

import scala.collection.mutable

object ConnectFourBoardSpec extends TestSuite {
  override def tests: Tests = Tests {

    test("Find row from column") {
      val rowLength = 3
      val colLength = 3

      test("Yellow plays in column 0") - {
        val board =
          ConnectFourBoard(mutable.ArrayDeque(0, 0, 0, 0, 0, 0, 0, 0, 0), rowLength, colLength)
        findAndAssertRowResponse(0, 0, board)
      }
      test("Red plays in column 0") - {
        val board =
          ConnectFourBoard(mutable.ArrayDeque(1, 0, 0, 0, 0, 0, 0, 0, 0), rowLength, colLength)
        findAndAssertRowResponse(0, 1, board)
      }
      test("Yellow plays again in column 0") - {
        val board =
          ConnectFourBoard(mutable.ArrayDeque(1, 0, 0, 2, 0, 0, 0, 0, 0), rowLength, colLength)
        findAndAssertRowResponse(0, 2, board)
      }
      test("Red plays in column 1") - {
        val board =
          ConnectFourBoard(mutable.ArrayDeque(1, 0, 0, 2, 0, 0, 1, 0, 0), rowLength, colLength)
        findAndAssertRowResponse(1, 0, board)
      }
      test("Yellow plays in column 1") - {
        val board =
          ConnectFourBoard(mutable.ArrayDeque(1, 2, 0, 2, 0, 0, 1, 0, 0), rowLength, colLength)
        findAndAssertRowResponse(1, 1, board)
      }

      test("Red plays in column 2") - {
        val board =
          ConnectFourBoard(mutable.ArrayDeque(1, 2, 0, 2, 1, 0, 1, 0, 0), rowLength, colLength)
        findAndAssertRowResponse(2, 0, board)
      }

      test("Yellow plays in column 2") - {
        val board =
          ConnectFourBoard(mutable.ArrayDeque(1, 2, 1, 2, 1, 0, 1, 0, 0), rowLength, colLength)
        findAndAssertRowResponse(2, 1, board)
      }
    }
  }

  private def findAndAssertRowResponse(
      col: Int,
      expectedRow: Int,
      board: ConnectFourBoard
  ): Unit = {
    board.findRow(col) match {
      case Some(actualRow) => {
        if (expectedRow != actualRow) println(board.draw())
        assert(expectedRow == actualRow)
      }
      case None => Common.fail()
    }

  }
}
