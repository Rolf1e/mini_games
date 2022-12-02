package fr.coolnerds.minigames.boards.connectfour

import fr.coolnerds.minigames.boards._
import fr.coolnerds.minigames.boards.connectfour.BitBoard.createBitBoardWithLength
import fr.coolnerds.minigames.engine.ConnectFourConstants._
import fr.coolnerds.minigames.engine._

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
private[connectfour] case class ConnectFourBoard(
    cases: mutable.ArrayDeque[Case],
    rowLength: Int,
    columnLength: Int
) extends Board[Case] {

  private[connectfour] def copyCases: Seq[Case] = cases.toSeq

  override def at(coo: Coordinates): Option[Case] = coo match {
    case Point2D(x, y) => Some(cases((rowLength - 1) * y + x))
    case _             => None
  }

  override def play(action: Action): State = action match {
    case AddPonYellow(col) => playAt(yellowPon, col)
    case AddPonRed(col)    => playAt(redPon, col)
  }

  private def playAt(color: Case, col: Int): State = {
    isStateWon match {
      case Won(color) => Won(color)
      case YellowTurn(_) | RedTurn(_) =>
        findRow(col) match {
          case Some(row) => {
            cases(row * rowLength + col) = color
            if (color == yellowPon) RedTurn(this) else YellowTurn(this)
          }
          // Player can not play in this column
          case None => if (color == yellowPon) YellowTurn(this) else RedTurn(this)
        }
    }
  }

  private[connectfour] def findRow(col: Int): Option[Int] = {
    cases.zipWithIndex.view
      .filter { case (_, i) => i % rowLength == col }
      .map(_._1)
      .zipWithIndex
      .find { case (c, _) => c == 0 }
      .map(_._2)
  }

  def isStateWon: State = {
    implicit val bitBoard: ConnectFourBoard = this
    (BitBoardOps.checkHasWin(yellowPon), BitBoardOps.checkHasWin(redPon)) match {
      case (false, false) =>
      case (true, _)              => Won(yellowPon)
      case (_, true)              => Won(redPon)

    }
  }

  override def isWon: Boolean = {
    implicit val bitBoard: ConnectFourBoard = this
    (BitBoardOps.checkHasWin(yellowPon), BitBoardOps.checkHasWin(redPon)) match {
      case (false, false) => false
      case _              => true
    }
  }

  override def draw(): String = {
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

private[connectfour] object BitBoardOps {

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

case class BitBoard(board: Long, lastIndex: Int)

private[connectfour] object BitBoard {

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
