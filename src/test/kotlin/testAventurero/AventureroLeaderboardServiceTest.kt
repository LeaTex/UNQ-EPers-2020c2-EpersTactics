package testAventurero

import ar.edu.unq.epers.tactics.modelo.Aventurero
import ar.edu.unq.epers.tactics.persistencia.dao.hibernate.HibernateAventureroDAO
import ar.edu.unq.epers.tactics.persistencia.dao.hibernate.HibernatePartyDAO
import ar.edu.unq.epers.tactics.service.AventureroLeaderboardService
import ar.edu.unq.epers.tactics.service.AventureroService
import ar.edu.unq.epers.tactics.service.impl.AventureroLeaderboardServiceImpl
import ar.edu.unq.epers.tactics.service.impl.AventureroServiceImpl
import helpers.DataService
import helpers.DataServiceImplement
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class AventureroLeaderboardServiceTest {
    private val service: AventureroService = AventureroServiceImpl(HibernateAventureroDAO(), HibernatePartyDAO())
    private val serviceLeaderboard: AventureroLeaderboardService = AventureroLeaderboardServiceImpl(HibernateAventureroDAO())
    private val dataSrv : DataService = DataServiceImplement(service, null, null)

    @Before
    fun setUp() {
        //
    }

    @Test
    fun testRecuperarMejorGuerrero() {
        var aventureroGuerrero: Aventurero = dataSrv.aventureroBueno()
        val adventurerGuerreroId = service.guardar(aventureroGuerrero)
        var aventureroGuerreroRecuperado: Aventurero = service.recuperar(adventurerGuerreroId)

        var aventurero: Aventurero = dataSrv.aventureroMalo()
        service.guardar(aventurero)

        // TODO revisar este test cuando se termine de resolver el turno
        aventureroGuerreroRecuperado.resolverTurno(listOf(aventurero))

        Assert.assertEquals(serviceLeaderboard.mejorGuerrero(), aventureroGuerreroRecuperado)
    }

    @Test
    fun testRecuperarMejorMago() {
        var aventureroMago: Aventurero = dataSrv.aventureroBueno()
        val adventurerMagoId = service.guardar(aventureroMago)
        var aventureroMagoRecuperado: Aventurero = service.recuperar(adventurerMagoId)

        var aventurero: Aventurero = dataSrv.aventureroMalo()
        val aventureroId = service.guardar(aventurero)

        // TODO revisar este test cuando se termine de resolver el turno
        aventureroMagoRecuperado.resolverTurno(listOf(aventurero))

        Assert.assertEquals(serviceLeaderboard.mejorMago(), aventureroMagoRecuperado)
    }

    @Test
    fun testRecuperarMejorCurandero() {
        var aventureroCurandero: Aventurero = dataSrv.aventureroBueno()
        val adventurerCuranderoId = service.guardar(aventureroCurandero)
        var aventureroCuranderoRecuperado: Aventurero = service.recuperar(adventurerCuranderoId)

        var aventurero: Aventurero = dataSrv.aventureroMalo()
        service.guardar(aventurero)
        service.actualizar(aventureroCurandero)

        // TODO revisar este test cuando se termine de resolver el turno
        aventureroCuranderoRecuperado.curar(1)

        Assert.assertEquals(serviceLeaderboard.mejorCurandero(), aventureroCuranderoRecuperado)
    }

    @Test
    fun testRecuperarBuda() {
        var aventureroMeditador: Aventurero = dataSrv.aventureroBueno()
        val adventurerMeditadorId = service.guardar(aventureroMeditador)
        var aventureroMeditadorRecuperado: Aventurero = service.recuperar(adventurerMeditadorId)

        var aventurero: Aventurero = dataSrv.aventureroMalo()
        service.guardar(aventurero)

        aventureroMeditadorRecuperado.meditar()

        Assert.assertEquals(serviceLeaderboard.buda(), aventureroMeditadorRecuperado)
    }

    @After
    fun tearDown() {
        dataSrv.eliminarAventureros()
    }

}