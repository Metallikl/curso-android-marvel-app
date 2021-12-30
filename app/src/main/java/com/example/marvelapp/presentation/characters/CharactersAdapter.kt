package com.example.marvelapp.presentation.characters

import android.os.Parcel
import android.os.Parcelable
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.luche.core.domain.Character

class CharactersAdapter : PagingDataAdapter<Character, CharactersViewHolder>(diffCallback)  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharactersViewHolder {
        return CharactersViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: CharactersViewHolder, position: Int) {
        getItem(position)?.let{
            holder.bind(it)
        }
    }

    companion object{
        //Callback que verifica se o item é diferente
        private val diffCallback = object : DiffUtil.ItemCallback<Character>() {
            //Fun que define se é o mesmo item, por hora, valida se nome são iguais.
            override fun areItemsTheSame(
                oldItem: Character,
                newItem: Character
            ): Boolean {
                return oldItem.name == newItem.name
            }
            //Fun que verifica se o conteudo é o mesmo, por hora, verifica a instancia
            override fun areContentsTheSame(
                oldItem: Character,
                newItem: Character
            ): Boolean {
                return oldItem == newItem
            }

        }
    }



}