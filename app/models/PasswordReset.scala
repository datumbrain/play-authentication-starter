package models

import java.sql.Timestamp

import play.api.libs.json.Json

case class PasswordReset(id: Long, userId: Long, token: String, timeSent: Timestamp, verified: Int)

object PasswordReset {
  import utils.Implicits._

  implicit val passwordResetFormat = Json.format[PasswordReset]
}
