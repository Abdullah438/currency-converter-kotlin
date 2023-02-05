package com.example.valutaomvandlare.api

import com.example.valutaomvandlare.data.CurrencyResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ExchangeRateService {

    //Function for getting currency rates
    @GET("{apiKey}/latest/{currency}")
    fun getCurrencyRates(
        @Path("apiKey") apiKey: String,
        @Path("currency") currency: String,
    ): Call<CurrencyResponse>
}