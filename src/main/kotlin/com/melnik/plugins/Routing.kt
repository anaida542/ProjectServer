package com.melnik.plugins

import com.mongodb.kotlin.client.coroutine.MongoDatabase
import com.melnik.mongoDB.BillServiceImplementation
import com.melnik.mongoDB.RegionServiceImplementation
import com.melnik.mongoDB.UserServiceImplementation
import com.melnik.mongoDB.UtilityServiceImplementation
import com.melnik.routes.*
import com.melnik.security.hashing.HashingService
import com.melnik.security.token.TokenConfig
import com.melnik.security.token.TokenService
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting(
    database: MongoDatabase,
    hashingService: HashingService,
    tokenService: TokenService,
    tokenConfig: TokenConfig
) {

    val regionService = RegionServiceImplementation(database)
    val utilityService = UtilityServiceImplementation(database)
    val userService = UserServiceImplementation(database)
    val billService = BillServiceImplementation(database)

    routing {

        authenticationRouting(userService,hashingService,tokenService,tokenConfig)
        regionRouting(regionService)
        utilityRouting(utilityService)
        billRouting(billService, utilityService)
        userRouting(userService)
    }
}
