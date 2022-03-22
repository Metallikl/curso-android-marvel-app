package com.example.core.repository

import androidx.paging.PagingSource
import com.example.core.domain.Character

interface CharactersRepository {
    //PagingSource usar key(numero da pagina) e valor retornado
    fun getCharacters(query: String) : PagingSource<Int,Character>
}