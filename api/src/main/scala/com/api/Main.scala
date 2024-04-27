package com.api

import com.api.models.ApiOptions
import com.api.utils.Constants._
import zio._
import zio.cli.HelpDoc.Span.text
import zio.cli._
import zio.http._

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

    val homeRoute = Method.GET / "greet"  -> handler(Response.text("Hello World!"))

    Server.serve(homeRoute.toHttpApp).provide(Server.default)
  }
}
