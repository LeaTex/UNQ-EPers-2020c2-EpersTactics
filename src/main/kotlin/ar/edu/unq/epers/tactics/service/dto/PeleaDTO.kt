package ar.edu.unq.epers.tactics.service.dto

import ar.edu.unq.epers.tactics.modelo.Pelea
import java.time.LocalDateTime

data class PeleaDTO(var partyId:Long?, var date: LocalDateTime, var peleaId:Long?, var partyEnemiga:String){

    companion object {
        fun desdeModelo(pelea: Pelea):PeleaDTO {
            return PeleaDTO(pelea.party!!.id , pelea.timestamp, pelea.id, pelea.partyEnemiga)
        }
    }

    fun aModelo(): Pelea {
        var pelea = Pelea()
        pelea.id = peleaId
        pelea.timestamp = date
        pelea.partyEnemiga = partyEnemiga

        return pelea
    }
}