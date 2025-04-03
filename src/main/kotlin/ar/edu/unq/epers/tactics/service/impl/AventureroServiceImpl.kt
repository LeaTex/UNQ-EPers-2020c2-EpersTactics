package ar.edu.unq.epers.tactics.service.impl

import ar.edu.unq.epers.tactics.modelo.Aventurero
import ar.edu.unq.epers.tactics.modelo.exception.PartyNotInFightException
import ar.edu.unq.epers.tactics.persistencia.dao.AventureroDAO
import ar.edu.unq.epers.tactics.persistencia.dao.PartyDAO
import ar.edu.unq.epers.tactics.persistencia.dao.exception.AventureroNotFoundException
import ar.edu.unq.epers.tactics.service.AventureroService
import ar.edu.unq.epers.tactics.service.runner.HibernateTransactionRunner.runTrx
import java.io.Serializable

class AventureroServiceImpl(val aventureroDAO: AventureroDAO, val partyDAO: PartyDAO): AventureroService {

    override fun recuperar(aventureroId: Long): Aventurero {
        return runTrx { aventureroDAO.recuperar(aventureroId) }
    }

    override fun actualizar(aventurero: Aventurero): Aventurero {
        return runTrx { aventureroDAO.actualizar(aventurero) }
    }

    override fun eliminar(aventureroId: Long) {
        return runTrx { aventureroDAO.eliminar(aventureroId) }
    }

    override fun guardar(aventurero: Aventurero): Long {
        return runTrx { aventureroDAO.guardar(aventurero) }
    }

    override fun eliminarTodo() {
        return runTrx { aventureroDAO.deleteAll() }
    }
}