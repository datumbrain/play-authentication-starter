package services

import java.sql.Timestamp

import javax.inject.Inject
import models.User
import repository.PasswordResetRepository

import scala.concurrent.Future

class PasswordResetService @Inject()(passwordRepository: PasswordResetRepository) {
  def create(userId: Long, token: String, timeSent: Timestamp, verified: Int = 0): Future[Long] = {
    passwordRepository.create(userId: Long, token: String, timeSent: Timestamp, verified)
  }

  def get(token: String) = {
    passwordRepository.get(token: String)
  }

  def updateVerified(token: String): Future[Int] = {
    passwordRepository.updateVerified(token: String)
  }

}