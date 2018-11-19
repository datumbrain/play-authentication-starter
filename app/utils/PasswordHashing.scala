package utils

import org.mindrot.jbcrypt.BCrypt

import scala.util.Try

object PasswordHashing {
  def createBcryptHash(text: String) = {
    BCrypt.hashpw(text, BCrypt.gensalt());
  }

  def matchBcryptHash(candidate: String, hashed: String): Try[Boolean] = {
    Try {
      BCrypt.checkpw(candidate, hashed)
    }
  }
}
