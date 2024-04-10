package com.migrator.service

import com.migrator.dto.CustomerDto
import com.migrator.repository.CustomerRepository
import zio._

trait PostgresMigratorService{
  def performPostgresMigration(customerDto: Seq[CustomerDto], postgresUrl: String, postgresUsername: String, postgresPassword: String): Task[Unit]
}

class PostgresMigratorServiceImpl(customerRepository: CustomerRepository) extends PostgresMigratorService{

  override def performPostgresMigration(customerDto: Seq[CustomerDto], postgresUrl: String, postgresUsername: String, postgresPassword: String): Task[Unit] = {
    for{
      _ <- customerRepository.insertCustomers(customerDto, postgresUrl, postgresUsername, postgresPassword)
    }yield ()
  }
}

object PostgresMigratorServiceImpl{
  private def create(customerRepository: CustomerRepository): PostgresMigratorService =
    new PostgresMigratorServiceImpl(customerRepository)

  lazy val live: ZLayer[CustomerRepository, Throwable, PostgresMigratorService] =
    ZLayer.fromFunction(create _)
}

