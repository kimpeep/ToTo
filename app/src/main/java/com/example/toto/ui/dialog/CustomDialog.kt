package com.example.toto.ui.dialog

import android.app.Dialog
import android.content.Context
import com.example.toto.databinding.LayoutDialog2Binding

class CustomDialog(context: Context) {

    lateinit var binding: LayoutDialog2Binding
    private val dialog = Dialog(context)

    fun show(title: String, content: String) {
        binding = LayoutDialog2Binding.inflate(dialog.layoutInflater)
        dialog.setContentView(binding.root)
//        dialog.setContentView(R.layout.layout_dialog)

        if(title != "") {
            binding.dialogTitle.text = title
        }
        if(content != "") {
            binding.dialogContent.text = content
        }

        initView()
        dialog.show()
    }

    fun initView() {
        binding.btnConfirm.setOnClickListener {
            android.os.Process.killProcess(android.os.Process.myPid());  //앱 종료
        }

        binding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }
    }

}