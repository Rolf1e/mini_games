package fr.coolnerds.minigames

package object domain {

  sealed trait MiniGamesException extends Exception

  class InGameException(message: String) extends Exception(message) with MiniGamesException

  class InAppException(message: String) extends Exception(message) with MiniGamesException
  object InAppException {
    def apply(message: String) = new InAppException(message)
  }

  class ExternalComponentException(e: Throwable) extends Exception(e) with MiniGamesException
  object ExternalComponentException {
    def apply(e: Throwable) = new ExternalComponentException(e)
  }
}
