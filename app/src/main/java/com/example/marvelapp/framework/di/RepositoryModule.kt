package com.example.marvelapp.framework.di

import com.example.marvelapp.framework.CharactersRepositoryImpl
import com.example.marvelapp.framework.network.response.DataWrapperResponse
import com.example.marvelapp.framework.remote.RetrofitCharactersDataSource
import com.luche.core.data.repository.CharacterRemoteDataSource
import com.luche.core.data.repository.CharactersRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    //Usamos @Bind para instruir o hilt de qual implementação ele deve utilizar ao injetar
    //uma dependencia de interface. Dessa forma, se amanha a implementação dessa interface for
    // alterada,basta alterar o tipo da implementação usada.
    //Ex:Aqui temos que para as dependencias de CharacterRemoteDataSource, usaremos a implementação
    //de RetrofitCharactersDataSource porem, se mudarmos para outra lib de conexão no futuro, basta
    //mudar o tipo de dataSource de RetrofitCharactersDataSource para a nova implementação
    @Binds
    fun bindCharacterRepository(repository: CharactersRepositoryImpl) : CharactersRepository

    @Binds
    fun bindRemoteDataSource(
        dataSource: RetrofitCharactersDataSource
    ) : CharacterRemoteDataSource<DataWrapperResponse>
}