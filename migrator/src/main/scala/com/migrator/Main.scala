package com.migrator

import com.migrator.models.MigratorOptions
import com.migrator.repository.{MongoDbCashFlowRepositoryImpl, MongoDbCategoryRepositoryImpl, MongoDbItemRepositoryImpl}
import com.migrator.service.{MongoDbCashFlowService, MongoDbCashFlowServiceImpl, MongoDbCategoryService, MongoDbCategoryServiceImpl, MongoDbItemService, MongoDbItemServiceImpl, PostgresCashFlowService, PostgresCategoryService, PostgresItemService, PostgresItemServiceImpl, PostgresCategoryServiceImpl, PostgresCashFlowServiceImpl}
import com.migrator.utils.Constants._
import com.migrator.utils.MongoDbConnectionImpl
import zio._
import zio.cli.HelpDoc.Span.text
import zio.cli.{CliApp, Command, Options, ZIOCliDefault}
import com.migrator.repository.{PostgresCashFlowRepositoryImpl, PostgresItemRepositoryImpl, PostgresCategoryRepositoryImpl}

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
      mongoDbCashFlowRecords <- mongoDbCashFlowService.getCashFlowRecords(mongoDbUrl)
      mongoDbItemRecords <- mongoDbItemService.getItemRecords(mongoDbUrl)
      mongoDbCategoryRecords <- mongoDbCategoryService.getCategoryRecords(mongoDbUrl)
      /* POSTGRES IMPLEMENTATION */
      _ <- postgresCashFlowService.insertCashFlowRecords(mongoDbCashFlowRecords)(postgresUrl, postgresUsername, postgresPassword)
      _ <- postgresItemService.insertItemRecords(mongoDbItemRecords)(postgresUrl, postgresUsername, postgresPassword)
      _ <- postgresCategoryService.insertCategoryRecords(mongoDbCategoryRecords)(postgresUrl, postgresUsername, postgresPassword)
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
      /* POSTGRESQL REPOSITORY LAYERS */
      PostgresCashFlowRepositoryImpl.live,
      PostgresItemRepositoryImpl.live,
      PostgresCategoryRepositoryImpl.live,
      /* POSTGRESQL SERVICE LAYERS */
      PostgresCashFlowServiceImpl.live,
      PostgresItemServiceImpl.live,
      PostgresCategoryServiceImpl.live
    )
  }


}
