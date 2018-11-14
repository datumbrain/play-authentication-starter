package authentication

import be.objectify.deadbolt.scala.models.Subject
import be.objectify.deadbolt.scala.{AuthenticatedRequest, DeadboltHandler, DynamicResourceHandler}
import play.api.libs.json.Json
import play.api.mvc.{Request, Result, Results}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class AuthenticationHandler extends DeadboltHandler {
  override def beforeAuthCheck[A](request: Request[A]): Future[Option[Result]] = Future(None)

  override def getDynamicResourceHandler[A](request: Request[A]): Future[Option[DynamicResourceHandler]] = Future(None)

  override def getSubject[A](request: AuthenticatedRequest[A]): Future[Option[Subject]] = {
    request.session.get("userId").map(e => Future(Some(AuthenticationSubject(e, request.session.get("roles").get.split(",").map(r => AuthenticationRole(r.trim)).toList, List.empty))))
      .getOrElse(Future(None))
  }

  override def onAuthFailure[A](request: AuthenticatedRequest[A]): Future[Result] = {
    Future {
      Results.Forbidden(Json.toJson("Permission denied!"))
    }
  }
}
