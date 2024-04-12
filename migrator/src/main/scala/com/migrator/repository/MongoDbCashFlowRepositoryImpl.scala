package com.migrator.repository

import com.migrator.models.CashFlow
import com.migrator.utils.Constants.MONGODB_DATABASE
import com.mongodb.client.{MongoClient, MongoCollection, MongoDatabase}
import zio._

trait MongoDbCashFlowRepository{
  def getCashFlowRecords(mongoClient: MongoClient): Task[Seq[CashFlow]]
}

class MongoDbCashFlowRepositoryImpl extends MongoDbCashFlowRepository{

  override def getCashFlowRecords(mongoClient: MongoClient): Task[Seq[CashFlow]] = ZIO.succeed{
    val database: MongoDatabase = mongoClient.getDatabase(MONGODB_DATABASE)
    val collection: MongoCollection[CashFlow] = database.getCollection("cash_flow", classOf[CashFlow])

    var cashFlowDocuments: Seq[CashFlow] = Seq()

    collection.find().forEach{record =>
      cashFlowDocuments = cashFlowDocuments :+ record
    }

    cashFlowDocuments
  }
}

object MongoDbCashFlowRepositoryImpl{
  private def apply: MongoDbCashFlowRepository = new MongoDbCashFlowRepositoryImpl

  lazy val live: ZLayer[Any, Nothing, MongoDbCashFlowRepository] =
    ZLayer.succeed(apply)
}
