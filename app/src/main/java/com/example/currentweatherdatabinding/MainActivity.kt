package com.example.currentweatherdatabinding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.currentweatherdatabinding.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.io.FileNotFoundException
import java.io.InputStream
import java.net.URL
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.weather = Weather("", "")
        // TODO: show images for the weather, the cloudiness and the wind direction
    }

    fun onGetClick(v: View) {
        val city = findViewById<EditText>(R.id.city)
        // Используем IO-диспетчер вместо Main (основного потока)
        GlobalScope.launch (Dispatchers.IO) {
            loadWeather(city.text.toString())
        }
    }

    private fun loadWeather(city: String) {
        val API_KEY = getString(R.string.appid)
        val weatherURL = "https://api.openweathermap.org/data/2.5/weather?q=${city}&appid=${API_KEY}&units=metric"

        try {
            val stream = URL(weatherURL).getContent() as InputStream
            val rdata = Scanner(stream).nextLine()
            val data = JSONObject(rdata).toMap()
            binding.weather = Weather(city, getString(R.string.temp_not_avail))
            if (data.containsKey("main")){
                val dataMain = JSONObject(data["main"].toString()).toMap()
                if (dataMain.containsKey("temp")) {
                    binding.weather = Weather(city, dataMain["temp"].toString())
                }
            }
        } catch (e: FileNotFoundException) {
            this@MainActivity.runOnUiThread(java.lang.Runnable {
                Toast.makeText(this, "ERROR 404", Toast.LENGTH_SHORT).show()
            })
            binding.weather = Weather(city, getString(R.string.file_not_found))
        }
    }

    // https://stackoverflow.com/questions/44870961/how-to-map-a-json-string-to-kotlin-map
    private fun JSONObject.toMap(): Map<String, *> = keys().asSequence().associateWith {
        when (val value = this[it])
        {
            is JSONArray ->
            {
                val map = (0 until value.length()).associate { Pair(it.toString(), value[it]) }
                JSONObject(map).toMap().values.toList()
            }
            is JSONObject -> value.toMap()
            JSONObject.NULL -> null
            else            -> value
        }
    }
}