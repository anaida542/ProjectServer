package com.melnik.services

import com.melnik.model.PayedBill

interface BillService {

    suspend fun addPayedBill(payedBill: PayedBill): Boolean

    suspend fun getPayedBillsForUser(userId: String): List<PayedBill>
}
