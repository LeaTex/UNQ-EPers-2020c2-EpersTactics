package ar.edu.unq.epers.tactics.service.impl

import ar.edu.unq.epers.tactics.modelo.Aventurero
import ar.edu.unq.epers.tactics.modelo.Habilidad
import ar.edu.unq.epers.tactics.modelo.Pelea
import ar.edu.unq.epers.tactics.modelo.exception.PartyAlreadyInFightException
import ar.edu.unq.epers.tactics.modelo.exception.PartyNotInFightException
import ar.edu.unq.epers.tactics.persistencia.dao.AventureroDAO
import ar.edu.unq.epers.tactics.persistencia.dao.PartyDAO
import ar.edu.unq.epers.tactics.persistencia.dao.PeleaDAO
import ar.edu.unq.epers.tactics.service.PeleaService
import ar.edu.unq.epers.tactics.service.PeleasPaginadas
import ar.edu.unq.epers.tactics.service.runner.HibernateTransactionRunner.runTrx
import javax.persistence.NoResultException

class PeleaServiceImpl(val peleaDAO: PeleaDAO, val partyDAO: PartyDAO, val aventureroDAO: AventureroDAO): PeleaService {

    // La party comienza una pelea. Mientras está en pelea no puede iniciar otra pelea.
    override fun iniciarPelea(idParty: Long, partyEnemiga: String): Pelea {
        if (estaEnPelea(idParty)) { throw PartyAlreadyInFightException(idParty) }

        return runTrx {
            val party = partyDAO.recuperar(idParty)
            val pelea = Pelea(party, partyEnemiga)
            peleaDAO.crear(pelea) }
    }

    override fun estaEnPelea(idParty: Long): Boolean {
        return runTrx {
            try { peleaDAO.peleaEnCurso(idParty).enCurso() }
            catch (e: NoResultException) { false }

        }
    }

    // Dada la lista de enemigos, el aventurero debe utilizar sus Tacticas para elegir que habilidad utilizar sobre que receptor.
    override fun resolverTurno(idPelea: Long, idAventurero: Long, enemigos: List<Aventurero>): Habilidad {
        return runTrx {
            var aventurero = aventureroDAO.recuperar(idAventurero)
            var habilidadParaEjecutar : Habilidad = aventurero.resolverTurno(enemigos)

            // se actualiza el aventurero porque algunas habilidades modifican su estado
            aventureroDAO.actualizar(aventurero)

            habilidadParaEjecutar
        }
    }

    // El aventurero debe resolver la habilidad que esta siendo ejecutada sobre el.
    override fun recibirHabilidad(idPelea: Long, idAventurero: Long, habilidad: Habilidad): Aventurero {
        // idPelea no se utiliza
        // idAventurero no se utiliza porque el aventurero es el objetivo de la habilidad
        return runTrx {
            var aventurero = habilidad.ejecutar()

            aventureroDAO.actualizar(aventurero)
            if (aventurero.defensor != null) {aventureroDAO.actualizar(aventurero.defensor!!)}

            aventurero
        }
    }

    // La party deja de estar peleando y todos los aventureros vuelven a su estado inicial.
    override fun terminarPelea(idParty: Long): Pelea {
        if (!estaEnPelea(idParty)) { throw PartyNotInFightException(idParty) }

        return runTrx {
            val pelea = peleaDAO.peleaEnCurso(idParty)
            pelea.terminar()
            peleaDAO.actualizar( pelea )
            pelea
        }
    }

    override fun recuperarOrdenadas(partyId: Long, pagina: Int?): PeleasPaginadas {
        // programación defensiva
        var pageNumber : Int = pagina ?: 0

        return runTrx {
            // si el count con where es 0 hibernate dispara excepción en lugar de retornar el 0  (cosa'e mandinga)
            try { peleaDAO.recuperarOrdenadas(partyId, pageNumber) }
            catch (e: NoResultException) { PeleasPaginadas(listOf(), 0) }
        }
    }

    override fun eliminarTodo() {
        return runTrx { peleaDAO.deleteAll() }
    }
}