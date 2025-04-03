package ar.edu.unq.epers.tactics.modelo

import javax.persistence.*

@Entity
class Clase() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    // Si se mejora como un many to many se puede hacer unique
    // @Column(unique = true, nullable = false)
    @Column(nullable = false)
    var nombre: String = ""

    @ManyToOne
    @JoinColumn(name="Aventurero_ID")
    var aventurero: Aventurero? = null

    constructor(nombreDeClase: String): this() {
        this.nombre = nombreDeClase
    }
    constructor(nombreDeClase: String, aventurero: Aventurero): this() {
        this.nombre = nombreDeClase
        this.aventurero = aventurero
    }
}