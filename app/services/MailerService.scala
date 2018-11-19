package services

import javax.inject.Inject
import play.api.libs.mailer._
import com.typesafe.config.ConfigFactory
import scala.concurrent.Future

class MailerService @Inject()(mailerClient: MailerClient) {
  val config = ConfigFactory.load()
  private val host = config.getString("project.host")
  private val port = config.getString("project.port")
  def sendEmail(token: String, userEmail: String): Future[String] = Future.successful {

    val senderAdd = config.getString("play.mailer.from")
    val email = Email(
      subject = "Password Reset Link",
      from = s"Play Skeleton $senderAdd",
      to = Seq(s"$userEmail"),
      bodyText = Some(s"""Hi!\nPlease Go to this link to reset your password: http://$host:$port/$token/resetlink\n""")
    )
    mailerClient.send(email)
  }

  def sendEmailValidation(token: String, userEmail: String): Future[String] = Future.successful {

    val config = ConfigFactory.load()

    val senderAdd = config.getString("play.mailer.from")
    val email = Email(
      subject = "Password Reset Link",
      from = s"Play Skeleton $senderAdd",
      to = Seq(s"$userEmail"),
      bodyText = Some(s"""Hi!\nPlease Go to this link to reset your password: http://$host:$port/$token/validationlink\n""")
    )
    mailerClient.send(email)
  }
}