package com.migrator.codec

import com.migrator.models.Item
import org.bson.{BsonReader, BsonType, BsonWriter}
import org.bson.codecs.{Codec, DecoderContext, EncoderContext}
import org.bson.types.ObjectId

import java.util.Date

class ItemCodec extends Codec[Item]{

  override def decode(bsonReader: BsonReader, decoderContext: DecoderContext): Item = {

    var item = Item(new ObjectId(), "", 0.00, "", new ObjectId(), new Date(), new Date())

    bsonReader.readStartDocument()

    while(bsonReader.readBsonType() != BsonType.END_OF_DOCUMENT){
      item = bsonReader.readName() match {
        case "_id" => item.copy(id = bsonReader.readObjectId())
        case "item_name" => item.copy(itemName = bsonReader.readString())
        case "amount" => item.copy(amount = bsonReader.readDouble())
        case "item_type" => item.copy(itemType = bsonReader.readString())
        case "category" => item.copy(categoryId = bsonReader.readObjectId())
        case "create_date" => item.copy(createDate = new Date(bsonReader.readDateTime()))
        case "modified_date" => item.copy(modifiedDate = new Date(bsonReader.readDateTime()))
        case x => throw new Exception(s"Item field $x not found")
      }
    }

    bsonReader.readEndDocument()

    item
  }

  override def encode(bsonWriter: BsonWriter, t: Item, encoderContext: EncoderContext): Unit = ???

  override def getEncoderClass: Class[Item] = classOf[Item]
}
