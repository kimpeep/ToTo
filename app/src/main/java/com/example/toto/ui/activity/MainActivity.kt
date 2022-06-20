package com.example.toto.ui.activity

import android.app.PendingIntent.getActivity
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import com.example.toto.MyApplication
import com.example.toto.R
import com.example.toto.data.myCheckPermission
import com.example.toto.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Activity 이동
        binding.addFab0.setOnClickListener {
            startActivity(Intent(this, TodoActivity::class.java))
        }
        binding.addFab1.setOnClickListener {
            startActivity(Intent(this, WeatherActivity::class.java))
        }
        binding.addFab2.setOnClickListener {
            startActivity(Intent(this, DiaryActivity::class.java))
        }
        
        //sharedPreference
        binding.setup.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }

        /* sharedPreferences */
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        val bgColor = sharedPreferences.getString("color", "")
        binding.rootLayout.setBackgroundColor(Color.parseColor(bgColor))

        val nickName = sharedPreferences.getString("id", "")
        if (!nickName.isNullOrBlank()) {
            if (MyApplication.checkAuth() || MyApplication.email != null)
                binding.authTv.text = nickName
            else
                binding.authTv.text = ""
        }

        binding.btnLogin.setOnClickListener {
            // 로그인: 별도의 액티비티를 만들어 처리
            val intent = Intent(this, AuthActivity::class.java)
            if(binding.btnLogin.text.equals("로그인")) // 로그아웃 상태
                intent.putExtra("data", "logout")
            else if(binding.btnLogin.text.equals("로그아웃")) // 로그인 상태
                intent.putExtra("data", "login")
            startActivity(intent)
        }
        myCheckPermission(this)

        /* 전화 앱 연동 */
        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission() ) {  // 여기 들어가는 함수 주의

        }

        binding.callBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel: 010-1920-0419"))
            val status = ContextCompat.checkSelfPermission(this, "android.permission.CALL_PHONE")  // 현재의 권한 허용 상태 확인
            if(status == PackageManager.PERMISSION_GRANTED){
                startActivity(intent)
            }
            else {
                requestPermissionLauncher.launch("android.permission.CALL_PHONE")
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val bgColor = sharedPreferences.getString("color", "")
        binding.rootLayout.setBackgroundColor(Color.parseColor(bgColor))
        val nickName = sharedPreferences.getString("id", "")
        if (!nickName.isNullOrBlank()) {
            if (MyApplication.checkAuth() || MyApplication.email != null)
                binding.authTv.text = nickName
            else
                binding.authTv.text = ""
        }
    }

    // AuthActivity에서 돌아온 후
    override fun onStart() {
        super.onStart()
        if(MyApplication.checkAuth() || MyApplication.email != null){  // 검증된 이메일인가
            // 로그인 상태
            binding.btnLogin.text = "로그아웃"
            binding.authTv.text = "${MyApplication.email}님 반갑습니다."
            binding.authTv.textSize = 16F
        }
        else{
            // 로그아웃 상태
            binding.btnLogin.text = "로그인"
            binding.authTv.text = "덕성 모바일"
            binding.authTv.textSize = 24F
        }
    }

}