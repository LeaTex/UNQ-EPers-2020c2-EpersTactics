package ar.edu.unq.epers.tactics.modelo

import org.bson.codecs.pojo.annotations.BsonIgnore
import org.bson.codecs.pojo.annotations.BsonProperty


class Formacion() {

    @BsonProperty("id")
    val id: String ?= null

    var nombreFormacion:String = ""
    var requerimientos: MutableList<Requerimiento> = mutableListOf()
    var atributos: MutableList<AtributoDeFormacion> = mutableListOf()

    @BsonIgnore
    var partys: MutableList<Party> = mutableListOf()


    constructor(nombreFormacion: String, requerimientos: List<Requerimiento>, stats: List<AtributoDeFormacion>): this() {
        this.nombreFormacion = nombreFormacion
        this.requerimientos = requerimientos.toMutableList()
        this.atributos = stats.toMutableList()
    }

    fun addParty(party: Party) {
        partys.add(party)
    }

    fun addAtributoDeFormacion(atributoDeFormacion: AtributoDeFormacion) {
        atributos.add(atributoDeFormacion)
    }

    fun addRequerimientos(requerimiento: Requerimiento) {
        requerimientos.add(requerimiento)
    }

}
