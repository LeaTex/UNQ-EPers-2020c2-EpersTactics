package helpers

import ar.edu.unq.epers.tactics.modelo.Atributos
import ar.edu.unq.epers.tactics.modelo.Aventurero
import ar.edu.unq.epers.tactics.modelo.Party
import ar.edu.unq.epers.tactics.modelo.Tactica
import ar.edu.unq.epers.tactics.persistencia.dao.PartyDAO
import ar.edu.unq.epers.tactics.persistencia.dao.hibernate.HibernatePartyDAO
import ar.edu.unq.epers.tactics.service.AventureroService
import ar.edu.unq.epers.tactics.service.PartyService
import ar.edu.unq.epers.tactics.service.PeleaService
import ar.edu.unq.epers.tactics.service.dto.Accion
import ar.edu.unq.epers.tactics.service.dto.Criterio
import ar.edu.unq.epers.tactics.service.dto.TipoDeEstadistica
import ar.edu.unq.epers.tactics.service.dto.TipoDeReceptor

class DataServiceImplement(val aventureroSrv : AventureroService?,
                                val peleaSrv : PeleaService?,
                                val partySrv : PartyService?) : DataService {

    private val partyDao: PartyDAO = HibernatePartyDAO()

    override fun eliminarTodo() {
        this.eliminarPeleas()

        this.eliminarParties()

        // Se eliminan en forma independiente los aventureros "huérfanos" que se crearon sin una Party específica
        this.eliminarAventureros()
    }
    override fun crearSetDeDatosIniciales() {
        crearPartyOne()
    }

    override fun aventureroMalo(): Aventurero {
        var adventurer: Aventurero = Aventurero()
        adventurer.nombre = "El Malo"
        adventurer.nivel = 5
        adventurer.imagenURL = ""
        adventurer.atributos = Atributos(null, 8,7,9,4)
        return adventurer
    }

    override fun aventureroFeo(): Aventurero {
        var adventurer: Aventurero = Aventurero()
        adventurer.nombre = "El Feo"
        adventurer.nivel = 5
        adventurer.imagenURL = ""
        adventurer.atributos = Atributos(null, 10,20,30,40)

        var tactic: Tactica = Tactica(null, 1, TipoDeReceptor.UNO_MISMO, TipoDeEstadistica.VIDA, Criterio.IGUAL, 1, Accion.MEDITAR, adventurer)
        adventurer.tacticas.add(tactic)

        tactic = Tactica(null, 2, TipoDeReceptor.ALIADO, TipoDeEstadistica.VELOCIDAD, Criterio.MENOR_QUE, 5, Accion.DEFENDER, adventurer)
        adventurer.tacticas.add(tactic)

        return adventurer
    }

    override fun aventureroBueno(): Aventurero {
        var adventurer: Aventurero = Aventurero()
        adventurer.nombre = "El Bueno"
        adventurer.nivel = 2
        adventurer.imagenURL = ""
        adventurer.atributos = Atributos(null, 10,5,40,70)

        var tactic: Tactica = Tactica(null, 1, TipoDeReceptor.ENEMIGO, TipoDeEstadistica.VIDA, Criterio.MAYOR_QUE, 50, Accion.ATAQUE_FISICO, adventurer)
        adventurer.tacticas.add(tactic)

        return adventurer
    }

    override fun aventureroDefensor(): Aventurero {
        var adventurer: Aventurero = Aventurero()
        adventurer.nombre = "El Abogado"
        adventurer.nivel = 10
        adventurer.imagenURL = ""
        adventurer.atributos = Atributos(null, 5,10,50,80)
        return adventurer
    }

    fun crearPartyParaPelea() {
        var party: Party = Party(nombre = "Party One", aventureros = mutableListOf(), imagenURL = "", id = 1)
        partySrv!!.crear(party)
    }
    fun crearPartyOne() {
        var party: Party = Party(nombre = "Party One", aventureros = mutableListOf(), imagenURL = "", id = 1)
        party.numeroDeAventureros = 3
        partySrv!!.crear(party)
    }

    override fun crearPartiesParaOrdenarPorPoder() {
        var p : Party
        var a : Aventurero

        p = Party(nombre = "P-Uno", aventureros = mutableListOf(), imagenURL = "", id = 1)
        partySrv!!.crear(p)
        a = this.aventureroBueno()
        a.inicializarEstadisticas()
        partySrv.agregarAventureroAParty(1, a)

        p = Party(nombre = "P-Dos", aventureros = mutableListOf(), imagenURL = "", id = 2)
        partySrv.crear(p)
        a = this.aventureroMalo()
        a.inicializarEstadisticas()
        partySrv.agregarAventureroAParty(2, a)
        a = this.aventureroDefensor()
        a.inicializarEstadisticas()
        partySrv.agregarAventureroAParty(2, a)
    }

    override fun crearPartiesParaOrdenarPorVictorias() {
        var p : Party
        var a : Aventurero

        p = Party(nombre = "P-Uno", aventureros = mutableListOf(), imagenURL = "", id = 1)
        partySrv!!.crear(p)
        a = this.aventureroBueno()
        a.inicializarEstadisticas()
        partySrv.agregarAventureroAParty(1, a)

        p = Party(nombre = "P-Dos", aventureros = mutableListOf(), imagenURL = "", id = 2)
        partySrv.crear(p)

        peleaSrv!!.iniciarPelea(1, "P-Dos")
        peleaSrv!!.terminarPelea(1)
        peleaSrv!!.iniciarPelea(1, "P-Dos")
        peleaSrv!!.terminarPelea(1)
    }

    override fun eliminarPeleas() {
        peleaSrv!!.eliminarTodo()
    }

    override fun eliminarParties() {
        partySrv!!.eliminarTodo()
    }

    override fun eliminarAventureros() {
        aventureroSrv!!.eliminarTodo()
    }

}