package testAventurero

import ar.edu.unq.epers.tactics.modelo.Aventurero
import ar.edu.unq.epers.tactics.modelo.exception.PartyNotInFightException
import ar.edu.unq.epers.tactics.persistencia.dao.exception.AventureroNotFoundException
import ar.edu.unq.epers.tactics.persistencia.dao.hibernate.HibernateAventureroDAO
import ar.edu.unq.epers.tactics.persistencia.dao.hibernate.HibernatePartyDAO
import ar.edu.unq.epers.tactics.service.AventureroService
import ar.edu.unq.epers.tactics.service.impl.AventureroServiceImpl
import helpers.DataService
import helpers.DataServiceImplement
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class AventureroServiceBasicTest {

    private val service: AventureroService = AventureroServiceImpl(HibernateAventureroDAO(), HibernatePartyDAO())
    private val dataSrv : DataService = DataServiceImplement(service, null, null)

    @Before
    fun setUp() {
        //
    }

    @Test
    fun testGuardar() {
        var adventurer: Aventurero = dataSrv.aventureroMalo()
        var adventurerId = service.guardar(adventurer)

        Assert.assertNotNull( adventurerId )
    }

    @Test
    fun testRecuperar() {
        var adventurer: Aventurero = dataSrv.aventureroFeo()
        val adventurerId = service.guardar(adventurer)

        var adventurerLoaded : Aventurero = service.recuperar(adventurerId)

        Assert.assertEquals(adventurer.nombre, adventurerLoaded.nombre)
        Assert.assertEquals(20, adventurerLoaded.atributos.destreza)
        Assert.assertEquals(2, adventurerLoaded.tacticas.size)
    }

    @Test
    fun testActualizar() {
        var advDefendido: Aventurero = dataSrv.aventureroBueno()
        var advDefensorId = service.guardar(advDefendido)

        var advDefensor: Aventurero = dataSrv.aventureroDefensor()
        service.guardar(advDefensor)

        advDefendido.defensor = advDefensor
        advDefendido.imagenURL = "./temp/image.bmp"

        service.actualizar(advDefendido)

        var adventurerLoaded : Aventurero = service.recuperar(advDefensorId)

        Assert.assertEquals(advDefendido.imagenURL, adventurerLoaded.imagenURL)
        Assert.assertEquals(adventurerLoaded.defensor!!.nombre , advDefensor.nombre)

    }

    @Test(expected = AventureroNotFoundException::class)
    fun testEliminar() {
        var adventurer: Aventurero = dataSrv.aventureroMalo()
        var adventurerId = service.guardar(adventurer)

        var adventurerLoaded : Aventurero = service.recuperar(adventurerId)
        Assert.assertNotNull(adventurerLoaded)

        service.eliminar(adventurerId)

        service.recuperar(adventurerId)
    }

    @After
    fun tearDown() {
        dataSrv.eliminarAventureros()
    }
}