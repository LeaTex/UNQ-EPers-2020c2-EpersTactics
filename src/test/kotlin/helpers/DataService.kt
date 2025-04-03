package helpers

import ar.edu.unq.epers.tactics.modelo.Aventurero

interface DataService {
    fun crearSetDeDatosIniciales()
    fun aventureroMalo(): Aventurero
    fun aventureroFeo(): Aventurero
    fun aventureroBueno(): Aventurero
    fun aventureroDefensor(): Aventurero
    fun crearPartiesParaOrdenarPorPoder()
    fun crearPartiesParaOrdenarPorVictorias()
    fun eliminarTodo()
    fun eliminarAventureros()
    fun eliminarPeleas()
    fun eliminarParties()
}