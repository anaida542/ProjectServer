package com.melnik.model

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Address(

    @BsonId
    var id: String? = ObjectId().toString(),
    val userId: String? = null,
    val city: String,
    val street: String,
    val house: String,
    val apartment: String? = ""
) {
    init {
        if (id==null) {
            id = ObjectId().toString()
        }
    }
}
