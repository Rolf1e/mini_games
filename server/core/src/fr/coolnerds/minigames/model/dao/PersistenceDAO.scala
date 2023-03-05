package fr.coolnerds.minigames.model.dao

import fr.coolnerds.minigames.infra.sql.JavaEntityParser
import fr.coolnerds.minigames.utils.Result

import java.sql.ResultSet
import scala.runtime.Nothing$

trait PersistenceDAO[E] {
  implicit val entityParser: JavaEntityParser[Unit] = InsertEntityParser
  def save(entity: E): Result[Unit]
}

object InsertEntityParser extends JavaEntityParser[Unit] {
  override def toEntity(resultSet: ResultSet): Result[Unit] = Right(())
}
