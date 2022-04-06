package com.example.dogsearch

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.dogsearch.databinding.ItemDogBinding
import com.squareup.picasso.Picasso

class DogViewHolder(view: View):RecyclerView.ViewHolder(view) {

    private val binding = ItemDogBinding.bind(view)

    fun bind(image:String){
        //Gracias al componente picasso se hace request al APi y lo convierte en imagen
        Picasso.get().load(image).into(binding.ivDog)
    }
}