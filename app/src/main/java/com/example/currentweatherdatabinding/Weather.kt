package com.example.currentweatherdatabinding

data class Weather(var City: String,
                   var temperature: String,
                   var wicon: String?, var wdesc: String,
                   var windspeed: String)