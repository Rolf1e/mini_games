package fr.coolnerds.minigames.boards.connectfour

import fr.coolnerds.minigames.boards.{Board, Coordinates, Point2D, Action, State}
import fr.coolnerds.minigames.engine.ConnectFourConstants._
import fr.coolnerds.minigames.engine.{AddPonRed, AddPonYellow, RedTurn, YellowTurn, Won}

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
    cases: mutable.ArrayDeque[Case]
) extends Board[Case] {

  override def at(coo: Coordinates): Option[Case] = coo match {
    case Point2D(x, y) => Some(cases((rowLength - 1) * y + x))
    case _             => None
  }

  override def play(action: Action): State = action match {
    case AddPonYellow(col) => playAt(yellowPon, col)
    case AddPonRed(col)    => playAt(redPon, col)
  }

  private def playAt(color: Case, col: Int): State = {
    isWon match {
      case Won(color) => Won(color)
      case YellowTurn(_) | RedTurn(_) => {
        findRow(col) match {
          case Some(row) => cases(row + col) = color
          case None => // Player can not play in this column
            return if (color == yellowPon) YellowTurn(this) else RedTurn(this)
        }
        if (color == yellowPon) RedTurn(this) else YellowTurn(this)
      }
    }
  }

  private def findRow(col: Int): Option[Int] = {
    cases.zipWithIndex.view
      .filter { case (_, i) => i % rowLength == col }
      .filter { case (c, _) => c == 0 }
      .headOption
      .map(_._2)
  }

  private def isWon: State = RedTurn(this)

  override def draw(): String = {
    var map = ""
    var row = ""
    for ((c, i) <- cases.zipWithIndex.reverse) {
      if (i % rowLength == 0) {
        map += s"|${c}|${row}\n"
        row = ""
      } else { row = s"${c}|" + row }
    }
    map
  }
}

object ConnectFourBoard {

  def emptyBoard(): ConnectFourBoard = {
    ConnectFourBoard(mutable.ArrayDeque.fill(rowLength * columnLength)(0))
  }
}
