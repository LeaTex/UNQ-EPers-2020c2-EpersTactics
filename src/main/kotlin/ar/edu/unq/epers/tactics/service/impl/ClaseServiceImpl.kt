package ar.edu.unq.epers.tactics.service.impl

import ar.edu.unq.epers.tactics.modelo.*
import ar.edu.unq.epers.tactics.modelo.exception.AdventurerCantUpgradeException
import ar.edu.unq.epers.tactics.persistencia.dao.AventureroDAO
import ar.edu.unq.epers.tactics.persistencia.dao.ClaseDAO
import ar.edu.unq.epers.tactics.service.ClaseService
import ar.edu.unq.epers.tactics.service.runner.HibernateTransactionRunner.runTrx

class ClaseServiceImplement(val claseDao: ClaseDAO, val aventureroDao: AventureroDAO): ClaseService {
    override fun crearClase(nombreDeClase: String) {
        var clase = Clase(nombreDeClase)
        claseDao.crearClase(clase)
    }

    override fun crearMejora(nombreDeClasePrevia: String, nombreDeClasePosterior: String, atributos: MutableList<Atributo>, cantidadDeAtributos: Int) {
        var mejora = Mejora(Clase(nombreDeClasePrevia), Clase(nombreDeClasePosterior), atributos, cantidadDeAtributos)
        claseDao.crearMejora(mejora)
    }

    override fun requerir(nombreDeClaseConRequerimiento: String, nombreDeClasePrevia: String) {
        claseDao.requerir(nombreDeClaseConRequerimiento, nombreDeClasePrevia)
    }

    override fun puedeMejorar(aventureroId: Long, mejora: Mejora): Boolean {
        var canImprove: Boolean = runTrx {
            var aventurero = aventureroDao.recuperar(aventureroId)
            var clasesActivas = aventurero.clases.map { it.nombre }

            (aventurero.experiencia >= 1) // tiene al menos 1 punto de experiencia
                    && claseDao.puedeMejorarseAClaseDesde(mejora.clasePosterior, clasesActivas)
        }
        return canImprove
    }

    override fun ganarProficiencia(aventureroId: Long, nombreDeClaseActual: String, nombreDeClaseDeseada: String): Aventurero {

        var mejora = claseDao.buscarMejoraEntreClases(nombreDeClaseActual, nombreDeClaseDeseada)

        if (!this.puedeMejorar(aventureroId, mejora)) { throw AdventurerCantUpgradeException(aventureroId, nombreDeClaseDeseada) }

        return runTrx { var aventurero = aventureroDao.recuperar(aventureroId)
            aventurero.ganarProficiencia(mejora)
            aventureroDao.actualizar(aventurero)
            aventurero
        }
    }

    override fun posiblesMejoras(aventureroId: Long): Set<Mejora> {

        var aventurero = runTrx { aventureroDao.recuperar(aventureroId) }
        if (aventurero.experiencia == 0) return emptySet()

        var clasesActivas = runTrx { aventureroDao.recuperar(aventureroId).clases.map { it.nombre } }

        var mejoras = claseDao.posiblesMejorasSegunClases(clasesActivas)

        return mejoras
    }

    override fun caminoMasRentable(puntosDeExperiencia: Int, aventureroId: Long, atributo: Atributo): List<Mejora> {
        var aventurero= runTrx {aventureroDao.recuperar(aventureroId)}

        var clases = aventurero.clases
        var clasesString= clases.map { c -> c.nombre }

        var mejoras = claseDao.caminoMasRentable(puntosDeExperiencia, clasesString, atributo)

        return mejoras
    }

    override fun clear() {
        claseDao.clear()
    }

}