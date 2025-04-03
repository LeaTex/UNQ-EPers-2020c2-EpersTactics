package ar.edu.unq.epers.tactics.service.dto

import ar.edu.unq.epers.tactics.modelo.Aventurero
import ar.edu.unq.epers.tactics.modelo.Party


data class PartyDTO(var id: Long?, var nombre: String, var imagenURL: String, var aventureros: MutableList<AventureroDTO>){

    companion object {

        fun desdeModelo(party: Party): PartyDTO{
            return PartyDTO(party.id, party.nombre, party.imagenURL, aventurerosADTO(party))
        }

        private fun aventurerosADTO(party: Party): MutableList<AventureroDTO> {
            return party.aventureros.map { AventureroDTO.desdeModelo(it) }.toMutableList()
        }
    }

    fun aModelo(): Party {
        var party = Party()
        party.id = this.id
        party.imagenURL = this.imagenURL
        party.nombre = nombre

        party.aventureros = aventurerosDTOAModel(party)

        return party
    }

    private fun aventurerosDTOAModel(party : Party) : MutableList<Aventurero>{

        var result = mutableListOf<Aventurero>()

        // TODO ¿se podría mejorar con un map?
        for(aveDTO in aventureros) {
            var adventurer : Aventurero
            adventurer = aveDTO.aModelo()
            adventurer.party = party
            result.add(adventurer)
        }
        return  result
    }

    fun actualizarModelo(party: Party){
        party.nombre = this.nombre
        party.imagenURL = this.imagenURL

        // TODO revisar la magia de agregar y sacar cosas
        party.aventureros = this.aventurerosDTOAModel(party)
    }
}