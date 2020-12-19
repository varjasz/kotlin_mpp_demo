package hu.vanio.kotlin_mpp_demo

import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.html.*
import kotlinx.html.dom.append
import kotlinx.html.js.*
import org.w3c.dom.HTMLInputElement
import kotlinx.browser.document
import kotlin.coroutines.CoroutineContext
import kotlinx.dom.clear

/**
 * Client entry point
 */
fun main() {
    VehiclePage.init()
}

object VehiclePage : CoroutineScope {

    private val httpClient = HttpClient {
        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
    }

    /**
     * Using coroutines to execute async http requests
     */
    override val coroutineContext: CoroutineContext by lazy { Job() }

    private val mainDiv by lazy { document.getElementById("mainDiv")!! }
    private val resultDiv by lazy { document.getElementById("resultDiv")!! }
    private val vehicleMake by lazy { document.getElementById("vehicleMake") as HTMLInputElement }
    private val vehicleType by lazy { document.getElementById("vehicleType") as HTMLInputElement }

    /**
     * Create the initial client page
     */
    fun init() {
        mainDiv.append {
            style { +"div {margin: 10px; padding:10px;}" }
            style { +"input {text-transform: uppercase;}" }
            style { +".bluebackground {background: AliceBlue;}" }
            div(classes = "bluebackground") {
                form(classes = "pure-form pure-form-stacked") {
                    autoComplete = false
                    onSubmit = "return false;"
                    fieldSet {
                        legend { +"Search parameters" }
                        label {
                            +"Vehicle make"
                            textInput { id = "vehicleMake" }
                        }
                        label {
                            +"Vehicle type"
                            textInput { id = "vehicleType" }
                        }
                        br()
                        button(classes = "pure-button pure-button-primary") {
                            +"Search"
                            onClickFunction = {
                                launch {
                                    showResult(search(vehicleMake.value, vehicleType.value))
                                }
                            }
                        }
                    }
                }
            }
            div(classes = "bluebackground") { id = "resultDiv" }
        }
        showResult(listOf())
    }

    /**
     * Show the results in html table
     */
    private fun showResult(vehicleList: List<Vehicle>) {
        resultDiv.clear()
        resultDiv.append {
            legend { +"Results" }
            br()
            table(classes = "pure-table pure-table-bordered") {
                thead {
                    tr {
                        th { +"Registration num." }
                        th { +"Vin number" }
                        th { +"Make" }
                        th { +"Type" }
                    }
                }
                tbody {
                    vehicleList.forEach {
                        tr {
                            td { +it.registrationNumber }
                            td { +it.vinNumber }
                            td { +it.make }
                            td { +it.type }
                        }
                    }
                }
            }
        }
    }

    /**
     * Make a http request to query the vehicles
     */
    private suspend fun search(make: String?, type: String?): List<Vehicle> {
        return httpClient.get("http://127.0.0.1:8080/vehicles") {
            parameter("make", make?.toUpperCase())
            parameter("type", type?.toUpperCase())
        }
    }
}
