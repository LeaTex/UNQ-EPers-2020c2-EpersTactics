package ar.edu.unq.epers.tactics.persistencia.dao.hibernate

import ar.edu.unq.epers.tactics.modelo.Party
import ar.edu.unq.epers.tactics.persistencia.dao.PartyDAO
import ar.edu.unq.epers.tactics.service.*
import ar.edu.unq.epers.tactics.service.runner.HibernateTransactionRunner
import org.hibernate.query.Query

class HibernatePartyDAO: PartyDAO {

    override fun crear(party: Party): Party {
        val session = HibernateTransactionRunner.currentSession
        val partyId = session.save(party)
        party.id = (partyId as Long?)!!

        return party
    }

    override fun actualizar(party: Party): Party{
        val session = HibernateTransactionRunner.currentSession
        session.update(party)
        return party
    }

    override fun recuperar(idDeLaParty: Long): Party {
        val session = HibernateTransactionRunner.currentSession

        return session.get(Party::class.java, idDeLaParty)
    }

    override fun recuperarTodas(): List<Party> {
        val session = HibernateTransactionRunner.currentSession
        return session.createQuery("from Party", Party::class.java).resultList
    }

    override fun eliminarTodo() {
        val session = HibernateTransactionRunner.currentSession
        val nombreDeTablas = session.createNativeQuery("show tables").resultList
        session.createNativeQuery("SET FOREIGN_KEY_CHECKS=0;").executeUpdate()
        nombreDeTablas.forEach { result ->
            var tabla = ""
            when(result){
                is String -> tabla = result
                is Array<*> -> tabla= result[0].toString()
            }
            session.createNativeQuery("truncate table $tabla").executeUpdate()
        }
        session.createNativeQuery("SET FOREIGN_KEY_CHECKS=1;").executeUpdate()
    }

    override fun recuperarOrdenadas(orden: Orden, direccion: Direccion, pagina: Int?): PartyPaginadas {
        val session = HibernateTransactionRunner.currentSession

        /* Poder: La suma de daño físico + poder mágico + precisión física de todos sus aventureros.
        Victorias: La suma de peleas ganadas que tenga la party.
        Derrotas: La suma de peleas perdidas que tenga la party.*/

        val hqlCount = "select count(*) from Party"
        val queryCount = session.createQuery(hqlCount)
        var totalParties = queryCount.getSingleResult() as Long

        var hqlSelect = "select party from Party party "
        var hqlJoin = ""
        var hqlGroup = "group by party.id, party.imagenURL, party.nombre, party.numeroDeAventureros "
        var hqlOrder = ""
        var hqlDir = "ASC"
        if (direccion == Direccion.DESCENDENTE) { hqlDir = "DESC" }
        var estadoPelea : Estado? = null

        when (orden) {
            Orden.PODER -> {
                hqlJoin = "left outer join party.aventureros aventurero "
                hqlOrder = "order by sum(aventurero.danioFisico + aventurero.poderMagico + aventurero.precisionFisica) "

            }
            Orden.DERROTAS -> {
                hqlJoin = "left outer join Pelea pelea with pelea.party = party and pelea.estado = :estadoPelea "
                hqlOrder = "order by count(pelea.estado) "
                estadoPelea = Estado.PERDIDA
            }
            Orden.VICTORIAS -> {
                hqlJoin = "left outer join Pelea pelea with pelea.party = party and pelea.estado = :estadoPelea "
                hqlOrder = "order by count(pelea.estado) "
                estadoPelea = Estado.GANADA
            }
        }

        var hql = hqlSelect + hqlJoin + hqlGroup + hqlOrder + hqlDir
        var query = session.createQuery(hql, Party::class.java)
        if (estadoPelea != null) { query.setParameter("estadoPelea", estadoPelea) }

        query.setMaxResults(10)
        query.setFirstResult(10 * pagina!!)
        var partiesPaginadas = query.resultList

        return PartyPaginadas(partiesPaginadas , totalParties.toInt())

    }
}