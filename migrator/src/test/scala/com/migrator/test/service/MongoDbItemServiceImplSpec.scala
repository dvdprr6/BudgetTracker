package com.migrator.test.service

import com.migrator.models.Item
import com.migrator.repository.{MongoDbItemRepository, MongoDbItemRepositoryImpl}
import com.migrator.test.mocks.MongoDbConnectionMock
import com.migrator.test.utils.Utils
import org.bson.types.ObjectId
import org.junit.runner.RunWith
import zio.{Scope, ZIO}
import zio.mock.Expectation.value
import zio.test.Assertion.equalTo
import zio.test.{Spec, TestEnvironment, ZIOSpecDefault}
import zio.test.junit.ZTestJUnitRunner
import zio.test._

@RunWith(classOf[ZTestJUnitRunner])
class MongoDbItemServiceImplSpec extends ZIOSpecDefault{

  override def spec: Spec[TestEnvironment with Scope, Any] =
    suite("Mongo Database Item Service Implementation Spec")(
      test("MongoDbItemServiceImpl should return successfully"){
        val expected: Seq[Item] = Seq(
          Item(new ObjectId("66190843309f7e198f6dd3ee"), "Bonanza", 80.00, "Expense", new ObjectId("6619081b309f7e198f6dd3eb"), Utils.stringToDate("2024-04-11 06:42:51"), Utils.stringToDate("2024-04-11 06:42:51")),
          Item(new ObjectId("66190858309f7e198f6dd3ef"), "parking", 20.00, "Expense", new ObjectId("6619082a309f7e198f6dd3ec"), Utils.stringToDate("2024-04-11 06:42:51"), Utils.stringToDate("2024-04-11 06:42:51")),
          Item(new ObjectId("66190870309f7e198f6dd3f0"), "Morgan Stanley", 3000.00, "Revenue", new ObjectId("66190831309f7e198f6dd3ed"), Utils.stringToDate("2024-04-11 06:42:51"), Utils.stringToDate("2024-04-11 06:42:51"))
        )

        val mongoDbConnectionMock = MongoDbConnectionMock.GetMongoRecords.of[(String, String, Class[Item]), Exception, Seq[Item]](
          equalTo(("mongoUrl", "items", classOf[Item])),
          value(expected)
        )

        val testRun = for{
          mongoDbItemRepository <- ZIO.service[MongoDbItemRepository]
          item <- mongoDbItemRepository.getItemRecords("mongoUrl")
        } yield assert(item)(equalTo(expected))

        testRun.provide(MongoDbItemRepositoryImpl.live, mongoDbConnectionMock)

      }
    )
}
