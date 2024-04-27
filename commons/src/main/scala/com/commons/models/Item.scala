package com.commons.models

import org.bson.types.ObjectId

import java.util.Date

case class Item(id: ObjectId,
                itemName: String,
                amount: Double,
                itemType: String,
                categoryId: ObjectId,
                createDate: Date,
                modifiedDate: Date)
