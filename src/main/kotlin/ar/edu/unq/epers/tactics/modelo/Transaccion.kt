package ar.edu.unq.epers.tactics.modelo

import java.sql.Timestamp
import java.util.*

class Transaccion() {

    var timestamp: String = ""
    var origen: String = ""
    var destino: String = ""
    var cantidad: Int = 0

    constructor(timestamp: String, origen: String, destino: String, cantidad: Int): this() {
        this.timestamp = timestamp
        this.origen = origen
        this.destino = destino
        this.cantidad = cantidad
    }

    fun toJsonString(): String {
        return "{ timestamp: " + this.timestamp +
                    ", origen: " + this.origen +
                    ", destino: " + this.destino +
                    ", cantidad: " + cantidad.toString() + " }"
    }
}