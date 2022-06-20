package com.example.toto.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.toto.databinding.FragmentCalendarBinding
import com.example.toto.ui.dialog.MyCustomDialog
import androidx.lifecycle.Observer
import com.example.toto.adapter.TodoAdapter
import com.example.toto.model.Memo
import com.example.toto.ui.dialog.MyCustomDialogInterface
import com.example.toto.viewmodel.MemoViewModel

class CalendarFragment : Fragment(), MyCustomDialogInterface {

    private var binding : FragmentCalendarBinding? = null
    private val memoViewModel: MemoViewModel by viewModels() // 뷰모델 연결
    private val adapter : TodoAdapter by lazy { TodoAdapter(requireActivity().application) } // 어댑터 선언

    private var year : Int = 0
    private var month : Int = 0
    private var day : Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 뷰바인딩
        binding = FragmentCalendarBinding.inflate(inflater,container,false)
        memoViewModel.aaa(requireActivity().application)
        // 아이템에 아이디를 설정해줌 (깜빡이는 현상방지)
        adapter.setHasStableIds(true)

        // 아이템을 가로로 하나씩 보여주고 어댑터 연결
        binding!!.calendarRecyclerview.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL,false)
        binding!!.calendarRecyclerview.adapter = adapter

        binding!!.calendarView.setOnDateChangeListener { calendarView, year, month, day ->
            // 날짜 선택시 그 날의 정보 할당
            this.year = year
            this.month = month+1
            this.day = day

            //binding!!.calendarDateText.text = "${this.year}/${this.month}/${this.day}"

            // 리스트 관찰하여 변경시 어댑터에 전달해줌
//            memoViewModel.readDateData(this.year,this.month,this.day).observe(viewLifecycleOwner, Observer {
//                //상단 it: List<Memo>! 가 떠야 하는데 안 뜸 == 결과적으로 MemoViewModel이 생성되지 않는 문제?
//                adapter.setData(it)
//            })

            // 리스트 관찰하여 변경시 어댑터에 전달해줌
            memoViewModel.readAllData.observe(viewLifecycleOwner, Observer {
                adapter.setData(it)
            })
        }

        // Fab 클릭시 다이얼로그 띄움
        binding!!.calendarDialogButton.setOnClickListener {
            if(year == 0) {
                Toast.makeText(activity, "날짜를 선택해주세요.", Toast.LENGTH_SHORT).show()
            }
            else {
                onFabClicked()
            }
        }

        return binding!!.root
    }

    // Fab 클릭시 사용되는 함수
    private fun onFabClicked(){
        val myCustomDialog = MyCustomDialog(requireActivity(),this)
        myCustomDialog.show()
    }

    // 프래그먼트는 뷰보다 오래 지속 . 프래그먼트의 onDestroyView() 메서드에서 결합 클래스 인스턴스 참조를 정리
    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    override fun onOkButtonClicked(content: String) {
        // 선택된 날짜로 메모를 추가해줌
        val memo = Memo(id, false, content, year, month, day)
        memoViewModel.addMemo(memo)
        Toast.makeText(activity, "추가", Toast.LENGTH_SHORT).show()
    }

    override fun MyCustomDialog(context: FragmentActivity): MyCustomDialog {
        TODO("Not yet implemented")
    }
}