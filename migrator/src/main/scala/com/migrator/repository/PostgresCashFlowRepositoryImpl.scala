//package com.migrator.repository
//
//import com.migrator.models.CashFlow
//import com.migrator.utils.PostgresConnection
//import org.bson.types.ObjectId
//import zio.schema.DeriveSchema
//import zio.{ZIO, ZLayer}
//import zio.sql.ConnectionPool
//import zio.sql.postgresql.PostgresJdbcModule
//
//import java.util.Date
//
//trait PostgresCashFlowRepository{
//  def insert(cashFlow: Seq[CashFlow])(postgresUrl: String, postgresUsername: String, postgresPassword: String): ZIO[Any, Exception, Int]
//}
//
//class PostgresCashFlowRepositoryImpl extends PostgresCashFlowRepository with PostgresJdbcModule{
//
//  import Entity._
//
//  override def insert(cashFlow: Seq[CashFlow])(postgresUrl: String, postgresUsername: String, postgresPassword: String): ZIO[Any, Exception, Int] = {
//    val statement = insertInto(cashFlowTable)(id, amount, delta, createDate, modifiedDate).values(cashFlow)
//
//    execute(statement).provide(
//      PostgresConnection.live(postgresUrl, postgresUsername, postgresPassword),
//      ConnectionPool.live,
//      SqlDriver.live
//    )
//  }
//
//  private object Entity{
//    case class CashFlowYee(id: ObjectId,
//                        amount: Double,
//                        delta: Double,
//                        createDate: Date,
//                        modifiedDate: Date)
//
//
//    implicit val customerSchema = DeriveSchema.gen[CashFlowYee]
//
//    val cashFlowTable = defineTable[CashFlowYee]("cash_flow")
//
//    val (id, amount, delta, createDate, modifiedDate) = cashFlowTable.columns
//  }
//}
//
//object PostgresCashFlowRepositoryImpl{
//  private def apply: PostgresCashFlowRepository = new PostgresCashFlowRepositoryImpl
//
//  lazy val live: ZLayer[Any, Throwable, PostgresCashFlowRepository] =
//    ZLayer.succeed(apply)
//}
