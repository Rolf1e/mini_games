package fr.coolnerds.minigames.domain.impl.connectfour

import fr.coolnerds.minigames.components.Drawable
import fr.coolnerds.minigames.domain.*
import fr.coolnerds.minigames.domain.impl.connectfour.ConnectFourConstants.*
import fr.coolnerds.minigames.utils.Result

import scala.collection.mutable
import scala.util

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
    cells: mutable.ArrayDeque[ConnectFourCell],
    rowLength: Int,
    columnLength: Int
) extends Board[ConnectFourCell]
    with BoardOps[ConnectFourCell]
    with BoardStateOps
    with Drawable {

  private var bitBoard = BitBoard.fromBoard(this)

  override def isWon: Boolean = {
    println(s"isWon yellow:${bitBoard.checkIsWin(Yellow)} red:${bitBoard.checkIsWin(Red)} ")
    bitBoard.checkIsWin(Yellow) || bitBoard.checkIsWin(Red)
  }

  override def isFull: Boolean = !cells.contains(Color.emptyCell)

  override def play(action: Action): Result[Unit] = {
    action match {
      case AddPon(color, column) => playAt(color, column)
      case _                     => Left(InGameException("Illegal action for connect four game"))
    }
  }

  private def updateBitBoard(): Unit = {
    bitBoard = BitBoard.fromBoard(this)
  }

  private def playAt(color: Color, col: Int): Result[Unit] = {
    if !isFull && !isWon then {
      findRow(col) match {
        case Some(row) =>
          cells(row * rowLength + col) = color.ponValue
          updateBitBoard()
          Right(())
        case None => Left(InGameException(" Player can not play in this column "))
      }
    } else Right(())
  }

  private[connectfour] def findRow(col: Int): Option[Int] = {
    cells.zipWithIndex.view
      .filter { case (_, i) => i % rowLength == col }
      .map(_._1)
      .zipWithIndex
      .find { case (c, _) => c == 0 }
      .map(_._2)
  }

  override def state: State = {
    val status =
      if bitBoard.checkIsWin(Yellow) then Status.YellowWinner
      else if bitBoard.checkIsWin(Red) then Status.RedWinner
      else if isFull then Status.Draw
      else Status.InProgress
    ConnectFourState(bitBoard.reds, bitBoard.yellows, status)
  }

  override def draw: String = {
    var map = ""
    var row = ""
    for ((cell, i) <- cells.zipWithIndex.reverse) {
      if (i % rowLength == 0) {
        map += s"|${drawCell(cell)}|${row}$lineSeparator"
        row = ""
      } else { row = s"${drawCell(cell)}|" + row }
    }
    map
  }

  private def drawCell(cell: ConnectFourCell): String = {
    Color.fromCell(cell) match
      case Some(color) => s"$color"
      case None        => s"${Color.emptyCell}"
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

// https://github.com/denkspuren/BitboardC4/blob/master/BitboardDesign.md
private case class BitBoard(reds: Long, yellows: Long, lastIndex: Int)
    extends BoardFactory[ConnectFourCell] {

  override def toBoard: Result[Board[ConnectFourCell]] = ???

  def checkIsWin(color: Color): Boolean = color match {
    case Red    => checkIsWin(reds, lastIndex)
    case Yellow => checkIsWin(yellows, lastIndex)
  }

  private def checkIsWin(bitBoard: Long, width: Int): Boolean = {
    var bb: Long = 0
    for direction <- directions(width) do {
      bb = bitBoard & (bitBoard >> direction)
      if (bb & (bb >> (2 * direction))) != 0 then return true
    }
    false
  }

  @inline
  private def directions(width: Int): Seq[Int] = Seq(1, width, width - 1, width + 1)

}

private object BitBoard {

  def fromBoard(board: ConnectFourBoard): BitBoard = {
    val cells = board.cells.toSeq
    BitBoard(
      toBitBoard(cells, Red),
      toBitBoard(cells, Yellow),
      cells.length
    )
  }

  private def toBitBoard(cases: Seq[ConnectFourCell], color: Color): Long = {
    var bitBoard: Long = 0
    for (cell, i) <- cases.zipWithIndex do {
      if Color.emptyCell == cell then {
        bitBoard ^= 0 << i
      } else if color.ponValue == cell then {
        bitBoard ^= 1 << i
      } else {
        bitBoard ^= 0 << i
      }
    }
    bitBoard
  }
}
