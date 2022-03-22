package com.example.core.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.core.repository.CharactersRepository
import com.example.core.domain.Character
import com.example.core.usecase.GetCharactersUseCase.GetCharactersParams
import com.example.core.usecase.base.PagingUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


/**
 * Criamos a interface com a fun invoke e fazemos anossa classe GetCharactersUseCaseImpl implemetala
 * Isso ja era feito anteriormente, mas como uma fun.
 * Além de seguir conceitos do SOLID, depender de interfaces e não implementações, agora o mockito
 * poderá mockar esse usecase
 */
interface GetCharactersUseCase {
    operator fun invoke(params: GetCharactersParams): Flow<PagingData<Character>>

    //Cria data class que representa o tipo de entrada para o usecase.
    data class GetCharactersParams(val query: String, val pagingConfig: PagingConfig)
}


class GetCharactersUseCaseImpl @Inject constructor(
    //Sempre passar a dependencia da interface e não da implementação.
    private val charactersRepository: CharactersRepository
) : PagingUseCase<GetCharactersUseCase.GetCharactersParams, Character>(),
    GetCharactersUseCase {
    override fun createFlowObservable(params: GetCharactersParams): Flow<PagingData<Character>> {
        //Cria pager, cujo retorno é um PagingData, que recebe a configuração enviada pelo ViewModel
        //e realiza chamada da api passando os params de query recebidos. Ao final, usa o . flow
        // para converter o PagingData em um Flow.
        val paginSource = charactersRepository.getCharacters(params.query)
        return Pager(config = params.pagingConfig) {
            paginSource
        }.flow
    }
}