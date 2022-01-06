package com.example.marvelapp.presentation.characters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.example.marvelapp.databinding.ItemCharacterLodMoreStateBinding

class CharactersLoadMoreStateViewHolder(
    itemBinding: ItemCharacterLodMoreStateBinding,
    retry: () -> Unit
) : RecyclerView.ViewHolder(itemBinding.root) {
    private val binding = ItemCharacterLodMoreStateBinding.bind(itemView)
    private val progressBarLoadMore = binding.progressLoadingMore
    private val textTryAgain = binding.textTryAgain.also {
        it.setOnClickListener {
            retry()
        }
    }

    fun bind(loadState: LoadState){
        progressBarLoadMore.isVisible = loadState is LoadState.Loading
        textTryAgain.isVisible = loadState is LoadState.Error
    }
   // Fun static para criação do viewHolder
    companion object{
        fun create(parent: ViewGroup, retry: () -> Unit) : CharactersLoadMoreStateViewHolder{
            val itemBinding = ItemCharacterLodMoreStateBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            //
            return CharactersLoadMoreStateViewHolder(itemBinding, retry)
        }
    }

}