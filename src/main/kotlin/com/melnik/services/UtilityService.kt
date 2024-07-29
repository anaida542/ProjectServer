package com.melnik.services

import com.melnik.model.Utility

interface UtilityService {

    suspend fun getUtilityListForRegion(regionId: String): List<Utility>

    suspend fun addUtilityList(list: List<Utility>): Boolean

    suspend fun getUtilityById(id: String): Utility?
}
