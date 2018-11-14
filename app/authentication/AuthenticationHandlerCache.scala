package authentication

import be.objectify.deadbolt.scala.cache.HandlerCache
import be.objectify.deadbolt.scala.{DeadboltHandler, HandlerKey}
import javax.inject.Singleton

@Singleton
class AuthenticationHandlerCache extends HandlerCache {
  val defaultHandler: DeadboltHandler = new AuthenticationHandler

  override def apply(): DeadboltHandler = defaultHandler

  override def apply(handlerKey: HandlerKey): DeadboltHandler = defaultHandler
}