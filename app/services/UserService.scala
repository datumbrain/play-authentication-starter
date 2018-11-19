package services

import javax.inject.Inject
import models.User
import repository.UserRepository

import scala.concurrent.Future

class UserService @Inject()(userRepository: UserRepository) {
  def getUserByEmail(email: String): Future[User] = {
    userRepository.getByEmail(email)
  }

  def getOptByEmail(email: String): Future[Option[User]] = {
    userRepository.getOptByEmail(email)
  }

  def updatePassword(id: Long, password: String): Future[Int] = {
    userRepository.updatePassword(id, password)
  }

  def create(email: String, password: String, userRole: String, validationToken: String): Future[Long] = {
    userRepository.create(email: String, password: String, userRole: String, validationToken)
  }

  def updateValidated(id: Long): Future[Int] = {
    userRepository.updateValidated(id)
  }

  def updateValidationToken(email: String, token: String): Future[Int] = {
    userRepository.updateValidationToken(email, token)
  }

  def getUserByToken(token: String) = {
    userRepository.getUserByToken(token)
  }
}
