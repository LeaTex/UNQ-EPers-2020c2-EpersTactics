package ar.edu.unq.epers.tactics.modelo


import ar.edu.unq.epers.tactics.modelo.exception.PartyFullException
import ar.edu.unq.epers.tactics.service.dto.AventureroDTO
import ar.edu.unq.epers.tactics.service.dto.PartyDTO
import org.bson.codecs.pojo.annotations.BsonIgnore
import org.bson.codecs.pojo.annotations.BsonProperty
import javax.persistence.*
import java.math.BigInteger
import java.security.MessageDigest

@Entity
class Party() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    var id: Long? = null

    @Column
    var numeroDeAventureros: Int = 0
    var nombre = ""
    var imagenURL = ""

    @OneToMany(mappedBy = "party", cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    var aventureros: MutableList<Aventurero> = mutableListOf()

    @Transient
    @BsonIgnore
    var formacion: Formacion ?= null

    constructor(nombre:String, imagenURL: String, id: Long?): this() {
        this.nombre = nombre
        this.imagenURL = imagenURL
        this.id = id
    }

    constructor(nombre:String, imagenURL: String, aventureros: MutableList<Aventurero> , id: Long?): this() {
        this.nombre = nombre
        this.imagenURL = imagenURL
        this.id = id
        this.aventureros = aventureros
    }

    private fun partyLlena(): Boolean {
        return numeroDeAventureros == 5
    }

    fun agregarUnAventurero(aventurero: Aventurero): Aventurero {
        if (partyLlena()) { throw PartyFullException(this.id!!) }

        aventurero.party = this
        numeroDeAventureros += 1

        var modifyAventurero =  aventureros.toMutableList()
        modifyAventurero.add(aventurero)
        aventureros = modifyAventurero

        return aventurero
    }

    fun hayAventurerosVivos(): Boolean {
        return this.aventureros.any { it.vida > 0 }
    }

    fun inicializarEstadisticasAventureros() {
        for(ave in aventureros) {
            ave.inicializarEstadisticas()
        }
    }
    fun subirNivelAventureros() {
        for(ave in aventureros) {
            ave.subirNilvel()
        }
    }

  /*
    fun billeteraDos(): String {
        val baseString: String = this.nombre + this.id.toString()
        val md = MessageDigest.getInstance("MD5")
        val bigInt = BigInteger(1, md.digest(baseString.toByteArray(Charsets.UTF_8)))
        return String.format("%032x", bigInt)
    }
    */

    fun billetera(): String {
        val baseString: String = this.nombre + this.id.toString()
        return HashUtils.md5(baseString)
    }


}