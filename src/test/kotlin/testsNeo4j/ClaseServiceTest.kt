package testsNeo4j

import ar.edu.unq.epers.tactics.modelo.*
import ar.edu.unq.epers.tactics.modelo.exception.AdventurerCantUpgradeException
import ar.edu.unq.epers.tactics.modelo.exception.PartyNotInFightException
import ar.edu.unq.epers.tactics.persistencia.dao.neo4j.Neo4jClaseDao
import ar.edu.unq.epers.tactics.persistencia.dao.hibernate.HibernateAventureroDAO
import ar.edu.unq.epers.tactics.persistencia.dao.hibernate.HibernatePartyDAO
import ar.edu.unq.epers.tactics.service.AventureroService
import ar.edu.unq.epers.tactics.service.ClaseService
import ar.edu.unq.epers.tactics.service.impl.AventureroServiceImpl
import ar.edu.unq.epers.tactics.service.impl.ClaseServiceImplement
import org.junit.After
import org.junit.Test
import org.junit.jupiter.api.Assertions.*

class ClaseServiceTest {

    var service: ClaseService = ClaseServiceImplement(Neo4jClaseDao(), HibernateAventureroDAO())
    private val serviceAventurero: AventureroService = AventureroServiceImpl(HibernateAventureroDAO(), HibernatePartyDAO())

    @Test
    fun seCreaUnaClaseDeNombreAventurero() {
        service.crearClase("Aventurero")
    }

    @Test
    fun seCreaUnaClaseDeNombreMagicoYOtraDeNombreFisico() {
        service.crearClase("Magico")
        service.crearClase("Fisico")
    }

    @Test
    fun seCreaUnaMejoraEntreClaseAventureroYClaseMagico() {
        service.crearClase("Aventurero")
        service.crearClase("Magico")

        service.crearMejora("Aventurero", "Magico", mutableListOf<Atributo>(), 2)
    }


    @Test
    fun seCreaUnaMejoraEntreClaseAventureroYClaseFisico() {
        service.crearClase("Aventurero")
        service.crearClase("Fisico")
        val atributos = mutableListOf<Atributo>(Atributo.INTELIGENCIA)

        service.crearMejora("Aventurero", "Fisico", atributos, 3)
    }

    @Test
    fun seCreaUnaMejoraEntreClaseAventureroClaseFisicoYClaseMagicoLaCLaseMagicoHabilitaAlFisico() {
        service.crearClase("Aventurero")
        service.crearClase("Fisico")
        val atributos = mutableListOf<Atributo>(Atributo.INTELIGENCIA, Atributo.CONSTITUCION)

        service.crearMejora("Aventurero", "Fisico", atributos, 2)

        service.crearClase("Magico")
        val atributosCM = mutableListOf<Atributo>(Atributo.DESTREZA)

        service.crearMejora("Aventurero", "Magico", atributosCM, 3)
        val atributoFAM = mutableListOf<Atributo>(Atributo.INTELIGENCIA, Atributo.CONSTITUCION)

        service.crearMejora("Magico", "Fisico", atributoFAM, 1)
    }

    @Test
    fun seCreaUnaMejoraEntreClaseAventureroClaseFisicoYUnaEntreElFisicoYElGuerrero() {
        service.crearClase("Aventurero")
        service.crearClase("Fisico")
        val atributos = mutableListOf<Atributo>(Atributo.DESTREZA)

        service.crearMejora("Aventurero", "Fisico", atributos, 1)

        service.crearClase("Guerrero")
        val atributosCM = mutableListOf<Atributo>(Atributo.INTELIGENCIA)

        service.crearMejora("Fisico", "Guerrero", atributosCM, 3)

    }

    @Test
    fun seCreaUnaMejoraEntreClaseAventureroClaseFisicoYSeRequiereSerFisicoParaSerGuerreroPeroFisicoNoHabilitaGuerrero() {
        service.crearClase("Aventurero")
        service.crearClase("Fisico")
        val atributos = mutableListOf<Atributo>(Atributo.DESTREZA, Atributo.INTELIGENCIA)

        service.crearMejora("Aventurero", "Fisico", atributos, 1)

        service.crearClase("Guerrero")

        service.requerir("Guerrero", "Fisico")
    }

    @Test
    fun seCreaUnaMejoraEntreClaseAventureroClaseFisicoYSeRequiereSerFisicoParaSerGuerreroPeroFisicoSIHabilitaGuerrero() {
        service.crearClase("Aventurero")
        service.crearClase("Fisico")
        val atributos = mutableListOf<Atributo>(Atributo.DESTREZA, Atributo.INTELIGENCIA)

        service.crearMejora("Aventurero", "Fisico", atributos, 1)
        service.crearClase("Guerrero")
        val atributosCM = mutableListOf<Atributo>()

        service.crearMejora("Fisico", "Guerrero", atributosCM, 3)
        service.requerir("Guerrero", "Fisico")
    }

    @Test
    fun seCreaUnaMejoraEntreClaseAventureroClaseFisicoYSeRequiereSerFisicoParaSerGuerreroPeroFisicoSIHabilitaGuerreroYFisicoNoPuedeRequerirGuerrero() {
        service.crearClase("Aventurero")
        service.crearClase("Fisico")
        val atributos = mutableListOf<Atributo>(Atributo.INTELIGENCIA)

        service.crearMejora("Aventurero", "Fisico", atributos, 3)

        service.crearClase("Guerrero")
        val atributosCM = mutableListOf<Atributo>(Atributo.DESTREZA)

        service.crearMejora("Fisico", "Guerrero", atributosCM, 3)
        service.requerir("Guerrero", "Fisico")
        service.requerir("Fisico", "Guerrero")
    }

