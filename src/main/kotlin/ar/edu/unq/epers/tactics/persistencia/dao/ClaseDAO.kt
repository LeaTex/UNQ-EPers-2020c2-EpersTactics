package ar.edu.unq.epers.tactics.persistencia.dao

import ar.edu.unq.epers.tactics.modelo.*

interface ClaseDAO {

    fun crearClase(clase: Clase)
    fun crearMejora(mejora: Mejora)
    fun requerir(nombreDeClaseConRequerimiento: String, nombreDeClaseAnterior: String)
    fun buscarMejoraEntreClases(nombreDeClaseActual: String, nombreDeClaseDeseada: String): Mejora
    fun puedeMejorarseAClaseDesde(clasePosterior: Clase, clases: List<String>): Boolean
    fun posiblesMejorasSegunClases(clases: List<String>): Set<Mejora>
    fun caminoMasRentable(puntosDeExperiencia: Int, clases: List<String>, atributo: Atributo): List<Mejora>

    fun clear()

}
