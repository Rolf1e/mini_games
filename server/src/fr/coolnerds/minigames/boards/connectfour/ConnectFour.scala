package fr.coolnerds.minigames.boards.connectfour

import fr.coolnerds.minigames.boards.connectfour.ConnectFour.{InternalBoard, InternalPlayer}
import fr.coolnerds.minigames.boards.connectfour.ConnectFourBoard._
import fr.coolnerds.minigames.boards.connectfour.ConnectFourConstants.{
  Action,
  Case,
  State,
  redPon,
  yellowPon
}
import fr.coolnerds.minigames.boards.{Board, Coordinates, Player}

class ConnectFour(board: InternalBoard, yellow: InternalPlayer, red: InternalPlayer) {
  private var currentPlayer = yellow

  def askAndPlayAction(): Seq[State] = {
    checkActions() match {
      case Some(action) => {
        val state = board.play(action)
        nextTurn()
        Seq(state)
      }
      case None =>
        if (currentPlayer.getColor() == yellowPon) Seq(YellowTurn(board)) else Seq(RedTurn(board))
    }
  }


  /**
    * At connect four, a player can do only one action.
    */
  private def checkActions(): Option[Action] = {
    val actions = currentPlayer.askAction(board)
    if (actions.size == 1) {
      Some(actions(0))
    } else {
      None
    }
  }

  def at(coordinates: Coordinates): Option[Case] = board.at(coordinates)

  def draw(): String = {
    s"ConnectFour(E: 0 Y: ${yellowPon} R: ${redPon})\n${board.draw()}"
  }

  private def nextTurn(): Unit = {
    currentPlayer = if (currentPlayer == yellow) red else yellow
  }

}

object ConnectFour {

  type InternalPlayer = Player[Case, Action, State]
  type InternalBoard = Board[Case, Action, State]

  def apply(yellow: InternalPlayer, red: InternalPlayer): ConnectFour = {
    new ConnectFour(ConnectFourBoard.emptyBoard(), yellow, red)
  }

}

object ConnectFourConstants {
  type Case = Int
  type Action = ConnectFourAction
  type State = ConnectFourState

  val yellowPon = 1
  val redPon = 2
}

sealed trait ConnectFourAction {}

case class AddPonYellow(col: Int) extends ConnectFourAction
case class AddPonRed(col: Int) extends ConnectFourAction

sealed trait ConnectFourState {}

case class YellowTurn(board: InternalBoard) extends ConnectFourState
case class RedTurn(board: InternalBoard) extends ConnectFourState
case class Won(color: Int) extends ConnectFourState
