package dev.boisselle.demo.qovery

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.qovery.client.Qovery
import dev.boisselle.demo.qovery.controller.AddressController
import dev.boisselle.demo.qovery.controller.ContactController
import dev.boisselle.demo.qovery.service.AddressService
import dev.boisselle.demo.qovery.service.ContactService
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.jackson.jackson
import io.ktor.routing.routing
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import org.kodein.di.ktor.controller.controller
import org.kodein.di.ktor.di
import java.text.SimpleDateFormat

fun Application.startup() {

    // Dependency Injection with Kodein-DI Custom Feature
    di {
        bind() from singleton { Qovery() }
        bind() from singleton {
            DatabaseFactory(di)
        }
        bind() from singleton { AddressService() }
        bind() from singleton { ContactService(di) }
    }

    // JSON (De-)serialization
    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
            registerModule(JavaTimeModule())
            dateFormat = SimpleDateFormat()
        }
    }

    // Database initialization
    val dbFactory: DatabaseFactory by di().instance()
    dbFactory.init()

    // Routes using controllers
    routing {
        controller { AddressController(instance()) }
        controller { ContactController(instance()) }
    }
}