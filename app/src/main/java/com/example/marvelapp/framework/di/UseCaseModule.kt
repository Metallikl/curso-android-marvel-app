package com.example.marvelapp.framework.di

import com.luche.core.usecase.GetCharactersUseCase
import com.luche.core.usecase.GetCharactersUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
//Indica que esse modelo terá o mesmo ciclo que o viewmodel em que for injetado
@InstallIn(ViewModelComponent::class)
interface UseCaseModule {
    //Define que ao injetar a interface GetCharactersUseCase, será passada a implementação
    // GetCharactersUseCaseImpl
    @Binds
    fun bindCharactersUseCase(usecase: GetCharactersUseCaseImpl) : GetCharactersUseCase
}