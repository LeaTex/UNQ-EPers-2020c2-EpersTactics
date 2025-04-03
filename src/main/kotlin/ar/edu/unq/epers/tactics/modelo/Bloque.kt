package ar.edu.unq.epers.tactics.modelo

import java.sql.Timestamp
import java.util.*

class Bloque() {
    var numero: Long = 0
    var timestamp: String = ""
    var previousHash: String = ""
    var transacciones: MutableList<Transaccion> = mutableListOf()
    var nonce: Int = 0

    constructor(numero: Long, timestamp: String, previousHash: String, transacciones: MutableList<Transaccion>, nonce: Int): this() {
        this.numero = numero
        this.timestamp = timestamp
        this.previousHash = previousHash
        this.transacciones = transacciones
        this.nonce = nonce
    }

    fun toJsonString(): String {
        return "{ numero: " + numero.toString() +
                ", timestamp: " + timestamp +
                ", previousHash: " + previousHash +
                ", transacciones: " + transaccionesToJsonString() +
                ", nonce: " + nonce.toString()
    }

    fun transaccionesToJsonString() : String {
        var jsonStr : String = "["
        this.transacciones.forEach { trx -> jsonStr = jsonStr + trx.toJsonString() + ", " }
        jsonStr = jsonStr + "]"
        return jsonStr
    }
}