    @Test
    fun deAventureroNoPuedeMejorarseAFisico() {
        val aventurero= Aventurero()
        val advId= serviceAventurero.guardar(aventurero)

        val me3= serviceAventurero.recuperar(advId)
        service.crearClase("Aventurero")
        service.crearClase("Fisico")

        val atributos = mutableListOf<Atributo>()
        val puntosExtra = 3


        service.crearMejora("Aventurero", "Fisico", atributos, puntosExtra)
        val me2= serviceAventurero.recuperar(advId)
        val mejora = Mejora(Clase("Aventurero"), Clase("Fisico"), mutableListOf(), 0)
        val me= serviceAventurero.recuperar(advId)


        val cierto = service.puedeMejorar(advId, mejora)

        assertFalse(cierto)
    }

    @Test
    fun deAventureroPuedeMejorarseAFisico() {
        service.crearClase("Aventurero")
        service.crearClase("Fisico")

        val atributos = mutableListOf<Atributo>(Atributo.DESTREZA)
        val aventurero= Aventurero()
        aventurero.experiencia = 2
        val advId= serviceAventurero.guardar(aventurero)

        service.crearMejora("Aventurero", "Fisico", atributos, 3)

        val mejora = Mejora(Clase("Aventurero"), Clase("Fisico"), mutableListOf(), 0)

        val cierto = service.puedeMejorar(advId, mejora)

        assertTrue(cierto)
    }

    @Test
    fun deAventureroGanaProficienciaAFisico() {
        service.crearClase("Aventurero")
        service.crearClase("Fisico")

        val atributos = mutableListOf<Atributo>(Atributo.INTELIGENCIA)
        val aventurero= Aventurero()
        aventurero.experiencia = 2
        val advId= serviceAventurero.guardar(aventurero)

        service.crearMejora("Aventurero", "Fisico", atributos, 3)
        service.ganarProficiencia(advId, "Aventurero", "Fisico")

    }

    @Test(expected = AdventurerCantUpgradeException::class)
    fun aventureroSinExperienciaNoGanaProficiencia() {
        service.crearClase("Aventurero")
        service.crearClase("Fisico")

        val atributos = mutableListOf<Atributo>(Atributo.INTELIGENCIA)
        val aventurero= Aventurero()
        val advId= serviceAventurero.guardar(aventurero)

        service.crearMejora("Aventurero", "Fisico", atributos, 3)
        service.ganarProficiencia(advId, "Aventurero", "Fisico")
    }

    @Test
    fun unAventureroSinExperienciaConsultaSusPosiblesMejoras() {
        service.crearClase("Aventurero")
        service.crearClase("Fisico")

        val atributos = mutableListOf<Atributo>(Atributo.DESTREZA, Atributo.CONSTITUCION)
        val aventurero= Aventurero()
        val advId= serviceAventurero.guardar(aventurero)

        service.crearMejora("Aventurero", "Fisico", atributos, 1)

        assertTrue(service.posiblesMejoras(advId).isEmpty())
    }

    @Test
    fun unAventureroCon1DeEsperienciaConsultaSusPosiblesMejoras() {
        service.crearClase("Aventurero")
        service.crearClase("Fisico")

        val atributos = mutableListOf<Atributo>(Atributo.DESTREZA)
        val aventurero= Aventurero()
        aventurero.experiencia = 1
        val advId= serviceAventurero.guardar(aventurero)

        service.crearMejora("Aventurero", "Fisico", atributos, 3)

        assertEquals(service.posiblesMejoras(advId).size, 1)
    }


    @Test
    fun unAventureroCon2DeEsperienciaYDosClasesConsultaSusPosiblesMejoras() {
        service.crearClase("Aventurero")
        service.crearClase("Fisico")
        service.crearClase("Mago")

        val aventurero= Aventurero()
        aventurero.experiencia = 2
        aventurero.clases.add(Clase("Fisico", aventurero))
        val advId= serviceAventurero.guardar(aventurero)

        val atributos = mutableListOf<Atributo>(Atributo.DESTREZA)
        service.crearMejora("Aventurero", "Fisico", atributos, 3)
        service.crearMejora("Fisico", "Mago", atributos, 2)

        assertEquals(service.posiblesMejoras(advId).size, 1)
    }

    @Test
    fun unAventureroCon1DeEsperienciaConsultaSusPosiblesMejorasPeroConDosClasesDisponibles() {
        service.crearClase("Aventurero")
        service.crearClase("Fisico")
        service.crearClase("Magico")

        val atributos = mutableListOf<Atributo>(Atributo.DESTREZA)
        val aventurero= Aventurero()
        aventurero.experiencia = 1
        val advId= serviceAventurero.guardar(aventurero)

        service.crearMejora("Aventurero", "Fisico", atributos, 1)
        service.crearMejora("Aventurero", "Magico", atributos, 1)

        assertEquals(service.posiblesMejoras(advId).size, 2)
    }


    @Test
    fun unAventureroConConsultaSusCaminos() {
        service.crearClase("Aventurero")
        service.crearClase("Fisico")
        service.crearClase("Magico")

        val atributos = mutableListOf<Atributo>(Atributo.FUERZA)
        val aventurero= Aventurero()
        aventurero.experiencia = 1
        val advId= serviceAventurero.guardar(aventurero)

        service.crearMejora("Aventurero", "Fisico", atributos, 1)
        service.crearMejora("Aventurero", "Magico", atributos, 1)

        assertEquals(service.caminoMasRentable(1, advId, Atributo.FUERZA).size, 1)
    }


    @After
    fun clear(){
        service.clear()
        serviceAventurero.eliminarTodo()
    }
}