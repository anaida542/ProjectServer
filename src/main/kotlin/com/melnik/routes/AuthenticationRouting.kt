package com.melnik.routes

import com.melnik.model.AuthRequest
import com.melnik.model.User
import com.melnik.security.hashing.HashingService
import com.melnik.security.hashing.SaltedHash
import com.melnik.security.token.TokenClaim
import com.melnik.security.token.TokenConfig
import com.melnik.security.token.TokenService
import com.melnik.services.UserService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.authenticationRouting(
    userService: UserService,
    hashingService: HashingService,
    tokenService: TokenService,
    tokenConfig: TokenConfig
) {
    route("/authentication") {

        post("/signUp") {
            val request = call.receiveNullable<AuthRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }

            val isBlank = request.email.isBlank() && request.password.isBlank()
            if (isBlank) {
                call.respond(HttpStatusCode.BadRequest, "Data is blank")
                return@post
            }
            val existingUser = userService.getUserByEmail(request.email)
            if (existingUser != null) {
                call.respond(HttpStatusCode.Conflict, "This email is already used")
                return@post
            }
            val saltedHash = hashingService.generateSaltedHash(request.password)
            val user = User(
                email = request.email,
                password = saltedHash.hash,
                salt = saltedHash.salt
            )
            val wasAcknowledged = userService.insertUser(user)
            if (!wasAcknowledged) {
                call.respond(HttpStatusCode.Conflict)
                return@post
            } else {
                val userWithId = userService.getUserByEmail(request.email)
                if (userWithId == null) {
                    call.respond(HttpStatusCode.NotFound)
                    return@post
                }
                val token = tokenService.generate(
                    config = tokenConfig,
                    TokenClaim(
                        name = "userId",
                        value = userWithId.id ?: "error_tag"
                    )
                )
                call.respond(HttpStatusCode.OK, token)
            }
        }

        post("/signIn") {
            val request = call.receiveNullable<AuthRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }

            val user = userService.getUserByEmail(request.email)
            if (user == null) {
                call.respond(HttpStatusCode.NotFound)
                return@post
            }

            val isValidPassword = hashingService.verify(
                value = request.password,
                saltedHash = SaltedHash(
                    hash = user.password,
                    salt = user.salt
                )
            )
            if (!isValidPassword) {
                call.respond(HttpStatusCode.Forbidden)
                return@post
            }

            val token = tokenService.generate(
                config = tokenConfig,
                TokenClaim(
                    name = "userId",
                    value = user.id ?: "error_tag"
                )
            )
            call.respond(HttpStatusCode.OK, token)
        }

        authenticate {
            get("/authenticate") {
                call.respond(HttpStatusCode.OK)
            }

            get("/secret") {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.getClaim("userId", String::class)
                if (userId != null) {
                    call.respond(HttpStatusCode.OK, userId)
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            }
        }
    }
}
