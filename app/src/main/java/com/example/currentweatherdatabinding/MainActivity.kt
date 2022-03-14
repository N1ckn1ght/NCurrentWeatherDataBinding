package com.example.currentweatherdatabinding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
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
    var detailsOpened = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.weather = Weather(getString(R.string.tvecity), "", null, getString(R.string.wdesk_not_avail))
    }

    fun onGetClick(v: View) {
        val city = findViewById<EditText>(R.id.city)
        // Используем IO-диспетчер вместо Main (основного потока)
        GlobalScope.launch (Dispatchers.IO) {
            loadWeather(city.text.toString())
        }
    }

    fun onWeatherClick(v: View) {
        if (detailsOpened) {
            supportFragmentManager.popBackStack()
        } else {
            supportFragmentManager.beginTransaction().add(R.id.wdetails, WeatherFragment()).commit()
        }
        detailsOpened = !detailsOpened
    }

    private fun loadWeather(city: String) {
        val API_KEY = getString(R.string.appid)
        val weatherURL = "https://api.openweathermap.org/data/2.5/weather?q=${city}&appid=${API_KEY}&units=metric"

        var temp: String = getString(R.string.temp_not_avail)
        var wicon: String? = null
        var wdesc: String = getString(R.string.wdesk_not_avail)

        try {
            val stream = URL(weatherURL).getContent() as InputStream
            val rdata = Scanner(stream).nextLine()
            val data = JSONObject(rdata).toMap()

            if (data.containsKey("main")) {
                val dataMain = JSONObject(data["main"].toString()).toMap()
                if (dataMain.containsKey("temp")) {
                    temp = dataMain["temp"].toString()
                }
            }
            if (data.containsKey("weather")) {
                var dataWeather = JSONObject(data["weather"].toString()).toMap()
                dataWeather = JSONObject(dataWeather[0].toString()).toMap()
                if (dataWeather.containsKey("icon")) {
                    wicon = "http://openweathermap.org/img/w/" + dataWeather["icon"].toString() + ".png"
                }
                if (dataWeather.containsKey("wdesk")) {
                    wdesc = dataWeather["description"].toString()
                }
            }
        } catch (e: FileNotFoundException) {
            this@MainActivity.runOnUiThread(java.lang.Runnable {
                Toast.makeText(this, "ERROR 404", Toast.LENGTH_SHORT).show()
            })
            temp = getString(R.string.file_not_found)
        } finally {
            binding.weather = Weather(city, temp, wicon, wdesc)
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