package com.melnik

import com.mongodb.kotlin.client.coroutine.MongoClient
import com.melnik.plugins.*
import com.melnik.security.hashing.SHA256HashingService
import com.melnik.security.token.JwtTokenService
import com.melnik.security.token.TokenConfig
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = System.getenv("PORT").toInt(), module = Application::module)
        .start(wait = true)
}

val uri: String = System.getenv("CONNECTION_STRING_URI")
val mongoDBClient = MongoClient.create(uri)
val database = mongoDBClient.getDatabase("utilpay-db")

val tokenConfig = TokenConfig(
    issuer = "http://0.0.0.0:8000",
    audience = "users",
    expiresIn = 365L * 100L * 60L * 60L * 24L,
    secret = System.getenv("JWT_SECRET")
)
val hashingService = SHA256HashingService()
val tokenService = JwtTokenService()

fun Application.module() {

    configureSecurity(tokenConfig)
    configureMonitoring()
    configureSerialization()
    configureRouting(
        database = database,
        hashingService = hashingService,
        tokenService = tokenService,
        tokenConfig = tokenConfig
    )
}
