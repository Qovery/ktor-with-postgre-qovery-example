package dev.boisselle.demo.qovery

import com.qovery.client.DatabaseConfiguration
import com.qovery.client.Qovery
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import dev.boisselle.demo.qovery.dao.Addresses
import dev.boisselle.demo.qovery.dao.Contacts
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.generic.instance

class DatabaseFactory(override val di: DI) : DIAware {
    fun init() {
        Database.connect(hikari())

        transaction {
            SchemaUtils.createMissingTablesAndColumns(Addresses)
            SchemaUtils.createMissingTablesAndColumns(Contacts)
        }
    }

    private fun hikari(): HikariDataSource {
        val qovery: Qovery by instance()

        val databaseConfiguration = qovery.getDatabaseConfiguration("my-postgresql-6132005") ?: getLocalDataSource()

        val host = databaseConfiguration.host
        val port = databaseConfiguration.port
        val username = databaseConfiguration.username
        val password = databaseConfiguration.password

        val config = HikariConfig()

        config.driverClassName = "org.postgresql.Driver"
        config.jdbcUrl = "jdbc:postgresql://$host:$port/postgres"
        config.username = username
        config.password = password
        config.maximumPoolSize = 3
        config.isAutoCommit = false
        config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        config.validate()

        return HikariDataSource(config)
    }

    private fun getLocalDataSource(): DatabaseConfiguration {
        return DatabaseConfiguration("POSTGRESQL", "postgres-qovery", "localhost",5432, "postgres", "docker","11.5")
    }
}

suspend fun <T> dbQuery(block: () -> T): T =
    withContext(Dispatchers.IO) {
        transaction { block() }
    }