package dev.boisselle.demo.qovery.service

import dev.boisselle.demo.qovery.dao.AddressEntity
import dev.boisselle.demo.qovery.dao.ContactEntity
import dev.boisselle.demo.qovery.dbQuery
import dev.boisselle.demo.qovery.model.Address
import dev.boisselle.demo.qovery.model.toModel

class AddressService {
    suspend fun findAll() = dbQuery {
        AddressEntity.all().map(AddressEntity::toModel)
    }

    suspend fun findById(id: Int) = dbQuery {
        AddressEntity.findById(id)?.toModel()
    }

    suspend fun findByParticipantId(participantId: Int) =
        dbQuery {
            ContactEntity.findById(participantId)?.address?.toModel()
        }

    fun createOrUpdate(Address: Address) : AddressEntity? {
            val addressEntity =
                when (Address.id) {
                    -1 -> AddressEntity.new { }
                    else -> AddressEntity.findById(Address.id)
                }

        return addressEntity?.apply {
            line1 = Address.line1
            line2 = Address.line2
            postalCode = Address.postalCode
            city = Address.city
            country = Address.country
        }
    }

    suspend fun delete(id: Int) = dbQuery {
        AddressEntity.findById(id)?.delete()
    }
}