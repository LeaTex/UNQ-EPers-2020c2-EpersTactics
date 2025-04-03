package ar.edu.unq.epers.tactics.persistencia.dao.mongodb

import ar.edu.unq.epers.tactics.modelo.Bloque
import com.mongodb.client.model.Accumulators
import com.mongodb.client.model.Aggregates
import com.mongodb.client.model.Filters.*
import com.mongodb.client.model.Indexes
import com.mongodb.client.model.Projections
import org.bson.conversions.Bson

class BloqueDAO : BlockchainMongoDAO<Bloque>(Bloque::class.java) {

    fun guardarBloque(bloque: Bloque) : Long {
        save(bloque)
        return bloque.numero
    }

    fun deleteAll() {
        deleteAllM()
    }

}
