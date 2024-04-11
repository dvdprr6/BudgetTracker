package com.migrator.service

import com.migrator.models.CashFlow
import com.migrator.repository.{CashFlowRepository, CashFlowRepositoryImpl}
import com.migrator.utils.MongoDbConnection
import zio._

trait MongoDbMigratorService{
  def performMongoDbMigration(mongoDbUrl: String): Task[Seq[CashFlow]]
}

class MongoDbMigratorServiceImpl(mongoDbConnection: MongoDbConnection) extends MongoDbMigratorService{

  override def performMongoDbMigration(mongoDbUrl: String): Task[Seq[CashFlow]] = {
    val program = for{
      cashFlowRepository <- ZIO.service[CashFlowRepository]
      mongoDbClient <- mongoDbConnection.getMongoClient(mongoDbUrl)
      cashFlow <- cashFlowRepository.getCashFlowRecords(mongoDbClient)
      _ = mongoDbClient.close()
    } yield cashFlow

    program.provide(CashFlowRepositoryImpl.live)
  }
}

object MongoDbMigratorServiceImpl{
  private def create(mongoDbConnection: MongoDbConnection): MongoDbMigratorService =
    new MongoDbMigratorServiceImpl(mongoDbConnection)

  lazy val live: ZLayer[MongoDbConnection, Throwable, MongoDbMigratorService] =
    ZLayer.fromFunction(create _)
}
