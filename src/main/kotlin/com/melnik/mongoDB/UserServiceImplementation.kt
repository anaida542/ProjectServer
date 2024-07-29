package com.melnik.mongoDB

import com.mongodb.kotlin.client.coroutine.MongoDatabase
import com.melnik.model.Address
import com.melnik.model.CreditCard
import com.melnik.model.User
import com.melnik.services.UserService
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import org.litote.kmongo.eq

class UserServiceImplementation(database: MongoDatabase): UserService {

    private val userCollection = database.getCollection<User>("User")
    private val addressCollection = database.getCollection<Address>("Addresses")
    private val creditCardCollection = database.getCollection<CreditCard>("CreditCards")

    override suspend fun getUserByEmail(email: String): User? {
        return userCollection.find(User::email eq email).firstOrNull()
    }

    override suspend fun getUserById(id: String): User? {
        return userCollection.find(User::id eq id).firstOrNull()
    }

    override suspend fun insertUser(user: User): Boolean {
        return userCollection.insertOne(user).wasAcknowledged()
    }

    override suspend fun addAddress(address: Address): Boolean {
        return addressCollection.insertOne(address).wasAcknowledged()
    }

    override suspend fun addCreditCard(creditCard: CreditCard): Boolean {
        return creditCardCollection.insertOne(creditCard).wasAcknowledged()
    }

    override suspend fun getAddressesForUser(userId: String): List<Address> {
        return addressCollection.find(Address::userId eq userId).toList()
    }

    override suspend fun getCreditCardsForUser(userId: String): List<CreditCard> {
        return creditCardCollection.find(CreditCard::userId eq userId).toList()
    }

    override suspend fun deleteUserById(id: String): Boolean {
        return userCollection.deleteOne(User::id eq id).wasAcknowledged()
    }

    override suspend fun deleteAddressById(id: String): Boolean {
        return addressCollection.deleteOne(Address::id eq id).wasAcknowledged()
    }

    override suspend fun deleteCreditCardById(id: String): Boolean {
        return creditCardCollection.deleteOne(CreditCard::id eq id).wasAcknowledged()
    }
}
