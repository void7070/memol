package com.example.memoi.todo

import kotlin.Throws
import com.example.memoi.todo.Task.NullIntegrityException
import java.lang.Exception
import java.time.LocalDate
import java.time.LocalTime
import java.util.*
import kotlin.collections.ArrayList

class TodoBuilder {
    var title: String?
    var description: String?
    var date: String?
    var time: String?
    var url: String?

    constructor() {
        title = null
        description = null
        date = null
        time = null
        url = null
    }

    constructor(
        title: String?,
        description: String?,
        date: LocalDate,
        time: LocalTime,
        url: String?
    ) {
        this.title = title
        this.description = description
        this.date = date.toString()
        this.time = time.toString()
        this.url = url
    }

    constructor(
        title: String?,
        description: String?,
        date: String?,
        time: String?,
        url: String?
    ) {
        if (date != null) LocalDate.parse(date)
        if (time != null) LocalTime.parse(time)
        this.title = title
        this.description = description
        this.date = date
        this.time = time
        this.url = url
    }

    @JvmName("todoBuilder_setTitle")
    fun setTitle(t: String?) {
        title = t
    }

    @JvmName("todoBuilder_setDescription")
    fun setDescription(d: String?) {
        description = d
    }

    fun setDate(date: LocalDate) {
        this.date = date.toString()
    }

    @JvmName("todoBuilder_setDate")
    fun setDate(date: String?) {
        LocalDate.parse(date) // parsable checking
        this.date = date
    }

    fun setDate(y: Int, m: Int, d: Int) {
        this.setDate(LocalDate.of(y, m, d))
    }

    fun setTime(time: LocalTime) {
        this.time = time.toString()
    }

    @JvmName("todoBuilder_setTime")
    fun setTime(time: String?) {
        LocalTime.parse(time)
        this.time = time
    }

    fun setTime(h: Int, m: Int) {
        this.setTime(LocalTime.of(h, m))
    }

    @JvmName("todoBuilder_setUrl")
    fun setUrl(url: String?) {
        this.url = url
    }

    @Throws(Exception::class)
    fun build(): Todo {
        if (title == null) {
            throw NullIntegrityException()
        }
        return Todo(title, description, date, time, url)
    }

    // being used when the data is loaded.
    @Throws(Exception::class)
    fun build(created: String): Todo {
        if (title == null) {
            throw NullIntegrityException()
        }
        val tmp = Todo(title, description, date, time, url)
        tmp.setCreated(created)

        return tmp
    }

    /* format of
     * "{title}, {description}, {time}, {date}, {url}\n"
     * "___", "___", "yyyy-MM-dd", "HH-mm", "___"
     * all values has "null" if null
     */
    companion object {
        fun of(csv: String): TodoBuilder {
            val st = StringTokenizer(csv, ", ");
            //System.out.println(st.countTokens());
            val tokens: ArrayList<String> = ArrayList<String>();

            while (st.hasMoreTokens()) tokens.add(st.nextToken());
            //System.out.println(tokens.size());
            try {
                val title = tokens[0];

                var desc: String? = tokens[1];
                if (desc == "null") desc = null;

                var date: String? = tokens[2];
                if (date == "null") date = null;

                var time: String? = tokens[3];
                if (time == "null") time = null;

                var url: String? = tokens[4];
                if (url == "null") url = null;

                return TodoBuilder(title, desc, date, time, url)
            } catch (e: IndexOutOfBoundsException) {
                System.err.println("TodoBuilder.of() failed. returning Error Todo");
                e.printStackTrace();
                return TodoBuilder("Error", "Occurred.", null, null, null);
            }
        }
    }   // end of companion object

}