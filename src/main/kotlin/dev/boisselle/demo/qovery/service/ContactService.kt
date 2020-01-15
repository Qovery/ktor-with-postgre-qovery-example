package dev.boisselle.demo.qovery.service

import dev.boisselle.demo.qovery.dao.ContactEntity
import dev.boisselle.demo.qovery.dbQuery
import dev.boisselle.demo.qovery.model.Contact
import dev.boisselle.demo.qovery.model.toModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.transactions.transaction
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.generic.instance

class ContactService(override val di: DI) : DIAware {

    private val voteService: AddressService by instance()

    suspend fun findAll() = dbQuery {
        ContactEntity.all().map(ContactEntity::toModel)
    }

    suspend fun findById(id: Int) = dbQuery {
        ContactEntity
            .findById(id)?.toModel()
    }

    suspend fun create(contact: Contact) = dbQuery {
        return@dbQuery ContactEntity.new {
            firstname = contact.firstname
            lastname = contact.lastname
            address = if (contact.address != null)
                voteService.createOrUpdate(contact.address)
            else null
        }.id.value
    }

    suspend fun update(contact: Contact) =
        withContext(Dispatchers.IO) {
            transaction {
                val addressEntity =
                    if (contact.address != null)
                        voteService.createOrUpdate(contact.address)
                    else null

                ContactEntity.findById(contact.id)?.apply {
                    this.firstname = contact.firstname
                    this.lastname = contact.lastname
                    this.dateOfBirth = contact.dateOfBirth
                    if (address == null) this.address = addressEntity
                }?.toModel()
            }
        }


    suspend fun delete(id: Int) = dbQuery {
        val contact = ContactEntity.findById(id)
        contact?.delete()
        contact?.address?.delete()
    }
}