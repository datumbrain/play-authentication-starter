package authentication

import be.objectify.deadbolt.scala.models.{Permission, Role, Subject}

case class AuthenticationSubject(identifier: String, roles: List[_ <: Role], permissions: List[_ <: Permission]) extends Subject

case class AuthenticationRole(name: String) extends Role

case class AuthenticationPermission(value: String) extends Permission
