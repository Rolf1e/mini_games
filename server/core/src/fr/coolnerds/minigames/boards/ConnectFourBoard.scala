package fr.coolnerds.minigames.boards

import fr.coolnerds.minigames.boards.BitBoard.createBitBoardWithLength
import fr.coolnerds.minigames.components.Drawable
import fr.coolnerds.minigames.engines.ConnectFourConstants._
import fr.coolnerds.minigames.engines._

import scala.collection.mutable

/**
  *                      y
  *                      ˆ|0|2|0|
  * The following board: ||0|1|2|   rowLength = 3
  *                       |1|2|1|   columnLength = 3
  *                           -> x
  *
  * is represented by [1, 2, 1, 0, 1, 2, 0, 2, 0]
  * - We access (1, 2) with cases((rowLength * 1) y + x)
  * - Find row from a given column
  */
case class ConnectFourBoard(
    cases: mutable.ArrayDeque[Case],
    rowLength: Int,
    columnLength: Int
) extends Board[Case]
    with BoardOps[Case]
    with Drawable {

  private[boards] def copyCases: Seq[Case] = cases.toSeq

  override def at(coo: Coordinates): Option[Case] = coo match {
    case Point2D(x, y) => Some(cases((rowLength - 1) * y + x))
    case _             => None
  }

  override def play(action: Action): Unit = action match {
    case AddPonYellow(col) => playAt(yellowPon, col)
    case AddPonRed(col)    => playAt(redPon, col)
  }

  private def playAt(color: Case, col: Int): Unit = {
    if (!isWon) {
      findRowIndex(col) match {
        case Some(row) => cases(row * rowLength + col) = color
        case None      => // Player can not play in this column
      }
    }
  }

  private[boards] def findRowIndex(col: Int): Option[Int] = {
    cases.zipWithIndex.view
      .filter { case (_, i) => i % rowLength == col }
      .map(_._1) // We reset the index to have row indexes
      .zipWithIndex
      .view
      .find { case (c, _) => c == 0 }
      .map(_._2)
  }

  override def isFull: Boolean = {
    val caseAtZeroOnTopRow = cases.zipWithIndex.view
      .filter { case (_, i) => i % columnLength == rowLength - 1 }
      .count { case (c, _) => c == 0 }
    caseAtZeroOnTopRow == 0
  }

  override def isWon: Boolean = {
    implicit val bitBoard: ConnectFourBoard = this
    (BitBoardOps.checkHasWin(yellowPon), BitBoardOps.checkHasWin(redPon)) match {
      case (false, false) => false
      case _              => true
    }
  }

  override def draw: String = {
    var map = ""
    var row = ""
    for ((c, i) <- cases.zipWithIndex.reverse) {
      if (i % rowLength == 0) {
        map += s"|${c}|${row}$lineSeparator"
        row = ""
      } else { row = s"${c}|" + row }
    }
    map
  }
}

object ConnectFourBoard {

  def emptyBoard(): ConnectFourBoard = {
    ConnectFourBoard(
      mutable.ArrayDeque.fill(rowLength * columnLength)(0),
      rowLength,
      columnLength
    )
  }
}

private object BitBoardOps {

  def checkHasWin(color: Case)(implicit board: ConnectFourBoard): Boolean = {
    val bitBoard = createBitBoardWithLength(board, color)
    var bb: Long = 0
    for (direction <- directions(bitBoard.lastIndex)) {
      bb = bitBoard.board & (bitBoard.board >> direction)
      if ((bb & (bb >> (2 * direction))) != 0) {
        return true
      }
    }
    false
  }

  private def directions(width: Int): Seq[Int] = Seq(1, width, width - 1, width + 1)

}

private case class BitBoard(board: Long, lastIndex: Int)

private object BitBoard {

  private val emptyCase = 0

  // https://github.com/denkspuren/BitboardC4/blob/master/BitboardDesign.md
  def createBitBoardWithLength(board: ConnectFourBoard, color: Case): BitBoard = {
    var bitBoard = 0
    val cases = board.copyCases

    for ((c, i) <- cases.zipWithIndex) {
      if (emptyCase == c) {
        bitBoard ^= 0 << i
      } else if (color == c) {
        bitBoard ^= 1 << i
      } else {
        bitBoard ^= 0 << i
      }
    }

    BitBoard(bitBoard, cases.length)
  }

}
