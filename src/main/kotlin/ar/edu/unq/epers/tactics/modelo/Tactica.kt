package ar.edu.unq.epers.tactics.modelo

import ar.edu.unq.epers.tactics.service.dto.*
import javax.persistence.*

@Entity
class Tactica() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column
    var prioridad: Int = 1
    var tipoDeReceptor: TipoDeReceptor = TipoDeReceptor.UNO_MISMO
    var tipoDeEstadistica: TipoDeEstadistica = TipoDeEstadistica.VIDA
    var criterio: Criterio = Criterio.IGUAL
    var valor: Int = 1
    var accion: Accion = Accion.MEDITAR

    @ManyToOne
    @JoinColumn(name="Aventurero_ID")
    var aventurero: Aventurero? = null

    constructor(id: Long?, prioridad: Int, tipoDeReceptor: TipoDeReceptor, tipoDeEstadistica: TipoDeEstadistica,
                criterio: Criterio, valor: Int, accion: Accion, aventurero: Aventurero): this() {
        this.id = id
        this.prioridad = prioridad
        this.tipoDeReceptor = tipoDeReceptor
        this.tipoDeEstadistica = tipoDeEstadistica
        this.criterio = criterio
        this.valor = valor
        this.accion = accion
        this.aventurero = aventurero
    }

    @Transient
    var receptor: Aventurero? = null

    fun puedeEjecutarseEnTurno(enemigos: List<Aventurero>) : Boolean {
        when (tipoDeReceptor) {
            TipoDeReceptor.UNO_MISMO -> { return puedeEjecutarsePara(aventurero!!) }
            TipoDeReceptor.ALIADO -> { return aventurero!!.party!!.aventureros.any { this.puedeEjecutarsePara(it) } }
            TipoDeReceptor.ENEMIGO -> { return enemigos.any { this.puedeEjecutarsePara(it) } }
        }
    }

    private fun puedeEjecutarsePara(adventurer: Aventurero) : Boolean {
        receptor = adventurer

        // valido que el aventurero tenga maná suficiente
        if ( (this.accion == Accion.ATAQUE_MAGICO || this.accion == Accion.CURAR) && this.aventurero!!.mana < 5) { return false }

        // valido que el defendido ya no esté siendo defendido por otro y que no esté defendiendo a otro
        if (this.accion == Accion.DEFENDER && (this.aventurero!!.turnosRestantesComoDefensor > 0 || adventurer.defensor != null)) { return false }

        when (criterio) {
            Criterio.IGUAL -> { return adventurer.estadistica(tipoDeEstadistica) == valor  }
            Criterio.MENOR_QUE -> { return adventurer.estadistica(tipoDeEstadistica) < valor  }
            Criterio.MAYOR_QUE -> { return adventurer.estadistica(tipoDeEstadistica) > valor  }
        }
    }

    fun habilidadCorrespondiente() : Habilidad {
        when (accion) {
            Accion.ATAQUE_FISICO -> { this.aventurero!!.danioFisicoRealizado += this.aventurero!!.danioFisico
                return Ataque(this.aventurero!!.danioFisico , this.aventurero!!.precisionFisica, this.receptor!!) }

            Accion.DEFENDER -> { this.aventurero!!.ponerseComoDefensor()
                return Defensa(this.aventurero!!, this.receptor!!) }

            Accion.CURAR -> { this.aventurero!!.consumirMana(5)
                return Curar(this.aventurero!!.poderMagico , this.receptor!!) }

            Accion.ATAQUE_MAGICO -> { this.aventurero!!.consumirMana(5)
                this.aventurero!!.danioMagicoRealizado += this.aventurero!!.poderMagico
                return AtaqueMagico(this.aventurero!!.poderMagico, this.aventurero!!.nivel, this.receptor!!) }

            Accion.MEDITAR -> { return Meditar(this.aventurero!!) }
        }
    }

}
