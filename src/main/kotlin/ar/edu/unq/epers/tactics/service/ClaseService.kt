package ar.edu.unq.epers.tactics.service

import ar.edu.unq.epers.tactics.modelo.Atributo
import ar.edu.unq.epers.tactics.modelo.Aventurero
import ar.edu.unq.epers.tactics.modelo.Mejora

interface ClaseService {
    //Crea una nueva clase
    fun crearClase(nombreDeClase: String)

    // Crea la relación de habilitar de una clase a otra, junto con la lista de atributos que un aventurero ganaría
    // y cuánto a cada atributo de ganar proficiencia por este medio.
    fun crearMejora(nombreDeClasePrevia:String, nombreDeClasePosterior:String, atributos: MutableList<Atributo>, cantidadDeAtributos: Int)

    // crea la relación de requerimiento de una clase a otra. Esta relación no puede ser bi-direccional.
    fun requerir(nombreDeClaseConRequerimiento:String, nombreDeClasePrevia:String)

    // Devuelve true si el aventurero puede realizar la obtención de una nueva proficiencia desde una clase a otra.
    fun puedeMejorar(aventureroId: Long, mejora: Mejora): Boolean

    // El aventurero gana la proficiencia desde la clase indicada. Nota: Debe lanzarse una excepción si el aventurero no debería poder ganar
    fun ganarProficiencia(aventureroId: Long, nombreDeClaseActual: String, nombreDeClaseDeseada: String): Aventurero

    // Dado un aventurero, devuelve un set con todas las posibles mejoras.
    fun posiblesMejoras(aventureroId: Long): Set<Mejora>

    // Devuelve la lista de mejoras en las cuales el aventurero debería invertir para maximizar la ganancia en el atributo dado
    fun caminoMasRentable(puntosDeExperiencia: Int, aventureroId: Long, atributo: Atributo): List<Mejora>


    fun clear()
}