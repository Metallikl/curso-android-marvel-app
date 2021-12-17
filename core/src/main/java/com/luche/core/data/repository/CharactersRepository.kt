package com.luche.core.data.repository

import androidx.paging.PagingSource
import com.luche.core.domain.Character

interface CharactersRepository {
    //PagingSource usar key(numero da pagina) e valor retornado
    fun getCharacters(query: String) : PagingSource<Int,Character>
}