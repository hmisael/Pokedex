package com.hmisael.pokedex.view

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hmisael.pokedex.model.PokeResponse
import com.hmisael.pokedex.model.PokeService
import com.hmisael.pokedex.databinding.FragmentMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainFragment : Fragment() {
     val TAG = "POKEDEX"

    private lateinit var pokeAdapter : PokeAdapter
    private lateinit var binding: FragmentMainBinding
    private lateinit var retrofit : Retrofit
    private var offset = 0
    private var carga = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()

        retrofit = Retrofit.Builder()
            .baseUrl("https://pokeapi.co")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        carga = true
        offset= 0
        obtenerDatos(offset)
    }


    private fun initRecyclerView() {
        pokeAdapter = PokeAdapter()

        binding.rvPokemon.apply {
            val orientation = resources.configuration.orientation
            setHasFixedSize(true)
            layoutManager = if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                // Orientación vertical
                GridLayoutManager(requireContext(), 3)
            } else {
                // Orientación horizontal
                GridLayoutManager(requireContext(),5)
            }

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    //si scroll hacia abajo o distinto a tope superior
                    if(dy>0){
                        val visibleCount = (layoutManager as GridLayoutManager).childCount
                        val totalItemCount = (layoutManager as GridLayoutManager).itemCount
                        val pastVisibleItems = (layoutManager as GridLayoutManager).findFirstVisibleItemPosition()

                        if (carga){
                            if (visibleCount + pastVisibleItems >= totalItemCount){
                                Log.i(TAG, "¡Fin de recycler view!")
                                carga = false
                                offset += 20
                                obtenerDatos(offset)
                            }
                        }
                    }
                }
            })

            adapter = pokeAdapter
        }
    }

    private fun obtenerDatos(offset: Int){
        CoroutineScope(Dispatchers.IO).launch{
            //A continuación, ,se ejecutará en un hilo secundario
            //Crear servicio y obtener respuesta
            val service = retrofit.create(PokeService::class.java)
            val pokemonResponse = service.getPokeList(20, offset)
            pokemonResponse.enqueue(object : Callback<PokeResponse> {
                override fun onResponse(call: Call<PokeResponse>, response: Response<PokeResponse>) {
                    carga = true
                    requireActivity().runOnUiThread {
                        if (response.isSuccessful) {
                            val respuesta = response.body()
                            val listaPokemon = respuesta?.results
                            pokeAdapter.differPokemons.submitList(listaPokemon)
                        }
                        else{
                            Log.e(TAG, "onResponse: "+response.errorBody())
                        }
                    }
                }
                override fun onFailure(call: Call<PokeResponse>, t: Throwable) {
                    carga = true
                    Log.e(TAG, "onFailure: " + t.message)
                }
            })
        }
    }

}