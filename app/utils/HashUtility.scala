package utils

import java.security.MessageDigest

object HashUtility {
  private def md5(s: String): String = {
    MessageDigest.getInstance("MD5").digest(s.getBytes).map(0xFF & _).map {
      "%02x".format(_)
    }.foldLeft("")(_ + _)
  }

  def generateKey(str: String): String = md5(str)
}