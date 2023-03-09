package fr.coolnerds.minigames.domain.impl.connectfour

import fr.coolnerds.minigames.components.Drawable
import fr.coolnerds.minigames.domain.*
import fr.coolnerds.minigames.domain.impl.connectfour.ConnectFourConstants.*

import scala.collection.mutable

case class ConnectFourBoard(
    cells: Seq[mutable.Stack[Cell]],
    rowLength: Int,
    columnLength: Int
) extends Board[Cell]
    with BoardOps[Cell]
    with BoardStateOps
    with Drawable {

  override def isWon: Boolean = {
    val bitBoard = BitBoard.fromBoard(this)
    bitBoard.checkIsWin(Red) && bitBoard.checkIsWin(Yellow)
  }

  override def isFull: Boolean = ???

  override def at(coordinates: Coordinates): Option[Cell] = ???

  override def play(action: Action): Unit = ???

  override def state: State = ???

  override def draw: String = {

    def drawCell(cell: Cell): String = {
      Color.fromCell(cell) match
        case Some(color) => s"$color"
        case None        => s"${Color.emptyCell}"
    }

    val a = getCellsAsSeq.reverse.view.zipWithIndex
      .map { case (cell, i) => (drawCell(cell), i) }

    a.foldLeft("") { case (board, (cell, i)) =>
      val drawn =
        if i % rowLength == 0 then s"|$cell|"
        else if i % rowLength == rowLength - 1 then s"$cell|$lineSeparator"
        else s"$cell|"
      board ++ drawn
    }
  }

  private[connectfour] def getCellsAsSeq: Seq[Cell] = {
    for {
      rowIndex <- 0 until rowLength
      columnIndex <- 0 until columnLength
    } yield cells(rowIndex)(columnIndex)
  }
}

object ConnectFourBoard {

  def emptyBoard(): ConnectFourBoard = {
    ConnectFourBoard(
      Seq.fill(rowLength)(mutable.Stack.fill(columnLength)(Color.emptyCell)),
      rowLength,
      columnLength
    )
  }

}

case class ConnectFourState(bitBoard: BitBoard) extends State {
  implicit override def toJson: String = {
    s"""
       |{
       |  "reds": ${bitBoard.reds},
       |  "yellows": ${bitBoard.yellows},
       |}
       |""".stripMargin
  }
}

// https://github.com/denkspuren/BitboardC4/blob/master/BitboardDesign.md
private case class BitBoard(reds: Long, yellows: Long, lastIndex: Int) extends BoardFactory[Cell] {

  override def toBoard: Either[MiniGamesException, Board[Cell]] = ???

  def checkIsWin(color: Color): Boolean = color match {
    case Red    => checkIsWin(reds, lastIndex)
    case Yellow => checkIsWin(yellows, lastIndex)
  }

  private def checkIsWin(board: Long, width: Int): Boolean = {
    var bitboard: Long = 0
    for direction <- directions(width) do {
      bitboard = board & (board >> direction)
      if (bitboard & (bitboard >> (2 * direction))) != 0 then return true
    }
    false
  }

  @inline
  private def directions(width: Int): Seq[Int] = Seq(1, width, width - 1, width + 1)

}

private object BitBoard {

  def fromInt(board: Long): BitBoard = ???

  def fromBoard(board: ConnectFourBoard): BitBoard = {
    val cases = board.getCellsAsSeq
    BitBoard(
      toBitBoard(cases, Red),
      toBitBoard(cases, Yellow),
      cases.length
    )
  }

  private def toBitBoard(cases: Seq[Cell], color: Color): Long = {
    var bitBoard: Long = 0
    for ((cell, i) <- cases.zipWithIndex) {
      if (Color.emptyCell == cell) {
        bitBoard ^= 0 << i
      } else if (color.ponValue == cell) {
        bitBoard ^= 1 << i
      } else {
        bitBoard ^= 0 << i
      }
    }
    bitBoard
  }
}
