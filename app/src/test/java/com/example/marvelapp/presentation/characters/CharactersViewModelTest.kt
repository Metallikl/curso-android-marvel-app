package com.example.marvelapp.presentation.characters

import androidx.paging.PagingData
import com.example.testing.MainCoroutineRule
import com.luche.core.domain.Character
import com.luche.core.usecase.GetCharactersUseCase
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.lang.RuntimeException

//Ao criar uma classe de test, devemos informar quem será o runner do teste.
//Usamos a notação RunWith do JUnit e como usaremos o mockito, passamos o MockitiJunitRunner pois,
//dessa forma, poderemos usar as notações do Mockito para gerar os obsj mocados.
@RunWith(MockitoJUnitRunner::class)
class CharactersViewModelTest{

    @ExperimentalCoroutinesApi
    //Cria dispatcher do coroutine para utilzar chamadas de coroutine nos testes
    //O TestCoroutineDispatcher executa tudo na mesma thread e executa o codigo imediaamente
    val testDispatchers : TestCoroutineDispatcher = TestCoroutineDispatcher()

    //Notação que indica que o mockito deve criar a instancia dessa var.
    @Mock
    lateinit var getCharactersUseCase: GetCharactersUseCase

    private lateinit var charactersViewModel: CharactersViewModel

    //Cria retorno do paging data para usar como retorno do usecase
    private val pagingDataCharacter = PagingData.from(
        listOf(
            Character(
                "3-D Man",
                "http://i.annihil.us/u/prod/marvel/i/mg/c/e0/535fecbbb9784.jpg"
            ),
            Character(
                "A-Bomb (HAS)",
                "http://i.annihil.us/u/prod/marvel/i/mg/3/20/5232158de5b16.jpg"
            ),
        )
    )
    //Notação que indica que essa fun deve ser rodada sempre antes das funções anotadas com @Test
    @ExperimentalCoroutinesApi
    @Before
    //Fun que rodara a inicilização das vars e instancias que usaremos nos testes
    fun setUp(){
        //Define para rodar no bloco main
        Dispatchers.setMain(testDispatchers)
        charactersViewModel = CharactersViewModel(getCharactersUseCase)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `should validate the paging data object values when calling charactersPagingData`() =
        //Tudo que trabalhe com coroutine deve ser usado dentro do runBlockingTest
        runBlockingTest{
            //No mockito usamos o when, porem, por causa do when do kotlin, usamos uma dependencia
            // mockitokotlin2 que permite usar o campos whenever no lugar.
            //Comando whenever é usado quando estamos trabalhando com objeto mockado. Nesse caso,vc
            //toda vez que  getCharactersUseCase.invoke for chamado, será retornado (thenReturn)
            // algo.
            //No caso esse usecase retorna um flow de pagingData de characters então geramos um flow
            //com a lista pagingDataCharacter definda anteriormente.
            //Então , toda vez que nosso usecase for chamado, será retornado a lista de
            // pagingDataCharacter
            // No invoke, estamos usando o comando any() que indica que pra qualquer param deve ser
            // aceito
            whenever(
                getCharactersUseCase.invoke(
                    any()
                )
            ).thenReturn(
                flowOf(
                    pagingDataCharacter
                )
            )

            val result = charactersViewModel.charactersPagingData("")
            //Para simplificar, faz o teste de se a lista retornou 1 item.
            assertEquals(1,result.count())
    }

    @ExperimentalCoroutinesApi
    //Qando testamos erro, via um exception, devemos explicitar qual exception é esperada dentro
    //da notavao Test
    @Test(expected = RuntimeException::class)
    fun `should throw an exception when the calling to the use case returns an exception`()=
        runBlockingTest {
            //Toda vez que o useCase for chamado, independente do param, será retornada uma
            // exception
            whenever(
                getCharactersUseCase.invoke(any())
            )
            //comando thenThrow para retorna ma exception
            .thenThrow(RuntimeException())
            //
            charactersViewModel.charactersPagingData("")
            //Quando teste não possui avalidação, assert, é considerado sucesso.
        }

    /*
     * Assim como temos o @Before anotado metodo setUp para seja executado sempre antes de um teste
     * ser iniciado, temos tambem @After, que roda sempre apos o teste finalizar. Aqui anotamos o
     * metodo tearDown que vai resetar o Dispatcher main e limpara os dados da coroutine
     */
    @ExperimentalCoroutinesApi
    @After
    fun tearDownDispatcher(){
        Dispatchers.resetMain()
        testDispatchers.cleanupTestCoroutines()
    }
}