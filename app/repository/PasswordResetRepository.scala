package repository

import java.sql.Timestamp

import javax.inject.{Inject, Singleton}
import models.PasswordReset
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class PasswordResetRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private class ForgotPasswordTable(tag: Tag) extends Table[PasswordReset](tag, "ForgotPassword") {
    // mapped to ForgotPassowrd > id in database
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

    // mapped to ForgotPassowrd > userid in database
    def userId = column[Long]("user_id")

    // mapped to ForgotPassowrd > token in database
    def token = column[String]("token")

    // mapped to ForgotPassowrd > time_sent in database
    def timeSent = column[Timestamp]("time_sent")

    // mapped to ForgotPassowrd > verified in database
    def verified = column[Int]("verified")

    def * = (id, userId, token, timeSent, verified) <> ((PasswordReset.apply _).tupled, PasswordReset.unapply)
  }

  private val table = TableQuery[ForgotPasswordTable]

  def create(userId: Long, token: String, timeSent: Timestamp, verified: Int = 0) = db.run {
    table returning table.map(_.id) += PasswordReset(id = 0L, userId = userId, token = token, timeSent = timeSent, verified = verified)
  }

  def get(token: String) = db.run {
    table.filter(_.token === token).result.headOption
  }

  def list(): Future[Seq[PasswordReset]] = db.run {
    table.result
  }

  def updateVerified(token: String) = db.run {
    table.filter { row =>
      row.token === token
    }.map(r => r.verified).update(1)
  }
}
