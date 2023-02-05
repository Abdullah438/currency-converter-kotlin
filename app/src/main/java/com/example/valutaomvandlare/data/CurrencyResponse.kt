package com.example.valutaomvandlare.data

data class CurrencyResponse(
    val base_code: String?, // USD
    val conversion_rates: ConversionRates?,
    val documentation: String?, // https://www.exchangerate-api.com/docs
    val result: String?, // success
    val terms_of_use: String?, // https://www.exchangerate-api.com/terms
    val time_last_update_unix: Int?, // 1673395201
    val time_last_update_utc: String?, // Wed, 11 Jan 2023 00:00:01 +0000
    val time_next_update_unix: Int?, // 1673481601
    val time_next_update_utc: String? // Thu, 12 Jan 2023 00:00:01 +0000
)