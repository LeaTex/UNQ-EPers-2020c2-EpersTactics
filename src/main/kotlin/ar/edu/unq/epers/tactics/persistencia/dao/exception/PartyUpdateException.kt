package ar.edu.unq.epers.tactics.persistencia.dao.exception

import ar.edu.unq.epers.tactics.modelo.Party

class PartyUpdateException(private val party: Party) : RuntimeException(){
    override val message: String?
        get() = "No se pudo actualizar la Party $party"

    companion object {
        private val serialVersionUID = 1L
    }
}