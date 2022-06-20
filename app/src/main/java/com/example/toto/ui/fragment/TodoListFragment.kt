package com.example.toto.ui.fragment


import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.toto.databinding.FragmentTodoListBinding
import androidx.lifecycle.Observer
import com.example.toto.model.Memo
import com.example.toto.viewmodel.MemoViewModel
import com.example.toto.adapter.TodoAdapter
import com.example.toto.ui.dialog.MyCustomDialog
import com.example.toto.ui.dialog.MyCustomDialogInterface
import java.util.*
class TodoListFragment : Fragment(), MyCustomDialogInterface {

    private var binding: FragmentTodoListBinding? = null
    private val memoViewModel: MemoViewModel by viewModels()

    //    lateinit var memoViewModel: MemoViewModel
    private val adapter : TodoAdapter by lazy { TodoAdapter(requireActivity().application) } // 어댑터 선언

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

//        memoViewModel =  ViewModelProvider(this, MyViewModel.Factory(application = requireActivity().application)).get(MemoViewModel::class.java)
        memoViewModel.aaa(requireActivity().application)

        // 상단 메뉴 추가
        setHasOptionsMenu(true)
        // 뷰바인딩
        binding = FragmentTodoListBinding.inflate(inflater, container, false)

        // 아이템에 아이디를 설정해줌 (깜빡이는 현상방지)
        adapter.setHasStableIds(true)

        // 아이템을 가로로 하나씩 보여주고 어댑터 연결
        binding!!.todoRecyclerView.layoutManager = LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false)
        binding!!.todoRecyclerView.adapter = adapter

        // 리스트 관찰하여 변경시 어댑터에 전달해줌
        memoViewModel.readAllData.observe(viewLifecycleOwner, Observer {
            adapter.setData(it)
        })

        binding!!.addButton.setOnClickListener {
            onFabClicked()
        }

        return binding!!.root
    }

    // Fab 클릭시 다이얼로그 띄움
    private fun onFabClicked(){
        val myCustomDialog = MyCustomDialog(requireActivity())
        myCustomDialog.show()
    }

    override fun onOkButtonClicked(content: String) {

        // 현재의 날짜를 불러옴
        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH) + 1
        val day = cal.get(Calendar.DATE)

        // 현재의 날짜로 메모를 추가해줌
        val memo = Memo(id, false,content, year, month, day)
        memoViewModel.addMemo(memo)
        Toast.makeText(activity,"추가", Toast.LENGTH_SHORT).show()
    }

    override fun MyCustomDialog(context: FragmentActivity): MyCustomDialog {
        Log.d("TodoListFragment Test", "구현 필요")
        return MyCustomDialog(requireContext(), myInterface = this)
    }
}