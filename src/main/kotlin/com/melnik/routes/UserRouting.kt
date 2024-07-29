package com.melnik.routes

import com.melnik.model.*
import com.melnik.services.UserService
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.userRouting(
    userService: UserService
) {
    route("/user") {

        authenticate {

            delete {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.getClaim("userId", String::class)
                val result = userService.deleteUserById(userId ?: "")
                if (result) {
                    call.respond(HttpStatusCode.OK)
                } else {
                    call.respond(HttpStatusCode.InternalServerError)
                }
            }

            route("/address") {

                get("/list") {

                    val principal = call.principal<JWTPrincipal>()
                    val userId = principal?.getClaim("userId", String::class)
                    val list = userService.getAddressesForUser(userId ?: "")
                    call.respond(list)
                }

                post {
                    val address = call.receiveNullable<Address>() ?: kotlin.run {
                        call.respond(HttpStatusCode.BadRequest)
                        return@post
                    }
                    val principal = call.principal<JWTPrincipal>()
                    val userId = principal?.getClaim("userId", String::class)
                    val result = userService.addAddress(address.copy(userId = userId))
                    if (result) {
                        call.respond(HttpStatusCode.OK)
                    } else {
                        call.respond(HttpStatusCode.InternalServerError)
                    }
                }

                delete("{addressId}") {
                    val addressId = call.parameters["addressId"]

                    if (addressId == null) {
                        call.respond(HttpStatusCode.BadRequest, "Provide addressId")
                        return@delete
                    }
                    val result = userService.deleteAddressById(addressId)
                    if (result) {
                        call.respond(HttpStatusCode.OK)
                    } else {
                        call.respond(HttpStatusCode.InternalServerError)
                    }
                }
            }

            route("/creditCard") {

                get("/list") {

                    val principal = call.principal<JWTPrincipal>()
                    val userId = principal?.getClaim("userId", String::class)
                    val list = userService.getCreditCardsForUser(userId ?: "")
                    call.respond(list)
                }

                post {
                    val creditCard = call.receiveNullable<CreditCard>() ?: kotlin.run {
                        call.respond(HttpStatusCode.BadRequest)
                        return@post
                    }
                    val principal = call.principal<JWTPrincipal>()
                    val userId = principal?.getClaim("userId", String::class)
                    val result = userService.addCreditCard(creditCard.copy(userId = userId))
                    if (result) {
                        call.respond(HttpStatusCode.OK)
                    } else {
                        call.respond(HttpStatusCode.InternalServerError)
                    }
                }

                delete("{creditCardId}") {
                    val creditCardId = call.parameters["creditCardId"]

                    if (creditCardId == null) {
                        call.respond(HttpStatusCode.BadRequest, "Provide creditCardId")
                        return@delete
                    }
                    val result = userService.deleteCreditCardById(creditCardId)
                    if (result) {
                        call.respond(HttpStatusCode.OK)
                    } else {
                        call.respond(HttpStatusCode.InternalServerError)
                    }
                }
            }
        }
    }
}
