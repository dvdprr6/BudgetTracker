package com.migrator.models

import org.bson.types.ObjectId

import java.util.Date

case class CashFlow(id: ObjectId,
                    amount: Double,
                    delta: Double,
                    createDate: Date,
                    modifiedDate: Date)
