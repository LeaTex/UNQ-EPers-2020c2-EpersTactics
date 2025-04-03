package ar.edu.unq.epers.tactics.modelo


import javax.persistence.*


@Entity
class Atributos() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long?= null

    @Column()
    var fuerza: Int= 0
    var destreza: Int= 0
    var constitucion: Int= 0
    var inteligencia: Int= 0

    constructor(id: Long?, fuerza: Int, destreza: Int, constitucion: Int, inteligencia: Int): this() {
        this.id = id
        this.fuerza = fuerza
        this.destreza = destreza
        this.constitucion = constitucion
        this.inteligencia = inteligencia
    }

}

enum class Atributo {
    FUERZA,
    DESTREZA,
    CONSTITUCION,
    INTELIGENCIA
}