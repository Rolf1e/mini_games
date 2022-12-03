package fr.coolnerds.minigames.engine

import fr.coolnerds.minigames.boards.State

trait Engine {

  def isWon: State

}

trait MiniGamesException extends Exception
