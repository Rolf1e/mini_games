package fr.coolnerds.minigames.model.dao

import fr.coolnerds.minigames.engines.{
  ExternalComponentException,
  InAppException,
  MiniGamesException
}

import java.sql.ResultSet
import javax.sql.DataSource
import scala.util.{Failure, Success, Using}

object DAOUtils {

  def executeQuery[E](sql: String)(
      implicit source: DataSource,
      entityParser: EntityParser[E]
  ): Either[MiniGamesException, E] = {
    Using(source.getConnection) { connection =>
      val result = connection
        .createStatement()
        .executeQuery(sql)

      // this method must not touch the ResultSet and
      // delegate this to EntityParser
      entityParser.toEntity(result)
    } match {
      case Failure(e)      => Left(ExternalComponentException(e))
      case Success(result) => result
    }
  }

}

trait EntityParser[E] {
  implicit def toEntity(resultSet: ResultSet): Either[MiniGamesException, E]
}

case class MiniGamesNotFoundException(message: String = "Failed to find resource")
    extends InAppException(message)
