package ar.edu.unq.epers.tactics.persistencia.dao.neo4j

import ar.edu.unq.epers.tactics.modelo.*
import ar.edu.unq.epers.tactics.persistencia.dao.ClaseDAO
import org.neo4j.driver.*

class Neo4jClaseDao: ClaseDAO {
    private val driver: Driver

    init {
        val env = System.getenv()
        val url = env.getOrDefault("NEO_URL", "bolt://localhost:7687")
        val username = env.getOrDefault("NEO_USER", "neo4j")
        val password = env.getOrDefault("NEO_PASSWORD", "root")

        driver = GraphDatabase.driver(url, AuthTokens.basic(username, password),
            Config.builder().withLogging(Logging.slf4j()).build()
        )
    }

    override fun crearClase(clase: Clase) {
        driver.session().use { session ->
            session.writeTransaction {
                val query = "MERGE (n:Clase {nombre: ${'$'}elNombre })"
                it.run(query, Values.parameters(
                    "elNombre", clase.nombre
                ))
            }
        }
    }

    override fun crearMejora(mejora: Mejora) {
        driver.session().use { session ->
            session.writeTransaction {
                val query = "MATCH (ClaseInferior: Clase { nombre: ${'$'}elnombreCI }) "+
               "MATCH (ClaseSuperior: Clase { nombre: ${'$'}elnombreCS }) "+
               "MERGE (ClaseInferior)-[:Habilita { FUERZA: ${'$'}cantFuerza , DESTREZA: ${'$'}cantDestreza , "+
                        "CONSTITUCION: ${'$'}cantConstitucion , INTELIGENCIA: ${'$'}cantInteligencia }]->(ClaseSuperior)"
                it.run(query, Values.parameters(
                        "elnombreCI", mejora.clasePrevia.nombre,
                        "elnombreCS", mejora.clasePosterior.nombre,
                        "cantFuerza", cantidadParaAtributo(mejora, Atributo.FUERZA),
                        "cantDestreza", cantidadParaAtributo(mejora, Atributo.DESTREZA),
                        "cantConstitucion", cantidadParaAtributo(mejora, Atributo.CONSTITUCION),
                        "cantInteligencia", cantidadParaAtributo(mejora, Atributo.INTELIGENCIA)
                ))
            }
        }
    }
    fun cantidadParaAtributo(mejora: Mejora, atributo: Atributo): Int {
        if (mejora.atributos.contains(atributo)) { return mejora.cantidadDeAtributos }
        return 0
    }

    override fun requerir(nombreDeClaseConRequerimiento: String, nombreDeClaseAnterior: String) {
        driver.session().use { session ->
            session.writeTransaction {
            val query = """
                MATCH (ClaseAnterior: Clase {nombre: ${'$'}elnombreCAnt})
                MATCH (ClaseConRequerimiento: Clase {nombre: ${'$'}elnombreCCR}) WHERE NOT (ClaseConRequerimiento)-[:Requiere]->(ClaseAnterior)
                MERGE (ClaseConRequerimiento)-[:Requiere]->(ClaseAnterior)
            """
                it.run(query, Values.parameters(
                        "elnombreCCR", nombreDeClaseConRequerimiento,
                        "elnombreCAnt", nombreDeClaseAnterior
                ))
            }
        }
    }

    override  fun buscarMejoraEntreClases(nombreDeClaseActual: String, nombreDeClaseDeseada: String): Mejora {

        return driver.session().use { session ->
            val query = "MATCH (claseSuperior: Clase)-[mejora: Habilita]->(claseInferior: Clase) " +
                    "WHERE claseSuperior.nombre = ${'$'}nombreClSuperior " +
                    "AND claseInferior.nombre = ${'$'}nombreClInferior " +
                    "RETURN claseSuperior, mejora, claseInferior "
            val result = session.run(query, Values.parameters(
                    "nombreClSuperior", nombreDeClaseActual,
                    "nombreClInferior", nombreDeClaseDeseada
            ))

            val record = result.single()

            val mejora = mejoraDesdeRecord(record[0], record[2], record[1])
            mejora
        }
    }

