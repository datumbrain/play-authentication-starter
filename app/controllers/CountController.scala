package controllers

import javax.inject._
import play.api.mvc._
import services.Counter
import play.api.mvc._

import scala.concurrent.Future

/**
  * This controller demonstrates how to use dependency injection to
  * bind a component into a controller class. The class creates an
  * `Action` that shows an incrementing count to users. The [[Counter]]
  * object is injected by the Guice dependency injection system.
  */
@Singleton
class CountController @Inject()(components: MessagesControllerComponents, cc: ControllerComponents, counter: Counter)
  extends MessagesAbstractController(components) {

  import models.forms.UserForm._

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

  def signUpSave = Action { implicit request =>
    Redirect(routes.HomeController.index()).withSession(
      "userId" -> "useridrandom",
      "roles" -> "MYROLE1"
    )
  }

  private val postUrl = routes.CountController.signUpSave()

  def signUp = Action { implicit request: MessagesRequest[AnyContent] =>
    Ok(views.html.signUp(form, postUrl))
  }
}
