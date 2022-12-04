package fr.coolnerds.minigames.players

import fr.coolnerds.minigames.boards.Action
import fr.coolnerds.minigames.components.Drawable
import fr.coolnerds.minigames.engine.MiniGamesException

trait Player[Case] {

  def askAction(drawable: Drawable): Either[MiniGamesException, Seq[Action]]

  def getColor: Int

}
