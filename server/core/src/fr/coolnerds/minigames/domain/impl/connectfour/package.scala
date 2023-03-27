package fr.coolnerds.minigames.domain.impl

import fr.coolnerds.minigames.components.InputParser
import fr.coolnerds.minigames.domain.impl.connectfour.ConnectFourConstants.{
  ConnectFourCell,
  rowLength
}
import fr.coolnerds.minigames.domain.impl.connectfour.ConnectFourEngine.InternalBoard
import fr.coolnerds.minigames.domain.{
  Action,
  ExternalComponentException,
  InAppException,
  InGameException,
  MiniGamesException,
  State
}

import scala.io.StdIn.readLine
import scala.util.{Failure, Properties, Success, Try}

package object connectfour {

  sealed trait Color {
    def ponValue: Int
  }

  object Yellow extends Color {
    val yellow = 1

    override def ponValue: ConnectFourCell = yellow
    override def toString: String = s"$yellow"
  }

  object Red extends Color {
    val red = 2

    override def ponValue: ConnectFourCell = red
    override def toString: String = s"$red"
  }

  object Color {

    val emptyCell = 0

    def fromCell(cell: ConnectFourCell): Option[Color] = {
      cell match
        case Red.red       => Some(Red)
        case Yellow.yellow => Some(Yellow)
        case _             => None
    }
  }

  enum Status(val json: String) {
    case InProgress extends Status("IN PROGRESS")
    case YellowWinner extends Status("WINNER YELLOW")
    case RedWinner extends Status("WINNER RED")
    case Draw extends Status("DRAW")
  }

  case class ConnectFourState(reds: Long, yellows: Long, status: Status) extends State {
    implicit override def toJson: String = {
      s"""
         |{
         |  "reds": $reds,
         |  "yellows": $yellows,
         |  "status": "${status.json}"
         |}
         |""".stripMargin
    }
  }

  object ConnectFourConstants {
    type ConnectFourCell = Int

    val rowLength = 7
    val columnLength = 6

    val lineSeparator: String = Properties.lineSeparator
  }

  sealed trait ConnectFourAction extends Action
  case class AddPon(color: Color, column: Int) extends ConnectFourAction

  trait ConnectFourParser[T] extends InputParser[T]

  object ConnectFourParser {

    def parseFromConsole[T](
        implicit parser: ConnectFourParser[T]
    ): Either[MiniGamesException, T] = {
      Try(readLine()) match {
        case Failure(exception) => Left(ExternalComponentException(exception))
        case Success(input)     => parser.parse(input)
      }
    }

    implicit object ColumnParser extends ConnectFourParser[ConnectFourCell] {

      override def parse(input: String): Either[MiniGamesException, ConnectFourCell] = {
        Try(input.toInt) match {
          case Failure(e) =>
            e match {
              case _: NumberFormatException => Left(InAppException(input))
            }
          case Success(column) =>
            if (column >= 0 && column < rowLength) Right(column)
            else Left(InAppException(s"Column must be between 0 and ${rowLength - 1}"))
        }
      }

    }

  }
}
