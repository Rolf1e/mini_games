package fr.coolnerds.minigames.model.dao

import fr.coolnerds.minigames.engines.{InAppException, MiniGamesException}
import fr.coolnerds.minigames.infra.sql.{ConnectionPool, JavaEntityParser, Statement}

case class MiniGamesNotFoundException(message: String = "Failed to find resource")
    extends InAppException(message)
