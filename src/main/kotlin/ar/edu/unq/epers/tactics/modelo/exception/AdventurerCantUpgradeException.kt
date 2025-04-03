package ar.edu.unq.epers.tactics.modelo.exception

class AdventurerCantUpgradeException(private val aventureroId: Long, private val nombreDeClaseDeseada: String) : RuntimeException() {
    override val message: String?
        get() = "El Aventurero con ID $aventureroId no puede mejorar a la clase $nombreDeClaseDeseada"

    companion object {
        private val serialVersionUID = 1L
    }
}