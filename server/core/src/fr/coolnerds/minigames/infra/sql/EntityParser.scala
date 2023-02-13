package fr.coolnerds.minigames.infra.sql

import fr.coolnerds.minigames.utils.Result

import java.sql.ResultSet

trait EntityParser[S, E] {
  def toEntity(resultSet: S): Result[E]
}

trait JavaEntityParser[E] extends EntityParser[ResultSet, E] {
  override def toEntity(resultSet: ResultSet): Result[E]
}
