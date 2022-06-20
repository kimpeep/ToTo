package com.example.toto.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.toto.R
import com.example.toto.model.ForecastListModel

class ListAdapter(context: Context): RecyclerView.Adapter<ListAdapter.ListViewHolder>() {

    var mContext: Context = context
    private var forecastItems: List<ForecastListModel> = listOf()

    /*
    * 이 어뎁터가 아이템을 얼마나 가지고 있는지 얻는 함수
    * */
    override fun getItemCount(): Int {
        return forecastItems.size
    }

    /*
    * 현재 아이템이 사용할 뷰홀더를 생성하여 반환하는 함수
    * item_list 레이아웃을 사용하여 뷰를 생성하고 뷰홀더에 뷰를 전달하여 생성된 뷰홀더를 반환
    * */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        val viewHolder = ListViewHolder(view)
        return viewHolder
    }

    /*
    * 현재 아이템의 포지션에 대한 데이터 모델을 리스트에서 얻고
    * holder 객체를 ListViewHolder 로 형변환한 뒤 bind 메서드에 이 모델을 전달하여 데이터를 바인딩하도록 한다
    * */
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val forecastModel = forecastItems[position]
        val listViewHolder = holder as ListViewHolder
        listViewHolder.bind(forecastModel)
    }

    /* 데이터베이스가 변경될 때마다 호출 */
    fun setForecastItems(forecastItems: List<ForecastListModel>) {
        this.forecastItems = forecastItems
        notifyDataSetChanged()
    }

    /*
    * 뷰홀더는 리스트를 스크롤하는 동안 뷰를 생성하고 다시 뷰의 구성요소를 찾는 행위를 반복하면서 생기는
    * 성능저하를 방지하기 위해 미리 저장 해 놓고 빠르게 접근하기 위하여 사용하는 객체
    * */
    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tv_detail_date = itemView.findViewById<TextView>(R.id.tv_detail_date)
        private val iv_detail_img = itemView.findViewById<ImageView>(R.id.iv_detail_img)
        private val tv_detail_main = itemView.findViewById<TextView>(R.id.tv_detail_main)
        private val tv_detail_temp = itemView.findViewById<TextView>(R.id.tv_detail_temp)

        fun bind(forecastModel: ForecastListModel) {
            //tv_detail_date.text = forecastModel?.dt_txt!!.convertForecastDate()
            iv_detail_img.setImageResource(mContext.resources.getIdentifier("icon_"+forecastModel?.weather?.get(0)?.icon, "drawable", mContext.packageName))
            tv_detail_main.text = forecastModel?.weather?.get(0)?.main
            tv_detail_temp.text = doubleToStrFormat(2,
                forecastModel?.main?.temp!!.minus(273.15)
            )+" 'C"
        }
    }

    private fun doubleToStrFormat(n: Int, value: Double): String? {
        return String.format("%." + n + "f", value)
    }

    private fun convertForecastDate() : String? {
        return "0"
    }


}


