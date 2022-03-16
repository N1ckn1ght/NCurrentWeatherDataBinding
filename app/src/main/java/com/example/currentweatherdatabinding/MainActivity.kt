package com.example.currentweatherdatabinding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.currentweatherdatabinding.databinding.ActivityMainBinding
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.FileNotFoundException
import java.io.InputStream
import java.net.URL
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private var detailsOpened = false
    var secondFragment = false

    lateinit var wdesc: String
    lateinit var windspeed: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        wdesc = getString(R.string.wdesc_not_avail)
        windspeed = getString(R.string.windspeed_not_avail)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.weather = Weather(getString(R.string.tvecity), "", getString(R.string.wdesc_not_avail), "")
    }

    fun onGetClick(v: View) {
        val city = findViewById<EditText>(R.id.city)
        // Используем IO-диспетчер вместо Main (основного потока)
        GlobalScope.launch (Dispatchers.IO) {
            loadWeather(city.text.toString())
        }
    }

    fun onSettingsClick(v: View) {
        SettingsDialog(this).show(supportFragmentManager, "test")
    }

    fun onWeatherClick(v: View) {
        if (detailsOpened) {
            supportFragmentManager.popBackStack()
        } else {
            if (secondFragment) {
                supportFragmentManager.beginTransaction().add(R.id.wdetails, WeatherFragmentSecond(wdesc, windspeed)).commit()
            } else {
                supportFragmentManager.beginTransaction().add(R.id.wdetails, WeatherFragment(wdesc, windspeed)).commit()
            }
        }
        detailsOpened = !detailsOpened
    }

    private fun loadWeather(city: String) {
        val API_KEY = getString(R.string.appid)
        val weatherURL = "https://api.openweathermap.org/data/2.5/weather?q=${city}&appid=${API_KEY}&units=metric"

        var temp: String = getString(R.string.temp_not_avail)
        var wicon: String? = null

        try {
            val stream = URL(weatherURL).content as InputStream
            val rData = Scanner(stream).nextLine().trimIndent()
            val weatherData: WeatherJSON = Gson().fromJson(rData, WeatherJSON::class.java)

            temp = weatherData.main.temp
            wdesc = weatherData.weather[0].description
            wicon ="http://openweathermap.org/img/w/" + weatherData.weather[0].icon + ".png"
            windspeed = weatherData.wind.speed
        } catch (e: FileNotFoundException) {
            this@MainActivity.runOnUiThread {
                Toast.makeText(this, "ERR: NO DATA FOUND", Toast.LENGTH_SHORT).show()
            }
            temp = getString(R.string.file_not_found)
        } finally {
            binding.weather = Weather("City: ${city}", temp, wdesc, windspeed)

            // temporary solution ((as always it becomes permanent))
            this@MainActivity.runOnUiThread {
                val ivIcon = findViewById<ImageView>(R.id.wicon)
                if (wicon != null) {
                    Picasso.with(this).load(wicon).into(ivIcon)
                    ivIcon.layoutParams.width = 200
                    ivIcon.layoutParams.height = 200
                } else {
                    ivIcon.setImageResource(0)
                }
            }
        }
    }

    // https://github.com/captaincod/CurrentWeatherDataBinding/blob/main/app/src/main/java/com/example/currentweatherdatabinding/MainActivity.kt
    data class WeatherJSON(val coord: Coord, val weather: Array<WeatherArray>, val base: String,
                           val main: WeatherMain, val visibility: Long, val wind: WeatherWind,
                           val clouds: WeatherClouds, val dt: Long, val sys: WeatherSys,
                           val timezone: Long, val id: Long, val name: String, val cod: Int)

    data class Coord(val lon: Double, val lat: Double)
    data class WeatherArray(val id: Int, val main: String, val description: String, val icon: String)
    data class WeatherMain(val temp: String, val feels_like: Double,
                           val temp_min: Double, val temp_max: Double,
                           val pressure: Int, val humidity: Int,
                           val gust: Double = 0.0, val sea_level: Int = 0, val grnd_level:Int = 0)
    data class WeatherWind(val speed: String, val deg: Int,
                           val gust: Double = 0.0)
    data class WeatherClouds(val all: Int)
    data class WeatherSys(val type: Int, val id: Int, val country: String, val sunrise: Long, val sunset: Long)
}