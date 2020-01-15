package dev.boisselle.demo.qovery.controller

import dev.boisselle.demo.qovery.model.Address
import dev.boisselle.demo.qovery.service.AddressService
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.transactions.transaction
import org.kodein.di.DI
import org.kodein.di.generic.instance
import org.kodein.di.ktor.controller.DIController
import org.kodein.di.ktor.di

class AddressController(application: Application) : DIController {
    override val di: DI by di { application }

    override fun Route.getRoutes() {
        val service: AddressService by instance()

        route("/address") {
            get("/all") {
                call.respond(service.findAll())
            }

            route("/{id}") {
                get {
                    val id = call.parameters["id"]
                    if (id != null) {
                        val vote = service.findById(id.toInt())
                        if (vote != null) call.respond(vote)
                        else call.respond(HttpStatusCode.NotFound)
                    } else call.respond(HttpStatusCode.BadRequest)
                }

                post {
                    val id = call.parameters["id"]
                    val vote = call.receive<Address>()
                    if (id != null && id.toInt() == vote.id) {
                        withContext(Dispatchers.IO) {
                            transaction { service.createOrUpdate(vote) }
                        }
                        call.respond(HttpStatusCode.OK)
                    } else call.respond(HttpStatusCode.BadRequest)
                }

                delete {
                    val id = call.parameters["id"]
                    if (id != null) {
                        service.delete(id.toInt())
                        call.respond(HttpStatusCode.OK)
                    } else call.respond(HttpStatusCode.BadRequest)
                }
            }

            route("/contact/{id}") {
                get {
                    val id = call.parameters["id"]
                    if (id != null) {
                        val address = service.findByParticipantId(id.toInt())
                        if (address != null) call.respond(address)
                        else call.respond(HttpStatusCode.NotFound)
                    } else call.respond(HttpStatusCode.BadRequest)
                }
            }
        }
    }
}