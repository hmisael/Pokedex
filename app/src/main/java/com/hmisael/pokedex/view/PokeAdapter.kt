package com.hmisael.pokedex.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hmisael.pokedex.databinding.ItemListBinding
import com.hmisael.pokedex.model.Pokemon
import com.squareup.picasso.Picasso

class PokeAdapter : RecyclerView.Adapter<PokeAdapter.PokemonViewHolder>() {

    class PokemonViewHolder(val itemBinding: ItemListBinding) : RecyclerView.ViewHolder(itemBinding.root)

    private val differCallback =
        object : DiffUtil.ItemCallback<Pokemon>() {
            override fun areItemsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean {
                return oldItem.url == newItem.url && oldItem.name == newItem.name
            }
            override fun areContentsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean {
                return oldItem == newItem
            }
        }

    val differPokemons = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        return PokemonViewHolder(
            ItemListBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val pokemonActual = differPokemons.currentList[position]
        val pokeText = "#"+ pokemonActual.getNumber()+ pokemonActual.name
        holder.itemBinding.textPokemon.text = pokeText
        Picasso.get()
            .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/"
            +pokemonActual.getNumber()+".png")
            .into(holder.itemBinding.ivPokemon)
    }

    override fun getItemCount(): Int = differPokemons.currentList.size


}