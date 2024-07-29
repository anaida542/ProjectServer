package com.melnik.model

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class CreditCard(

    @BsonId
    var id: String? = ObjectId().toString(),
    val userId: String? = null,
    val cardNumber: String,
    val ccvNumber: String,
    val expDate: String,
    val cardName: String
) {
    init {
        if (id==null) {
            id = ObjectId().toString()
        }
    }
}
