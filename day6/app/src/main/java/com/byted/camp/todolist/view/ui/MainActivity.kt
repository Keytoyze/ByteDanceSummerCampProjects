package com.byted.camp.todolist.view.ui

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.byted.camp.todolist.NoteOperator
import com.byted.camp.todolist.R
import com.byted.camp.todolist.db.AppDataBase
import com.byted.camp.todolist.model.Note
import com.byted.camp.todolist.model.Priority
import com.byted.camp.todolist.model.State
import com.byted.camp.todolist.db.TodoContract.TodoNote
import com.byted.camp.todolist.db.TodoDbHelper
import com.byted.camp.todolist.view.adapter.NoteListAdapter
import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

//    private var dbHelper: TodoDbHelper? = null
//    private var database: SQLiteDatabase? = null

    companion object {
        private const val REQUEST_CODE_ADD = 1002
    }

    private val notesAdapter: NoteListAdapter by lazy {
        NoteListAdapter(object : NoteOperator {
            override fun deleteNote(note: Note) {
                this@MainActivity.deleteNote(note)
            }

            override fun updateNote(note: Note) {
                this@MainActivity.updateNode(note)
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener {
            //                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
            //                        .setAction("Action", null).show();
            startActivityForResult(
                    Intent(this@MainActivity, NoteActivity::class.java),
                    REQUEST_CODE_ADD)
        }

//        dbHelper = TodoDbHelper(this)
//        database = dbHelper!!.writableDatabase

        list_todo.run {
            layoutManager = LinearLayoutManager(this@MainActivity,
                    LinearLayoutManager.VERTICAL, false)
            addItemDecoration(DividerItemDecoration(this@MainActivity,
                    DividerItemDecoration.VERTICAL))
            adapter = notesAdapter
        }

        loadNotesFromDatabase()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                notesAdapter.refresh(it)
            }, {
                it.printStackTrace()
            })

    }

    override fun onDestroy() {
        super.onDestroy()
//        database!!.close()
//        database = null
//        dbHelper!!.close()
//        dbHelper = null
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            R.id.action_debug -> {
                startActivity(Intent(this, DebugActivity::class.java))
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_ADD && resultCode == Activity.RESULT_OK) {
            refresh()
        }
    }

    private fun loadNotesFromDatabase(): Flowable<List<Note>> {
//        if (database == null) {
//            return emptyList()
//        }
//        val result = LinkedList<Note>()
//        var cursor: Cursor? = null
//        try {
//            cursor = database!!.query(TodoNote.TABLE_NAME, null, null, null, null, null,
//                    TodoNote.COLUMN_PRIORITY + " DESC")
//
//            while (cursor!!.moveToNext()) {
//                val id = cursor.getLong(cursor.getColumnIndex(TodoNote._ID))
//                val content = cursor.getString(cursor.getColumnIndex(TodoNote.COLUMN_CONTENT))
//                val dateMs = cursor.getLong(cursor.getColumnIndex(TodoNote.COLUMN_DATE))
//                val intState = cursor.getInt(cursor.getColumnIndex(TodoNote.COLUMN_STATE))
//                val intPriority = cursor.getInt(cursor.getColumnIndex(TodoNote.COLUMN_PRIORITY))
//
//                val note = Note(id)
//                note.content = content
//                note.date = Date(dateMs)
//                note.state = State.from(intState)
//                note.priority = Priority.from(intPriority)
//
//                result.add(note)
//            }
//        } finally {
//            cursor?.close()
//        }
//        return result

        return AppDataBase.getInstance(this)
            .todoDao()
            .getAll()
            .subscribeOn(Schedulers.io())
    }

    private fun refresh() {

    }

    private fun deleteNote(note: Note) {
//        if (database == null) {
//            return
//        }
//        val rows = database!!.delete(TodoNote.TABLE_NAME,
//                TodoNote._ID + "=?",
//                arrayOf(note.id.toString()))
//        if (rows > 0) {
//            notesAdapter.refresh(loadNotesFromDatabase())
//        }
        AppDataBase.getInstance(this)
            .todoDao()
            .delete(note)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                refresh()
            }, {
                it.printStackTrace()
            })
    }

    private fun updateNode(note: Note) {
//        if (database == null) {
//            return
//        }
//        val values = ContentValues()
//        values.put(TodoNote.COLUMN_STATE, note.state.intValue)
//
//        val rows = database!!.update(TodoNote.TABLE_NAME, values,
//                TodoNote._ID + "=?",
//                arrayOf(note.id.toString()))

        AppDataBase.getInstance(this)
            .todoDao()
            .update(note)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                refresh()
            }, {
                it.printStackTrace()
            })

//        if (rows > 0) {
//
//        }
    }

}
