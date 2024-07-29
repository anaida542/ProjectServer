package com.melnik.mongoDB

import com.mongodb.kotlin.client.coroutine.MongoDatabase
import com.melnik.model.Region
import com.melnik.services.RegionService
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import org.litote.kmongo.eq

class RegionServiceImplementation(database: MongoDatabase): RegionService {

    private val regionCollection = database.getCollection<Region>("Regions")

    override suspend fun getRegionList(): List<Region> {

        return regionCollection.find().toList()
    }

    override suspend fun getRegionById(id: String): Region? {

        return regionCollection.find(Region::id eq id).firstOrNull()
    }

    override suspend fun addRegionList(region: Region): Boolean {

        return regionCollection.insertOne(region).wasAcknowledged()
    }
}
