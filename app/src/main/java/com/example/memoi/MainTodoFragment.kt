package com.example.memoi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.memoi.databinding.FragmentMainTodoBinding
import com.example.memoi.viewmodel.TodoListViewModel

class MainTodoFragment : Fragment() {

    val vm: TodoListViewModel by activityViewModels()

    var binding: FragmentMainTodoBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity)
            .currentFragment = MainActivity.FragmentType.MainTodo
        //println((activity as MainActivity).currentFragment)

        binding = FragmentMainTodoBinding.inflate(inflater, container, false);
        (activity as MainActivity).showButtons()

        return binding?.root
    }

}