package com.example.valutaomvandlare

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.valutaomvandlare.api.ExchangeRateClient
import com.example.valutaomvandlare.api.ExchangeRateService
import com.example.valutaomvandlare.data.CurrencyResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var convertButton: Button
    private lateinit var fromSpinner: Spinner
    private lateinit var toSpinner: Spinner
    private lateinit var inputEditText: EditText
    private lateinit var resultTextView: TextView
    private lateinit var exchangeRateService: ExchangeRateService

    private var exchangeResponse: CurrencyResponse? = null

    private var isFirstTime = true;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fromSpinner = findViewById(R.id.from_spinner)
        toSpinner = findViewById(R.id.to_spinner)

        val currencies = this.resources.getStringArray(R.array.currencies)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, currencies)
        fromSpinner.adapter = adapter
        toSpinner.adapter = adapter
        exchangeRateService =
            ExchangeRateClient.getClient()?.create(ExchangeRateService::class.java)!!


        fromSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long,
            ) {
                loadCurrencyRates(fromSpinner.selectedItem.toString(),
                    inputEditText.text.toString() != "")
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
        toSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long,
            ) {
                if (!isFirstTime) {
                    convertCurrency()
                } else {
                    isFirstTime = false
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        convertButton = findViewById(R.id.convert_button)
        inputEditText = findViewById(R.id.input_edit_text)
        resultTextView = findViewById(R.id.result_text_view)

        convertButton.setOnClickListener {
            convertCurrency()
        }
    }

    private fun loadCurrencyRates(currency: String, convertAfterLoad: Boolean) {
        exchangeRateService.getCurrencyRates(apiKey = getString(R.string.api_key),
            currency = currency)
            .enqueue(object : Callback<CurrencyResponse?> {
                override fun onResponse(
                    call: Call<CurrencyResponse?>,
                    response: Response<CurrencyResponse?>,
                ) {
                    exchangeResponse = response.body()
                    if(convertAfterLoad){
                        convertCurrency()
                    }
                }

                override fun onFailure(call: Call<CurrencyResponse?>, t: Throwable) {
                    Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "onFailure: Error => ${t.message}")
                }

            })
    }

    private fun convertCurrency() {
        try {
            val toCurrency = toSpinner.selectedItem.toString()

            if (exchangeResponse == null) {
                Toast.makeText(this, "Getting response please wait", Toast.LENGTH_SHORT).show()
            } else {
                val text: Double = inputEditText.text.toString().toDouble()
                if (text == 0.00) {
                    Toast.makeText(this, "Can't convert zero", Toast.LENGTH_SHORT).show()
                } else {
                    val result = text * getDoubleValue(toCurrency)
                    resultTextView.text = String.format(Locale.getDefault(), "%.2f", result)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "convertCurrency: Error: $e")
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }


    private fun getDoubleValue(currency: String): Double {
        when (currency) {
            "SEK" -> {
                return exchangeResponse?.conversion_rates?.SEK!!
            }
            "EUR" -> {
                return exchangeResponse?.conversion_rates?.EUR!!
            }
            "DKK" -> {
                return exchangeResponse?.conversion_rates?.DKK!!
            }
            "NOK" -> {
                return exchangeResponse?.conversion_rates?.NOK!!
            }
            "GBP" -> {
                return exchangeResponse?.conversion_rates?.GBP!!
            }
            "PLN" -> {
                return exchangeResponse?.conversion_rates?.PLN!!
            }
            "USD" -> {
                return exchangeResponse?.conversion_rates?.USD!!
            }
            else -> {
                return exchangeResponse?.conversion_rates?.AUD!!
            }
        }
    }
}

