package com.budgettracker.repository

import com.budgettracker.dto.CustomerDto
import com.budgettracker.utils.PostgresConnection
import zio._
import zio.schema.DeriveSchema
import zio.sql.ConnectionPool
import zio.sql.postgresql.PostgresJdbcModule

import java.time.LocalDate
import java.util.UUID

class CustomerRepository extends PostgresJdbcModule{

  import Entity._

  def insertCustomers(customerDto: Seq[CustomerDto]): ZIO[Any, Exception, Int] = {
    val customer = customerDto.map(item => Customer(item.id, item.age, item.dob, item.firstName, item.lastName))
    val statement = insertInto(customers)(id, age, dob, firstName, lastName).values(customer)


    execute(statement).provide(PostgresConnection.live, ConnectionPool.live, SqlDriver.live)
  }

  private object Entity{
    case class Customer(id: UUID, age: Int, dob: LocalDate, firstName: String, lastName: String)

    implicit val customerSchema = DeriveSchema.gen[Customer]

    val customers = defineTable[Customer]("customer")

    val (id, age, dob, firstName, lastName) = customers.columns
  }

}

object CustomerRepository{
  private def create() = new CustomerRepository

  lazy val live: ZLayer[Any, Nothing, CustomerRepository] =
    ZLayer.succeed(create)
}
