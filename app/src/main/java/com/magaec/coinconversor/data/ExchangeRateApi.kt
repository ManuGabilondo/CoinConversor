package com.magaec.coinconversor.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

const val BASE_URL = "https://v6.exchangerate-api.com/v6/8ef9934a7d65cc9ecd80db77/"

interface ExchangeRateApi {
    @GET("latest/USD")
    suspend fun getExchangeRates(): ExchangeRatesResponse

    companion object {
        fun create(): ExchangeRateApi {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(ExchangeRateApi::class.java)
        }
    }
}
