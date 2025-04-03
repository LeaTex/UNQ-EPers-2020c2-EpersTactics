package ar.edu.unq.epers.tactics.modelo.exception

class PartyAlreadyInFightException(private val partyId: Long) : RuntimeException(){
    override val message: String?
        get() = "La Party con ID $partyId se encuentra en pelea actualmente"

    companion object {
        private val serialVersionUID = 1L
    }

}