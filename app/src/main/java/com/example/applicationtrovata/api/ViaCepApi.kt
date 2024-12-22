package com.example.applicationtrovata.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

data class CepResponse(
    val logradouro: String?,
    val bairro: String?,
    val localidade: String?,
    val uf: String?
)

interface ViaCepApi {
    @GET("{cep}/json/")
    fun buscarCep(@Path("cep") cep: String): Call<CepResponse>
}
