package com.migrator.codec

import com.migrator.models.CashFlow
import org.bson.{BsonReader, BsonType, BsonWriter}
import org.bson.codecs.{Codec, DecoderContext, EncoderContext}
import org.bson.types.ObjectId

import java.util.Date

class CashFlowCodec extends Codec[CashFlow]{

  override def decode(bsonReader: BsonReader, decoderContext: DecoderContext): CashFlow = {
    var cashFlow = CashFlow(new ObjectId(), 0.00, 0.00, new Date, new Date)

    bsonReader.readStartDocument()

    while (bsonReader.readBsonType() != BsonType.END_OF_DOCUMENT){
      cashFlow = bsonReader.readName() match {
        case "_id" => cashFlow.copy(id = bsonReader.readObjectId())
        case "amount" => cashFlow.copy(amount = bsonReader.readDouble())
        case "create_date" => cashFlow.copy(createDate = new Date(bsonReader.readDateTime()))
        case "delta" => cashFlow.copy(delta = bsonReader.readDouble())
        case "modified_date" => cashFlow.copy(modifiedDate = new Date(bsonReader.readDateTime()))
        case x => throw new Exception(s"Cash Flow field $x not found")
      }

    }

    bsonReader.readEndDocument()

    cashFlow
  }

  override def encode(bsonWriter: BsonWriter, t: CashFlow, encoderContext: EncoderContext): Unit = ???

  override def getEncoderClass: Class[CashFlow] = classOf[CashFlow]
}
