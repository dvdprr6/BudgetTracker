package com.api

import com.api.models.{ApiOptions, PostgresConnectionDto}
import com.api.repository.{CashFlowRepositoryImpl, CashFlowService, CashFlowServiceImpl, CategoryRepositoryImpl, ItemRepositoryImpl}
import com.api.service.{CategoryService, CategoryServiceImpl, ItemService, ItemServiceImpl}
import com.api.utils.Constants._
import com.api.utils.PostgresDbConnectionImpl
import zio._
import zio.cli.HelpDoc.Span.text
import zio.cli._
import zio.http.Middleware.CorsConfig
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

    implicit val postgresConnectionDto: PostgresConnectionDto = PostgresConnectionDto(postgresUrl, postgresUsername, postgresPassword)

    val corsConfig = CorsConfig()

    val app: HttpApp[Any] = Routes(
      /* CASH FLOW URLs */
      Method.GET / ROOT_URL / API_URL / CASH_FLOW_URL -> handler(getCashFlow).orDie,
      /* CATEGORY URL */
      Method.GET / ROOT_URL / API_URL / CATEGORY_URL / CATEGORY_GROUP_BY_WITH_TOTALS_URL -> handler(getCategoryWithGroupByTotals).orDie,
      /* ITEM URLs */
      Method.GET / ROOT_URL / API_URL / ITEM_URL -> handler(getItems()).orDie,
      Method.GET / ROOT_URL / API_URL / ITEM_URL / ITEM_BY_CATEGORY_URL / string("categoryId") -> handler((categoryId: String, _: Request) => getItemsByCategoryId(categoryId)).orDie
    ).toHttpApp @@ Middleware.cors(corsConfig)

    val config = Server.Config.default.port(8080)

    val configLayer = ZLayer.succeed(config)

    Server.serve(app).provide(configLayer, Server.live)
  }

  private def getCashFlow(implicit postgresConnectionDto: PostgresConnectionDto): ZIO[Any, Throwable, Response] = {
    val cashFlow = for {
      postgresCashFlowService <- ZIO.service[CashFlowService]
      cashFlowDtoRecords <- postgresCashFlowService.getCashFlowRecords()
    } yield Response.json(cashFlowDtoRecords.toJson)

    val postgresConnectionDtoLayer = ZLayer.succeed(postgresConnectionDto)

    cashFlow.provide(CashFlowServiceImpl.live, CashFlowRepositoryImpl.live, PostgresDbConnectionImpl.live, postgresConnectionDtoLayer)
  }

  private def getCategoryWithGroupByTotals(implicit postgresConnectionDto: PostgresConnectionDto): ZIO[Any, Throwable, Response] = {
    val categoryWithGroupByTotalsRecords = for{
      categoryService <- ZIO.service[CategoryService]
      categoryWithGroupByTotals <- categoryService.getCategoryGroupByWithTotals()
    } yield Response.json(categoryWithGroupByTotals.toJson)

    val postgresConnectionDtoLayer = ZLayer.succeed(postgresConnectionDto)

    categoryWithGroupByTotalsRecords.provide(CategoryServiceImpl.live, CategoryRepositoryImpl.live, PostgresDbConnectionImpl.live, postgresConnectionDtoLayer)
  }

  private def getItems()(implicit postgresConnectionDto: PostgresConnectionDto): ZIO[Any, Throwable, Response] = {
    val itemsByCategoryIdRecords = for{
      itemService <- ZIO.service[ItemService]
      itemByCategoryIdDto <- itemService.getItemRecords()
    } yield Response.json(itemByCategoryIdDto.toJson)

    val postgresConnectionDtoLayer = ZLayer.succeed(postgresConnectionDto)

    itemsByCategoryIdRecords.provide(ItemServiceImpl.live, ItemRepositoryImpl.live, PostgresDbConnectionImpl.live, postgresConnectionDtoLayer)
  }

  private def getItemsByCategoryId(categoryId: String)(implicit postgresConnectionDto: PostgresConnectionDto): ZIO[Any, Throwable, Response] = {
    val itemsByCategoryIdRecords = for{
      itemService <- ZIO.service[ItemService]
      itemByCategoryIdDto <- itemService.getItemsByCategoryId(categoryId)
    } yield Response.json(itemByCategoryIdDto.toJson)

    val postgresConnectionDtoLayer = ZLayer.succeed(postgresConnectionDto)

    itemsByCategoryIdRecords.provide(ItemServiceImpl.live, ItemRepositoryImpl.live, PostgresDbConnectionImpl.live, postgresConnectionDtoLayer)
  }
}
