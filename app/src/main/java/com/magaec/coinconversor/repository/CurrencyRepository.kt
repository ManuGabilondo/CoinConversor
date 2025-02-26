package com.magaec.coinconversor.repository

import com.magaec.coinconversor.data.ExchangeRateApi

class CurrencyRepository(private val api: ExchangeRateApi) {
    suspend fun getExchangeRate(base: String, target: String): Double {
        val response = api.getExchangeRates()
        return response.conversion_rates[target] ?: 1.0
    }
}
