package controllers

import be.objectify.deadbolt.scala.DeadboltActions
import javax.inject._
import models.forms.NewPasswordForm.{NewPassword, newpasswordform}
import org.sqlite.SQLiteException
import play.api.data.Form
import play.api.mvc._
import services.{MailerService, PasswordResetService, UserService}
import utils.DateUtility._
import utils.HashUtility._
import utils.PasswordHashing._
import play.api.Logger
import play.api.libs.json.Json

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserController @Inject()(mailerService: MailerService, userService: UserService, passwordResetService: PasswordResetService, components: MessagesControllerComponents, deadboltActions: DeadboltActions)(implicit exec: ExecutionContext, assetsFinder: AssetsFinder)
  extends MessagesAbstractController(components) {

  import models.forms.ForgotPasswordEmailForm._
  import models.forms.UserLoginForm._

  private val postUrlSignUp = routes.UserController.signUpSave()
  private val postUrlLogin = routes.UserController.loginPage()

  /*
    THESE ARE THE ACTIONS FOR SIGN UP
  */
  def signUpSave = Action.async { implicit request =>
    request.body.asJson.map(json => {
      Json.fromJson[UserLogin](json).asOpt match {
        case Some(user) =>
          val token = generateKey(now.getTime + user.email)
          (for {
            id <- userService.create(user.email, createBcryptHash(user.password), "USER", token)
            _ <- mailerService.sendEmailValidation(token, user.email)
          } yield {
            Logger.info(s"New user created with email ${user.email}.")
            Ok(Json.toJson(Json.obj("status" -> "CREATED", "message" -> "A validation email has been sent to you! Click on the validation link to proceed.")))
          }).recover({ case e => Ok(Json.toJson(Json.obj("status" -> "EXISTS", "message" -> "A user already exists with this email."))) })
        case None => Future.successful(BadRequest("Invalid data format."))
      }
    }).getOrElse(Future.successful(BadRequest("Invalid data format.")))
  }

  def validationLink(token: String) = Action.async { implicit request =>
    (for {
      tokenobject <- userService.getUserByToken(token)
    } yield {
      tokenobject.map { to =>
        if (to.validated == 1)
          Ok(views.html.index("Your email is already verified"))
        else if (to.validated == 0)
          Redirect(routes.UserController.updatevalidation()).withSession(("userid", to.id.toString))
        else Ok(views.html.index("Your link is either expired or invalid."))
      }.getOrElse(Forbidden(views.html.index("Invalid link.")))
    }).recover({ case e => Ok(views.html.index(s"${e.getMessage}")) })
  }

  def updatevalidation = Action.async {
    implicit request =>
      val id = request.session.get("userid").getOrElse("N/A")
      if (id != "N/A") {
        (for {
          id <- userService.updateValidated(id.toLong)
        } yield {
          Logger.info(s"Email has been verified for userid ${id}")
          Ok(views.html.index("Email has been verified. Go to login to login again."))
        }).recover({ case e => BadRequest(views.html.index(s"${e.getStackTrace.mkString("s\n")}")) })
      } else Future.successful(Ok(views.html.index("You are not allowed to be here.")))
  }

  /*
  THESE ARE THE ACTIONS FOR Login
  */
  def loginPage = Action.async { implicit request =>
    val errorFunction = { formWithErrors: Form[UserLogin] =>
      Future.successful(BadRequest(views.html.loginAndSignup(formWithErrors, postUrlLogin)))
    }

    val successFunction = { userlogin: UserLogin =>
      for {
        user <- userService.getOptByEmail(userlogin.email)
      } yield {
        user.map { u =>
          if (matchBcryptHash(userlogin.password, u.password).toOption.getOrElse(false) && u.validated == 1)
            Redirect(routes.UserController.userInfo()).withSession(("email", u.email.toString), ("roles", "USER"))
          else if (u.validated == 0) Ok(views.html.index("Your email is not verified yet. Please verifiy your email first"))
          else Ok(views.html.index("Invalid credentials, please try again."))
        }.getOrElse(Ok(views.html.index("User doesn't exist. Please sign up first.")))
      }
    }
    val formValidationResult = loginform.bindFromRequest
    formValidationResult.fold(errorFunction, successFunction)
  }

  def login = Action { implicit request =>
    Ok(views.html.loginAndSignup(loginform, postUrlLogin))
  }

  /*
  This is the Logout.
   */
  def logout = Action { implicit request =>
    Redirect(routes.HomeController.index()).withNewSession
  }

  /*
  This is the restricted action.
   */
  def userInfo: EssentialAction = deadboltActions.SubjectPresent()() {
    implicit request =>
      for {
        user <- userService.getOptByEmail(request.session.get("email").getOrElse("Doesn't exist."))
      } yield {
        user
          .map { u => Ok(views.html.restrictedPage(u)) }
          .getOrElse(Forbidden(views.html.index("Forbidden!")))
      }
  }

  /*
  THESE ARE THE ACTIONS FOR Password Reset
  */
  def getForgotEmail: Action[AnyContent] = Action.async { implicit request =>
    val errorFunction = { formWithErrors: Form[ForgotEmail] =>
      Future.successful(BadRequest(views.html.forgotPasswordEmail(formWithErrors, routes.UserController.getForgotEmail())))
    }

    val successFunction = { forgotEmail: ForgotEmail =>
      val nowTime = now()

      val result = for {
        user <- userService.getUserByEmail(forgotEmail.email.trim)
        generatedToken = generateKey(user.email + nowTime.getTime)
        _ <- passwordResetService.create(user.id, generatedToken, nowTime)
        _ <- mailerService.sendEmail(generatedToken, user.email)

      } yield {
        Logger.info(s"User ${user.email} has requested a password reset.")
        Ok(views.html.index("An email with the reset link has been sent to you."))
      }

      result.recover({
        case e: SQLiteException => InternalServerError("Database error.")
        case _ => NotFound(s"User not found against the email.")
      })
    }
    val formValidationResult = forgotpasswordemailform.bindFromRequest
    formValidationResult.fold(errorFunction, successFunction)
  }

  def resetlink(token: String) = Action.async { implicit request =>
    import java.util.concurrent.TimeUnit
    (for {
      tokenobject <- passwordResetService.get(token)
    } yield {
      tokenobject.map { to =>
        val newts = to.timeSent
        newts.setTime(newts.getTime + TimeUnit.MINUTES.toMillis(30))
        if (newts.compareTo(now) > 0 && to.verified == 0)
          Redirect(routes.UserController.resetpassword()).withSession(("token", to.token.toString), ("userid", to.userId.toString))
        else Ok(views.html.index("Your link is either expired or invalid."))
      }.getOrElse(Forbidden(views.html.index("Invalid link.")))
    }).recover({ case e => Ok(views.html.index(s"${e.getMessage}")) })
  }

  def resetpassword = Action.async { implicit request =>
    val token = request.session.get("token").getOrElse("N/A")
    val userId = request.session.get("userid").getOrElse("N/A")
    if (token != "N/A" && userId != "N/A")
      Future.successful(Ok(views.html.newPassword(newpasswordform, routes.UserController.savePassword())).withSession(("token", token), ("userid", userId)))
    else
      Future.successful(Ok(views.html.index("You are not allowed to be here.")))
  }

  def savePassword = Action.async { implicit request =>
    val errorFunction = { formWithErrors: Form[NewPassword] =>
      Future.successful(BadRequest(views.html.newPassword(formWithErrors, routes.UserController.savePassword())))
    }
    val token = request.session.get("token").getOrElse("N/A")
    val userId = request.session.get("userid").getOrElse("N/A")

    val successFunction = { newp: NewPassword =>
      val newpassword = createBcryptHash(newp.password)
      if (token != "N/A" && userId != "N/A") {
        (for {
          id <- userService.updatePassword(userId.toLong, newpassword)
          ver <- passwordResetService.updateVerified(token)
        } yield {
          Logger.info(s"Password reset completed for user $userId.")
          Ok(views.html.index("Password has been reset successfully. Go to login to login again.")).withNewSession
        }).recover({ case e => BadRequest(views.html.index(s"${e.getStackTrace.mkString("s\n")}")) })
      }
      else Future.successful(Ok(views.html.index("Your session has expired.")))

    }
    val formValidationResult = newpasswordform.bindFromRequest
    formValidationResult.fold(errorFunction, successFunction)
  }

  def forgotPassword = Action { implicit request =>
    Ok(views.html.forgotPasswordEmail(forgotpasswordemailform, routes.UserController.getForgotEmail()))
  }
}
