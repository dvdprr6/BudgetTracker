package com.budgettracker.dto

import java.time.LocalDate
import java.util.UUID

case class CustomerDto(id: UUID,
                       age: Int,
                       dob: LocalDate,
                       firstName: String,
                       lastName: String)
