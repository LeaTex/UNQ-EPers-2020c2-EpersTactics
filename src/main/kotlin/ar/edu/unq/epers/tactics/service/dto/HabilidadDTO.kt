package ar.edu.unq.epers.tactics.service.dto


import ar.edu.unq.epers.tactics.modelo.*
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import kotlin.random.Random

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes(
        JsonSubTypes.Type(value = AtaqueDTO::class, name = "Attack"),
        JsonSubTypes.Type(value = DefensaDTO::class, name = "Defend"),
        JsonSubTypes.Type(value = CurarDTO::class, name = "Heal"),
        JsonSubTypes.Type(value = AtaqueMagicoDTO::class, name = "MagicAttack"),
        JsonSubTypes.Type(value = MeditarDTO::class, name = "Meditate")
)
abstract class HabilidadDTO(){
    companion object {
        fun desdeModelo(habilidad: Habilidad): HabilidadDTO? {
            var habilidadDTO: HabilidadDTO? = null
            when (habilidad) {
                is Defensa -> { habilidadDTO = DefensaDTO(AventureroDTO.desdeModelo(habilidad.source), AventureroDTO.desdeModelo(habilidad.objetivo)) }
                is Meditar -> { habilidadDTO = MeditarDTO(AventureroDTO.desdeModelo(habilidad.objetivo)) }
                is Curar -> { habilidadDTO = CurarDTO(habilidad.poderMagico, AventureroDTO.desdeModelo(habilidad.objetivo)) }
                is Ataque -> { habilidadDTO = AtaqueDTO(habilidad.daño, habilidad.prisicionFisica, AventureroDTO.desdeModelo(habilidad.objetivo)) }
                is AtaqueMagico -> { habilidadDTO = AtaqueMagicoDTO(habilidad.poderMagico, habilidad.sourceLevel, AventureroDTO.desdeModelo(habilidad.objetivo)) }
                }
            return habilidadDTO
        }
    }
    abstract fun aModelo(): Habilidad
}

data class AtaqueDTO(val daño: Int, val prisicionFisica: Int, val objetivo: AventureroDTO): HabilidadDTO() {

    override fun aModelo(): Ataque {
        return Ataque(daño, prisicionFisica, objetivo.aModelo())
    }
}

class DefensaDTO(val source: AventureroDTO, val objetivo: AventureroDTO): HabilidadDTO() {
    override fun aModelo(): Defensa {
        return Defensa(source.aModelo(), objetivo.aModelo())
    }
}

data class CurarDTO(val poderMagico: Int, val objetivo: AventureroDTO): HabilidadDTO(){
    override fun aModelo(): Curar {
        return Curar(poderMagico, objetivo.aModelo())
    }
}

data class AtaqueMagicoDTO(val poderMagico: Int, val sourceLevel: Int, val objetivo: AventureroDTO): HabilidadDTO(){
    override fun aModelo(): AtaqueMagico {
        return AtaqueMagico(poderMagico, sourceLevel, objetivo.aModelo())
    }
}

class MeditarDTO(val objetivo: AventureroDTO): HabilidadDTO(){
    override fun aModelo(): Meditar {
        return Meditar(objetivo.aModelo())
    }
}

