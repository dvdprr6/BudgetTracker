package com.migrator.test.service

import com.migrator.models.Item
import com.migrator.service.{PostgresItemService, PostgresItemServiceImpl}
import com.migrator.repository.PostgresItemRepositoryImpl
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
class PostgresItemServiceImplSpec extends ZIOSpecDefault{

  override def spec: Spec[TestEnvironment with Scope, Any] =
    suite("Postgres Item Service Implementation Spec")(
      test("PostgresItemService should return successfully"){
        val now = new Date()

        val item: Seq[Item] = Seq(
          Item(new ObjectId("66190843309f7e198f6dd3ee"), "Bonanza", 80.00, "Expense", new ObjectId("6619081b309f7e198f6dd3eb"), now, now),
          Item(new ObjectId("66190858309f7e198f6dd3ef"), "parking", 20.00, "Expense", new ObjectId("6619082a309f7e198f6dd3ec"), now, now),
          Item(new ObjectId("66190870309f7e198f6dd3f0"), "Morgan Stanley", 3000.00, "Revenue", new ObjectId("66190831309f7e198f6dd3ed"), now, now)
        )

        val batchParams: Seq[Seq[(String, Any)]] = Seq(
          Seq(
            ("id", "66190843309f7e198f6dd3ee"),
            ("item_name", "Bonanza"),
            ("amount", 80.0),
            ("item_type", "Expense"),
            ("category_id", "6619081b309f7e198f6dd3eb"),
            ("create_date", Utils.dateToLocalDateTime(now)),
            ("modified_date", Utils.dateToLocalDateTime(now))
          ),
          Seq(
            ("id", "66190858309f7e198f6dd3ef"),
            ("item_name", "parking"),
            ("amount", 20.0),
            ("item_type", "Expense"),
            ("category_id", "6619082a309f7e198f6dd3ec"),
            ("create_date", Utils.dateToLocalDateTime(now)),
            ("modified_date", Utils.dateToLocalDateTime(now))
          ),
          Seq(
            ("id", "66190870309f7e198f6dd3f0"),
            ("item_name", "Morgan Stanley"),
            ("amount", 3000.0),
            ("item_type", "Revenue"),
            ("category_id", "66190831309f7e198f6dd3ed"),
            ("create_date", Utils.dateToLocalDateTime(now)),
            ("modified_date", Utils.dateToLocalDateTime(now))
          )
        )

        val insertQuery =
          """
            | INSERT INTO item (id, item_name, amount, item_type, category_id, create_date, modified_date)
            | VALUES ({id}, {item_name}, {amount}, {item_type}, {category_id}, {create_date}, {modified_date})""".stripMargin

        val postgresDbConnectionMock = PostgresDbConnectionMock.Truncate(
          equalTo("item"),
          Expectation.unit
        ) ++ PostgresDbConnectionMock.Insert(
          equalTo((insertQuery, batchParams)),
          Expectation.unit
        )

        val testRun = for{
          postgresItemService <- ZIO.service[PostgresItemService]
          _ <- postgresItemService.insertItemRecords(item)
        } yield assertTrue(true)

        testRun.provide(
          PostgresItemServiceImpl.live,
          PostgresItemRepositoryImpl.live,
          postgresDbConnectionMock
        )
      }
    )
}
