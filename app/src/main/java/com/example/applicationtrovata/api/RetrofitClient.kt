package com.example.applicationtrovata.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://viacep.com.br/ws/"

    // Cria e configura o cliente Retrofit
    val api: ViaCepApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL) // Define a URL base do ViaCEP
            .addConverterFactory(GsonConverterFactory.create()) // Converte JSON automaticamente
            .build()
            .create(ViaCepApi::class.java) // Cria a implementação da interface ViaCepApi
    }
}
