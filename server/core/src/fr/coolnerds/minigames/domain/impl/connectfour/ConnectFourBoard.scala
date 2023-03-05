package fr.coolnerds.minigames.domain.impl.connectfour

import fr.coolnerds.minigames.components.Drawable
import fr.coolnerds.minigames.domain.*
import fr.coolnerds.minigames.domain.impl.connectfour.ConnectFourConstants.*

import scala.collection.mutable

case class ConnectFourBoard(
    cells: mutable.ArrayDeque[mutable.Stack[Cell]],
    rowLength: Int,
    columnLength: Int
) extends Board[Cell]
    with BoardOps[Cell]
    with BoardStateOps
    with Drawable {

  override def isWon: Boolean = {
    val bitBoard = BitBoard.fromBoard(this)
    bitBoard.checkIsWin(Color.Red) && bitBoard.checkIsWin(Color.Yellow)
  }

  override def isFull: Boolean = ???

  override def at(coordinates: Coordinates): Option[Cell] = ???

  override def play(action: Action): Unit = ???

  override def state: State = ???

  override def draw: String = ???
}

object ConnectFourBoard {

  def emptyBoard(): ConnectFourBoard = {
    ConnectFourBoard(
      mutable.ArrayDeque.fill(rowLength)(mutable.Stack.fill(columnLength)(emptyCell)),
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

  def checkIsWin(color: Color): Boolean = color match {
    case Color.Red    => checkIsWin(reds, lastIndex)
    case Color.Yellow => checkIsWin(yellows, lastIndex)
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

  override def toBoard: Either[MiniGamesException, Board[Cell]] = ???

}

private object BitBoard {

  def fromInt(board: Long): BitBoard = ???

  def fromBoard(board: Board[Cell]): BitBoard = ???
}
