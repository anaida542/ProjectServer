package com.melnik.model

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
import java.time.LocalDateTime
import java.time.ZoneOffset

@Serializable
data class PayedBill(

    @BsonId
    var id: String? = ObjectId().toString(),
    val utilityId: String,
    val utilityName: String? = null,
    val userId: String? = null,
    val creditCardNumber: String,
    val address: String? = null,
    val date: Long? = LocalDateTime.now().toEpochSecond(ZoneOffset.of("+3")),
    val personalAccount: String? = null,
    val paymentAmount: Double
)
