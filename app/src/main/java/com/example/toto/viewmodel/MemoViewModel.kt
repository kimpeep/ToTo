package com.example.toto.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.toto.data.MemoDatabase
import com.example.toto.model.Memo
import com.example.toto.repository.MemoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
// 뷰모델은 DB에 직접 접근하지 않아야함. Repository 에서 데이터 통신.
class MemoViewModel() : ViewModel() {

//    var readAllData : LiveData<List<Memo>>? = null
//    private var repository : MemoRepository? = null

    var readAllData : LiveData<List<Memo>> = MutableLiveData()
    //lateinit var readDoneData : LiveData<List<Memo>>

    private lateinit var repository : MemoRepository
    init{
        Log.e("testLog","application : ")
    }

    fun aaa(application: Application) {
        val memoDao = MemoDatabase.getDatabase(application)!!.memoDao()
        repository = MemoRepository(memoDao)
        readAllData = repository!!.readAllData.asLiveData()
        //readDoneData = repository!!.readDoneData.asLiveData()
    }

    fun addMemo(memo : Memo){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addMemo(memo)
        }
    }

    fun updateMemo(memo : Memo){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateMemo(memo)
        }
    }

    fun deleteMemo(memo : Memo){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteMemo(memo)
        }
    }

    fun searchDatabase(searchQuery: String): LiveData<List<Memo>> {
        return repository.searchDatabase(searchQuery).asLiveData()
    }
}