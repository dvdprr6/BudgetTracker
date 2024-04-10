package com.migrator

import com.migrator.dto.CustomerDto
import com.migrator.models.MigratorOptions
import com.migrator.repository.CustomerRepository
import com.migrator.service.{MongoDbMigratorService, PostgresMigratorService}
import zio._
import zio.cli.HelpDoc.Span.text
import zio.cli.{CliApp, Command, Options, ZIOCliDefault}

import java.time.LocalDate
import java.util.UUID

object Main extends ZIOCliDefault {

//  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] = {
//
//    val options: Options[Boolean] = Options.boolean("local").alias("l")
//    val postgresUrl: Options[String] = Options.text("postgres-url")
//    val postgresUsername: Options[String] = Options.text("postgres-username")
//    val postgresPassword: Options[String] = Options.text("postgres-password")
//    val mongoDbUrl: Options[String] = Options.text("mongodb-url")
//
//    val arguments: Args[String] = Args.text("repository")
//    val postgresUrlArgs: Args[String] = Args.text("postgres url")
//    val help: HelpDoc = HelpDoc.p("Creates a copy of an existing repository")
//
//    val command: Command[(Boolean, String)] = Command("clone").subcommands(
//      Command("clone", options, arguments).withHelp(help),
//      Command("postgres url", postgresUrl, postgresUrlArgs))
//
//    // Define val cliApp using CliApp.make
//    val cliApp = CliApp.make(
//      name = "Sample Git",
//      version = "1.1.0",
//      summary = text("Sample implementation of git clone"),
//      command = command
//    ) {
//      // Implement logic of CliApp
//      case _ => printLine("executing git clone")
//    }
//
//
//    val customerDto = Seq(
//      CustomerDto(
//        id = UUID.randomUUID(),
//        age = 35,
//        dob = LocalDate.of(1988, 6, 8),
//        firstName = "David",
//        lastName = "Parr"
//      )
//    )
//
//    val program = for{
//      mongoDbMigratorService <- ZIO.service[MongoDbMigratorService]
//      postgresMigratorService <- ZIO.service[PostgresMigratorService]
//      _ <- postgresMigratorService.performPostgresMigration(customerDto)
//    } yield ()
//
//    program.provide(
//      MongoDbConnection.live,
//      MongoDbMigratorService.live,
//      CustomerRepository.live,
//      PostgresMigratorService.live
//    )
//  }

  override def cliApp: CliApp[Any with ZIOAppArgs with Scope, Any, Any] = {
    val postgresUrl: Options[String] = Options.text("postgres-url")
    val postgresUsername: Options[String] = Options.text("postgres-username")
    val postgresPassword: Options[String] = Options.text("postgres-password")
    val mongoDbUrl: Options[String] = Options.text("mongodb-url")

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
      postgresMigratorService <- ZIO.service[PostgresMigratorService]
      _ <- postgresMigratorService.performPostgresMigration(customerDto, postgresUrl, postgresUsername, postgresPassword)
    } yield()

    program.provide(
      PostgresMigratorService.live,
      CustomerRepository.live
    )
  }


}
