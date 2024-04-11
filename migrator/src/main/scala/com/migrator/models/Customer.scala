package com.migrator.models

import java.time.LocalDate
import java.util.UUID

case class Customer(id: UUID,
                    age: Int, dob: LocalDate,
                    firstName: String,
                    lastName: String)
