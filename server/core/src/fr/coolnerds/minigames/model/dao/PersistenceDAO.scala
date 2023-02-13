package fr.coolnerds.minigames.model.dao

import fr.coolnerds.minigames.infra.sql.JavaEntityParser
import fr.coolnerds.minigames.utils.Result

import java.sql.ResultSet

trait PersistenceDAO[E] {
  implicit val entityParser: JavaEntityParser[Nothing] = InsertEntityParser
  def save(entity: E): Result[Nothing]
}

object InsertEntityParser extends JavaEntityParser[Nothing] {
  override def toEntity(resultSet: ResultSet): Result[Nothing] = Right()
}
