package mongTest

import ar.edu.unq.epers.tactics.modelo.*
import ar.edu.unq.epers.tactics.persistencia.dao.hibernate.HibernateAventureroDAO
import ar.edu.unq.epers.tactics.persistencia.dao.hibernate.HibernatePartyDAO
import ar.edu.unq.epers.tactics.persistencia.dao.mongodb.FormacionDAO
import ar.edu.unq.epers.tactics.service.AventureroService
import ar.edu.unq.epers.tactics.service.FormacionService
import ar.edu.unq.epers.tactics.service.PartyService
import ar.edu.unq.epers.tactics.service.impl.AventureroServiceImpl
import ar.edu.unq.epers.tactics.service.impl.FormacionServiceImplement
import ar.edu.unq.epers.tactics.service.impl.PartyServiceImplement
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class FormacionTest {

    lateinit var service: FormacionService
    lateinit var serviceParty: PartyService
    lateinit var serviceAventurero: AventureroService

    @Before
    fun setup() {
        service = FormacionServiceImplement(FormacionDAO(), HibernatePartyDAO())
        serviceParty = PartyServiceImplement(HibernatePartyDAO())
        serviceAventurero= AventureroServiceImpl(HibernateAventureroDAO(), HibernatePartyDAO())
    }

    @Test
    fun seCreaUnaFormacion() {
        var nombre = "boyScout"
        var requerimientos = listOf<Requerimiento>()
        var stats = listOf<AtributoDeFormacion>()
        var formacion = service.crearFormacion(nombre, requerimientos, stats)

        Assert.assertEquals(formacion.nombreFormacion, nombre)
        Assert.assertEquals(formacion.requerimientos, requerimientos)
        Assert.assertEquals(formacion.atributos, stats)
    }

    @Test
    fun seCreanDosFormaciones() {
        var nombre = "boyScout"
        var requerimientos = listOf<Requerimiento>()
        var stats = listOf<AtributoDeFormacion>()
        var formacion = service.crearFormacion(nombre, requerimientos, stats)

        var nombre2 = "ramboMode"
        var requerimientos2 = listOf<Requerimiento>()
        var stats2 = listOf<AtributoDeFormacion>()
        var formacion2 = service.crearFormacion(nombre2, requerimientos2, stats2)

        Assert.assertEquals(formacion.nombreFormacion, nombre)
        Assert.assertEquals(formacion.requerimientos, requerimientos)
        Assert.assertEquals(formacion.atributos, stats)

        Assert.assertEquals(formacion2.nombreFormacion, nombre2)
        Assert.assertEquals(formacion2.requerimientos, requerimientos2)
        Assert.assertEquals(formacion2.atributos, stats2)
    }

    @Test
    fun sePidenTodasLasFormacionesYNoHayNinguna() {
        Assert.assertTrue(service.todasLasFormaciones().isEmpty())
    }

    @Test
    fun sePidenTodasLasFormacionesYAhoraHayUnaFormacion() {
        service.crearFormacion("boyScout", listOf(), listOf())

        var formaciones = service.todasLasFormaciones()
        Assert.assertEquals( 1, formaciones.size)
        Assert.assertTrue(formaciones.first().requerimientos.isEmpty())
    }

    @Test
    fun unaFormacionNoTieneRequerimientos() {
        var nombre = "boyScout"
        var requerimientos = listOf<Requerimiento>()
        var stats = listOf<AtributoDeFormacion>()
        var formacion = service.crearFormacion(nombre, requerimientos, stats)

        Assert.assertEquals(formacion.requerimientos.size, 0)
    }

    @Test
    fun aUnaPartyRecienCreadaNoLeCorrespondenAtributos() {
        var party = Party()

        val partyId = serviceParty.crear(party).id!!

        Assert.assertEquals(service.atributosQueCorresponden(partyId).size, 0)
    }

    @Test
    fun aUnaPartyRecienCreadaPeroCon2AventurerosDeClaseAventureroNoLeCorrespondenAtributos() {

        var nombre = "boyScout"
        var requerimientos = listOf<Requerimiento>()
        var stats = listOf<AtributoDeFormacion>()
        var formacion = service.crearFormacion(nombre, requerimientos, stats)

        var nombre2 = "boyScout"
        var requerimientos2 = listOf<Requerimiento>(Requerimiento(Clase("Guerrero"), 3))
        var stats2 = listOf<AtributoDeFormacion>(AtributoDeFormacion(Atributo.FUERZA,2))
        var formacion2 = service.crearFormacion(nombre, requerimientos2, stats2)


        var party = Party()


        val partyService = serviceParty.crear(party)

        var aventurero1 = Aventurero()
        var idAventurero1 = serviceAventurero.guardar(aventurero1)
        var aventurero1ConId = serviceAventurero.recuperar(idAventurero1)



        var aventurero2 = Aventurero()

        aventurero2.nombre = "aventurero2"
        var idAventurero2 = serviceAventurero.guardar(aventurero2)
        var aventurero2ConId = serviceAventurero.recuperar(idAventurero2)


        serviceParty.agregarAventureroAParty(partyService.id!!, aventurero1ConId)
        serviceParty.agregarAventureroAParty(partyService.id!!, aventurero2ConId)

        var nuestaparty =serviceParty.recuperar(partyService.id!!)

        Assert.assertEquals(nuestaparty.aventureros.size, 2)
        Assert.assertEquals(service.atributosQueCorresponden(nuestaparty.id!!).size, 0)
    }

    @Test
    fun aUnaPartyCon4AventurerosDeClaseAventureroLeCorrespondenAtributos() {

        var nombre = "boyScout"
        var requerimientos = listOf<Requerimiento>(Requerimiento(Clase("Aventurero"), 3))
        var stats = listOf<AtributoDeFormacion>(AtributoDeFormacion(Atributo.FUERZA, 2))
        service.crearFormacion(nombre, requerimientos, stats)

        val partyCreada = serviceParty.crear(Party())

        var aventurero1 = Aventurero()
        aventurero1.nombre = "aventurero1"
        var idAventurero1 = serviceAventurero.guardar(aventurero1)
        var aventurero1ConId = serviceAventurero.recuperar(idAventurero1)

        var aventurero2 = Aventurero()
        aventurero2.nombre = "aventurero2"
        var idAventurero2 = serviceAventurero.guardar(aventurero2)
        var aventurero2ConId = serviceAventurero.recuperar(idAventurero2)

        var aventurero3 = Aventurero()
        aventurero3.nombre = "aventurero3"
        var idAventurero3 = serviceAventurero.guardar(aventurero3)
        var aventurero3ConId = serviceAventurero.recuperar(idAventurero3)

        var aventurero4 = Aventurero()
        aventurero4.nombre = "aventurero4"
        var idAventurero4 = serviceAventurero.guardar(aventurero4)
        var aventurero4ConId = serviceAventurero.recuperar(idAventurero4)

        serviceParty.agregarAventureroAParty(partyCreada.id!!, aventurero1ConId)
        serviceParty.agregarAventureroAParty(partyCreada.id!!, aventurero2ConId)
        serviceParty.agregarAventureroAParty(partyCreada.id!!, aventurero3ConId)
        serviceParty.agregarAventureroAParty(partyCreada.id!!, aventurero4ConId)

        var atributos = service.atributosQueCorresponden(partyCreada.id!!)
        Assert.assertEquals(1, atributos.size)
    }

    @Test
    fun aUnaPartyCon4AventurerosDeClaseAventureroLeCorrespondenAtributosDe2Formaciones() {

        var nombre = "boyScout"
        var requerimientos = listOf<Requerimiento>(Requerimiento(Clase("Aventurero"), 3))
        var stats = listOf<AtributoDeFormacion>(AtributoDeFormacion(Atributo.FUERZA, 2))
        service.crearFormacion(nombre, requerimientos, stats)


        var nombre2 = "ramboMode"
        var requerimientos2 = listOf<Requerimiento>(Requerimiento(Clase("Aventurero"), 1))
        var stats2 = listOf<AtributoDeFormacion>(AtributoDeFormacion(Atributo.INTELIGENCIA, 1))
        service.crearFormacion(nombre2, requerimientos2, stats2)

        val partyCreada = serviceParty.crear(Party())

        var aventurero1 = Aventurero()
        aventurero1.nombre = "aventurero1"
        var idAventurero1 = serviceAventurero.guardar(aventurero1)
        var aventurero1ConId = serviceAventurero.recuperar(idAventurero1)

        var aventurero2 = Aventurero()
        aventurero2.nombre = "aventurero2"
        var idAventurero2 = serviceAventurero.guardar(aventurero2)
        var aventurero2ConId = serviceAventurero.recuperar(idAventurero2)

        var aventurero3 = Aventurero()
        aventurero3.nombre = "aventurero3"
        var idAventurero3 = serviceAventurero.guardar(aventurero3)
        var aventurero3ConId = serviceAventurero.recuperar(idAventurero3)

        var aventurero4 = Aventurero()
        aventurero4.nombre = "aventurero4"
        var idAventurero4 = serviceAventurero.guardar(aventurero4)
        var aventurero4ConId = serviceAventurero.recuperar(idAventurero4)

        serviceParty.agregarAventureroAParty(partyCreada.id!!, aventurero1ConId)
        serviceParty.agregarAventureroAParty(partyCreada.id!!, aventurero2ConId)
        serviceParty.agregarAventureroAParty(partyCreada.id!!, aventurero3ConId)
        serviceParty.agregarAventureroAParty(partyCreada.id!!, aventurero4ConId)

        var atributos = service.atributosQueCorresponden(partyCreada.id!!)
        Assert.assertEquals(2, atributos.size)
    }

    @Test
    fun aUnaPartyCon4AventurerosDeClaseAventureroNoLeCorrespondenAtributosDePeroHay2FormacionesCreadas() {

        var nombre = "boyScout"
        var requerimientos = listOf<Requerimiento>(Requerimiento(Clase("Mago"), 3))
        var stats = listOf<AtributoDeFormacion>(AtributoDeFormacion(Atributo.INTELIGENCIA, 2))
        service.crearFormacion(nombre, requerimientos, stats)

        var nombre2 = "ramboMode"
        var requerimientos2 = listOf<Requerimiento>(Requerimiento(Clase("Guerrero"), 1))
        var stats2 = listOf<AtributoDeFormacion>(AtributoDeFormacion(Atributo.FUERZA, 1))
        service.crearFormacion(nombre2, requerimientos2, stats2)

        val partyCreada = serviceParty.crear(Party())

        var aventurero1 = Aventurero()
        aventurero1.nombre = "aventurero1"
        var idAventurero1 = serviceAventurero.guardar(aventurero1)
        var aventurero1ConId = serviceAventurero.recuperar(idAventurero1)

        var aventurero2 = Aventurero()
        aventurero2.nombre = "aventurero2"
        var idAventurero2 = serviceAventurero.guardar(aventurero2)
        var aventurero2ConId = serviceAventurero.recuperar(idAventurero2)

        var aventurero3 = Aventurero()
        aventurero3.nombre = "aventurero3"
        var idAventurero3 = serviceAventurero.guardar(aventurero3)
        var aventurero3ConId = serviceAventurero.recuperar(idAventurero3)

        var aventurero4 = Aventurero()
        aventurero4.nombre = "aventurero4"
        var idAventurero4 = serviceAventurero.guardar(aventurero4)
        var aventurero4ConId = serviceAventurero.recuperar(idAventurero4)

        serviceParty.agregarAventureroAParty(partyCreada.id!!, aventurero1ConId)
        serviceParty.agregarAventureroAParty(partyCreada.id!!, aventurero2ConId)
        serviceParty.agregarAventureroAParty(partyCreada.id!!, aventurero3ConId)
        serviceParty.agregarAventureroAParty(partyCreada.id!!, aventurero4ConId)

        var atributos = service.atributosQueCorresponden(partyCreada.id!!)
        Assert.assertEquals(0, atributos.size)
    }

    @Test
    fun aUnaPartyCon4AventurerosDeClaseAventureroLeCorrespondenAtributosDeInteligenciaFuerzaYDEstrezacon3FormacionesCreadas() {

        var nombre = "boyScout"
        var requerimientos = listOf<Requerimiento>(Requerimiento(Clase("Aventurero"), 4))
        var stats = listOf<AtributoDeFormacion>(AtributoDeFormacion(Atributo.INTELIGENCIA, 2))
        service.crearFormacion(nombre, requerimientos, stats)

        var nombre2 = "ramboMode"
        var requerimientos2 = listOf<Requerimiento>(Requerimiento(Clase("Aventurero"), 3))
        var stats2 = listOf<AtributoDeFormacion>(AtributoDeFormacion(Atributo.FUERZA, 1))
        service.crearFormacion(nombre2, requerimientos2, stats2)

        var nombre3 = "ramboMode"
        var requerimientos3 = listOf<Requerimiento>(Requerimiento(Clase("Aventurero"), 4))
        var stats3= listOf<AtributoDeFormacion>(AtributoDeFormacion(Atributo.DESTREZA, 1))
        service.crearFormacion(nombre3, requerimientos3, stats3)

        val partyCreada = serviceParty.crear(Party())

        var aventurero1 = Aventurero()
        aventurero1.nombre = "aventurero1"
        var idAventurero1 = serviceAventurero.guardar(aventurero1)
        var aventurero1ConId = serviceAventurero.recuperar(idAventurero1)

        var aventurero2 = Aventurero()
        aventurero2.nombre = "aventurero2"
        var idAventurero2 = serviceAventurero.guardar(aventurero2)
        var aventurero2ConId = serviceAventurero.recuperar(idAventurero2)

        var aventurero3 = Aventurero()
        aventurero3.nombre = "aventurero3"
        var idAventurero3 = serviceAventurero.guardar(aventurero3)
        var aventurero3ConId = serviceAventurero.recuperar(idAventurero3)

        var aventurero4 = Aventurero()
        aventurero4.nombre = "aventurero4"
        var idAventurero4 = serviceAventurero.guardar(aventurero4)
        var aventurero4ConId = serviceAventurero.recuperar(idAventurero4)

        serviceParty.agregarAventureroAParty(partyCreada.id!!, aventurero1ConId)
        serviceParty.agregarAventureroAParty(partyCreada.id!!, aventurero2ConId)
        serviceParty.agregarAventureroAParty(partyCreada.id!!, aventurero3ConId)
        serviceParty.agregarAventureroAParty(partyCreada.id!!, aventurero4ConId)

        var atributos = service.atributosQueCorresponden(partyCreada.id!!)
        Assert.assertEquals(3, atributos.size)
    }

    @Test
    fun testUnaPartyCon2AventurerosDeClaseAventureroPoseeUnaFormacion() {

        var nombre = "Bomberos"
        var requerimientos = listOf<Requerimiento>(Requerimiento(Clase("Aventurero"), 2))
        var stats = listOf<AtributoDeFormacion>(AtributoDeFormacion(Atributo.FUERZA, 2))
        service.crearFormacion(nombre, requerimientos, stats)

        val partyCreada = serviceParty.crear(Party())

        var aventurero1 = Aventurero()
        aventurero1.nombre = "aventurero1"
        var idAventurero1 = serviceAventurero.guardar(aventurero1)
        var aventurero1ConId = serviceAventurero.recuperar(idAventurero1)

        var aventurero2 = Aventurero()
        aventurero2.nombre = "aventurero2"
        var idAventurero2 = serviceAventurero.guardar(aventurero2)
        var aventurero2ConId = serviceAventurero.recuperar(idAventurero2)

        serviceParty.agregarAventureroAParty(partyCreada.id!!, aventurero1ConId)
        serviceParty.agregarAventureroAParty(partyCreada.id!!, aventurero2ConId)

        var formaciones = service.formacionesQuePosee(partyCreada.id!!)
        Assert.assertEquals(1, formaciones.size)

        Assert.assertEquals(nombre, formaciones.first().nombreFormacion)
        Assert.assertEquals(requerimientos.size, formaciones.first().requerimientos.size)
        Assert.assertEquals(stats.size, formaciones.first().atributos.size)
    }

    @Test
    fun testUnaPartyCon3AventurerosDeClaseAventureroPoseeDosFormaciones() {

        service.crearFormacion("Bomberos",
                listOf<Requerimiento>(Requerimiento(Clase("Aventurero"), 2)),
                listOf<AtributoDeFormacion>(AtributoDeFormacion(Atributo.FUERZA, 2)))

        service.crearFormacion("Trapecistas",
                listOf<Requerimiento>(Requerimiento(Clase("Aventurero"), 3)),
                listOf<AtributoDeFormacion>(AtributoDeFormacion(Atributo.DESTREZA, 5)))

        val partyCreada = serviceParty.crear(Party())

        var aventurero = Aventurero()
        aventurero.nombre = "aventurero1"
        serviceParty.agregarAventureroAParty(partyCreada.id!!, aventurero)

        aventurero = Aventurero()
        aventurero.nombre = "aventurero2"
        serviceParty.agregarAventureroAParty(partyCreada.id!!, aventurero)

        aventurero = Aventurero()
        aventurero.nombre = "aventurero3"
        serviceParty.agregarAventureroAParty(partyCreada.id!!, aventurero)

        var formaciones = service.formacionesQuePosee(partyCreada.id!!)
        Assert.assertEquals(2, formaciones.size)

    }

    @After
    fun dropAll() {
        serviceParty.eliminarTodo()
        service.deleteAll()
        serviceAventurero.eliminarTodo()
    }
}