package testPartyService

import ar.edu.unq.epers.tactics.modelo.Aventurero
import ar.edu.unq.epers.tactics.modelo.Party
import ar.edu.unq.epers.tactics.persistencia.dao.hibernate.HibernateAventureroDAO
import ar.edu.unq.epers.tactics.service.impl.PartyServiceImplement
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import ar.edu.unq.epers.tactics.persistencia.dao.hibernate.HibernatePartyDAO
import ar.edu.unq.epers.tactics.persistencia.dao.hibernate.HibernatePeleaDAO
import ar.edu.unq.epers.tactics.service.*
import ar.edu.unq.epers.tactics.service.impl.PeleaServiceImpl
import helpers.DataService
import helpers.DataServiceImplement
import org.junit.After

class PartyServiceImplementTest {
    private val partyService: PartyService = PartyServiceImplement(HibernatePartyDAO())
    private val peleaService: PeleaService = PeleaServiceImpl(HibernatePeleaDAO(), HibernatePartyDAO(), HibernateAventureroDAO())

    val dataSrv : DataService = DataServiceImplement(null, peleaService, partyService)

    @Before
    fun setUp() {
        // nothing
    }

    @Test
    fun seCreaPartyYAlRecuperarDevuelveLaPartyIntelliPipol() {
        var party: Party = Party()
        val newPartyID : Long = partyService.crear(party).id!!
        val partyIntellipipol: Party = partyService.recuperar(newPartyID)

        Assert.assertEquals(partyIntellipipol.nombre, party.nombre)
    }

    @Test
    fun seCreaPartyYAlRecuperarTodasDevuelveListaConUnaParty() {
        dataSrv.crearSetDeDatosIniciales()

        val parties = partyService.recuperarTodas()

        Assert.assertFalse(parties.isEmpty())
        Assert.assertEquals(parties[0].nombre, "Party One")
    }

    @Test
    fun seAgregaAventureroALaParty() {
        dataSrv.crearSetDeDatosIniciales()
        var partyOne = partyService.recuperarTodas()[0]

        Assert.assertEquals(3, partyOne.numeroDeAventureros)

        val aventurero = Aventurero()
        partyService.agregarAventureroAParty(partyOne.id!!, aventurero)
        var nroAventurero = partyService.recuperar(partyOne.id!!).numeroDeAventureros

        Assert.assertEquals(4, nroAventurero)
    }

    @Test
    fun testPartyPaginadasSinTenerParties() {
        var paginadas: PartyPaginadas = partyService.recuperarOrdenadas(Orden.PODER, Direccion.ASCENDENTE, 0)

        Assert.assertTrue(paginadas.parties.isEmpty())
        Assert.assertEquals(0, paginadas.total)
    }

    @Test
    fun testPartyPaginadasPorPoder() {
        dataSrv.crearPartiesParaOrdenarPorPoder()

        var paginadasAsc: PartyPaginadas = partyService.recuperarOrdenadas(Orden.PODER, Direccion.ASCENDENTE, 0)
        var paginadasDesc: PartyPaginadas = partyService.recuperarOrdenadas(Orden.PODER, Direccion.DESCENDENTE, 0)

        Assert.assertFalse(paginadasAsc.parties.isEmpty())
        Assert.assertEquals(2, paginadasAsc.total)
        Assert.assertEquals(paginadasAsc.total, paginadasDesc.total)

        // la última en orden ASC es la primera en orden DESC y viceversa
        Assert.assertEquals(paginadasAsc.parties.last().id, paginadasDesc.parties.first().id)
        Assert.assertEquals(paginadasAsc.parties.first().id, paginadasDesc.parties.last().id)
    }

    @Test
    fun testPartyPaginadasPorVictorias() {
        dataSrv.crearPartiesParaOrdenarPorVictorias()

        var paginadasAsc: PartyPaginadas = partyService.recuperarOrdenadas(Orden.VICTORIAS, Direccion.ASCENDENTE, 0)
        var paginadasDesc: PartyPaginadas = partyService.recuperarOrdenadas(Orden.VICTORIAS, Direccion.DESCENDENTE, 0)

        Assert.assertFalse(paginadasAsc.parties.isEmpty())
        Assert.assertEquals(2, paginadasAsc.total)
        Assert.assertEquals(paginadasAsc.total, paginadasDesc.total)

        // la última en orden ASC es la primera en orden DESC y viceversa
        Assert.assertEquals(paginadasAsc.parties.last().id, paginadasDesc.parties.first().id)
        Assert.assertEquals(paginadasAsc.parties.first().id, paginadasDesc.parties.last().id)
    }

    @After
    fun tearDown() {
        dataSrv.eliminarParties()
        dataSrv.eliminarPeleas()
    }
}