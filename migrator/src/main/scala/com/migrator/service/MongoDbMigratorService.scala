package com.migrator.service

import com.migrator.utils.MongoDbConnection
import zio.{Task, ZLayer}

class MongoDbMigratorService(mongoDbConnection: MongoDbConnection){

  def performMongoDbMigration(): Task[Unit] =
    for{
      mongoDbClient <- mongoDbConnection.getMongoClient()
    } yield ()
}

object MongoDbMigratorService{
  private def create(mongoDbConnection: MongoDbConnection) =
    new MongoDbMigratorService(mongoDbConnection)

  lazy val live: ZLayer[MongoDbConnection, Any, MongoDbMigratorService] =
    ZLayer.fromFunction(create _)
}
