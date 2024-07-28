package com.migrator

import com.migrator.models.{MigratorOptions, PostgresConnectionDto}
import com.migrator.repository.{MongoDbCashFlowRepositoryImpl, MongoDbCategoryRepositoryImpl, MongoDbItemRepositoryImpl}
import com.migrator.service.{MongoDbCashFlowService, MongoDbCashFlowServiceImpl, MongoDbCategoryService, MongoDbCategoryServiceImpl, MongoDbItemService, MongoDbItemServiceImpl, PostgresCashFlowService, PostgresCashFlowServiceImpl, PostgresCategoryService, PostgresCategoryServiceImpl, PostgresItemService, PostgresItemServiceImpl}
import com.migrator.utils.Constants._
import com.migrator.utils.{MongoDbConnectionImpl, PostgresDbConnectionImpl}
import zio._
import zio.cli.HelpDoc.Span.text
import zio.cli.{CliApp, Command, Options, ZIOCliDefault}
import com.migrator.repository.{PostgresCashFlowRepositoryImpl, PostgresCategoryRepositoryImpl, PostgresItemRepositoryImpl}

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

    val mongoDbUrl = ZLayer.succeed(migratorOptions.mongoDbUrl)
    val postgresConnectionDto = ZLayer.succeed(PostgresConnectionDto(postgresUrl, postgresUsername, postgresPassword))

    val program = for {
      /* MONGO DB SERVICES */
      mongoDbCashFlowService <- ZIO.service[MongoDbCashFlowService]
      mongoDbItemService <- ZIO.service[MongoDbItemService]
      mongoDbCategoryService <- ZIO.service[MongoDbCategoryService]
      /* POSTGRES SERVICES */
      postgresCashFlowService <- ZIO.service[PostgresCashFlowService]
      postgresItemService <- ZIO.service[PostgresItemService]
      postgresCategoryService <- ZIO.service[PostgresCategoryService]
      /* MONGO DB IMPLEMENTATION */
      mongoDbCashFlowRecords <- mongoDbCashFlowService.getCashFlowRecords()
      mongoDbItemRecords <- mongoDbItemService.getItemRecords()
      mongoDbCategoryRecords <- mongoDbCategoryService.getCategoryRecords()
      /* POSTGRES IMPLEMENTATION */
      _ <- postgresCashFlowService.insertCashFlowRecords(mongoDbCashFlowRecords)
      _ <- postgresItemService.insertItemRecords(mongoDbItemRecords)
      _ <- postgresCategoryService.insertCategoryRecords(mongoDbCategoryRecords)
    } yield()

    program.provide(
      /* MONGO DB CONNECTION LAYERS */
      MongoDbConnectionImpl.live,
      /* MONGO DB REPOSITORY LAYERS */
      MongoDbCashFlowRepositoryImpl.live,
      MongoDbItemRepositoryImpl.live,
      MongoDbCategoryRepositoryImpl.live,
      /* MONGO DB SERVICE LAYERS */
      MongoDbCashFlowServiceImpl.live,
      MongoDbItemServiceImpl.live,
      MongoDbCategoryServiceImpl.live,
      /* POSTGRES DB CONNECTION LAYERS */
      PostgresDbConnectionImpl.live,
      /* POSTGRESQL REPOSITORY LAYERS */
      PostgresCashFlowRepositoryImpl.live,
      PostgresItemRepositoryImpl.live,
      PostgresCategoryRepositoryImpl.live,
      /* POSTGRESQL SERVICE LAYERS */
      PostgresCashFlowServiceImpl.live,
      PostgresItemServiceImpl.live,
      PostgresCategoryServiceImpl.live,
      /* OTHER LAYERS */
      mongoDbUrl,
      postgresConnectionDto
    )
  }


}
