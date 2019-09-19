package com.example.ktest.widget.weatherWidget

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.support.constraint.ConstraintLayout
import android.support.v4.app.ActivityCompat
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.content.ContextWrapper
import android.location.Location
import android.widget.ImageView
import com.example.custom2.R
import com.example.custom2.Repository
import com.example.custom2.widget.weatherWidget.retrofit.Controller
import com.example.custom2.widget.weatherWidget.retrofit.CurrentWeather
import com.google.android.gms.internal.s
import com.google.android.gms.internal.tt
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.lang.Exception
import kotlin.math.roundToInt


class WeatherWidget(context: Context?) : ConstraintLayout(context) {
    lateinit var city: TextView
    lateinit var temp: TextView
    lateinit var icon: ImageView
    lateinit var updateThread: Thread

    constructor(context: Context?, attr: AttributeSet?) : this(context) {
        LayoutInflater.from(context).inflate(R.layout.widget_weather, this, true)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        val mas = getChildrens(this)
        city = findViewById(R.id.city)
        temp = findViewById(R.id.temp)
        icon = findViewById(R.id.weatherIcon)
        updateThread = Thread {

            while (true) {
                fetchWeather()
                Thread.sleep(1000)
            }

        }
        updateThread.start()
    }

    private fun getChildrens(v: View?): ArrayList<View> {
        val result = ArrayList<View>()
        if (v != null) {

            if (v is ViewGroup) {
                result.add(v)
                val temp = ArrayList<View>()
                for (i in 0..v.childCount) {
                    temp.add(v.getChildAt(i))
                    temp.addAll(getChildrens(v.getChildAt(i)))
                    result.addAll(temp)
                }
            } else result.add(v as View)
            return result
        }
        return result
    }

    private fun fetchWeather() {
        if (ActivityCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                        context,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(getActivity(), arrayOf(ACCESS_FINE_LOCATION), 1)
        } else {
            var client: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
            client.lastLocation.addOnSuccessListener { location: Location? ->
                run {
                    try {
                        Controller.start().getCurrentWeather(
                                location?.latitude as Double,
                                location?.longitude as Double,
                                Controller.API_KEY,
                                Controller.DEFAULT_UNITS
                        )
                                .doOnError { s -> s.printStackTrace() }
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe({ result: CurrentWeather ->
                                    run {
                                        city.setText(result.cityName)
                                        var d: Double = result.main.temp.toString().toDouble();
                                        if (Repository.getInstance().settings.setParams.weather == 1) {
                                            d = d / 5 * 9 + 32;
                                            val t = d.roundToInt()
                                            temp.setText(t.toString() + "°F")
                                        } else {
                                            val t = d.roundToInt()
                                            temp.setText(t.toString() + "°C")
                                        }

                                        val ico: Int = getIconId(result.weather.get(0).id)
                                        icon.setImageResource(ico)
                                        val u = 0
                                    }
                                }, { error ->
                                    error.printStackTrace()
                                }
                                )


                    } catch (e: Exception) {
                        e.printStackTrace()
                    }


                }
            }
        }
    }

    private fun getActivity(): Activity {
        var context: Context = context
        while (context is ContextWrapper) {
            if (context is Activity) {
                return context
            }
            context = context.baseContext
        }
        return context as Activity
    }

    private fun getIconId(id: String): Int {
        return when (id) {
            "01n" -> R.drawable.n01
            "01d" -> R.drawable.d01
            "02n" -> R.drawable.n02
            "02d" -> R.drawable.d02
            "03d", "03n" -> R.drawable.n03
            "04n", "04d" -> R.drawable.n04
            "09d", "09n" -> R.drawable.d09
            "10n" -> R.drawable.n10
            "10d" -> R.drawable.d10
            "11d", "11n" -> R.drawable.d11
            "13n", "13d" -> R.drawable.d13
            "50d", "50n" -> R.drawable.d50
            else -> R.drawable.d01
        }

    }
}