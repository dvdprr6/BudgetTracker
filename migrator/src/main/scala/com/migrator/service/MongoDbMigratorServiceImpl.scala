package com.migrator.service

import com.migrator.utils.MongoDbConnection
import zio.{Task, ZLayer}

trait MongoDbMigratorService{
  def performMongoDbMigration(): Task[Unit]
}

class MongoDbMigratorServiceImpl(mongoDbConnection: MongoDbConnection) extends MongoDbMigratorService{

  override def performMongoDbMigration(): Task[Unit] =
    for{
      mongoDbClient <- mongoDbConnection.getMongoClient()
    } yield ()
}

object MongoDbMigratorServiceImpl{
  private def create(mongoDbConnection: MongoDbConnection): MongoDbMigratorService =
    new MongoDbMigratorServiceImpl(mongoDbConnection)

  lazy val live: ZLayer[MongoDbConnection, Throwable, MongoDbMigratorService] =
    ZLayer.fromFunction(create _)
}
