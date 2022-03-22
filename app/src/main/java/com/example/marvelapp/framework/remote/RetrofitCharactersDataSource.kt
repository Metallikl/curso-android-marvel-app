package com.example.marvelapp.framework.remote

import com.example.marvelapp.framework.network.MarvelApi
import com.example.marvelapp.framework.network.response.DataWrapperResponse
import com.example.core.repository.CharacterRemoteDataSource
import javax.inject.Inject

//Classes que são "nossas" criadas no projeto, não precisam ser criadas em modulos para que o
// dagger/hilt consiga fazer a injeção dela em outras classes. Para isso, basta colocar a anotação
// @Inject e declarar construtor no construtora da classe.

class RetrofitCharactersDataSource @Inject constructor(
    private val marvelApi: MarvelApi
): CharacterRemoteDataSource<DataWrapperResponse> {
    override suspend fun fetchCharacters(queries: Map<String, String>): DataWrapperResponse {
       return marvelApi.getCharacters(queries)
    }
}