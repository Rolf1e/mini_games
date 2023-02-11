package fr.coolnerds.minigames.infra.sql

import fr.coolnerds.minigames.engines.MiniGamesException

import java.sql.Connection

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
    statement.execute(connection)
  }
}
