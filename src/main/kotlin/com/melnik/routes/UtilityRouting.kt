package com.melnik.routes

import com.melnik.model.Utility
import com.melnik.services.UtilityService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.utilityRouting(
    utilityService: UtilityService
) {
    route("/utilities") {

        get("/{regionId}") {

            val regionId = call.parameters["regionId"]

            if (regionId == null) {
                call.respond(HttpStatusCode.BadRequest, "Provide regionId")
                return@get
            }

            val utilities = utilityService.getUtilityListForRegion(regionId)
            call.respond(HttpStatusCode.OK, utilities)
        }

        post {

            val utilities = call.receive<List<Utility>>()
            val result = utilityService.addUtilityList(utilities)
            if (result) {
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.BadRequest)
            }
        }
    }
}
