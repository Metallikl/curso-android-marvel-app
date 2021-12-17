package com.example.marvelapp.framework.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import java.util.*
import java.math.BigInteger
import java.security.MessageDigest

class AuthorizationInterceptor(
    private val publicKey: String,
    private val privateKey: String,
    private val calendar: Calendar
): Interceptor {

    @Suppress("MagicNumber")
    override fun intercept(chain: Interceptor.Chain): Response {
        //Chain é a chamada da api e atraves dele é possivel recuperar dados como a request, url etc
        val request = chain.request()
        val requestUrl = request.url
        //Gera timestamp em segundos
        val ts = (calendar.timeInMillis / 1000L).toString() //Convertendo milli segundo em segundos
        //Cria chave hash concatenado os valores conforme doc e gera md5
        val hash = "$ts$privateKey$publicKey".md5()
        //A partir da url, utilizamos o newBuilder, para adicionar o query parameter e gerar a nova url
        val newUrl = requestUrl.newBuilder()
                .addQueryParameter(QUERY_PARAMETER_TS,ts)
                .addQueryParameter(QUERY_PARAMETER_API_KEY,publicKey)
                .addQueryParameter(QUERY_PARAMETER_HASH, hash)
                .build()
        //A partir do chain, chamada original, chama fun proceed para proseguir
        // setando a nova url na request da chamada(chain)
        return chain.proceed(
                    request
                        .newBuilder()
                        .url(newUrl)
                        .build()
                )

    }

    @Suppress("MagicNumber")
    fun String.md5(): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1,
            md.digest(toByteArray())).toString(16).padStart(32, '0')
    }

    companion object{
        private const val QUERY_PARAMETER_TS = "ts"
        private const val QUERY_PARAMETER_API_KEY = "apikey"
        private const val QUERY_PARAMETER_HASH = "hash"
    }

}