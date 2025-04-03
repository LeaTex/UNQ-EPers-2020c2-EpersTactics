package ar.edu.unq.epers.tactics.persistencia.dao.mongodb

import ar.edu.unq.epers.tactics.modelo.AtributoDeFormacion
import ar.edu.unq.epers.tactics.modelo.Clase
import ar.edu.unq.epers.tactics.modelo.Requerimiento
import ar.edu.unq.epers.tactics.modelo.Formacion
import com.mongodb.client.model.Accumulators
import com.mongodb.client.model.Aggregates
import com.mongodb.client.model.Filters.*
import com.mongodb.client.model.Indexes
import com.mongodb.client.model.Projections
import org.bson.conversions.Bson

class FormacionDAO : GenericMongoDAO<Formacion>(Formacion::class.java) {

    fun guardarFormacion(formacion: Formacion) : Formacion {
        save(formacion)
        return formacion
    }

    fun todasLasFormaciones(): List<Formacion> {
        return getAll()
    }

    fun atributosQueCorresponden(clasesContadas: Map<String, Int>) : List<AtributoDeFormacion> {

        var operations : MutableList<Bson> = mutableListOf()
        clasesContadas.forEach { cl, cant ->
            operations.add( and( eq("clase", cl), lte("cantidadNecesaria", cant) ) )
        }

        val match = Aggregates.match(elemMatch("requerimientos", or( operations )))
        val unwind = Aggregates.unwind("\$atributos")
        val group = Aggregates.group("\$atributos.atributo",
                Accumulators.first("atributo","\$atributos.atributo" ) ,
                Accumulators.sum("bonus", "\$atributos.bonus"))

        return aggregate(listOf(match, unwind, group), AtributoDeFormacion::class.java)
    }

    fun formacionesQuePoseeSegun(clasesContadas: Map<String, Int>) : List<Formacion> {

        var operations : MutableList<Bson> = mutableListOf()
        clasesContadas.forEach { cl, cant ->
            operations.add( and( eq("clase", cl), lte("cantidadNecesaria", cant) ) )
        }

        val match = Aggregates.match(elemMatch("requerimientos", or( operations )))

        return aggregate(listOf(match), Formacion::class.java)
    }

    fun deleteAll() {
        deleteAllM()
    }

}
