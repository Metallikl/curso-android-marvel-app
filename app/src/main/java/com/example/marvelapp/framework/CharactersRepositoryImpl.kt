package com.example.marvelapp.framework

import androidx.paging.PagingSource
import com.example.marvelapp.framework.network.response.DataWrapperResponse
import com.example.marvelapp.framework.paging.CharactersPagingSource
import com.example.core.repository.CharacterRemoteDataSource
import com.example.core.repository.CharactersRepository
import com.example.core.domain.Character
import javax.inject.Inject

//@Inject consturctor, indica ao dagger/hilt que essa classe poderá ser invejetada
class CharactersRepositoryImpl @Inject constructor(
    private val remoteDataSource: CharacterRemoteDataSource<DataWrapperResponse>
): CharactersRepository {
    override fun getCharacters(query: String): PagingSource<Int, Character> {
        return CharactersPagingSource(remoteDataSource,query)
    }
}