package com.melnik.services

import com.melnik.model.Address
import com.melnik.model.CreditCard
import com.melnik.model.User

interface UserService {

    suspend fun getUserByEmail(email: String) : User?

    suspend fun getUserById(id: String) : User?

    suspend fun insertUser(user: User): Boolean

    suspend fun addAddress(address: Address): Boolean

    suspend fun addCreditCard(creditCard: CreditCard): Boolean

    suspend fun getAddressesForUser(userId: String): List<Address>

    suspend fun getCreditCardsForUser(userId: String): List<CreditCard>

    suspend fun deleteUserById(id: String): Boolean

    suspend fun deleteAddressById(id: String): Boolean

    suspend fun deleteCreditCardById(id: String): Boolean
}
