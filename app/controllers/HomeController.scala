package controllers

import be.objectify.deadbolt.scala.DeadboltActions
import javax.inject._
import play.api.mvc._
import play.api.libs.json.{JsValue, Json}
import scala.concurrent.Future
import models.forms._
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class HomeController @Inject()(cc: ControllerComponents, deadbolt: DeadboltActions)(implicit assetsFinder: AssetsFinder)
  extends AbstractController(cc) {

  /**
    * Create an Action to render an HTML page with a welcome message.
    * The configuration in the `routes` file means that this method
    * will be called when the application receives a `GET` request with
    * a path of `/`.
    */
  def index: EssentialAction = deadbolt.SubjectPresent()() { implicit request =>
    Future(Ok(views.html.index("Your new application is ready.")))
  }

}
