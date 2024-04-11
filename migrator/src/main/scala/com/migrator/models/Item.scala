package com.migrator.models

import org.bson.types.ObjectId

import java.util.Date

case class Item(objectId: ObjectId,
                itemName: String,
                amount: Double,
                itemType: String,
                category: Category,
                createDate: Date,
                modifiedDate: Date)
