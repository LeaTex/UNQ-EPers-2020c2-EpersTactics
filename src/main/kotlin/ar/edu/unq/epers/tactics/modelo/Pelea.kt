package ar.edu.unq.epers.tactics.modelo

import ar.edu.unq.epers.tactics.service.Estado
import ar.edu.unq.epers.tactics.service.dto.PeleaDTO
import java.time.LocalDateTime
import javax.persistence.*

@Entity
class Pelea() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
    var partyEnemiga: String = ""
    var estado: Estado = Estado.ENCURSO
    var timestamp = LocalDateTime.now()

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "Party_ID")
    var party: Party? = null

    constructor(party: Party, partyEnemiga: String): this() {
        this.party = party
        this.partyEnemiga = partyEnemiga
    }

    fun enCurso() : Boolean = this.estado == Estado.ENCURSO

    fun terminar() {
        if (this.party!!.hayAventurerosVivos()) {
            this.estado = Estado.GANADA
            this.party!!.subirNivelAventureros()
        }
        else {
            this.estado = Estado.PERDIDA
        }
        this.party!!.inicializarEstadisticasAventureros()
    }

}