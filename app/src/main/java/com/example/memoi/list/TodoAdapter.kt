package com.example.memoi.list

import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.memoi.R
import com.example.memoi.databinding.ListTodoBinding
import com.example.memoi.todo.Todo
import android.net.Uri
import androidx.fragment.app.Fragment
import com.example.memoi.repository.TodoRepository
import com.example.memoi.viewmodel.TodoListViewModel
import com.google.android.material.snackbar.Snackbar

class TodoAdapter(val todoList: ArrayList<Todo>) : RecyclerView.Adapter<TodoAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ListTodoBinding.inflate(LayoutInflater.from(parent.context))
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(todoList.get(position))
    }

    override fun getItemCount(): Int = todoList.size

    class Holder(private val binding: ListTodoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(todo: Todo) {

            with (binding) {

                /* time not null -> clock
                 * date not null & time null -> calender
                 * date null & time null -> check
                 */
                val imgsrc = if (todo.time != null) R.drawable.clock else
                        (if (todo.date == null) R.drawable.check else R.drawable.calendar)

                imgTodo.setImageResource(imgsrc)

                txtTodoInfoTitle.text = todo.title
                txtTodoInfoDesc.text = todo.description
                txtTodoInfoDate.text = todo.date
                txtTodoInfoTime.text = todo.time
                if (todo.url == null) {
                    btnUrlMove.visibility = View.INVISIBLE
                }
                else {
                    btnUrlMove.setOnClickListener {
                        val address = todo.url
                        binding.root.context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(address)))
                    }
                }

                /*
                // 할일 클릭 시, 삭제
                root.setOnClickListener {

                    val tmp = AlertDialog.Builder(binding.root.context)
                    val vm = TodoListViewModel()
                    tmp.setTitle("할일 삭제하기")
                        .setMessage("${todo.title}을(를) 삭제하시겠습니까?")
                        .setPositiveButton("예") { _, _ ->
                            TodoRepository().deleteTodo(todo)
                            vm.update()
                            Snackbar.make(binding.root,
                                "삭제되었습니다.", Snackbar.LENGTH_SHORT)
                                .show()
                        }
                        .setNegativeButton("아니오") { _, _ ->
                            Snackbar.make(binding.root,
                                "당신의 '${todo.title}'은 살아남았습니다.", Snackbar.LENGTH_SHORT)
                                .show()
                        }
                        .show()

                    frg.refresh()
                }*/
            }
        }
    }
}