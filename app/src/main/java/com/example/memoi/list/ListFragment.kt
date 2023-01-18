package com.example.memoi.list

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.memoi.MainActivity
import com.example.memoi.viewmodel.TodoListViewModel

abstract class ListFragment : Fragment() {

    val vm: TodoListViewModel by activityViewModels()
    lateinit var parentActivity: Activity

    // getting attached activity.
    override fun onAttach(context: Context) {
        super.onAttach(context)
        parentActivity = activity as MainActivity
    }

    override fun onResume() {
        super.onResume()
        refresh()
    }

    abstract fun refresh()
}