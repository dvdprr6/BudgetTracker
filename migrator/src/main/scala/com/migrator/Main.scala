package com.migrator

import com.migrator.dto.CustomerDto
import com.migrator.repository.CustomerRepository
import com.migrator.service.PostgresMigratorService
import zio._

import java.time.LocalDate
import java.util.UUID

object Main extends ZIOAppDefault {

  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] = {

    val customerDto = Seq(
      CustomerDto(
        id = UUID.randomUUID(),
        age = 35,
        dob = LocalDate.of(1988, 6, 8),
        firstName = "David",
        lastName = "Parr"
      )
    )

    val program = for{
      postgresMigratorService <- ZIO.service[PostgresMigratorService]
      _ <- postgresMigratorService.performPostgresMigration(customerDto)
    } yield ()

    program.provide(CustomerRepository.live, PostgresMigratorService.live)
  }
}
