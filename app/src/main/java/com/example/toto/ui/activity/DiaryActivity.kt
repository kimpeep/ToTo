package com.example.toto.ui.activity

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.toto.MyApplication
import com.example.toto.R
import com.example.toto.adapter.MyAdapter
import com.example.toto.data.ItemData
import com.example.toto.data.myCheckPermission
import com.example.toto.databinding.ActivityDiaryBinding
import com.kakao.sdk.common.util.Utility

class DiaryActivity : AppCompatActivity() {
    lateinit var binding: ActivityDiaryBinding
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        binding = ActivityDiaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addFab.setOnClickListener {
            startActivity(Intent(this, AddActivity::class.java))
        }

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val notification = sharedPreferences.getString("noti", "")
        if (notification.equals("YES")) {
            val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            val builder: NotificationCompat.Builder

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val ch_id = "Add"
                val channel = NotificationChannel(
                    ch_id,
                    "Add Write",
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                channel.description = "글 작성 알림"
                channel.setShowBadge(true)
                channel.enableLights(true)
                channel.lightColor = Color.RED

                manager.createNotificationChannel(channel)
                builder = NotificationCompat.Builder(this, ch_id)
            }
            else {
                builder = NotificationCompat.Builder(this)
            }

            builder.setSmallIcon(R.drawable.peep)
            builder.setWhen(System.currentTimeMillis())
            builder.setContentTitle("글 저장 알림")
            builder.setContentText("글 작성이 완료되었습니다.")

            manager.notify(11, builder.build())
        }
    }

    private fun makeRecyclerView() {
        MyApplication.db.collection("news")  // AddActivity에서 news에 저장하기로 함
            .get()
            .addOnSuccessListener { result ->
                val itemList = mutableListOf<ItemData>()
                for (document in result) {
                    val item = document.toObject(ItemData::class.java)
                    item.docId = document.id
                    itemList.add(item)
                }
                binding.mainRecyclerView.layoutManager = LinearLayoutManager(this)
                binding.mainRecyclerView.adapter = MyAdapter(this, itemList)
            }
            .addOnFailureListener {
                Toast.makeText(this, "서버 데이터 획득 실패", Toast.LENGTH_SHORT).show()
            }

    }

    override fun onStart() {
        super.onStart()
        if (MyApplication.checkAuth() || MyApplication.email != null) {  // 검증된 이메일인가
            binding.mainRecyclerView.visibility = View.VISIBLE // 이미지 리사이클러뷰
            makeRecyclerView()
        } else {
            // 로그아웃 상태

            binding.mainRecyclerView.visibility = View.GONE // 이미지 리사이클러뷰
        }
    }
}