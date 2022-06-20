package com.example.toto.model

import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Memo(
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0,
    val check : Boolean,
    val content : String,
    val year : Int,
    val month : Int,
    val day : Int
//    ,val date:String
) {


}