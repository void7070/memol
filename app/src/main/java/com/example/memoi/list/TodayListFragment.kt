package com.example.memoi.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.memoi.databinding.FragmentTodayListBinding
import com.example.memoi.todo.Todo
import java.util.ArrayList
import kotlin.concurrent.thread

class TodayListFragment : ListFragment() {

    //val vm: TodoListViewModel by activityViewModels()
    lateinit var todayList: ArrayList<Todo>

    var binding: FragmentTodayListBinding? = null
    //lateinit var parentActivity: Activity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTodayListBinding.inflate(inflater, container, false)
        todayList = vm.getTodayList()

        binding?.recToday?.layoutManager = LinearLayoutManager(parentActivity)
        binding?.recToday?.adapter = TodoAdapter(todayList)

        return binding?.root
    }

    override fun onResume() {
        super.onResume()
        refresh()
    }

    override fun refresh() {
        thread(start = true) {
            while (!vm.isReady);

            todayList = vm.getTodayList()

            activity?.runOnUiThread {
                binding?.recToday?.layoutManager = LinearLayoutManager(parentActivity)
                binding?.recToday?.adapter = TodoAdapter(todayList)
            }

        }
    }

}