package ar.edu.unq.epers.tactics.service

import ar.edu.unq.epers.tactics.modelo.Aventurero
import ar.edu.unq.epers.tactics.modelo.Party

interface PartyService {
    fun crear(party: Party): Party
    fun actualizar(party: Party): Party
    fun recuperar(partyId: Long): Party
    fun recuperarTodas(): List<Party>
    fun agregarAventureroAParty(idDeLaParty: Long, aventurero: Aventurero): Aventurero
    fun recuperarOrdenadas(orden: Orden, direccion: Direccion, pagina: Int?): PartyPaginadas

    fun eliminarTodo()
    fun billeteraDeParty(id: Long): String
}

class PartyPaginadas (var parties: List<Party>, var total: Int)

enum class Orden {
    PODER,
    VICTORIAS,
    DERROTAS
}

enum class Direccion {
    ASCENDENTE,
    DESCENDENTE
}