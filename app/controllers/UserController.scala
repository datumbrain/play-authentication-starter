package controllers

import akka.actor.ActorSystem
import be.objectify.deadbolt.scala.DeadboltActions
import javax.inject._
import play.api.data.Form
import play.api.mvc._
import repository.UserRepository
import services.Counter

import scala.concurrent.{ExecutionContext, Future}


@Singleton
class UserController @Inject()(userrep: UserRepository, components: MessagesControllerComponents, cc: ControllerComponents,
                               counter: Counter, actorSystem: ActorSystem, deadboltActions: DeadboltActions)(implicit exec: ExecutionContext)
  extends MessagesAbstractController(components) {

  import models.forms.UserForm._
  import models.forms.UserLoginForm._

  private val postUrlSignUp = routes.UserController.signUpSave()
  private val postUrlLogin = routes.UserController.loginPage()

  /*THESE ARE THE ACTIONS FOR SIGN UP*/
  def signUpSave = Action { implicit request =>
    val errorFunction = { formWithErrors: Form[UserSignUp] =>
      BadRequest(views.html.signUp(formWithErrors, postUrlSignUp))
    }

    val successFunction = { user: UserSignUp =>
      val id = userrep.create(user.firstName, user.lastName, user.email, user.password, "USER")

      Redirect(routes.HomeController.index())
    }

    val formValidationResult = signupform.bindFromRequest
    formValidationResult.fold(errorFunction, successFunction)
  }

  def signUp = Action { implicit request =>
    Ok(views.html.signUp(signupform, postUrlSignUp))
  }

  /*THESE ARE THE ACTIONS FOR Login*/
  def loginPage = Action.async { implicit request =>
    val errorFunction = { formWithErrors: Form[UserLogin] =>
      Future.successful(BadRequest(views.html.login(formWithErrors, postUrlLogin)))
    }

    val successFunction = { userlogin: UserLogin =>
      for {
        user <- userrep.get(userlogin.email)
      } yield {
        user
          .map {
            u => Redirect(routes.UserController.userInfo()).withSession(("email", u.email.toString))
          }
          .getOrElse(Forbidden("Not allowed!"))
      }
    }
    val formValidationResult = loginform.bindFromRequest
    formValidationResult.fold(errorFunction, successFunction)
  }

  def login = Action { implicit request =>
    Ok(views.html.login(loginform, postUrlLogin))
  }

  def userInfo: EssentialAction = deadboltActions.Restrict(List(Array("USER"))) {
    implicit request =>
      for {
        user <- userrep.get(request.session.get("email").getOrElse("Not Found"))
      } yield {
        user
          .map {
            u => Ok(views.html.restrictedPage(u))
          }
          .getOrElse(Forbidden("Not allowed!")
        )
      }
  }
}
