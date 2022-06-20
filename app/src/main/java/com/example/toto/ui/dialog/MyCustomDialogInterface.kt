package com.example.toto.ui.dialog

import androidx.fragment.app.FragmentActivity

interface MyCustomDialogInterface {
    fun onOkButtonClicked(content : String)
    abstract fun MyCustomDialog(context: FragmentActivity): MyCustomDialog
}