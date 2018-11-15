package controllers

import javax.inject._
import play.api.mvc._
import repository.UserRepository
import services.Counter

/**
  * This controller demonstrates how to use dependency injection to
  * bind a component into a controller class. The class creates an
  * `Action` that shows an incrementing count to users. The [[Counter]]
  * object is injected by the Guice dependency injection system.
  */
@Singleton
class CountController @Inject()(userrep: UserRepository, components: MessagesControllerComponents, cc: ControllerComponents, counter: Counter)
  extends MessagesAbstractController(components) {

  /**
    * Create an action that responds with the [[Counter]]'s current
    * count. The result is plain text. This `Action` is mapped to
    * `GET /count` requests by an entry in the `routes` config file.
    */
  def count = Action { implicit request =>

    Redirect(routes.HomeController.index()).withSession(
      "userId" -> "useridrandom",
      "roles" -> "MYROLE1"
    )
  }
}
