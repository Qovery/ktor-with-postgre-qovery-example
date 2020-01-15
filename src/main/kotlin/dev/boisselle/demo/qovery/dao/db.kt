package dev.boisselle.demo.qovery.dao

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.`java-time`.date

object Contacts : IntIdTable() {
    val firstname = varchar("firstname", 50)
    val lastname = varchar("lastname", 50)
    val dateOfBirth = date("dateOfBirth")
    val address = reference("address_id", Addresses, ReferenceOption.SET_NULL).nullable()
}

object Addresses : IntIdTable() {
    val line1 = varchar("line1", 255)
    val line2 = varchar("line2", 255)
    val postalCode = varchar("postalCode", 10)
    val city = varchar("city", 50)
    val country = varchar("country", 50)
}