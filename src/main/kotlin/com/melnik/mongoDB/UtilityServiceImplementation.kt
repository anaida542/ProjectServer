package com.melnik.mongoDB

import com.mongodb.kotlin.client.coroutine.MongoDatabase
import com.melnik.model.Utility
import com.melnik.services.UtilityService
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import org.litote.kmongo.eq

class UtilityServiceImplementation(database: MongoDatabase): UtilityService {

    private val utilityCollection = database.getCollection<Utility>("Utility")

    override suspend fun getUtilityListForRegion(regionId: String): List<Utility> {

        return utilityCollection.find(Utility::regionId eq regionId).toList()
    }

    override suspend fun addUtilityList(list: List<Utility>): Boolean {

        return utilityCollection.insertMany(list).wasAcknowledged()
    }

    override suspend fun getUtilityById(id: String): Utility? {

        return utilityCollection.find(Utility::id eq id).firstOrNull()
    }
}
