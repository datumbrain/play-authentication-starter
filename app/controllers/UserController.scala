package controllers

import javax.inject._
import play.api.data.Form
import play.api.mvc._
import repository.UserRepository
import services.Counter
import be.objectify.deadbolt.scala.DeadboltActions
import akka.actor.ActorSystem

import scala.concurrent.ExecutionContext


@Singleton
class UserController @Inject()(userrep: UserRepository, components: MessagesControllerComponents, cc: ControllerComponents,
                               counter: Counter, actorSystem: ActorSystem, deadboltActions: DeadboltActions)(implicit exec: ExecutionContext)
                                extends MessagesAbstractController(components) {

  import models.forms.UserForm._
  import models.forms.UserLoginForm._

  private val postUrlSignUp = routes.UserController.signUpSave()
  private val postUrlLogin = routes.UserController.loginPage()

  /*THESE ARE THE ACTIONS FOR SIGN UP*/
  def signUpSave = Action { implicit request: MessagesRequest[AnyContent] =>
    val errorFunction = { formWithErrors: Form[User] =>
      BadRequest(views.html.signUp(formWithErrors, postUrlSignUp))
    }

    val successFunction = { user: User =>
      val id = userrep.create(user.firstName, user.lastName, user.email, user.password, "USER")

      Redirect(routes.HomeController.index())
    }

    val formValidationResult = form.bindFromRequest
    formValidationResult.fold(errorFunction, successFunction)
  }

  def signUp = Action { implicit request: MessagesRequest[AnyContent] =>
    Ok(views.html.signUp(form, postUrlSignUp))
  }

  /*THESE ARE THE ACTIONS FOR Login*/
  def loginPage = Action { implicit request: MessagesRequest[AnyContent] =>

    val errorFunction = { formWithErrors: Form[UserLogin] =>
      BadRequest(views.html.login(formWithErrors, postUrlLogin))
    }

    val successFunction = { userlogin: UserLogin =>
      for {
        user <- userrep.get(userlogin.email)
      } yield {
        val user: User = user
        Redirect(routes.UserController.userinfo())
      }
      Redirect(routes.HomeController.index())
    }

    val formValidationResult = form.bindFromRequest
    formValidationResult.fold(errorFunction, successFunction)
  }

  def login = Action { implicit request: MessagesRequest[AnyContent] =>
    Ok(views.html.signUp(loginform, postUrlLogin))
  }

  def userinfo : EssentialAction = deadboltActions.Restrict(List(Array("USER"))) { implicit request: MessagesRequest[AnyContent] =>
    Ok(views.html.restrictedPage(loginform, postUrlLogin))
  }
}
