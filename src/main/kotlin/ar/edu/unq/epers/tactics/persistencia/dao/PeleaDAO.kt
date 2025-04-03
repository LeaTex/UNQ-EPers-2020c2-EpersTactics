package ar.edu.unq.epers.tactics.persistencia.dao

import ar.edu.unq.epers.tactics.modelo.Pelea
import ar.edu.unq.epers.tactics.service.PeleasPaginadas

interface PeleaDAO {
    fun crear(pelea: Pelea): Pelea
    fun actualizar(pelea: Pelea)
    fun recuperar(idPelea: Long): Pelea
    fun peleaEnCurso(idParty: Long): Pelea
    fun recuperarOrdenadas(partyId: Long, pagina: Int?): PeleasPaginadas

    fun deleteAll()
}