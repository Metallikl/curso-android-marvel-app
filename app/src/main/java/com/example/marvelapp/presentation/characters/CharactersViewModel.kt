package com.example.marvelapp.presentation.characters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.core.domain.Character
import com.example.core.usecase.GetCharactersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

//Instrui o hilt de que essa classe deve respeitar o ciclo de vida de um viewModel
//e "cria" o factory do viewModel
@HiltViewModel
class CharactersViewModel @Inject constructor(
    private val getCharactersUseCase: GetCharactersUseCase
) : ViewModel(){

    fun charactersPagingData(query: String) : Flow<PagingData<Character>> {
        return getCharactersUseCase(
            GetCharactersUseCase.GetCharactersParams(query, getPageConfig())
        ).cachedIn(viewModelScope)
        //Conforme, doc, seta cachedIn para que os dados sejam cacheado no ciclo de vida do
        // viewModel
    }

    private fun getPageConfig() = PagingConfig (
        pageSize = 20
    )
}