package com.luche.core.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.luche.core.data.repository.CharactersRepository
import com.luche.core.domain.Character
import com.luche.core.usecase.base.PagingUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.management.loading.ClassLoaderRepository

class GetCharactersUseCase @Inject constructor (
    //Sempre passar a dependencia da interface e não da implementação.
    private val charactersRepository: CharactersRepository
) : PagingUseCase<GetCharactersUseCase.GetCharactersParams, Character>() {

    override fun createFlowObservable(params: GetCharactersParams): Flow<PagingData<Character>> {
        //Cria pager, cujo retorno é um PagingData, que recebe a configuração enviada pelo ViewModel
        //e realiza chamada da api passando os params de query recebidos. Ao final, usa o . flow
        // para converter o PagingData em um Flow.
        return Pager( config = params.pagingConfig){
            charactersRepository.getCharacters(params.query)
        }.flow
    }

    //Cria data class que representa o tipo de entrada para o usecase.
    data class GetCharactersParams(val query: String, val pagingConfig: PagingConfig)

}