    override fun puedeMejorarseAClaseDesde(clasePosterior: Clase, clases: List<String>): Boolean {
        var clasesListStr: String = listaClasesStatement(clases)
        return driver.session().use { session ->
            val queryStatement = "MATCH (clActual:Clase)-[hab:Habilita]->(clBuscada:Clase)-[req:Requiere]->(clRequerida:Clase) " +
                    "WHERE clBuscada.nombre = ${'$'}nombreClaseBuscada AND NOT clBuscada.nombre IN [ " + clasesListStr + " ] " +
                    "AND clActual.nombre IN [" + clasesListStr + "] " +
                    "RETURN (size(collect(clRequerida.nombre)) = 0 ) OR all(n IN collect(clRequerida.nombre) WHERE n IN [ " + clasesListStr + " ]) "
            val result = session.run(queryStatement, Values.parameters(
                    "nombreClaseBuscada", clasePosterior.nombre
            ))
            val record = result.single()
            record[0].asBoolean()
        }
    }

    override fun posiblesMejorasSegunClases(clases: List<String>): Set<Mejora> {

        var clasesListStr: String = listaClasesStatement(clases)

        return driver.session().use { session ->
            val query = "MATCH (claseSuperior: Clase)-[mejora: Habilita]->(claseInferior: Clase) " +
                "WHERE claseSuperior.nombre IN [ " + clasesListStr +
                " ] AND NOT claseInferior.nombre IN [ " + clasesListStr +
                " ] RETURN claseSuperior, mejora, claseInferior "
            val result = session.run(query, Values.parameters())

            var lista = result.list {
                record: Record ->

                val mejora = mejoraDesdeRecord(record[0], record[2], record[1])
                mejora
            }
            lista.toSet()
        }
    }

    override fun caminoMasRentable(puntosDeExperiencia: Int, clases: List<String>, atributo: Atributo): List<Mejora> {
        return driver.session().use { session ->
            val query = """
                UNWIND ${'$'}clases as clase
                MATCH path = (ClaseDePartida:Clase {nombre: clase})-[:Habilita *..${puntosDeExperiencia}]->(ClaseSiguienteNivel:Clase)
                WITH reduce(total = 0, relacion IN relationships(path) | total + relacion.${atributo.name}) as cuenta, nodes(path) as clases, relationships(path) as relaciones 
                RETURN cuenta,clases, relaciones ORDER BY cuenta DESC, clases  ASC LIMIT 1
            """
            val result = session.run(query, Values.parameters(
                    "clases", clases
            ))

            var record = result.single()


            var nombresClasesQuery = record[1].values().map { it["nombre"].asString() }

            if(clases.containsAll(nombresClasesQuery)) {
                return listOf()
            }

            var mejoras:MutableList<Mejora> = mutableListOf()

            for (i in 0.. (record["relaciones"].size() - 1) ) {
                var claseInicial = record["clases"][i]
                var claseFinal = record["clases"][i+1]
                var relacion = record["relaciones"][i]
                mejoras.add(mejoraDesdeRecord(claseInicial, claseFinal, relacion))
            }

            mejoras.toList()
        }
    }

    private fun mejoraDesdeRecord(nodoInicial: Value, nodoFinal: Value, relacion: Value): Mejora {
            val clasePrevia = Clase(nodoInicial["nombre"].asString())
            val clasePosterior = Clase(nodoFinal["nombre"].asString())

            val listAtributos = mutableListOf<Atributo>()
            var cantidadAtributos: Int = 0

            if (relacion["FUERZA"].asInt() > 0) {
                listAtributos.add(Atributo.FUERZA)
                cantidadAtributos = relacion["FUERZA"].asInt()}
            if (relacion["DESTREZA"].asInt() > 0) {
                listAtributos.add(Atributo.DESTREZA)
                cantidadAtributos = relacion["DESTREZA"].asInt()}
            if (relacion["CONSTITUCION"].asInt() > 0) {
                listAtributos.add(Atributo.CONSTITUCION)
                cantidadAtributos = relacion["CONSTITUCION"].asInt()}
            if (relacion["INTELIGENCIA"].asInt() > 0) {
                listAtributos.add(Atributo.INTELIGENCIA)
                cantidadAtributos = relacion["INTELIGENCIA"].asInt()}

            return Mejora(clasePrevia, clasePosterior, listAtributos, cantidadAtributos)
        }

    private fun listaClasesStatement(clases: List<String>) : String {
        var clasesListStr: String = ""
        clasesListStr += "\'" + clases.first() + "\'"
        if (clases.size > 1) {
            clases.subList(1, clases.size).forEach { clasesListStr += ", \'" + it + "\'" }
        }
        return clasesListStr
    }

    override fun clear() {
        return driver.session().use { session ->
            session.run("MATCH (n) DETACH DELETE n")
        }
    }
}

