package ar.edu.unq.epers.tactics.persistencia.dao.mongodb



import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters
import org.bson.conversions.Bson

open class BlockchainMongoDAO<T>(entityType: Class<T>) {

    protected var connection: BlockchainMongoConnection = BlockchainMongoConnection()
    protected var collection: MongoCollection<T>

    init {
        collection = connection.getCollection(entityType.simpleName, entityType)
    }

    fun deleteAllM() {
        collection.drop()
    }

    fun getAll(): List<T> {
        return collection.find().into(mutableListOf())
    }

    fun save(anObject: T) {
        save(listOf(anObject))
    }

    fun update(anObject: T, id: String?) {
        collection.replaceOne(Filters.eq("id", id), anObject)
    }

    fun save(objects: List<T>) {
        collection.insertMany(objects)
    }

    operator fun get(id: String?): T? {
        return getBy("id", id)
    }

    fun getBy(property:String, value: String?): T? {
        return collection.find(Filters.eq(property, value)).first()
    }

    fun <E> findEq(field:String, value:E ): List<T> {
        return find(Filters.eq(field, value))
    }

    fun find(filter: Bson): List<T> {
        return collection.find(filter).into(mutableListOf())
    }

    fun <T> aggregate(pipeline:List<Bson>, resultClass:Class<T>): List<T> {
        return collection.aggregate(pipeline, resultClass).into(ArrayList())
    }
}