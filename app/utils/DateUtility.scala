package utils

import java.sql.Timestamp

object DateUtility {
  def now(): Timestamp = new Timestamp(System.currentTimeMillis())
}
