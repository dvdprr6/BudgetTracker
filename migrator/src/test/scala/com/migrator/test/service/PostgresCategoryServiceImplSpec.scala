package com.migrator.test.service

import com.migrator.models.Category
import com.migrator.service.{PostgresCategoryService, PostgresCategoryServiceImpl}
import com.migrator.repository.PostgresCategoryRepositoryImpl
import com.migrator.test.mocks.PostgresDbConnectionMock
import com.migrator.utils.Utils
import org.bson.types.ObjectId
import org.junit.runner.RunWith
import zio.{Scope, ZIO}
import zio.mock.Expectation
import zio.test.Assertion.equalTo
import zio.test.{Spec, TestEnvironment, ZIOSpecDefault, assertTrue}
import zio.test.junit.ZTestJUnitRunner

import java.util.Date

@RunWith(classOf[ZTestJUnitRunner])
class PostgresCategoryServiceImplSpec extends ZIOSpecDefault{

  override def spec: Spec[TestEnvironment with Scope, Any] =
    suite("Postgres Category Service Implementation Spec")(
      test("PostgresCategoryService should return successfully"){
        val now = new Date()

        val category: Seq[Category] = Seq(
          Category(new ObjectId("6619081b309f7e198f6dd3eb"), "Groceries", now, now),
          Category(new ObjectId("6619082a309f7e198f6dd3ec"), "parking", now, now),
          Category(new ObjectId("66190831309f7e198f6dd3ed"), "salary", now, now)
        )

        val batchParams: Seq[Seq[(String, Any)]] = Seq(
          Seq(
            ("id", "6619081b309f7e198f6dd3eb"),
            ("category_name", "Groceries"),
            ("create_date", Utils.dateToLocalDateTime(now)),
            ("modified_date", Utils.dateToLocalDateTime(now))
          ),
          Seq(
            ("id", "6619082a309f7e198f6dd3ec"),
            ("category_name", "parking"),
            ("create_date", Utils.dateToLocalDateTime(now)),
            ("modified_date", Utils.dateToLocalDateTime(now))
          ),
          Seq(
            ("id", "66190831309f7e198f6dd3ed"),
            ("category_name", "salary"),
            ("create_date", Utils.dateToLocalDateTime(now)),
            ("modified_date", Utils.dateToLocalDateTime(now))
          )
        )

        val insertQuery =
          """
            | INSERT INTO category (id, category_name, create_date, modified_date)
            | values ({id}, {category_name}, {create_date}, {modified_date})""".stripMargin

        val postgresDbConnectionMock = PostgresDbConnectionMock.Truncate(
          equalTo("category"),
          Expectation.unit
        ) ++ PostgresDbConnectionMock.Insert(
          equalTo((insertQuery, batchParams)),
          Expectation.unit
        )

        val testRun = for{
          postgresCategoryService <- ZIO.service[PostgresCategoryService]
          _ <- postgresCategoryService.insertCategoryRecords(category)
        } yield assertTrue(true)

        testRun.provide(
          PostgresCategoryServiceImpl.live,
          PostgresCategoryRepositoryImpl.live,
          postgresDbConnectionMock
        )
      }
    )
}
