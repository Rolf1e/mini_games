package fr.coolnerds.minigames.infra.sql

import fr.coolnerds.minigames.engines.MiniGamesException

import java.sql.ResultSet

trait EntityParser[S, E] {
  def toEntity(resultSet: S): Either[MiniGamesException, E]
}

trait JavaEntityParser[E] extends EntityParser[ResultSet, E] {
  override def toEntity(resultSet: ResultSet): Either[MiniGamesException, E]
}
