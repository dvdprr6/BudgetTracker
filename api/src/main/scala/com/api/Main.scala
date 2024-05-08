package com.api

import com.api.models.ApiOptions
import com.api.utils.Constants._
import com.commons.repository.{PostgresCashFlowRepositoryImpl, PostgresCategoryRepositoryImpl, PostgresItemRepositoryImpl}
import com.commons.service.{PostgresCashFlowService, PostgresCashFlowServiceImpl, PostgresCategoryService, PostgresCategoryServiceImpl, PostgresItemService, PostgresItemServiceImpl}
import zio._
import zio.cli.HelpDoc.Span.text
import zio.cli._
import zio.http._
import zio.json.EncoderOps

object Main extends ZIOCliDefault{

  override def cliApp: CliApp[Any with ZIOAppArgs with Scope, Any, Any] = {
    val postgresUrl: Options[String] = Options.text(OPTIONS_POSTGRES_URL)
    val postgresUsername: Options[String] = Options.text(OPTIONS_POSTGRES_USERNAME)
    val postgresPassword: Options[String] = Options.text(OPTIONS_POSTGRES_PASSWORD)

    val options = (postgresUrl ++ postgresUsername ++ postgresPassword).as(ApiOptions.apply _)
    val apiCommand: Command[ApiOptions] = Command("api", options)

    CliApp.make(
      "Budget Tracker Api",
      "0.0.1",
      text("Api for Budget Tracker"),
      apiCommand
    )(execute)
  }

  private def execute(apiOptions: ApiOptions): ZIO[Any, Throwable, Unit] = {
    val postgresUrl = apiOptions.postgresUrl
    val postgresUsername = apiOptions.postgresUsername
    val postgresPassword = apiOptions.postgresPassword

    /* REFERENCE: https://scalac.io/blog/getting-started-with-zio-http/ */

    val app: HttpApp[Any] = Routes(
      Method.GET / ROOT_URL / API_URL / CATEGORY_URL -> handler(getCategories(postgresUrl, postgresUsername, postgresPassword)).orDie,
      Method.GET / ROOT_URL / API_URL / CASH_FLOW_URL -> handler(getCashFlow(postgresUrl, postgresUsername, postgresPassword)).orDie,
      Method.GET / ROOT_URL / API_URL / ITEM_URL -> handler(getItem(postgresUrl, postgresUsername, postgresPassword)).orDie
    ).toHttpApp

    val config = Server.Config.default.port(8080)

    val configLayer = ZLayer.succeed(config)

    Server.serve(app).provide(configLayer, Server.live)
  }

  private def getCategories(postgresUrl: String, postgresUsername: String, postgresPassword: String): ZIO[Any, Throwable, Response] = {
    val categories = for {
      postgresCategoryService <- ZIO.service[PostgresCategoryService]
      categoryDtoRecords <- postgresCategoryService.getCategoryRecords()(postgresUrl, postgresUsername, postgresPassword)
    } yield Response.json(categoryDtoRecords.toJson)

    categories.provide(PostgresCategoryServiceImpl.live, PostgresCategoryRepositoryImpl.live)
  }

  private def getCashFlow(postgresUrl: String, postgresUsername: String, postgresPassword: String): ZIO[Any, Throwable, Response] = {
    val cashFlow = for {
      postgresCashFlowService <- ZIO.service[PostgresCashFlowService]
      cashFlowDtoRecords <- postgresCashFlowService.getCashFlowRecords()(postgresUrl, postgresUsername, postgresPassword)
    } yield Response.json(cashFlowDtoRecords.toJson)

    cashFlow.provide(PostgresCashFlowServiceImpl.live, PostgresCashFlowRepositoryImpl.live)
  }

  private def getItem(postgresUrl: String, postgresUsername: String, postgresPassword: String): ZIO[Any, Throwable, Response] = {
    val item = for {
      postgresItemService <- ZIO.service[PostgresItemService]
      itemDtoRecords <- postgresItemService.getItemRecords()(postgresUrl, postgresUsername, postgresPassword)
    } yield Response.json(itemDtoRecords.toJson)

    item.provide(PostgresItemServiceImpl.live, PostgresItemRepositoryImpl.live)
  }
}
