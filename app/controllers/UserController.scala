package controllers

import javax.inject._
import play.api.data.Form
import play.api.mvc._
import repository.UserRepository
import services.Counter

@Singleton
class UserController @Inject()(userrep: UserRepository, components: MessagesControllerComponents, cc: ControllerComponents, counter: Counter)
  extends MessagesAbstractController(components) {

  import models.forms.UserForm._

  private val postUrl = routes.UserController.signUpSave()

  def signUpSave = Action { implicit request: MessagesRequest[AnyContent] =>
    val errorFunction = { formWithErrors: Form[User] =>
      BadRequest(views.html.signUp(formWithErrors, postUrl))
    }

    val successFunction = { user: User =>
      val id = userrep.create(user.firstName, user.lastName, user.email, user.password, "USER")

      Redirect(routes.HomeController.index())
    }

    val formValidationResult = form.bindFromRequest
    formValidationResult.fold(errorFunction, successFunction)
  }

  def signUp = Action { implicit request: MessagesRequest[AnyContent] =>
    Ok(views.html.signUp(form, postUrl))
  }
}
