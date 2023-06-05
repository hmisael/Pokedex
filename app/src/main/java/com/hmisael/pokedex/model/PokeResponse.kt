package com.hmisael.pokedex.model

import com.google.gson.annotations.SerializedName

data class PokeResponse (
    //mismo nombre de variable json
    //la respuesta es un array de Pokemon con 2  atributos
    @SerializedName("results") val results: List<Pokemon>
)
