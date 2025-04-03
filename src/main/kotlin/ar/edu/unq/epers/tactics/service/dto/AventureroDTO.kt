package ar.edu.unq.epers.tactics.service.dto

import ar.edu.unq.epers.tactics.modelo.Atributos
import ar.edu.unq.epers.tactics.modelo.Aventurero
import ar.edu.unq.epers.tactics.modelo.Tactica


data class AventureroDTO(var id:Long?, var nivel:Int, var nombre:String, var imagenURL:String, var tacticas : List<TacticaDTO>, var atributos: AtributosDTO){

    companion object {
        fun desdeModelo(aventurero: Aventurero): AventureroDTO {
            return AventureroDTO(aventurero.id, aventurero.nivel, aventurero.nombre, aventurero.imagenURL!!,
                            tacticasADTO(aventurero), AtributosDTO.desdeModelo(aventurero.atributos))
        }

        private fun tacticasADTO(aventurero: Aventurero): List<TacticaDTO> {
            return aventurero.tacticas.map { TacticaDTO.desdeModelo(it) }.toList()
        }
    }

    fun aModelo(): Aventurero {

        var aventurero= Aventurero()
        aventurero.id = this.id
        aventurero.nivel =this.nivel
        aventurero.nombre = this.nombre
        aventurero.imagenURL = this.imagenURL
        aventurero.atributos = this.atributos.aModelo()
        aventurero.tacticas = this.tacticasAModel(aventurero)
        aventurero.inicializarEstadisticas()

        return aventurero
    }

    private fun tacticasAModel(aventurero : Aventurero): MutableList<Tactica> {
        return this.tacticas.map { it.aModelo(aventurero) }.toMutableList()
    }

    fun actualizarModelo(aventurero: Aventurero){
        aventurero.nombre = this.nombre
        aventurero.nivel = this.nivel
        aventurero.imagenURL = this.imagenURL
        this.atributos.actualizarModelo(aventurero.atributos)

        // TODO revisar cómo actualizar las tácticas
        // aventurero.tacticas = this.nuevasDisponibles(aventureroDTO.tacticas)
        // private fun nuevasDisponibles(tacticasDTOs: List<TacticaDTO>): MutableList<Tactica> {
        //    return tacticasDTOs.map { it.aModelo(this) }.toMutableList()
        // }

        aventurero.inicializarEstadisticas()
    }
}

data class AtributosDTO(var id:Long?, var fuerza:Int, var destreza:Int, var constitucion:Int, var inteligencia:Int) {
    companion object {
        fun desdeModelo(atributos: Atributos): AtributosDTO {
            return AtributosDTO(atributos.id, atributos.fuerza, atributos.destreza, atributos.constitucion, atributos.inteligencia)
        }
    }
    fun aModelo(): Atributos {
        return Atributos(this.id, this.fuerza, this.destreza, this.constitucion, this.inteligencia)
    }
    fun actualizarModelo(atributos: Atributos) {
        atributos.fuerza = this.fuerza
        atributos.destreza = this.destreza
        atributos.constitucion = this.constitucion
        atributos.inteligencia = this.inteligencia
    }
}

data class TacticaDTO(var id:Long?, var prioridad:Int, var receptor:TipoDeReceptor, var tipoDeEstadistica:TipoDeEstadistica, var criterio:Criterio, var valor:Int, var accion:Accion) {

    companion object {
        fun desdeModelo(tactica: Tactica): TacticaDTO {
            return TacticaDTO(tactica.id, tactica.prioridad, tactica.tipoDeReceptor, tactica.tipoDeEstadistica, tactica.criterio, tactica.valor, tactica.accion)
        }
    }

    fun aModelo(adventurer : Aventurero): Tactica {
        return Tactica(this.id, this.prioridad, this.receptor, this.tipoDeEstadistica, this.criterio, this.valor, this.accion, adventurer)
    }

    fun actualizarModelo(tactica: Tactica) {
        tactica.prioridad = this.prioridad
        tactica.tipoDeReceptor = this.receptor
        tactica.tipoDeEstadistica = this.tipoDeEstadistica
        tactica.criterio = this.criterio
        tactica.valor = this.valor
        tactica.accion = this.accion
    }
}

enum class TipoDeReceptor {
    ALIADO,
    ENEMIGO,
    UNO_MISMO
}
enum class TipoDeEstadistica {
    VIDA,
    ARMADURA,
    MANA,
    VELOCIDAD,
    DAÑO_FISICO,
    DAÑO_MAGICO,
    PRECISION_FISICA
}

enum class Criterio {
    IGUAL ,
    MAYOR_QUE ,
    MENOR_QUE ;
}

enum class Accion {
    ATAQUE_FISICO,
    DEFENDER,
    CURAR,
    ATAQUE_MAGICO,
    MEDITAR;
}