package ar.edu.unq.epers.tactics.modelo

import ar.edu.unq.epers.tactics.service.dto.*

import javax.persistence.*


@Entity
class Aventurero() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column(unique = true, nullable = false)
    var nombre: String=""
    var imagenURL:String?= null
    var nivel: Int = 1
    var experiencia:Int = 0

    @OneToMany(mappedBy="aventurero", cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    var clases: MutableList<Clase> = mutableListOf(Clase("Aventurero", this))

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name="Atributos_ID")
    var atributos: Atributos = Atributos()

    @OneToOne(cascade = [CascadeType.REFRESH], fetch = FetchType.LAZY)
    @JoinColumn(name="Defensor_ID", nullable = true)
    var defensor: Aventurero? = null
    var turnosRestantesConDefensor= 0
    var turnosRestantesComoDefensor = 0

    @OneToMany(mappedBy="aventurero", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @OrderBy("prioridad asc")
    var tacticas: MutableList<Tactica> = mutableListOf()

    @ManyToOne
    @JoinColumn(name="Party_ID", nullable=true)

    var party: Party? = null

    // Estadisticas
    // nota: las estadísticas se podrían implementar con algún diccionario para utilizar TipoDeEstadistica como clave
    var vida: Int =0
    var armadura: Int = 0
    var mana: Int =0
    var velocidad: Int = 0
    var danioFisico: Int =0
    var poderMagico: Int = 0
    var precisionFisica: Int = 0

    // leaderboard data
    var danioFisicoRealizado: Int = 0
    var danioMagicoRealizado: Int = 0
    var curacionesRealizadas: Int = 0
    var meditaciones: Int = 0

    fun subirNilvel() {
        nivel += 1
        experiencia += 1
    }

    fun inicializarEstadisticas() {
        vida = this.vidaMaxima()  // Nivel del aventurero x5 + Constitucion x 2 + Fuerza
        mana = nivel + atributos.inteligencia  // Nivel de aventurero + Inteligencia
        armadura = nivel + atributos.constitucion  // Nivel de aventurero + Constitucion
        velocidad = nivel + atributos.destreza  // Nivel de aventurero + Destreza
        danioFisico = nivel + atributos.fuerza + (atributos.destreza / 2)  // Nivel del aventurero + Fuerza + Destreza/2
        poderMagico = nivel + atributos.inteligencia  // Nivel del aventurero + Inteligencia
        precisionFisica = nivel + atributos.fuerza + atributos.destreza  // Nivel del aventurero + Fuerza + Destreza
    }
    private fun vidaMaxima(): Int = (nivel * 5) + (atributos.constitucion * 2) + atributos.fuerza

    fun estadistica(tipoDeEstadistica: TipoDeEstadistica) : Int {
        when (tipoDeEstadistica) {
            TipoDeEstadistica.VIDA -> return vida
            TipoDeEstadistica.MANA -> return mana
            TipoDeEstadistica.ARMADURA -> return armadura
            TipoDeEstadistica.VELOCIDAD -> return velocidad
            TipoDeEstadistica.DAÑO_FISICO -> return danioFisico
            TipoDeEstadistica.DAÑO_MAGICO -> return poderMagico
            TipoDeEstadistica.PRECISION_FISICA -> return precisionFisica
        }
    }

    fun resolverTurno(enemigos: List<Aventurero>): Habilidad {

        if (this.turnosRestantesComoDefensor > 0) { this.turnosRestantesComoDefensor -= 1 }

        if (this.turnosRestantesConDefensor > 0) {
            this.turnosRestantesConDefensor -= 1
            if (this.turnosRestantesConDefensor == 0) { this.defensor = null }
        }

        var tactic = tacticas.find { it.puedeEjecutarseEnTurno(enemigos) }

        if (tactic == null) { // siempre debería haber alguna, pero....
            return Meditar(this)
        }
        else {
            return tactic.habilidadCorrespondiente()
        }
    }

    fun consumirMana(consumido : Int) {
        // si hicimos bien las cuentas, esto nunca debería bajar de cero
        this.mana -= consumido
    }

    fun ponerseComoDefensor() {
        // cuando un aventurero defiende solo sufre la mitad del daño que recibe hasta su próximo turno.
        this.turnosRestantesComoDefensor = 3
    }

    fun meditar() {
        this.mana += nivel
        this.meditaciones += 1
    }

    fun curar(poderMagico: Int) {
        this.vida = minOf( this.vida + poderMagico, this.vidaMaxima())
        this.curacionesRealizadas += 1
    }

    fun aceptarDefensor(bodyguard : Aventurero) {
        this.defensor = bodyguard
        this.turnosRestantesConDefensor = 3
    }

    fun recibirDanio(danioSufrido: Int) {
        when {
            // cuando un aventurero defiende solo sufre la mitad del daño que recibe hasta su próximo turno.
            this.turnosRestantesComoDefensor == 3 -> { this.vida = maxOf(0 , this.vida - danioSufrido / 2) }

            // cuando un aventurero es defendido, por los próximos 3 turnos si ha de recibir algún ataque de cualquier tipo, este sera recibido por el defensor.
            this.turnosRestantesConDefensor > 0 -> { this.defensor!!.recibirDanio(danioSufrido) }

            else -> { this.vida = maxOf(0, this.vida - danioSufrido) }
        }
    }

    fun ganarProficiencia(mejora: Mejora) {
        this.experiencia -= 1

        var claseAdquirida = mejora.clasePosterior
        claseAdquirida.aventurero = this
        this.clases.add(claseAdquirida)

        mejora.atributos.forEach {
            when(it) {
                Atributo.INTELIGENCIA -> { this.atributos.inteligencia += mejora.cantidadDeAtributos }
                Atributo.DESTREZA -> { this.atributos.destreza += mejora.cantidadDeAtributos }
                Atributo.FUERZA -> { this.atributos.fuerza += mejora.cantidadDeAtributos }
                Atributo.CONSTITUCION -> { this.atributos.constitucion += mejora.cantidadDeAtributos }
            }
        }
    }
}