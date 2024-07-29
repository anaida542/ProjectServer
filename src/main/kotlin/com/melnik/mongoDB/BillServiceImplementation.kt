package com.melnik.mongoDB

import com.mongodb.kotlin.client.coroutine.MongoDatabase
import com.melnik.model.PayedBill
import com.melnik.services.BillService
import kotlinx.coroutines.flow.toList
import org.litote.kmongo.eq

class BillServiceImplementation(database: MongoDatabase): BillService {

    private val billCollection = database.getCollection<PayedBill>("Bills")

    override suspend fun addPayedBill(payedBill: PayedBill): Boolean {

        return billCollection.insertOne(payedBill).wasAcknowledged()
    }

    override suspend fun getPayedBillsForUser(userId: String): List<PayedBill> {

        return billCollection.find(PayedBill::userId eq userId).toList()
    }
}
