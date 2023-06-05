package com.hmisael.pokedex.model

data class Pokemon (
    val  name : String,
     val url  : String){

     fun getNumber() : Int{
        val urlPartes = url.split("/")
         //corte de url para obtener el número de pokemon
         return Integer.parseInt(urlPartes[urlPartes.size-2])
    }
}

