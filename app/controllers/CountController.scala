package controllers

import javax.inject._
import play.api.data.Form
import play.api.libs.json.Json
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

  import models.forms.UserForm._

  private val postUrl = routes.CountController.signUpSave()

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

  def signUpSave = Action { implicit request: MessagesRequest[AnyContent] =>
    val errorFunction = { formWithErrors: Form[User] =>
      BadRequest(views.html.signUp(formWithErrors, postUrl))
    }

    val successFunction = { user: User =>
      val id = userrep.create(user.firstName, user.lastName, user.email, user.password, "USER")
      //Ok(views.html.index("New user has been added."))
      Redirect(routes.HomeController.index())
      //Ok(Json.toJson(s"User ${user.firstName} has been added."))

    }

    val formValidationResult = form.bindFromRequest
    formValidationResult.fold(errorFunction, successFunction)
  }

  def signUp = Action { implicit request: MessagesRequest[AnyContent] =>
    Ok(views.html.signUp(form, postUrl))
  }
}
