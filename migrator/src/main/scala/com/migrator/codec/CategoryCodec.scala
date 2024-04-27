package com.migrator.codec

import com.commons.models.Category
import org.bson.{BsonReader, BsonType, BsonWriter}
import org.bson.codecs.{Codec, DecoderContext, EncoderContext}
import org.bson.types.ObjectId

import java.util.Date

class CategoryCodec extends Codec[Category]{

  override def decode(bsonReader: BsonReader, decoderContext: DecoderContext): Category = {
    var category = Category(new ObjectId(), "", new Date(), new Date())

    bsonReader.readStartDocument()

    while(bsonReader.readBsonType() != BsonType.END_OF_DOCUMENT){
      category = bsonReader.readName() match {
        case "_id" => category.copy(id = bsonReader.readObjectId())
        case "category_name" => category.copy(categoryName = bsonReader.readString())
        case "create_date" => category.copy(createDate = new Date(bsonReader.readDateTime()))
        case "modified_date" => category.copy(modifiedDate = new Date(bsonReader.readDateTime()))
        case x => throw new Exception(s"Category field $x not found")
      }
    }

    bsonReader.readEndDocument()

    category
  }

  override def encode(bsonWriter: BsonWriter, t: Category, encoderContext: EncoderContext): Unit = ???

  override def getEncoderClass: Class[Category] = classOf[Category]
}
