package com.melnik.model

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class Utility (

    @BsonId
    var id: String? = ObjectId().toString(),
    val regionId: String,
    val name: String,
    val category: Category,
    val phoneNumbers: List<String>
)

