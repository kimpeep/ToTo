package com.example.toto.ui.activity

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.toto.R
import com.example.toto.databinding.ActivityWeatherBinding
import com.example.toto.model.WeatherModel
import com.example.toto.ui.dialog.CustomDialog
import com.example.toto.viewmodel.WeatherViewModel
import org.json.JSONObject

@RequiresApi(Build.VERSION_CODES.M)
class WeatherActivity : AppCompatActivity() {

    private val TAG: String = WeatherActivity::class.java.simpleName

    private var viewModel: WeatherViewModel = WeatherViewModel()
    private lateinit var binding: ActivityWeatherBinding  //activity_weather.xml 을 바인딩

    private lateinit var fabOpen: Animation
    private lateinit var fabClose: Animation
    private var isFabOpen: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_weather)

        binding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root) //xml 전체를 감싸는 최상단 부모를 root 라는 property 로 제공

        //initView()
        initWeatherInfoViewModel()
        observeData()

    }


//    /* View 설정 */
//    private fun initView() {
//        fabOpen = AnimationUtils.loadAnimation(this, R.anim.fab_open)
//        fabClose = AnimationUtils.loadAnimation(this, R.anim.fab_close)
//
//        binding.fabMain.setOnClickListener {
//            toggleFab(it)
//        }
//
//        binding.fabGoDetail.setOnClickListener {  //DetailActivity 로 이동
//            val intent = Intent(this, DetailActivity::class.java)
//            startActivity(intent)
//        }
//
//        binding.fabGoShare.setOnClickListener {  //공유하기
//            var valueText = "오늘의 날씨 : " + binding.tvTemp.text + " / " + binding.tvMain.text
//            val intent = Intent(Intent.ACTION_SEND)
//            intent.type = "text/plain"
//            intent.putExtra(Intent.EXTRA_TEXT, valueText)
//            startActivity(Intent.createChooser(intent, getString(R.string.app_title)))
//        }
//
//    }


    /* viewModel 설정 */
    private fun initWeatherInfoViewModel() {
        viewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)
        var jsonObject = JSONObject()
        jsonObject.put("url", getString(R.string.weather_url))
        jsonObject.put("path", "weather")
        jsonObject.put("q", "Seoul")
        jsonObject.put("appid", getString(R.string.weather_app_id))
        viewModel.getWeatherInfoView(jsonObject)

    }


    /* viewModel observe 설정 */
    private fun observeData() {
        viewModel.isSuccWeather.observe(
            this, Observer { it ->
                if (it) {
                    viewModel.responseWeather.observe(
                        this, Observer {
                            setWeatherData(it)
                        }
                    )
                } else {
                    //ERROR dialog 띄우기
                    var customDialog = CustomDialog(this)
                    customDialog.show(getString(R.string.app_title), "현재 날씨 조회 실패 ")
                }
            }
        )
    }


    /* 통신하여 받아온 날씨 데이터를 통해 UI 업데이트 메소드 */
    private fun setWeatherData(model: WeatherModel) {
        binding.tvName.text = model.name
        binding.tvCountry.text = model.sys.country
        binding.tvTemp.text = doubleToStrFormat(2, model.main.temp!! - 273.15) + " 'C"
        binding.tvMain.text = model.weather[0].main
        binding.tvDescription.text = model.weather[0].description
        binding.tvWind.text = doubleToStrFormat(2, model.wind.speed!!) + " m/s"
        binding.tvCloud.text = doubleToStrFormat(2, model.clouds.all!!) + " %"
        binding.tvHumidity.text = doubleToStrFormat(2, model.main.humidity!!) + " %"
    }


    private fun doubleToStrFormat(n: Int, value: Double): String? {
        return String.format("%." + n + "f", value)
    }

}