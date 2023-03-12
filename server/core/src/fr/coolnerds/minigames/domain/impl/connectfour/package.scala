package fr.coolnerds.minigames.domain.impl

import fr.coolnerds.minigames.components.InputParser
import fr.coolnerds.minigames.domain.impl.connectfour.ConnectFourConstants.{
  ConnectFourCell,
  rowLength
}
import fr.coolnerds.minigames.domain.impl.connectfour.ConnectFourEngine.InternalBoard
import fr.coolnerds.minigames.domain.{Action, InGameException, MiniGamesException}

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

  object ConnectFourConstants {
    type ConnectFourCell = Int

    val rowLength = 7
    val columnLength = 6

    val lineSeparator: String = Properties.lineSeparator
  }

  sealed trait ConnectFourAction extends Action
  case class AddPon(color: Color, column: Int) extends ConnectFourAction

  abstract class ConnectFourException(message: String) extends InGameException(message)
  case class ReadInput(message: String) extends ConnectFourException(message)
  case class NaN(message: String) extends ConnectFourException(message)
  case class BadColumnIndex(message: String) extends ConnectFourException(message)

  trait ConnectFourParser[T] extends InputParser[T]

  object ConnectFourParser {

    def parseFromConsole[T](
        implicit parser: ConnectFourParser[T]
    ): Either[MiniGamesException, T] = {
      Try(readLine()) match {
        case Failure(exception) => Left(ReadInput(exception.getMessage))
        case Success(input)     => parser.parse(input)
      }
    }

    implicit object ColumnParser extends ConnectFourParser[ConnectFourCell] {

      override def parse(input: String): Either[MiniGamesException, ConnectFourCell] = {
        Try(input.toInt) match {
          case Failure(e) =>
            e match {
              case _: NumberFormatException => Left(NaN(input))
            }
          case Success(column) =>
            if (column >= 0 && column < rowLength) Right(column)
            else Left(BadColumnIndex(s"Column must be between 0 and ${rowLength - 1}"))
        }
      }

    }

  }
}
