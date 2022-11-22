package fr.coolnerds.minigames.engine

import fr.coolnerds.minigames.engine.ConnectFour.{InternalBoard, InternalPlayer}
import fr.coolnerds.minigames.boards.connectfour.ConnectFourBoard._
import fr.coolnerds.minigames.engine.ConnectFourConstants.{columnLength, Case, redPon, yellowPon}
import fr.coolnerds.minigames.boards.{Board, Coordinates, State, Action}
import fr.coolnerds.minigames.players.Player
import fr.coolnerds.minigames.boards.connectfour.ConnectFourBoard

import scala.io.StdIn.readLine
import scala.util.Try
import scala.util.Failure
import scala.util.Success

class ConnectFour(board: InternalBoard, yellow: InternalPlayer, red: InternalPlayer)
    extends Engine {

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
    currentPlayer.askAction(board) match {
      case Right(actions) if actions.size == 1 => Some(actions(0))
      case _                                   => None
    }
  }

  override def isWon(): Boolean = board.isWon()

  def at(coordinates: Coordinates): Option[Case] = board.at(coordinates)

  def draw(): String = {
    s"ConnectFour(E: 0 Y: ${yellowPon} R: ${redPon})\n${board.draw()}"
  }

  private def nextTurn(): Unit = {
    currentPlayer = if (currentPlayer == yellow) red else yellow
  }

}

object ConnectFour {

  type InternalPlayer = Player[Case]
  type InternalBoard = Board[Case]

  def apply(yellow: InternalPlayer, red: InternalPlayer): ConnectFour = {
    new ConnectFour(ConnectFourBoard.emptyBoard(), yellow, red)
  }

}

object ConnectFourConstants {
  type Case = Int

  val yellowPon = 1
  val redPon = 2

  val rowLength = 7
  val columnLength = 6

}

sealed trait ConnectFourAction extends Action {}

case class AddPonYellow(col: Int) extends ConnectFourAction
case class AddPonRed(col: Int) extends ConnectFourAction

sealed trait ConnectFourState extends State {}

case class YellowTurn(board: InternalBoard) extends ConnectFourState
case class RedTurn(board: InternalBoard) extends ConnectFourState
case class Won(color: Int) extends ConnectFourState

trait ConnectFourParser[T] extends InputParser[T] {}

object ConnectFourParser {
  type Column = Int

  def parseFromConsole[T](implicit parser: ConnectFourParser[T]): Either[String, T] = {
    Try(readLine()) match {
      case Failure(exception) => Left(exception.getMessage())
      case Success(input)     => parser.parse(input)
    }
  }

  implicit object ColumnParser extends ConnectFourParser[Column] {

    override def parse(input: String): Either[String, Column] = {
      try {
        val column = input.toInt
        if (1 <= column && column <= columnLength) {
          Right(column)
        } else Left(s"Column must be between 1 and $columnLength")
      } catch {
        case nan: java.lang.NumberFormatException => Left(s"$input is not a number !")
      }
    }

  }

}
