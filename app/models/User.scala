package models

import play.api.libs.json.Json

case class User (id: Long, email: String, password: String, userRole: String, validationToken: String, validated: Int)

object User {
  implicit val userFormat = Json.format[User]
}
