package ar.edu.unq.epers.tactics.service

import ar.edu.unq.epers.tactics.modelo.Aventurero
import ar.edu.unq.epers.tactics.modelo.Bloque
import ar.edu.unq.epers.tactics.modelo.Transaccion
import java.sql.Timestamp
import java.util.*

interface BlockchainService {
    fun agregarBloque(numero: Long, timestamp: String, trxs: MutableList<Transaccion>, nonce: Int): Long
    fun validarBloque(numero: Long, timestamp: String, previousHash: String, trxs: MutableList<Transaccion>, nonce: Int): Boolean
    fun obtenerBloque(numero: Long): Bloque
    fun saldoDe(billetera: String): Long
    fun pruebaDeTrabajo(trxs: MutableList<Transaccion>): Int

    fun blockHash(bloque: Bloque): String
    fun minarBloque(numero: Long, timestamp: String, previousHash: String, trxs: MutableList<Transaccion> ): Int

    fun deleteAll()
}