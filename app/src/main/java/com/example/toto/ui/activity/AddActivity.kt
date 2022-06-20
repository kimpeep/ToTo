package com.example.toto.ui.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.toto.MyApplication
import com.example.toto.R
import com.example.toto.data.dateToString
import com.example.toto.databinding.ActivityAddBinding
import java.io.File
import java.util.*

class AddActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddBinding
    lateinit var filePath: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_add)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.menuAddGallery.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
            requestLauncher.launch(intent)
        }

        binding.menuAddSave.setOnClickListener {
            if (binding.addImageView.drawable !== null && binding.addEditView.text.isNotEmpty()) {
                // save 동작 진행
                saveStore()
            }
            else {
                Toast.makeText(this, "데이터가 모두 입력되지 않았습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }



    val requestLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode === android.app.Activity.RESULT_OK){
            Glide
                .with(applicationContext)
                .load(it.data?.data)
                .apply(RequestOptions().override(250, 200))
                .centerCrop()
                .into(binding.addImageView)
            val cursor = contentResolver.query(it.data?.data as Uri,
                arrayOf<String>(MediaStore.Images.Media.DATA), null, null, null)
            cursor?.moveToFirst().let{
                filePath = cursor?.getString(0) as String
            }
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId === R.id.menu_add_gallery){
            val intent = Intent(Intent.ACTION_PICK)
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
            requestLauncher.launch(intent)
        }
        else if(item.itemId === R.id.menu_add_save){
            if(binding.addImageView.drawable !== null && binding.addEditView.text.isNotEmpty()){
                // save
                saveStore()
            }
            else {
                Toast.makeText(this, "데이터가 모두 입력되지 않았습니다", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveStore(){
        val data = mapOf(
            "email" to MyApplication.email, // 있어야 Firestore에서 read/write 권한 획득하도록 규칙을 설정했음
            "content" to binding.addEditView.text.toString(),
            "date" to dateToString(Date())
        )

        MyApplication.db.collection("news")
            .add(data)
            .addOnSuccessListener {
                uploadImage(it.id)
            }
            .addOnFailureListener {
                Log.d("mobileApp", "data save error")
            }
    }

    private fun uploadImage(docId: String){
        val storage = MyApplication.storage
        val storageRef = storage.reference
        val imageRef = storageRef.child("images/${docId}.jpg")

        val file = Uri.fromFile(File(filePath))
        imageRef.putFile(file)
            .addOnSuccessListener {
                Toast.makeText(this, "save ok...", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Log.d("mobileApp", "file save error")
            }
    }


}