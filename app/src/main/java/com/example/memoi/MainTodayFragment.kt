package com.example.memoi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.memoi.databinding.FragmentMainTodayBinding
import com.example.memoi.viewmodel.TodoListViewModel

class MainTodayFragment : Fragment() {

    val vm: TodoListViewModel by activityViewModels()

    var binding: FragmentMainTodayBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity)
            .currentFragment = MainActivity.FragmentType.MainToday
        //println((activity as MainActivity).currentFragment)

        binding = FragmentMainTodayBinding.inflate(inflater, container, false);
        (activity as MainActivity).showButtons()

        return binding?.root
    }

}