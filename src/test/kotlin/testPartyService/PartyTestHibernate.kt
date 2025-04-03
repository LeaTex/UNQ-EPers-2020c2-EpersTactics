package testPartyService


import ar.edu.unq.epers.tactics.modelo.Aventurero
import ar.edu.unq.epers.tactics.modelo.Party
import ar.edu.unq.epers.tactics.persistencia.dao.hibernate.HibernatePartyDAO
import ar.edu.unq.epers.tactics.service.impl.PartyServiceImplement
import org.junit.Before
import org.junit.Test
import ar.edu.unq.epers.tactics.service.PartyService

import org.junit.After
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue


class PartyTestHibernate {


    lateinit var service: PartyService

    @Before
    fun crearParty() {
        this.service = PartyServiceImplement(HibernatePartyDAO())
    }


    @Test
    fun seCreaUnaPartyNueva() {
        var partyM: Party = Party()
        var party = service.crear(partyM)
        assertTrue(party.id != null)
        assertEquals(party.id?.toInt(), 1)
    }

    @Test
    fun seIntentaRecuperarUnaPartyConNombrePartyPrueba() {
        var partyM = Party()
        partyM.nombre = "Party Prueba"
        var party = service.crear(partyM)
        var partyR = service.recuperar(party.id!!)

        assertEquals(partyR.nombre, partyM.nombre)
    }

    @Test
    fun seQuierenRecuperarTodasLasPartysDelServer() {
        var party1 = Party()
        var partyService1 = service.crear(party1)
        var party2 = Party()
        var partyservice2 = service.crear(party2)

        var todasLasPartys = service.recuperarTodas()

        assertEquals(todasLasPartys[0].id, partyService1.id)
        assertEquals(todasLasPartys[1].id, partyservice2.id)
    }

    @Test
    fun seAgregaUnNuevoAventureroALaParty() {
        var party1 = Party()
        var partyService1 = service.crear(party1)
        var aventurero = Aventurero()
        aventurero.nombre = "Charly"
        var aventurero2 = Aventurero()
        aventurero2.nombre = "Ylrahc"

        assertTrue(partyService1.aventureros.isEmpty())


        var aventureroConPartyService1= service.agregarAventureroAParty(partyService1.id!!, aventurero)
        service.agregarAventureroAParty(partyService1.id!!, aventurero2)
        var partyService1R = service.recuperar(partyService1.id!!)

        assertTrue(aventureroConPartyService1.id == aventurero.id)
        assertTrue(aventureroConPartyService1.party!!.id == partyService1R.id)
        assertEquals(2, partyService1R.aventureros.size)
    }

    @Test
    fun seObtieneLaBilleteraDeUnaParty() {
        var party = Party()
        party.nombre = "Party Prueba"
        service.crear(party)

        var wallet : String = party.billetera()
        assertEquals(32,wallet.length)
        assertEquals("304B03CA069385D1D2C3CC9DA453E117", wallet)
    }

    @After
    fun cleanup() {
        service.eliminarTodo()
    }

}
