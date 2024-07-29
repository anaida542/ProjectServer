package com.melnik.services

import com.melnik.model.Region

interface RegionService {

    suspend fun getRegionList(): List<Region>

    suspend fun getRegionById(id: String): Region?

    suspend fun addRegionList(region: Region): Boolean
}
