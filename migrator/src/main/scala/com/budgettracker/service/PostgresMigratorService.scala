package com.budgettracker.service

import com.budgettracker.dto.CustomerDto
import com.budgettracker.repository.CustomerRepository
import zio._

class PostgresMigratorService(customerRepository: CustomerRepository) {

  def performPostgresMigration(customerDto: Seq[CustomerDto]): Task[Unit] = {
    for{
      _ <- customerRepository.insertCustomers(customerDto)
    }yield ()
  }
}

object PostgresMigratorService{
  private def create(customerRepository: CustomerRepository) =
    new PostgresMigratorService(customerRepository)

  lazy val live: ZLayer[CustomerRepository, Any, PostgresMigratorService] =
    ZLayer.fromFunction(create _)
}

