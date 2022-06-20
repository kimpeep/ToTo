package com.example.toto.ui.activity


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.toto.R
import com.example.toto.adapter.ListAdapter
import com.example.toto.databinding.ActivityDetailBinding
import com.example.toto.ui.dialog.CustomDialog
import com.example.toto.viewmodel.WeatherViewModel
import org.json.JSONObject

class DetailActivity : AppCompatActivity() {

    private val TAG: String = DetailActivity::class.java.simpleName

    private var viewModel: WeatherViewModel = WeatherViewModel()
    private lateinit var binding: ActivityDetailBinding  //activity_detail.xml 을 바인딩

    private lateinit var listAdapter: ListAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_detail)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root) //xml 전체를 감싸는 최상단 부모를 root 라는 property 로 제공

        initRecyclerview()
        initForecastInfoViewModel()
        observeData()

    }


    /*
    * Recyclerview 설정
    * Recyclerview adapter 와 LinearLayoutManager 를 만들고 연결
    * GridLayoutManager : 그리드뷰로 표시
    * */
    private fun initRecyclerview() {
        listAdapter = ListAdapter(this)
        binding.recyclerviewList.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@DetailActivity)
            adapter = listAdapter
        }

        //그리드뷰로 설정 (GridLayoutManager)
        val gridLayoutManager = GridLayoutManager(this, 3)
        binding.recyclerviewList.layoutManager = gridLayoutManager
    }


    /* viewModel 설정 */
    private fun initForecastInfoViewModel() {
        viewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)
        var jsonObject= JSONObject()
        jsonObject.put("url", getString(R.string.weather_url))
        jsonObject.put("path", "forecast")
        jsonObject.put("id", getString(R.string.weather_seoul_id))
        jsonObject.put("appid", this.getString(R.string.weather_app_id))
        viewModel.getForecastInfoView(jsonObject)
    }


    /* viewModel observe 설정 */
    private fun observeData() {
        viewModel.isSuccForecast.observe(
            this, Observer { it ->
                if (it) {
                    viewModel.responseForecast.observe(
                        this, Observer {
                            listAdapter.setForecastItems(it.list!!)
                        }
                    )
                } else {
                    //ERROR dialog 띄우기
                    var customDialog = CustomDialog(this)
                    customDialog.show(getString(R.string.app_title), "날씨 예보 조회 실패 ")
                }
            }
        )
    }


}