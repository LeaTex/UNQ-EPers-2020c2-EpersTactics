package ar.edu.unq.epers.tactics.persistencia.dao.exception

class AventureroNotFoundException (private val idAventurero: Long) : RuntimeException(){
    override val message: String?
        get() = "No se encontró Aventurero con ID $idAventurero"

    companion object {
        private val serialVersionUID = 5L
    }
}