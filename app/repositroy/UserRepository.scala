package repositroy

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

    // mapped to Users > firstName in database
    def firstName = column[String]("firstName")

    // mapped to Users > lastName in database
    def lastName = column[String]("lastName")

    // mapped to Users > email in database
    def email = column[String]("email")

    // mapped to Users > password in database
    def password = column[String]("password")

    // mapped to Users > userRole in database
    def userRole = column[String]("userRole")


    def * = (id, firstName, lastName, email, password, userRole) <> ((User.apply _).tupled, User.unapply)
  }

  private val table = TableQuery[UserTable]

  def create(firstName: String, lastName: String, email: String, password: String, userRole: String, authenticated: Boolean) = db.run {
    table returning table.map(_.id) += User(id = 0L, firstName = firstName, lastName = lastName, email = email, password = password, userRole = userRole)
  }

  def get(id: Long) = db.run {
    table.filter(_.id === id).result.headOption
  }

  def list(): Future[Seq[User]] = db.run {
    table.result
  }

  def updatePassword(id: Long, password: String) = db.run {
    table.filter { row =>
      row.id === id
    }.map(r => password).update(password)
  }
}
