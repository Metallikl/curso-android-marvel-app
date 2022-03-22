package com.example.core.usecase

import androidx.paging.PagingConfig
import com.example.testing.MainCoroutineRule
import com.example.testing.model.CharacterFactory
import com.example.testing.pagingsource.PagingSourceFactory
import com.example.core.repository.CharactersRepository
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

//Se por algum motivo o runner tiver que ser do jUnit ao inves do mockto, as anotações @Mock nao
//funcionaram. Para resolver, no metodo anotado como Before, devemos add o codigo:
//MockitoAnnotations.initMocks(this), que inicializara o processamento das anotacoes como se fosse
//o runner do mockito
@RunWith(MockitoJUnitRunner::class)
class GetCharactersUseCaseImplTest{

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule= MainCoroutineRule()

    private lateinit var getCharactersUseCase: GetCharactersUseCase
    //Cria mock do repositorio
    @Mock
    private lateinit var repository: CharactersRepository

    private val hero = CharacterFactory().create(CharacterFactory.Hero.ThreeDMan)

    //Usa factory com o codigo fake para criar pagingSource esperado pelo stub.
    //Como o mockito não consegue mockar classes que não são nossas, tivemos que criar esse factory
    //para "mockar" manualmente essa classe.
    private val fakePagingSource = PagingSourceFactory().create(
        listOf(hero)
    )

    @Before
    fun setUp(){
        getCharactersUseCase = GetCharactersUseCaseImpl(repository)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `should validate flow paging data creation when invoke use case us called`() =
        runBlockingTest {
            val query = ""
            whenever(repository.getCharacters(query))
                .thenReturn(
                    fakePagingSource
                )

            val result = getCharactersUseCase.invoke(
                GetCharactersUseCase.GetCharactersParams(
                    query,
                    PagingConfig(20)
                )
            )
            //Verify, fun do mockito que verifica se o obj mockado foi chamado durante o teste.
            //Dentro do verify passamos o obj mockado e na sequencia a funcao que deveria ser
            // chamada
            //Como parametro opcional do verify, podemos definir a qtd de vezes que essa funcao
            // deveria ser chamada. Por padrao é 1.
            verify(repository, times(1)).getCharacters(query)
            //
            assertNotNull(result.first())
        }

}