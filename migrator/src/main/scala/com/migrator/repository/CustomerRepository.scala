package com.migrator.repository

import com.migrator.dto.CustomerDto
import com.migrator.utils.PostgresConnection
import zio._
import zio.schema.DeriveSchema
import zio.sql.ConnectionPool
import zio.sql.postgresql.PostgresJdbcModule

import java.time.LocalDate
import java.util.UUID

trait CustomerRepository{
  def insertCustomers(customerDto: Seq[CustomerDto], postgresUrl: String, postgresUsername: String, postgresPassword: String): ZIO[Any, Exception, Int]
}

class CustomerRepositoryImpl extends CustomerRepository with PostgresJdbcModule{

  import Entity._

  override def insertCustomers(customerDto: Seq[CustomerDto], postgresUrl: String, postgresUsername: String, postgresPassword: String): ZIO[Any, Exception, Int] = {
    val customer = customerDto.map(item => Customer(item.id, item.age, item.dob, item.firstName, item.lastName))
    val statement = insertInto(customers)(id, age, dob, firstName, lastName).values(customer)


    execute(statement).provide(
      PostgresConnection.live(postgresUrl, postgresUsername, postgresPassword),
      ConnectionPool.live,
      SqlDriver.live
    )
  }

  private object Entity{
    case class Customer(id: UUID, age: Int, dob: LocalDate, firstName: String, lastName: String)

    implicit val customerSchema = DeriveSchema.gen[Customer]

    val customers = defineTable[Customer]("customer")

    val (id, age, dob, firstName, lastName) = customers.columns
  }

}

object CustomerRepository{
  private def create(): CustomerRepository = new CustomerRepositoryImpl

  lazy val live: ZLayer[Any, Throwable, CustomerRepository] =
    ZLayer.succeed(create)
}
