package com.api.models

import scalikejdbc.WrappedResultSet

trait Entity[T] {
  def apply(rs: WrappedResultSet): T
}
