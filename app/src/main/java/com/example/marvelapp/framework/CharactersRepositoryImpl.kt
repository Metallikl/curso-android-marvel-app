package com.example.marvelapp.framework

import androidx.paging.PagingSource
import com.example.marvelapp.framework.network.response.DataWrapperResponse
import com.luche.core.data.repository.CharacterRemoteDataSource
import com.luche.core.data.repository.CharactersRepository
import com.luche.core.domain.Character
import javax.inject.Inject

//@Inject consturctor, indica ao dagger/hilt que essa classe poderá ser invejetada
class CharactersRepositoryImpl @Inject constructor(
    private val remoteDataSource: CharacterRemoteDataSource<DataWrapperResponse>
): CharactersRepository {
    override fun getCharacters(query: String): PagingSource<Int, Character> {
        //return
    }
}