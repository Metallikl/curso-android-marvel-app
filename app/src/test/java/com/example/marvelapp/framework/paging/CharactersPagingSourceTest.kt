package com.example.marvelapp.framework.paging

import androidx.paging.PagingSource
import com.example.core.domain.Character
import com.example.marvelapp.framework.network.response.DataWrapperResponse
import com.example.testing.MainCoroutineRule
import com.example.core.repository.CharacterRemoteDataSource
import com.example.marvelapp.factory.response.DataWrapperResponseFactory
import com.example.testing.model.CharacterFactory
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.lang.RuntimeException

@RunWith(MockitoJUnitRunner::class)
class CharactersPagingSourceTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var characterPagingSource: CharactersPagingSource

    @Mock
    lateinit var remoteDataSource: CharacterRemoteDataSource<DataWrapperResponse>

    private val dataWrapperResponse = DataWrapperResponseFactory()
    private val characterFactory = CharacterFactory()

    @Before
    fun setUp() {
        characterPagingSource = CharactersPagingSource(
            remoteDataSource,
            ""
        )
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `should return a success load result when load is called`() =
        runBlockingTest {
            /*
             * Padrao AAA
             * Arrange - Preparação . Prepara os mocks
             * Act - Função a ser testada
             * Assert - Verificaçoes
             */
            //Arrange
            whenever(
                remoteDataSource.fetchCharacters(any())
            ).thenReturn(
                dataWrapperResponse.create()
            )
            //Act
            val result = characterPagingSource.load(
                PagingSource.LoadParams.Refresh(
                    null,
                    loadSize = 2,
                    false
                )
            )
            //Assert
            //Lista esperada no retorno
            val expected = listOf(
                characterFactory.create(CharacterFactory.Hero.ThreeDMan),
                characterFactory.create(CharacterFactory.Hero.ABomb)
            )
            //Compara se os objs Page sao iguais.
            assertEquals(
                PagingSource.LoadResult.Page(
                    data = expected,
                    prevKey = null,
                    nextKey = 20
                ),
                result
            )
        }

    @ExperimentalCoroutinesApi
    @Test
    fun `should return a error load result when load is called`() =
        runBlockingTest {
            val exception= RuntimeException()
            //Arrange
            whenever(
                remoteDataSource.fetchCharacters(any())
            ).thenThrow(exception

            )
            //Act
            val result = characterPagingSource.load(
                PagingSource.LoadParams.Refresh(
                    key = null,
                    loadSize = 2,
                    placeholdersEnabled = false
                )
            )
            //Assert
            assertEquals(
                PagingSource.LoadResult.Error<Int,Character>(exception),
                result
            )
        }
}



