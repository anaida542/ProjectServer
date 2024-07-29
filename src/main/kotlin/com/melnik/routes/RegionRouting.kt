package com.melnik.routes

import com.melnik.model.Region
import com.melnik.services.RegionService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.regionRouting(
    regionService: RegionService
) {
    route("/regions") {

        get {

            val regions = regionService.getRegionList()
            call.respond(HttpStatusCode.OK, regions)
        }

        get("/{id}") {

            val id = call.parameters["id"]

            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Provide Id")
                return@get
            }

            val region = regionService.getRegionById(id)

            if (region == null) {
                call.respond(HttpStatusCode.NotFound, "No region with such Id")
            } else {
                call.respond(HttpStatusCode.OK, region)
            }
        }

        post {

            val region = call.receive<Region>()
            val result = regionService.addRegionList(region)
            if (result) {
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.BadRequest)
            }
        }
    }
}
