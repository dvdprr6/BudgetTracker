package com.migrator.test.service

import com.migrator.models.Category
import com.migrator.repository.{MongoDbCategoryRepository, MongoDbCategoryRepositoryImpl}
import com.migrator.test.mocks.MongoDbConnectionMock
import com.migrator.test.utils.TestUtils
import org.bson.types.ObjectId
import org.junit.runner.RunWith
import zio.{Scope, ZIO}
import zio.mock.Expectation.value
import zio.test.Assertion.equalTo
import zio.test.{Spec, TestEnvironment, ZIOSpecDefault}
import zio.test.junit.ZTestJUnitRunner
import zio.test._

@RunWith(classOf[ZTestJUnitRunner])
class MongoDbCategoryServiceImplSpec extends ZIOSpecDefault{

  override def spec: Spec[TestEnvironment with Scope, Any] =
    suite("Mongo Database Category Service Implementation Spec")(
      test("MongoDbCategoryServiceImpl should return successfully"){
        val expected: Seq[Category] = Seq(
          Category(new ObjectId("6619081b309f7e198f6dd3eb"), "Groceries", TestUtils.stringToDate("2024-04-11 06:42:51"), TestUtils.stringToDate("2024-04-11 06:42:51")),
          Category(new ObjectId("6619082a309f7e198f6dd3ec"), "parking", TestUtils.stringToDate("2024-04-11 06:42:51"), TestUtils.stringToDate("2024-04-11 06:42:51")),
          Category(new ObjectId("66190831309f7e198f6dd3ed"), "salary", TestUtils.stringToDate("2024-04-11 06:42:51"), TestUtils.stringToDate("2024-04-11 06:42:51"))
        )

        val mongoDbConnectionMock = MongoDbConnectionMock.GetMongoRecords.of[(String, Class[Category]), Exception, Seq[Category]](
          equalTo(("categories", classOf[Category])),
          value(expected)
        )

        val testRun = for{
          mongoDbCategoryRepository <- ZIO.service[MongoDbCategoryRepository]
          category <- mongoDbCategoryRepository.getCategoryRecords()
        } yield assert(category)(equalTo(expected))

        testRun.provide(MongoDbCategoryRepositoryImpl.live, mongoDbConnectionMock)

      }
    )
}
