package ar.edu.unq.epers.tactics.service

import ar.edu.unq.epers.tactics.modelo.Aventurero
import ar.edu.unq.epers.tactics.modelo.Party

interface AventureroLeaderboardService {
    // Devuelve el aventurero que más daño físico realizó en peleas
    fun mejorGuerrero(): Aventurero

    // Devuelve el aventurero que más daño mágico realizó en peleas
    fun mejorMago(): Aventurero

    // Devuelve el aventurero que más curo en peleas
    fun mejorCurandero(): Aventurero

    // Devuelve el aventurero que más meditó en peleas
    fun buda(): Aventurero
}