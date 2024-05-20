package com.api

import com.api.models.{ApiOptions, PostgresConnectionDto}
import com.api.repository.{CashFlowService, CashFlowServiceImpl}
import com.api.utils.Constants._
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

    implicit val postgresConnectionDto = PostgresConnectionDto(postgresUrl, postgresUsername, postgresPassword)

    val corsConfig = CorsConfig()

    val app: HttpApp[Any] = Routes(
      Method.GET / ROOT_URL / API_URL / CASH_FLOW_URL -> handler(getCashFlow).orDie,
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

    cashFlow.provide(CashFlowServiceImpl.live)
  }
}
