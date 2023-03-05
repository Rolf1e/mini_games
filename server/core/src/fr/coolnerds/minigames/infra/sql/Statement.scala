package fr.coolnerds.minigames.infra.sql

import fr.coolnerds.minigames.domain.ExternalComponentException
import fr.coolnerds.minigames.utils.Result

import java.sql.{Connection, SQLException}
import scala.util.{Failure, Success, Using}

sealed trait Statement {
  def execute[E](connection: Connection)(
      implicit entityParser: JavaEntityParser[E]
  ): Result[E]
}

case class UnPreparedStatement(sql: String) extends Statement {
  def execute[E](
      connection: Connection
  )(implicit entityParser: JavaEntityParser[E]): Result[E] = {
    Using.Manager { use =>
      val statement = use(connection.createStatement())
      val result = use(statement.executeQuery(sql))
      entityParser.toEntity(result)
    } match {
      case Failure(exception: SQLException) => Left(ExternalComponentException(exception))
      case Success(entity)                  => entity
    }
  }

}

/**
  * @param sql the sql template
  * @param preparation function to apply parameters to the sql template
  */
case class PreparedStatement(sql: String)(
    preparation: java.sql.PreparedStatement => java.sql.PreparedStatement
) extends Statement {
  override def execute[E](connection: Connection)(
      implicit entityParser: JavaEntityParser[E]
  ): Result[E] = {
    Using.Manager { use =>
      val statement = use(connection.prepareStatement(sql))
      val result = use(preparation(statement).executeQuery())
      entityParser.toEntity(result)
    } match {
      case Failure(exception: SQLException) => Left(ExternalComponentException(exception))
      case Success(entity)                  => entity
    }
  }
}
