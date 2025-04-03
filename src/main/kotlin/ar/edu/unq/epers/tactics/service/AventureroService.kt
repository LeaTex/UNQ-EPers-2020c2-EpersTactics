package ar.edu.unq.epers.tactics.service

import ar.edu.unq.epers.tactics.modelo.Aventurero

interface AventureroService {
    fun recuperar(aventureroId: Long): Aventurero
    fun actualizar(aventurero: Aventurero): Aventurero
    fun eliminar(aventureroId: Long)

    fun guardar(aventurero: Aventurero): Long
    fun eliminarTodo()
}