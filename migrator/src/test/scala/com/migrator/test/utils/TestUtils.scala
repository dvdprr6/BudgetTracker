package com.migrator.test.utils

import java.text.SimpleDateFormat
import java.util.Date

object TestUtils {

  def stringToDate(date: String): Date = {
    val simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    simpleDateFormat.parse(date)
  }

}
