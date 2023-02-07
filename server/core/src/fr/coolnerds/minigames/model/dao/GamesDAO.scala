package fr.coolnerds.minigames.model.dao

import com.mchange.v2.c3p0.DataSources
import fr.coolnerds.minigames.engines.{InAppException, MiniGamesException}
import fr.coolnerds.minigames.model.entities.GameEntity

import java.sql.ResultSet
import java.time.LocalDateTime
import javax.sql.DataSource
import scala.collection.mutable
import scala.jdk.CollectionConverters.MapHasAsJava
import scala.language.implicitConversions

class GamesDAO(source: DataSource) {

  def save(game: GameEntity): Unit = ???

  def findByName(name: String): Either[MiniGamesException, GameEntity] = {
    DAOUtils.executeQuery("SELECT * FROM games")(source, GameEntityParser)
  }

}

private object GameEntityParser extends EntityParser[GameEntity] {

  implicit override def toEntity(
      result: ResultSet
  ): Either[MiniGamesException, GameEntity] = {
    val game = mutable.Stack[GameEntity]()
    while (result.next()) {
      game.addOne(
        GameEntity(
          result.getInt("id"),
          result.getString("name"),
          result.getInt("min_players"),
          result.getInt("max_players"),
          LocalDateTime.now(),
          LocalDateTime.now()
        )
      )
    }

    if (game.isEmpty) {
      Left(MiniGamesNotFoundException())
    } else if (1 == game.length) {
      Right(game.head)
    } else {
      Left(InAppException("Ambiguous result found, please make a better request."))
    }
  }

}

object GamesDAO extends App {

  def apply(source: DataSource): GamesDAO = new GamesDAO(source)

  val url = "jdbc:postgresql://localhost:5432/default_database"

  val unpooled = DataSources.unpooledDataSource(url, "username", "password")

  implicit val source = DataSources.pooledDataSource(
    unpooled,
    Map("maxStatements" -> "200", "maxPoolSize" -> 50).asJava
  )

  GamesDAO(source)
    .findByName("") match {
    case Left(e)     => println(s"Left ${e}")
    case Right(game) => println(s"Right ${game}")
  }

}
