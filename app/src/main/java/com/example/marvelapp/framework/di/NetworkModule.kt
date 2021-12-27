package com.example.marvelapp.framework.di

import com.example.marvelapp.BuildConfig
import com.example.marvelapp.framework.network.MarvelApi
import com.example.marvelapp.framework.network.interceptor.AuthorizationInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

//Anotação Dagger que indica modulo
@Module
//Anotação do Hilt que indica como dele modulo deve ser usado. No caso singleton, esta no contexto da
//app e criação como singleton
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val TIMEOUT_SECONDS = 15L

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
        logginInterceptor: HttpLoggingInterceptor,
        authorizationInterceptor: AuthorizationInterceptor
    ) : OkHttpClient{
        return OkHttpClient.Builder()
                //Interceptor que loga as chamada
            .addInterceptor(logginInterceptor)
                //Interceptor que seta configuração de authorização com as chaves da api
            .addInterceptor(authorizationInterceptor)
            .readTimeout(TIMEOUT_SECONDS,TimeUnit.SECONDS)
            .connectTimeout(TIMEOUT_SECONDS,TimeUnit.SECONDS)
            .build()
    }

    @Provides
    fun provideAuthorizationInterceptor() :  AuthorizationInterceptor {
        return AuthorizationInterceptor(
            publicKey = BuildConfig.PUBLIC_KEY,
            privateKey = BuildConfig.PRIVATE_KEY,
            calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        )
    }

    @Provides
    fun provideGsonConverterFactory(): GsonConverterFactory{
        return GsonConverterFactory.create()
    }

    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        converterFactory: GsonConverterFactory
    ) : MarvelApi {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .build().create(MarvelApi::class.java)
    }

}