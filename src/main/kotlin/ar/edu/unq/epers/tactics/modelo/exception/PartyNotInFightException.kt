package ar.edu.unq.epers.tactics.modelo.exception

class PartyNotInFightException(private val partyId: Long) : RuntimeException(){
    override val message: String?
        get() = "La Party con ID $partyId no se encuentra en pelea"

    companion object {
        private val serialVersionUID = 1L
    }
}