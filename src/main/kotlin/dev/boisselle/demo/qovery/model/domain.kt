package dev.boisselle.demo.qovery.model

import dev.boisselle.demo.qovery.dao.AddressEntity
import dev.boisselle.demo.qovery.dao.ContactEntity
import java.time.LocalDate

data class Contact(val id: Int = -1,
                       val firstname: String,
                       val lastname: String,
                       val dateOfBirth: LocalDate,
                       val address: Address? = null)

fun ContactEntity.toModel() =
    Contact(id = this.id.value,
        firstname = this.firstname,
        lastname = this.lastname,
        dateOfBirth = this.dateOfBirth,
        address = this.address?.toModel())

data class Address(val id: Int = -1,
                val line1: String,
                val line2: String,
                val postalCode: String,
                val city: String,
                val country: String)

fun AddressEntity.toModel() : Address =
    Address(id = this.id.value,
        line1 = this.line1,
        line2 = this.line2,
        postalCode = this.postalCode,
        city = this.city,
        country = this.country)