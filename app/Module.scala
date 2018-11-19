import authentication.AuthenticationHandlerCache
import be.objectify.deadbolt.scala.cache.HandlerCache
import com.google.inject.AbstractModule

class Module extends AbstractModule {
  override def configure() = {
    bind(classOf[HandlerCache]).to(classOf[AuthenticationHandlerCache])
  }
}
