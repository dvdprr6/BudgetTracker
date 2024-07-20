package com.migrator.test.service

import com.migrator.models.CashFlow
import com.migrator.repository.{MongoDbCashFlowRepository, MongoDbCashFlowRepositoryImpl}
import com.migrator.test.mocks.MongoDbConnectionMock
import com.migrator.test.utils.Utils
import org.bson.types.ObjectId
import org.junit.runner.RunWith
import zio._
import zio.mock.Expectation.value
import zio.test.Assertion.equalTo
import zio.test._
import zio.test.junit.ZTestJUnitRunner

/**
 * REFERENCE: https://www.baeldung.com/scala/zio-test
 * REFERENCE: https://scala.monster/zio-test/
 * REFERENCE: https://zio.dev/zio-mock/
 * REFERENCE: https://stackoverflow.com/questions/65749255/zio-mock-method-that-works-with-generics
 * REFERENCE: https://index.scala-lang.org/zio/zio-mock
 * REFERENCE: https://zio.dev/reference/test/junit-integration/
 */
@RunWith(classOf[ZTestJUnitRunner])
class MongoDbCashFlowServiceImplSpec extends ZIOSpecDefault {

  override def spec: Spec[TestEnvironment with Scope, Any] =
    suite("Mongo Database Cash Flow Service Implementation Spec")(
      test("MongoDbCashFlowService should return successfully"){

        val expected: Seq[CashFlow] = Seq(
          CashFlow(new ObjectId("6617beabc24b9e297c8098b9"), 45.00, 0.00, Utils.stringToDate("2024-04-11 06:42:51"), Utils.stringToDate("2024-04-11 06:42:51")),
          CashFlow(new ObjectId("6617beb1c24b9e297c8098ba"), 100.00, 55.00, Utils.stringToDate("2024-04-11 06:42:51"), Utils.stringToDate("2024-04-11 06:42:51")),
          CashFlow(new ObjectId("6617bebac24b9e297c8098bb"), 75.00, -25.00, Utils.stringToDate("2024-04-11 06:42:51"), Utils.stringToDate("2024-04-11 06:42:51"))
        )

        val mongoDbConnectionMock = MongoDbConnectionMock.GetMongoRecords.of[(String, String, Class[CashFlow]), Exception, Seq[CashFlow]](
          equalTo(("mongoUrl", "cash_flow", classOf[CashFlow])),
          value(expected)
        )

        val testRun = for{
          mongoDbCashFlowRepository <- ZIO.service[MongoDbCashFlowRepository]
          cashFlow <- mongoDbCashFlowRepository.getCashFlowRecords("mongoUrl")
        } yield assert(cashFlow)(equalTo(expected))

        testRun.provide(MongoDbCashFlowRepositoryImpl.live, mongoDbConnectionMock)
      }
    )
}