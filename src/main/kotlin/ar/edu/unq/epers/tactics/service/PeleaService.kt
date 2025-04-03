package ar.edu.unq.epers.tactics.service

import ar.edu.unq.epers.tactics.modelo.Aventurero
import ar.edu.unq.epers.tactics.modelo.Habilidad
import ar.edu.unq.epers.tactics.modelo.Pelea

interface PeleaService {
    // La party comienza una pelea. Mientras esta en pelea no puede iniciar otra pelea.
    fun iniciarPelea(partyId: Long, partyEnemiga: String): Pelea

    // La party deja de estar peleando y todos los aventureros vuelven a su estado inicial.
    fun terminarPelea(partyId: Long): Pelea

    fun estaEnPelea(partyId: Long): Boolean

    // Dada la lista de enemigos, el aventurero debe utilizar sus Tacticas para elegir que habilidad utilizar sobre que receptor.
    // TODO Cada vez que se resuelve un turno y se genera una nueva habilidad, hay que guardar esa habilidad en la pelea.
    fun resolverTurno(peleaId: Long, aventureroId: Long, enemigos: List<Aventurero>): Habilidad

    // El aventurero debe resolver la habilidad que esta siendo ejecutada sobre el.
    // TODO Cada vez que se recibe una habilidad, hay que guardar esa habilidad en la pelea.
    fun recibirHabilidad(idPelea: Long, aventureroId: Long, habilidadId: Habilidad): Aventurero

    fun recuperarOrdenadas(partyId: Long, pagina: Int?): PeleasPaginadas

    // Test protocol
    fun eliminarTodo()
}

class PeleasPaginadas(var peleas: List<Pelea>, var total: Int)

enum class Estado {
    GANADA,
    PERDIDA,
    ENCURSO
}