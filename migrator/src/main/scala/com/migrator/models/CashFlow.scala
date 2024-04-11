package com.migrator.models

import org.bson.types.ObjectId

import java.util.Date

case class CashFlow(objectId: ObjectId,
                    amount: Double,
                    delta: Double,
                    createDate: Date,
                    modifiedDate: Date)
