package com.example.memoi.repository

import com.example.memoi.todo.Todo
import com.example.memoi.todo.TodoBuilder
import com.example.memoi.viewmodel.TodoListViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.*
//import retrofit2.*
//import retrofit2.http.GET

class TodoRepository {
    val database = Firebase.firestore
    val dbColl = database.collection("/todoList")

    fun selectTodo(vm: TodoListViewModel): ArrayList<Todo> {

        val res = ArrayList<Todo>()

        dbColl.get()
            .addOnSuccessListener { result ->
                println("/////////////\ntodo loading succeed.\n/////////////")

                for (todo in result) {
                    val tmp = todo.data
                    val title = tmp["title"] as String
                    val description = tmp["description"] as String
                    val date = tmp["date"] as String
                    val time = tmp["time"] as String
                    val url = tmp["url"] as String
                    var created = tmp["created"] as String

                    // LocalDateTime must be basically formatted in "yyyy-MM-ddThh-DD-ss"
                    // a line of code below is for taking format of "yy-MM-ddThh-DD-ss"
                    // this case shouldn't be happened... but just in case.
                    // hence, this application won't work in 22nd century. XD
                    if (created[8] == 'T') created = "20$created"

                    res.add(
                        TodoBuilder(
                            title,
                            if (description == "null") null else description,
                            if (date == "null") null else date,
                            if (time == "null") null else time,
                            if (url == "null") null else url
                        ).build(created)
                    )

                }
                /* for debug...
                for (todo in result) {
                    println("${todo.id} : ${todo.data}")
                }
                */

                vm.isReady = true
            }
            .addOnFailureListener { e ->
                System.err.println("/////////////\ntodo loading failed.\n/////////////")
                // better exception handling...?
                //e.printStackTrace()
                throw e
            }

        return res
    }

    fun insertTodo(newTodo: Todo) {
        println("\n\n\n${newTodo.created}\n\n\n\n")
        val obj = with(newTodo) {
            hashMapOf(
                "created" to created,
                "title" to title,
                "description" to (description ?: "null"),
                "date" to (date ?: "null"),
                "time" to (time ?: "null"),
                "url" to (url ?: "null")
            )
        }

        dbColl.document(newTodo.created)
            .set(obj)
            .addOnSuccessListener { _ ->
                println("/////////////\ninsert succeed\n/////////////")
            }
            .addOnFailureListener { _ ->
                println("/////////////\ninsert failed\n/////////////")
            }
    }

    fun deleteTodo(todo: Todo) {
        dbColl.document(todo.created.replace('.', '_'))
            .delete()
            .addOnSuccessListener { _ ->
                println("/////////////\ndelete succeed\n/////////////")
            }
            .addOnFailureListener { _ ->
                println("/////////////\ndelete failed\n/////////////")
            }
    }

}