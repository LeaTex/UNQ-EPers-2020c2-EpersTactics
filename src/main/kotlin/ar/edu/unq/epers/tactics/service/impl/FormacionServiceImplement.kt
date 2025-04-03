package ar.edu.unq.epers.tactics.service.impl

import ar.edu.unq.epers.tactics.modelo.AtributoDeFormacion
import ar.edu.unq.epers.tactics.modelo.Clase
import ar.edu.unq.epers.tactics.modelo.Requerimiento
import ar.edu.unq.epers.tactics.modelo.Formacion
import ar.edu.unq.epers.tactics.persistencia.dao.mongodb.FormacionDAO
import ar.edu.unq.epers.tactics.persistencia.dao.hibernate.HibernatePartyDAO
import ar.edu.unq.epers.tactics.service.FormacionService
import ar.edu.unq.epers.tactics.service.runner.HibernateTransactionRunner.runTrx

class FormacionServiceImplement (private val dao: FormacionDAO, private val daoP: HibernatePartyDAO) : FormacionService {

    override fun crearFormacion(nombreFormacion: String, requerimientos: List<Requerimiento>,stats: List<AtributoDeFormacion>) : Formacion {
        val formacion : Formacion = Formacion(nombreFormacion, requerimientos, stats)
        return dao.guardarFormacion(formacion)
    }

    override fun todasLasFormaciones(): List<Formacion> {
        return dao.todasLasFormaciones()
    }

    override fun atributosQueCorresponden(partyId: Long): List<AtributoDeFormacion> {
        var clasesContadas = this.clasesContadasDeParty(partyId)
        if (clasesContadas.isEmpty()) return listOf()

        return dao.atributosQueCorresponden( clasesContadas )
    }

    override fun formacionesQuePosee(partyId: Long): List<Formacion> {
        var clasesContadas = this.clasesContadasDeParty(partyId)
        if (clasesContadas.isEmpty()) return listOf()

        return dao.formacionesQuePoseeSegun(clasesContadas)
    }

    private fun clasesContadasDeParty(partyId: Long): Map<String, Int> {
        var clasesParty = runTrx{
            var party = daoP.recuperar(partyId)
            party.aventureros.map { it.clases } }

        return clasesParty.flatten().groupingBy { it.nombre }.eachCount()
    }

    override fun deleteAll() {
        dao.deleteAll()
    }


}