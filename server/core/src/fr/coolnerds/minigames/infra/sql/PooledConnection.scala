package fr.coolnerds.minigames.infra.sql

import fr.coolnerds.minigames.engines.{ExternalComponentException, MiniGamesException}

import java.sql.{Connection, SQLException}
import scala.util.{Failure, Success, Try}

private[sql] trait PooledConnection[I] {
  def identifier: I
  def executeQuery[E](statement: Statement)(
      implicit entityParser: JavaEntityParser[E]
  ): Either[MiniGamesException, E]
}

private[sql] case class JavaPooledConnection[I](identifier: I, connection: Connection)
    extends PooledConnection[I] {

  override def executeQuery[E](
      statement: Statement
  )(implicit entityParser: JavaEntityParser[E]): Either[MiniGamesException, E] = {
    // this method must not touch the ResultSet
    // and delegate this to EntityParser
    Try {
      connection
        .createStatement()
        .executeQuery(statement.readyQuery)
    } match {
      case Failure(exception: SQLException) => Left(ExternalComponentException(exception))
      case Success(result)                  => entityParser.toEntity(result)
    }
  }
}
