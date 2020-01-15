package dev.boisselle.demo.qovery.dao

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class ContactEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ContactEntity>(Contacts)

    var firstname by Contacts.firstname
    var lastname by Contacts.lastname
    var dateOfBirth by Contacts.dateOfBirth

    var address by AddressEntity optionalReferencedOn Contacts.address
}

class AddressEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<AddressEntity>(Addresses)

    var line1 by Addresses.line1
    var line2 by Addresses.line2
    var postalCode by Addresses.postalCode
    var city by Addresses.city
    var country by Addresses.country
}
