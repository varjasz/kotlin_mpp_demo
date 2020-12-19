package hu.vanio.kotlin_mpp_demo

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.html.respondHtml
import io.ktor.http.content.*
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.response.*
import io.ktor.serialization.*
import kotlinx.html.*
import org.slf4j.event.Level

fun main() {

    embeddedServer(Netty, port = 8080, host = "127.0.0.1") {
        install(DefaultHeaders)
        install(Compression)
        install(CallLogging) {
            level = Level.INFO
        }
        install(CORS)
        install(ContentNegotiation) {
            json()
        }
        routing {
            get("/") {
                call.respondHtml {
                    head {
                        title("Kotlin MPP Demo")
                        link(rel = "stylesheet", href = "https://unpkg.com/purecss@1.0.1/build/pure-min.css")
                    }
                    body {
                        div { id = "mainDiv" }
                        script(src = "/static/output.js") {}
                    }
                }
            }
            get("/vehicles") {
                call.respond(
                    createResponse(call.parameters["make"], call.parameters["type"] )
                )
            }
            static("/static") {
                resource("output.js")
            }
        }
    }.start()
}

fun createResponse(make: String?, type: String?): List<Vehicle> {
    return listOf(
        Vehicle(registrationNumber = "ABC123", vinNumber = "AL3453XCVBC", make = "OPEL", type = "ASTRA"),
        Vehicle(registrationNumber = "BBB111", vinNumber = "FHJFHJSSDFG", make = "OPEL", type = "CORSA"),
        Vehicle(registrationNumber = "CCC222", vinNumber = "WETERTER", make = "FORD", type = "FIESTA"),
        Vehicle(registrationNumber = "DDD333", vinNumber = "ALVAZ123", make = "FIAT", type = "STILO"),
        Vehicle(registrationNumber = "GGG444", vinNumber = "DFG345DFHDF", make = "SUZUKI", type = "SWIFT")
    )
        .filter {
            it.make.startsWith(make ?: "")
                    && it.type.startsWith(type ?: "")
        }

}


