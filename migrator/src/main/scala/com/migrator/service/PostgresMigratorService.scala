package com.migrator.service

import com.migrator.dto.CustomerDto
import com.migrator.repository.CustomerRepository
import zio._

class PostgresMigratorService(customerRepository: CustomerRepository) {

  def performPostgresMigration(customerDto: Seq[CustomerDto], postgresUrl: String, postgresUsername: String, postgresPassword: String): Task[Unit] = {
    for{
      _ <- customerRepository.insertCustomers(customerDto, postgresUrl, postgresUsername, postgresPassword)
    }yield ()
  }
}

object PostgresMigratorService{
  private def create(customerRepository: CustomerRepository) =
    new PostgresMigratorService(customerRepository)

  lazy val live: ZLayer[CustomerRepository, Throwable, PostgresMigratorService] =
    ZLayer.fromFunction(create _)
}

