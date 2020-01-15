plugins {
    kotlin("jvm") version "1.3.61"
    application
}

group = "dev.boisselle"
version = "0.0.1-SNAPSHOT"

application {
    mainClassName = "io.ktor.server.netty.EngineMain"
}

repositories {
    mavenCentral()
    jcenter()
    maven(url = "https://dl.bintray.com/kodein-framework/kodein-dev")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.9.5")
    implementation("com.fasterxml.jackson.core:jackson-core:2.9.5")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.9.5")

    val ktorVer = "1.3.0-rc2"
    fun ktor(module: String) =  "io.ktor:ktor-$module:$ktorVer"
    implementation(ktor("server-netty"))
    implementation(ktor("jackson")) {
        exclude("com.fasterxml.jackson.module")
        exclude("com.fasterxml.jackson.core")
    }

    implementation("ch.qos.logback:logback-classic:1.2.3")

    val kodeinVer = "7.0.0-dev-23"
    fun kodein(module: String) = "org.kodein.di:kodein-di-$module:$kodeinVer"
    implementation(kodein("generic-jvm"))
    implementation(kodein("framework-ktor-server-controller-jvm"))

    fun exposed(module: String) = "org.jetbrains.exposed:exposed-$module:0.20.2"
    implementation(exposed("core"))
    implementation(exposed("dao"))
    implementation(exposed("jdbc"))
    implementation(exposed("java-time"))

    implementation("com.zaxxer:HikariCP:3.4.1")
    implementation("org.postgresql:postgresql:42.2.9")

    implementation("com.qovery:client:0.2.1")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    jar {
        manifest {
            attributes(
                mapOf(
                    "Main-Class" to application.mainClassName
                )
            )
        }
        configurations.runtimeClasspath.get().filter {
            it.name.endsWith(".jar")
        }.forEach { jar ->
            from(zipTree(jar))
        }
    }
}