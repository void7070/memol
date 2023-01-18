package com.example.memoi.list

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.memoi.MainActivity
import com.example.memoi.databinding.FragmentTodoListBinding
import com.example.memoi.todo.Todo
import kotlin.concurrent.thread

class TodoListFragment : ListFragment() {

    lateinit var todoList: ArrayList<Todo>

    var binding: FragmentTodoListBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTodoListBinding.inflate(inflater, container, false)
        todoList = vm.getTodayList()

        binding?.recTodo?.layoutManager = LinearLayoutManager(parentActivity)
        binding?.recTodo?.adapter = TodoAdapter(todoList)

        return binding?.root
    }

    override fun onResume() {
        super.onResume()
        refresh()
    }

    override fun refresh() {
        thread(start = true) {
            while (!vm.isReady);

            todoList = vm.getList()

            activity?.runOnUiThread {
                binding?.recTodo?.layoutManager = LinearLayoutManager(parentActivity)
                binding?.recTodo?.adapter = TodoAdapter(todoList)
            }

        }
    }

}