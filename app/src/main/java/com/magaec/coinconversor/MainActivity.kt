package com.magaec.coinconversor

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.magaec.coinconversor.data.ExchangeRateApi
import com.magaec.coinconversor.repository.CurrencyRepository
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var amountEditText: EditText
    private lateinit var fromCurrencySpinner: Spinner
    private lateinit var toCurrencySpinner: Spinner
    private lateinit var convertButton: Button
    private lateinit var resultTextView: TextView
    private lateinit var lineChart: LineChart

    private val api = ExchangeRateApi.create()
    private val repository = CurrencyRepository(api)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Vincular los elementos de la interfaz con sus IDs
        amountEditText = findViewById(R.id.amountEditText)
        fromCurrencySpinner = findViewById(R.id.fromCurrencySpinner)
        toCurrencySpinner = findViewById(R.id.toCurrencySpinner)
        convertButton = findViewById(R.id.convertButton)
        resultTextView = findViewById(R.id.resultTextView)
        lineChart = findViewById(R.id.lineChart)

        // Lista de monedas
        val currencies = listOf("USD", "EUR", "GBP", "JPY", "MXN")

        // Configurar Spinners con las monedas
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, currencies)
        fromCurrencySpinner.adapter = adapter
        toCurrencySpinner.adapter = adapter

        // Acción del botón de conversión
        convertButton.setOnClickListener {
            convertCurrency()
        }

        // Configurar el gráfico
        setupChart()
    }

    private fun convertCurrency() {
        val amountText = amountEditText.text.toString()
        if (amountText.isEmpty()) {
            Toast.makeText(this, "Ingrese una cantidad", Toast.LENGTH_SHORT).show()
            return
        }

        val amount = amountText.toDouble()
        val fromCurrency = fromCurrencySpinner.selectedItem.toString()
        val toCurrency = toCurrencySpinner.selectedItem.toString()

        // Llamar a la API para obtener la tasa de conversión
        lifecycleScope.launch {
            try {
                val rate = repository.getExchangeRate(fromCurrency, toCurrency)
                val convertedAmount = amount * rate
                resultTextView.text = "$amount $fromCurrency = %.2f $toCurrency".format(convertedAmount)
                updateChart() // Actualizar el gráfico
            } catch (e: Exception) {
                Toast.makeText(this@MainActivity, "Error al obtener la tasa", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupChart() {
        // Datos ficticios para el gráfico
        val entries = mutableListOf<Entry>()
        entries.add(Entry(0f, 1f))  // Día 0, valor 1
        entries.add(Entry(1f, 1.2f)) // Día 1, valor 1.2
        entries.add(Entry(2f, 1.4f)) // Día 2, valor 1.4
        entries.add(Entry(3f, 1.5f)) // Día 3, valor 1.5

        val dataSet = LineDataSet(entries, "Tendencia de la moneda")
        dataSet.color = resources.getColor(android.R.color.holo_blue_dark)
        dataSet.valueTextColor = resources.getColor(android.R.color.black)

        val lineData = LineData(dataSet)
        lineChart.data = lineData
        lineChart.invalidate() // Refresca el gráfico
    }

    private fun updateChart() {
        // Datos ficticios para la actualización del gráfico
        val entries = mutableListOf<Entry>()
        entries.add(Entry(0f, 1f))  // Día 0, valor 1
        entries.add(Entry(1f, 1.3f)) // Día 1, valor 1.3
        entries.add(Entry(2f, 1.6f)) // Día 2, valor 1.6
        entries.add(Entry(3f, 1.7f)) // Día 3, valor 1.7

        val dataSet = LineDataSet(entries, "Tendencia de la moneda")
        dataSet.color = resources.getColor(android.R.color.holo_green_dark)
        dataSet.valueTextColor = resources.getColor(android.R.color.black)

        val lineData = LineData(dataSet)
        lineChart.data = lineData
        lineChart.invalidate() // Refresca el gráfico
    }
}
