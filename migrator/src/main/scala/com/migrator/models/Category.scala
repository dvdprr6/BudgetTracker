package com.migrator.models

import org.bson.types.ObjectId

import java.util.Date

case class Category(id: ObjectId,
                    categoryName: String,
                    createDate: Date,
                    modifiedDate: Date)
