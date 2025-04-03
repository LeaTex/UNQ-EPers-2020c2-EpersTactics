package ar.edu.unq.epers.tactics.persistencia.dao.hibernate

import ar.edu.unq.epers.tactics.modelo.Aventurero
import ar.edu.unq.epers.tactics.persistencia.dao.AventureroDAO
import ar.edu.unq.epers.tactics.persistencia.dao.exception.AventureroNotFoundException
import ar.edu.unq.epers.tactics.service.runner.HibernateTransactionRunner
import java.io.Serializable

class HibernateAventureroDAO: AventureroDAO {

    override fun guardar(aventurero: Aventurero): Long {
        val session = HibernateTransactionRunner.currentSession
        return session.save(aventurero) as Long
    }

    override fun recuperar(aventureroId: Long): Aventurero {
        val session = HibernateTransactionRunner.currentSession

        val hql = "from Aventurero a left join fetch a.tacticas " +
                "where a.id = :advId "
        val query = session.createQuery(hql, Aventurero::class.java)
        query.setParameter("advId", aventureroId)

        var results : List<Aventurero> = query.getResultList()

        if(results.isEmpty()){
            throw AventureroNotFoundException(aventureroId)
        }

        return results.get(0)
    }

    override fun actualizar(aventurero: Aventurero): Aventurero {
        val session = HibernateTransactionRunner.currentSession
        session.update(aventurero)
        return aventurero
    }

    override fun eliminar(aventureroId: Long) {
        val session = HibernateTransactionRunner.currentSession
        session.delete( this.recuperar(aventureroId) )
    }

    override fun deleteAll() {
        // Se eliminan en orden: tácticas, aventureros, atributos
        val session = HibernateTransactionRunner.currentSession
        session.createNativeQuery("SET FOREIGN_KEY_CHECKS=0;").executeUpdate()
        session.createNativeQuery("truncate table Tactica").executeUpdate()
        session.createNativeQuery("truncate table Clase").executeUpdate()
        session.createNativeQuery("truncate table Aventurero").executeUpdate()
        session.createNativeQuery("truncate table Atributos").executeUpdate()
        session.createNativeQuery("SET FOREIGN_KEY_CHECKS=1;").executeUpdate()
    }

    override fun mejorGuerrero(): Aventurero {
        val session = HibernateTransactionRunner.currentSession
        val hql = "from Aventurero where a.id in (select id from (select max(a.danioFisicoRealizado) from aventurero a) as maximo)"
        val query = session.createQuery(hql, Aventurero::class.java)

        query.maxResults = 1
        return query.singleResult
    }

    override fun mejorMago(): Aventurero {
        val session = HibernateTransactionRunner.currentSession
        val hql = "from Aventurero where a.id in (select id from (select max(a.danioMagicoRealizado) from aventurero a) as maximo)"
        val query = session.createQuery(hql, Aventurero::class.java)

        query.maxResults = 1
        return query.singleResult
    }

    override fun mejorCurandero(): Aventurero {
        val session = HibernateTransactionRunner.currentSession
        val hql = "from Aventurero where a.id in (select id from (select max(a.curacionesRealizadas) from aventurero a) as maximo)"
        val query = session.createQuery(hql, Aventurero::class.java)

        query.maxResults = 1
        return query.singleResult
    }

    override fun buda(): Aventurero {
        val session = HibernateTransactionRunner.currentSession
        val hql = "from Aventurero a where a.id in (select id from (select max(a.meditaciones) from aventurero a) as maximo)"
        val query = session.createQuery(hql, Aventurero::class.java)

        return query.getResultList().get(0)
    }
}