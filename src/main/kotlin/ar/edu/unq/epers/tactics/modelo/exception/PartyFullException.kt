package ar.edu.unq.epers.tactics.modelo.exception

class PartyFullException(private val partyId: Long) : RuntimeException(){
    override val message: String?
        get() = "La Party con ID $partyId ya está llena"

    companion object {
        private val serialVersionUID = 1L
    }
}
