package models

import play.api.libs.json.Json

case class User (id: Long, firstName: String, lastName: String, email: String, password: String, userRole: String, authenticated: Boolean);

object User {
  implicit val userFormat = Json.format[User]
}
