package ar.edu.unq.epers.tactics.persistencia.dao.hibernate

import ar.edu.unq.epers.tactics.modelo.Pelea
import ar.edu.unq.epers.tactics.persistencia.dao.PeleaDAO
import ar.edu.unq.epers.tactics.service.Estado

import ar.edu.unq.epers.tactics.service.PeleasPaginadas
import ar.edu.unq.epers.tactics.service.runner.HibernateTransactionRunner

class HibernatePeleaDAO: PeleaDAO {

    override fun crear(pelea: Pelea): Pelea {
        val session = HibernateTransactionRunner.currentSession
        val newId = session.save(pelea)
        pelea.id = (newId as Long?)!!
        return pelea
    }

    override fun actualizar(pelea: Pelea) {
        val session = HibernateTransactionRunner.currentSession
        session.update(pelea)
    }

    override fun recuperar(idPelea: Long): Pelea {
        val session = HibernateTransactionRunner.currentSession
        return session.get(Pelea::class.java, idPelea)
    }

    override fun peleaEnCurso(idParty: Long): Pelea {
        val session = HibernateTransactionRunner.currentSession

        val hql = "from Pelea p where p.party.id = :partyId and p.estado = :estadoEnCurso"

        val query = session.createQuery(hql, Pelea::class.java)

        query.setParameter("partyId", idParty)
        query.setParameter("estadoEnCurso", Estado.ENCURSO)
        query.setMaxResults(1)

        return query.singleResult
    }

    override fun deleteAll() {
        val session = HibernateTransactionRunner.currentSession
        session.createNativeQuery("SET FOREIGN_KEY_CHECKS=0;").executeUpdate()
        session.createNativeQuery("truncate table Pelea").executeUpdate()
        session.createNativeQuery("SET FOREIGN_KEY_CHECKS=1;").executeUpdate()
    }

    override fun recuperarOrdenadas(partyId: Long, pagina: Int?): PeleasPaginadas {
        val session = HibernateTransactionRunner.currentSession

        val hqlCount = "select count(*) from Pelea p where p.party.id = :partyId group by p.party.id"
        val queryCount = session.createQuery(hqlCount)
        queryCount.setParameter("partyId", partyId)
        var totalPeleas = queryCount.getSingleResult() as Long

        val hql = "from Pelea p where p.party.id = :partyId order by p.timestamp desc"
        val query = session.createQuery(hql, Pelea::class.java)
        query.setParameter("partyId", partyId)
        query.setMaxResults(10)
        query.setFirstResult(10 * pagina!!)
        var peleasPaginadas = query.resultList

        return PeleasPaginadas(peleasPaginadas , totalPeleas.toInt())
    }
}
