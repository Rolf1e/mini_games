package fr.coolnerds.minigames.domain.players

import fr.coolnerds.minigames.components.Drawable
import fr.coolnerds.minigames.domain.{Action, MiniGamesException}

trait Player[Case] {

  def askAction(drawable: Drawable): Either[MiniGamesException, Seq[Action]]

  def getColor: Int

}
