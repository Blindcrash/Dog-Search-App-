package com.example.dogsearch

import androidx.appcompat.app.AppCompatActivity
import android.view.inputmethod.InputMethodManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dogsearch.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(), android.widget.SearchView.OnQueryTextListener {
    // Variable creada para bindear las vistas
    private lateinit var binding:ActivityMainBinding
    private lateinit var adapter:DogAdapter
    // Se requiere esta variable para el cambio de imagenes al cambiar la busqueda
    private val dogImages = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.svDogs.setOnQueryTextListener(this)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        adapter = DogAdapter(dogImages)
        binding.rvDogs.layoutManager = LinearLayoutManager(this)
        binding.rvDogs.adapter = adapter
    }

    private fun getRetroFit():Retrofit{
        return Retrofit.Builder()
            .baseUrl("https://dog.ceo/api/breed/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    //Para las corrutinas hay que suspender las actividades o daran error
    private fun searchByName(query:String){
        CoroutineScope(Dispatchers.IO).launch {
            //Con esto se llama via la API los datos de la imagen del perro
            var call = getRetroFit().create(APIService::class.java).getDogsByBreed(url = "$query/images")
            var puppies = call.body()
            runOnUiThread {

                if (call.isSuccessful) {
                    //Show ReciclerView
                    // Evita que la respuesta sea null
                    val images = puppies?.images ?: emptyList()
                    //Borra las imagenes de la busqueda anterior
                    dogImages.clear()
                    // Luego de la borrada agrega las imagenes que estan en busqueda
                    dogImages.addAll(images)
                    // Se notifica al Adapter que han habido cambios en la fun
                    adapter.notifyDataSetChanged()

                } else {
                    //Show Error
                    showError()
                }
            }

        }

    }
    private fun showError(){
        Toast.makeText(this, "There has been an Error", Toast.LENGTH_SHORT).show()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (!query.isNullOrEmpty()){
            searchByName(query.lowercase())
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        //metodo usado cada que haya un cambio en el texto
        return true
    }

}