package com.melnik.routes

import com.melnik.model.PayedBill
import com.melnik.services.BillService
import com.melnik.services.UtilityService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.billRouting(
    billService: BillService,
    utilityService: UtilityService
) {


    route("/bills") {

        post("/guest") {
            val payedBill = call.receiveNullable<PayedBill>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
            val utilityName = utilityService.getUtilityById(payedBill.utilityId)?.name
            val result = billService.addPayedBill(payedBill.copy(
                utilityName = utilityName ?: ""
            ))
            if (result) {
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.InternalServerError)
            }
        }

        authenticate {

            post {
                val payedBill = call.receiveNullable<PayedBill>() ?: kotlin.run {
                    call.respond(HttpStatusCode.BadRequest)
                    return@post
                }
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.getClaim("userId", String::class)
                val utilityName = utilityService.getUtilityById(payedBill.utilityId)?.name
                val result = billService.addPayedBill(payedBill.copy(
                    userId = userId,
                    utilityName = utilityName ?: ""
                ))
                if (result) {
                    call.respond(HttpStatusCode.OK)
                } else {
                    call.respond(HttpStatusCode.InternalServerError)
                }
            }

            get {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.getClaim("userId", String::class) ?: ""

                val list = billService.getPayedBillsForUser(userId)
                call.respond(HttpStatusCode.OK, list)
            }
        }
    }


}
