package com.example.memoi.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.memoi.repository.TodoRepository
import com.example.memoi.todo.*
import java.time.LocalDate
import java.time.LocalTime
import java.util.ArrayList

class TodoListViewModel: ViewModel() {
    private val repository = TodoRepository()

    private val _todoList = MutableLiveData<ArrayList<Todo>>()
    val todoList : LiveData<ArrayList<Todo>> get() = _todoList

    // will be true, when repository.selectTodo() is done
    var isReady = false

    init {
        _todoList.value = repository.selectTodo(this)
    }

    fun add(todo: Todo) {
        this._todoList.value?.add(todo)
        repository.insertTodo(todo)
    }

    fun getTodayList(): ArrayList<Todo> {
        val resList = ArrayList<Todo>()

        _todoList.value?.run {

            for (todo in this) {
                println(todo)
                // don't get non-today
                // 1st case : no date, just time (everyday notification)
                // 2nd case : date today
                if (todo.date == null && todo.time != null && todo.localTime.isAfter(LocalTime.now())
                    || todo.date != null && (LocalDate.parse(todo.date)).isEqual(LocalDate.now())
                ) {
                    resList.add(todo)
                }
            }
        }

        return resList
    }

    fun getList(): ArrayList<Todo> {
        val resList = ArrayList<Todo>()

        _todoList.value?.run {
            // deep-copy all instances
            for (todo in this) {
                resList.add(todo)
            }
        }

        return resList
    }

}
