package com.hmisael.pokedex.model

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PokeService {
    //Acción y URL
    @GET("/api/v2/pokemon")
    //parámetros url: limit es la cantidad a mostrar (20 pokemons),
    // y offset es el pokerango del total (20, 40, 60, etc)
    fun getPokeList(@Query("limit") limit: Int, @Query("offset") offset: Int): Call<PokeResponse>
}