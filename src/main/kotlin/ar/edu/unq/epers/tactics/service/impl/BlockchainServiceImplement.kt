package ar.edu.unq.epers.tactics.service.impl

import ar.edu.unq.epers.tactics.modelo.Bloque
import ar.edu.unq.epers.tactics.modelo.HashUtils
import ar.edu.unq.epers.tactics.modelo.Transaccion
import ar.edu.unq.epers.tactics.persistencia.dao.mongodb.BloqueDAO
import ar.edu.unq.epers.tactics.service.BlockchainService
import java.sql.Timestamp
import java.util.*

class BlockchainServiceImplement(private val dificultad: Int, private val bloqueDao: BloqueDAO): BlockchainService {

    override fun agregarBloque(numero: Long, timestamp: String, trxs: MutableList<Transaccion>, nonce: Int): Long {
        var previousHash: String = ""

        if (numero > 0) {
            previousHash = blockHash(obtenerBloque(numero - 1))
        }

        // var nonceOk = minarBloque(numero, timestamp, previousHash, trxs)
        if (!validarBloque(numero, timestamp, previousHash, trxs, nonce)) { return 0 }

        val bloque = Bloque(numero, timestamp, previousHash, trxs, nonce)
        return bloqueDao.guardarBloque(bloque)
    }

    override fun validarBloque(numero: Long, timestamp: String, previousHash: String, trxs: MutableList<Transaccion>, nonce: Int): Boolean {
        var blockHash = blockHash(Bloque(numero, timestamp, previousHash, trxs, nonce))
        println("nonce: $nonce  hash: $blockHash")
        return blockHash.startsWith("0".repeat(dificultad))
    }

    override fun obtenerBloque(numero: Long): Bloque {
        return bloqueDao.findEq("numero", numero).first()
    }

    override fun saldoDe(billetera: String): Long {
        // TODO revisar
        return 0
    }

    override fun pruebaDeTrabajo(trxs: MutableList<Transaccion>): Int {
        // TODO revisar
        return 0
    }

    override fun blockHash(bloque: Bloque): String {
        return HashUtils.sha256(bloque.toJsonString())
    }

    override fun minarBloque(numero: Long, timestamp: String, previousHash: String, trxs: MutableList<Transaccion> ): Int {
        var nonce: Int = 0

        while (!validarBloque(numero, timestamp, previousHash, trxs, nonce)) {
            nonce++
        }
        return nonce
    }

    override fun deleteAll() {
        bloqueDao.deleteAll()
    }
}