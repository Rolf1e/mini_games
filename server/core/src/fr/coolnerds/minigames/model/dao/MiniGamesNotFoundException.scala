package fr.coolnerds.minigames.model.dao

import fr.coolnerds.minigames.engines.InAppException

case class MiniGamesNotFoundException(message: String = "Failed to find resource")
    extends InAppException(message)
