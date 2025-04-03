package ar.edu.unq.epers.tactics.service.impl

import ar.edu.unq.epers.tactics.modelo.Aventurero
import ar.edu.unq.epers.tactics.modelo.Party
import ar.edu.unq.epers.tactics.persistencia.dao.PartyDAO
import ar.edu.unq.epers.tactics.service.*
import ar.edu.unq.epers.tactics.service.runner.HibernateTransactionRunner.runTrx
import javax.persistence.NoResultException

class PartyServiceImplement(private val dao: PartyDAO): PartyService {

    override fun crear(party: Party): Party {
        return runTrx {
            dao.crear(party)
        }
    }

    override fun actualizar(party: Party): Party {
        return runTrx {
            dao.actualizar(party)
        }
    }

    override fun recuperar(idDeLaParty: Long): Party {
        return runTrx {
            dao.recuperar(idDeLaParty)
        }
    }

    override fun recuperarTodas(): List<Party> {
        return runTrx {
            dao.recuperarTodas()
        }
    }

    override fun agregarAventureroAParty(idDeLaParty: Long, aventurero: Aventurero): Aventurero {
        return runTrx {
            val party: Party = dao.recuperar(idDeLaParty)
            party.agregarUnAventurero(aventurero)
            dao.actualizar(party)

            aventurero
        }
    }

    override fun recuperarOrdenadas(orden: Orden, direccion: Direccion, pagina: Int?): PartyPaginadas {
        // programación defensiva
        var pageNumber : Int = pagina ?: 0
        return runTrx { dao.recuperarOrdenadas(orden, direccion, pageNumber) }
    }


    override fun eliminarTodo() {
        runTrx { dao.eliminarTodo() }
    }

    override fun billeteraDeParty(idDeLaParty: Long): String {
        return runTrx {
            dao.recuperar(idDeLaParty).billetera() }
    }
}