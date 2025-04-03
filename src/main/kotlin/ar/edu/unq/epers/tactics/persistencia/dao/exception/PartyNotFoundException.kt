package ar.edu.unq.epers.tactics.persistencia.dao.exception

class PartyNotFoundException(private val idDeLaParty: Long) : RuntimeException(){
        override val message: String?
            get() = "No se encontró Party con ID $idDeLaParty"

        companion object {
            private val serialVersionUID = 1L
        }
}