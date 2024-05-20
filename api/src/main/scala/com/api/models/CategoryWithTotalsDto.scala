package com.api.models

case class CategoryWithTotalsDto(
                                id: String,
                                categoryName: String,
                                total: Double,
                                createDate: String,
                                modifiedDate: String
                                )