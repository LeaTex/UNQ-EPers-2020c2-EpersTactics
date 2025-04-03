package ar.edu.unq.epers.tactics.persistencia.dao

import ar.edu.unq.epers.tactics.modelo.Aventurero

interface AventureroDAO {
    fun guardar (aventurero: Aventurero): Long
    fun recuperar(aventureroId: Long): Aventurero
    fun actualizar(aventurero: Aventurero): Aventurero
    fun eliminar(aventureroId: Long)
    fun deleteAll()
    fun mejorGuerrero(): Aventurero
    fun mejorMago(): Aventurero
    fun mejorCurandero(): Aventurero
    fun buda(): Aventurero
}