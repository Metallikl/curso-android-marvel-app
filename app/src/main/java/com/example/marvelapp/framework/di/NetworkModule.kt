package com.example.marvelapp.framework.di

import com.example.marvelapp.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

//Anotação Dagger que indica modulo
@Module
//Anotação do Hilt que indica como dele modulo deve ser usado. No caso singleton, esta no contexto da
//app e criação como singleton
@InstallIn(SingletonComponent::class)
object NetworkModule {
    //Provides, notação que indica que essa fun prove uma dependencia.

    @Provides
    fun provideLogginInterceptor() : HttpLoggingInterceptor{
        return HttpLoggingInterceptor().apply {
            setLevel(
                if(BuildConfig.DEBUG){
                    HttpLoggingInterceptor.Level.BODY
                } else{
                    HttpLoggingInterceptor.Level.NONE
                }
            )
        }
    }

    @Provides
    fun provideOkHttpClient(
        logginInterceptor: HttpLoggingInterceptor
    ) : OkHttpClient{
        return OkHttpClient.Builder()
            .addInterceptor(logginInterceptor)
            .readTimeout(15,TimeUnit.SECONDS)
            .connectTimeout(15,TimeUnit.SECONDS)
            .build()
    }

    @Provides
    fun provideGsonConverterFactory(): GsonConverterFactory{
        return GsonConverterFactory.create()
    }

    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        converterFactory: GsonConverterFactory
    ) : Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .build()
    }

}