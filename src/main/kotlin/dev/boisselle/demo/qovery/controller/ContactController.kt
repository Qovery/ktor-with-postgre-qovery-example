package dev.boisselle.demo.qovery.controller

import dev.boisselle.demo.qovery.model.Contact
import dev.boisselle.demo.qovery.service.ContactService
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*
import org.kodein.di.DI
import org.kodein.di.generic.instance
import org.kodein.di.ktor.controller.DIController
import org.kodein.di.ktor.di

class ContactController(application: Application) : DIController {

    override val di: DI by di { application }

    override fun Route.getRoutes() {
        val service: ContactService by instance()

        route("/contact") {
            get("/all") {
                call.respond(service.findAll())
            }

            put {
                val participant = call.receive<Contact>()
                val id = service.create(participant)
                call.respond(mapOf("id" to id))
            }

            route("/{id}") {
                get {
                    val id = call.parameters["id"]
                    if (id != null) {
                        val participant = service.findById(id.toInt())
                        if (participant != null) call.respond(participant)
                        else call.respond(HttpStatusCode.NotFound)
                    } else call.respond(HttpStatusCode.BadRequest)
                }

                post {
                    val id = call.parameters["id"]
                    val contact = call.receive<Contact>()
                    if (id != null && id.toInt() == contact.id) {
                        val result = service.update(contact)
                        if (result == null) call.respond(HttpStatusCode.UnprocessableEntity)
                            else call.respond(HttpStatusCode.OK)
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
        }
    }
}