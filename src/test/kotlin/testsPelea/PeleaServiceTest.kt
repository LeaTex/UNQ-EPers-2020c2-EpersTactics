package testsPelea

import ar.edu.unq.epers.tactics.modelo.AtaqueExitosoRandomizer
import ar.edu.unq.epers.tactics.modelo.exception.PartyAlreadyInFightException
import ar.edu.unq.epers.tactics.modelo.exception.PartyNotInFightException
import ar.edu.unq.epers.tactics.persistencia.dao.hibernate.HibernateAventureroDAO
import ar.edu.unq.epers.tactics.persistencia.dao.hibernate.HibernatePartyDAO
import ar.edu.unq.epers.tactics.persistencia.dao.hibernate.HibernatePeleaDAO
import ar.edu.unq.epers.tactics.service.PeleaService
import ar.edu.unq.epers.tactics.service.PeleasPaginadas
import ar.edu.unq.epers.tactics.service.impl.PartyServiceImplement
import ar.edu.unq.epers.tactics.service.impl.PeleaServiceImpl
import helpers.DataService
import helpers.DataServiceImplement
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert


class PeleaServiceTest {

    lateinit var service: PeleaService

    @Before
    fun setUp() {
        this.service = PeleaServiceImpl(HibernatePeleaDAO(), HibernatePartyDAO(), HibernateAventureroDAO())
        DataServiceImplement(null, service, PartyServiceImplement(HibernatePartyDAO())).crearPartyParaPelea()
        AtaqueExitosoRandomizer.setTestMode(5)
    }

    @Test
    fun testIniciarPeleaUnica() {
        val pelea = service.iniciarPelea(1, "Fiesta Enemiga")
        Assert.assertNotNull( pelea.id )
    }

    @Test
    fun testEstaEnPelea() {
        service.iniciarPelea(1, "Fiesta Enemiga")
        Assert.assertTrue( service.estaEnPelea(1))
    }

    @Test(expected = PartyAlreadyInFightException::class)
    fun testIniciarPeleaMientrasEstaEnPelea() {
        service.iniciarPelea(1, "Fiesta Enemiga")
        service.iniciarPelea(1, "Fiesta Enemiga")
    }

    @Test
    fun testTerminarPeleaIniciada() {
        val peleaIniciada = service.iniciarPelea(1, "Fiesta Enemiga")
        Assert.assertTrue(peleaIniciada.enCurso())

        val peleaTerminada = service.terminarPelea(1)
        Assert.assertEquals(peleaIniciada.id, peleaTerminada.id)
        Assert.assertFalse(peleaTerminada.enCurso())
    }

    @Test(expected = PartyNotInFightException::class)
    fun testTerminarPeleaNoIniciada() {
        service.terminarPelea(1)
    }

    @Test
    fun testPeleasPaginadasParaUnaPartySinPeleas() {
        var paginadas: PeleasPaginadas = service.recuperarOrdenadas(55,0)

        Assert.assertTrue(paginadas.peleas.isEmpty())
        Assert.assertEquals(0, paginadas.total)
    }

    @Test
    fun testPeleasPaginadasParaUnaPartyConUnaPelea() {
        service.iniciarPelea(1, "Partido Enemigo")
        var paginadas: PeleasPaginadas = service.recuperarOrdenadas(1,null)

        Assert.assertEquals(1, paginadas.peleas.size)
        Assert.assertEquals(1, paginadas.total)
    }

    @Test
    fun testPeleasPaginadasParaUnaPartyConTresPeleas() {
        service.iniciarPelea(1, "Partido Enemigo")
        service.terminarPelea(1)
        service.iniciarPelea(1, "Partido Enemigo")
        service.terminarPelea(1)
        service.iniciarPelea(1, "Partido Enemigo")
        var paginadas: PeleasPaginadas = service.recuperarOrdenadas(1,0)

        Assert.assertEquals(3, paginadas.peleas.size)
        Assert.assertEquals(3, paginadas.total)
    }

    @Test
    fun testPaginaUnoParaUnaPartyConDocePeleas() {
        for (i in 1..12) {
            service.iniciarPelea(1, "Partido Enemigo")
            service.terminarPelea(1)
        }

        var paginadas: PeleasPaginadas = service.recuperarOrdenadas(1,0)

        Assert.assertEquals(10, paginadas.peleas.size)
        Assert.assertEquals(12, paginadas.total)

        var peleaReciente = paginadas.peleas.first()
        var peleaAntigua = paginadas.peleas.last()
        Assert.assertTrue(peleaReciente.timestamp > peleaAntigua.timestamp)
    }

    @Test
    fun testPaginaDosParaUnaPartyConDocePeleas() {
        for (i in 1..12) {
            service.iniciarPelea(1, "Partido Enemigo")
            service.terminarPelea(1)
        }

        var paginadas: PeleasPaginadas = service.recuperarOrdenadas(1,1)

        Assert.assertEquals(2, paginadas.peleas.size)
        Assert.assertEquals(12, paginadas.total)
    }

    @After
    fun tearDown() {
        val dataSrv : DataService = DataServiceImplement(null, service, PartyServiceImplement(HibernatePartyDAO()))
        dataSrv.eliminarPeleas()
        dataSrv.eliminarParties()
        AtaqueExitosoRandomizer.setRuntimeMode()
    }
}