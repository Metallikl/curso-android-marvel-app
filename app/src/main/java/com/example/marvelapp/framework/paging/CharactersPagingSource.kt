package com.example.marvelapp.framework.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.marvelapp.framework.network.response.DataWrapperResponse
import com.example.marvelapp.framework.network.response.toCharacterModel
import com.example.core.repository.CharacterRemoteDataSource
import com.example.core.domain.Character

class CharactersPagingSource(
    private val remoteDataSource: CharacterRemoteDataSource<DataWrapperResponse>,
    private val query: String
) : PagingSource<Int,Character>(){

    @Suppress("TooGenericExceptionCaught")
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Character> {
        return try {
                //params é recebido via param. Na primeira chamada do metodo load, key é null entao
                // , elvis operator para definir o offset incial 0
                val offset = params.key ?: 0
                val queries = hashMapOf(
                    "offset" to offset.toString()
                )
                //
                if(query.isNotEmpty()){
                    queries["nameStartsWith"] = query
                }
                //Chama api da marvel e recebe a resposta
                val response = remoteDataSource.fetchCharacters(queries)
                //Resgata da response o offset atual devolvido pela api
                val responseOffset = response.data.offset
                //Resgata da response o total de personagens retornados
                val totalCharacters = response.data.total
                //Cria o LoadResult para retorno
                //data = Lista do tipo Value retrnado
                //prevKey = Ultima pagina
                //nextKey = Proxima chave. Enquanto diferente de null, o pagin entende que existem
                // mais dados para solicitar.

                LoadResult.Page(
                    data = response.data.results.map { it.toCharacterModel() },
                    prevKey = null,//Ultima pagina
                    //Enquanto next key diferente de null, o pagin entende que existem mais dados a
                    nextKey = if(responseOffset < totalCharacters){
                                    responseOffset + LIMIT
                                }else null
                )
            }catch (e: Exception){
                LoadResult.Error(e)
            }
    }
    //Fun que contem o ultimo estado do paging salvo. Usado quando o o.s tenta recuperar o app ou
    //recarregá-lo
    override fun getRefreshKey(state: PagingState<Int, Character>): Int? {
        //Se existe uma posição salva no state, tenta recupera ultima pagina mais proxima da posição
        //Se a pagina existir, tenta retorna prevKey + offset.
        //Se nulo, tenta retornar nextKey - offset
        return state.anchorPosition?.let { anchorposition ->
            val anchorPage = state.closestPageToPosition(anchorposition)
            anchorPage?.prevKey?.plus(LIMIT) ?: anchorPage?.nextKey?.minus(LIMIT)
        }
    }

    companion object{
        private const val LIMIT = 20
    }
}