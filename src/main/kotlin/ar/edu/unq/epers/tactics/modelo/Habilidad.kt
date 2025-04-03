package ar.edu.unq.epers.tactics.modelo

abstract class Habilidad() {
    abstract fun ejecutar(): Aventurero
}

class Meditar(var objetivo: Aventurero) : Habilidad() {
    override fun ejecutar() : Aventurero {
        objetivo.meditar()
        return objetivo
    }
}

class Ataque(var daño: Int,var  prisicionFisica: Int, var objetivo: Aventurero) : Habilidad() {

    override fun ejecutar() : Aventurero {

        var ataqueExitoso = (AtaqueExitosoRandomizer.getValue() + prisicionFisica) >= objetivo.armadura + (objetivo.velocidad / 2)

        if(ataqueExitoso) {
            objetivo.recibirDanio(daño)
        }
        return objetivo
    }
}

class AtaqueMagico(var poderMagico: Int, var  sourceLevel: Int, var  objetivo: Aventurero) : Habilidad() {

    override fun ejecutar() : Aventurero {
        var ataqueExitoso = (AtaqueExitosoRandomizer.getValue() + sourceLevel) >= (objetivo.velocidad / 2)

        if(ataqueExitoso) {
            objetivo.recibirDanio(poderMagico)
        }
        return objetivo
    }
}

class Curar(var poderMagico: Int,var  objetivo: Aventurero) : Habilidad() {

    override fun ejecutar() : Aventurero {
        objetivo.curar(poderMagico)
        return objetivo
    }

}

class Defensa(var source: Aventurero, var objetivo: Aventurero) : Habilidad() {

    override fun ejecutar() : Aventurero {
        objetivo.aceptarDefensor(source)
        return objetivo
    }

}
