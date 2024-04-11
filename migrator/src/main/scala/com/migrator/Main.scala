package com.migrator

import com.migrator.dto.CustomerDto
import com.migrator.models.MigratorOptions
import com.migrator.repository.CustomerRepositoryImpl
import com.migrator.service.{MongoDbCashFlowService, MongoDbCashFlowServiceImpl, PostgresMigratorService, PostgresMigratorServiceImpl}
import com.migrator.utils.Constants._
import com.migrator.utils.MongoDbConnectionImpl
import zio._
import zio.cli.HelpDoc.Span.text
import zio.cli.{CliApp, Command, Options, ZIOCliDefault}

import java.time.LocalDate
import java.util.UUID

object Main extends ZIOCliDefault {

  override def cliApp: CliApp[Any with ZIOAppArgs with Scope, Any, Any] = {
    val postgresUrl: Options[String] = Options.text(OPTIONS_POSTGRES_URL)
    val postgresUsername: Options[String] = Options.text(OPTIONS_POSTGRES_USERNAME)
    val postgresPassword: Options[String] = Options.text(OPTIONS_POSTGRES_PASSWORD)
    val mongoDbUrl: Options[String] = Options.text(OPTIONS_MONGODB_URL)

    val options = (postgresUrl ++ postgresUsername ++ postgresPassword ++ mongoDbUrl).as(MigratorOptions.apply _)
    val migratorCommand: Command[MigratorOptions] = Command("migrator", options)

    CliApp.make(
      "Budget Tracker Migrator", "0.0.1",
      text("Migrates records from mongodb to postgres"),
      migratorCommand
    )(execute)
  }

  private def execute(migratorOptions: MigratorOptions): ZIO[Any, Throwable, Unit] = {
    val postgresUrl = migratorOptions.postgresUrl
    val postgresUsername = migratorOptions.postgresUsername
    val postgresPassword = migratorOptions.postgresPassword
    val mongoDbUrl = migratorOptions.mongoDbUrl

    val customerDto = Seq(
      CustomerDto(
        id = UUID.randomUUID(),
        age = 35,
        dob = LocalDate.of(1988, 6, 8),
        firstName = "David",
        lastName = "Parr"
      )
    )

    val program = for {
      mongoDbCashFlowService <- ZIO.service[MongoDbCashFlowService]
      postgresMigratorService <- ZIO.service[PostgresMigratorService]
      _ <- mongoDbCashFlowService.getCashFlowRecords(mongoDbUrl)
      _ <- postgresMigratorService.performPostgresMigration(customerDto)(postgresUrl, postgresUsername, postgresPassword)
    } yield()

    program.provide(
      MongoDbConnectionImpl.live,
      MongoDbCashFlowServiceImpl.live,
      PostgresMigratorServiceImpl.live,
      CustomerRepositoryImpl.live
    )
  }


}
