package fr.coolnerds.minigames.engines

trait State extends StatePersistenceOps {}

trait StatePersistenceOps {
  implicit def toJson: String
}
