package com.melnik.model

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class Region(

    @BsonId
    var id: String? = ObjectId().toString(),
    val name: String,
    val imageUrl: String,
)
