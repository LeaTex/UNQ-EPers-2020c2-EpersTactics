package ar.edu.unq.epers.tactics.modelo

class Requerimiento() {

    var clase: String = ""
    var cantidadNecesaria: Int = 0

    constructor(clase: Clase, cantidadNecesaria:Int): this() {
        this.clase = clase.nombre
        this.cantidadNecesaria = cantidadNecesaria
    }

}
