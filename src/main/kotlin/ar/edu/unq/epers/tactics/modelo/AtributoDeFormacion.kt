package ar.edu.unq.epers.tactics.modelo

class AtributoDeFormacion() {

    var atributo: String = ""
    var bonus : Int = 0;

    constructor(atributo: Atributo, bonus: Int): this() {
        this.atributo = atributo.name
        this.bonus = bonus
    }
}
