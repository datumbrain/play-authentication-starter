package repository

import javax.inject.{Inject, Singleton}

import models.User
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private class UserTable(tag: Tag) extends Table[User](tag, "Users") {
    // mapped to Users > id in database
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

    // mapped to Users > email in database
    def email = column[String]("email")

    // mapped to Users > password in database
    def password = column[String]("password")

    // mapped to Users > userRole in database
    def userRole = column[String]("userRole")

    def validationToken = column[String]("validation_token")

    def validated = column[Int]("validated")

    def * = (id, email, password, userRole, validationToken, validated) <> ((User.apply _).tupled, User.unapply)
  }

  private val table = TableQuery[UserTable]

  def create(email: String, password: String, userRole: String, validationToken:String = "", validated: Int = 0): Future[Long] = db.run {
    table returning table.map(_.id) += User(id = 0L, email = email, password = password, userRole = userRole, validationToken = validationToken, validated = validated)
  }

  def getByEmail(email: String): Future[User] = db.run {
    table.filter(_.email === email).result.head
  }

  def getOptByEmail(email: String): Future[Option[User]] = db.run {
    table.filter(_.email === email).result.headOption
  }

  def list(): Future[Seq[User]] = db.run {
    table.result
  }

  def updatePassword(id: Long, password: String): Future[Int] = db.run {
    table.filter { row =>
      row.id === id
    }.map(r => r.password).update(password)
  }

  def updateValidated(id: Long): Future[Int] = db.run {
    table.filter { row =>
      row.id === id
    }.map(r => r.validated).update(1)
  }

  def updateValidationToken(email: String, token: String): Future[Int] = db.run {
    table.filter { row =>
      row.email === email
    }.map(r => r.validationToken).update(token)
  }

  def getUserByToken(token: String) = db.run {
    table.filter(_.validationToken === token).result.headOption
  }
}
