package com.migrator.test.service

import com.migrator.models.CashFlow
import com.migrator.service.{PostgresCashFlowService, PostgresCashFlowServiceImpl}
import com.migrator.utils.Utils
import org.bson.types.ObjectId
import org.junit.runner.RunWith
import zio.{Scope, ZIO}
import zio.mock.Expectation
import zio.test.Assertion.equalTo
import zio.test.{Spec, TestEnvironment, ZIOSpecDefault, assertTrue}
import zio.test.junit.ZTestJUnitRunner
import com.migrator.repository.PostgresCashFlowRepositoryImpl
import com.migrator.test.mocks.PostgresDbConnectionMock


import java.util.Date

@RunWith(classOf[ZTestJUnitRunner])
class PostgresCashFlowServiceImplSpec extends ZIOSpecDefault{

  override def spec: Spec[TestEnvironment with Scope, Any] =
    suite("Postgres Cash Flow Service Implementation Spec")(
      test("PostgresCashFlowService should return successfully"){
        val now = new Date()

        val cashFlow: Seq[CashFlow] = Seq(
          CashFlow(new ObjectId("6617beabc24b9e297c8098b9"), 45.00, 0.00, now, now),
          CashFlow(new ObjectId("6617beb1c24b9e297c8098ba"), 100.00, 55.00, now, now),
          CashFlow(new ObjectId("6617bebac24b9e297c8098bb"), 75.00, -25.00, now, now)
        )

        val batchParams: Seq[Seq[(String, Any)]] = Seq(
          Seq(
            ("id", "6617beabc24b9e297c8098b9"),
            ("amount", 45.0),
            ("delta", 0.0),
            ("create_date", Utils.dateToLocalDateTime(now)),
            ("modified_date", Utils.dateToLocalDateTime(now))
          ),
          Seq(
            ("id", "6617beb1c24b9e297c8098ba"),
            ("amount", 100.0),
            ("delta", 55.0),
            ("create_date", Utils.dateToLocalDateTime(now)),
            ("modified_date", Utils.dateToLocalDateTime(now))
          ),
          Seq(
            ("id", "6617bebac24b9e297c8098bb"),
            ("amount", 75.0),
            ("delta", -25.0),
            ("create_date", Utils.dateToLocalDateTime(now)),
            ("modified_date", Utils.dateToLocalDateTime(now))
          )
        )

        val insertQuery =
          """
            |INSERT INTO cash_flow (id, amount, delta, create_date, modified_date)
            |VALUES ({id}, {amount}, {delta}, {create_date}, {modified_date})""".stripMargin

        val postgresDbConnectionMock = PostgresDbConnectionMock.Truncate(
          equalTo("cash_flow"),
          Expectation.unit
        ) ++ PostgresDbConnectionMock.Insert(
          equalTo((insertQuery, batchParams)),
          Expectation.unit
        )

        val testRun = for{
          postgresCashFlowService <- ZIO.service[PostgresCashFlowService]
          _ <- postgresCashFlowService.insertCashFlowRecords(cashFlow)
        } yield assertTrue(true)

        testRun.provide(
          PostgresCashFlowServiceImpl.live,
          PostgresCashFlowRepositoryImpl.live,
          postgresDbConnectionMock
        )
      }
    )
}
