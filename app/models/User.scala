package models

import play.api.libs.json.Json

case class User (id: Long, firstName: String, lastName: String, email: String, password: String, userRole: String);

object User {
  implicit val userFormat = Json.format[User]
}
