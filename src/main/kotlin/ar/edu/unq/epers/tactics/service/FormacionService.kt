package ar.edu.unq.epers.tactics.service

import ar.edu.unq.epers.tactics.modelo.AtributoDeFormacion
import ar.edu.unq.epers.tactics.modelo.Requerimiento
import ar.edu.unq.epers.tactics.modelo.Formacion

interface FormacionService {
    fun crearFormacion(nombreFormacion:String, requerimientos:List<Requerimiento>, stats:List<AtributoDeFormacion>) :Formacion
    fun todasLasFormaciones():List<Formacion>
    fun atributosQueCorresponden(partyId: Long):List<AtributoDeFormacion>
    fun formacionesQuePosee(partyId: Long): List<Formacion>

    fun deleteAll()
}