package com.byted.camp.todolist.view.ui

import android.app.Activity
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
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
import com.byted.camp.todolist.view.adapter.NoteListAdapter
import com.byted.camp.todolist.viewmodel.TodoViewModel
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

//    private var dbHelper: TodoDbHelper? = null
//    private var database: SQLiteDatabase? = null

    companion object {
        private const val REQUEST_CODE_ADD = 1002
    }

    private val viewModel: TodoViewModel by lazy {
        ViewModelProviders.of(this)[TodoViewModel::class.java]
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

        // register database change callback.
        viewModel.getAllNodes({
            notesAdapter.refresh(it)
        }, {
            it.printStackTrace()
        })
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

    // refresh automatically!
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == REQUEST_CODE_ADD && resultCode == Activity.RESULT_OK) {
//            refresh()
//        }
//    }

    private fun loadNotesFromDatabase(): Flowable<List<Note>> {
        return AppDataBase.getInstance(this)
            .todoDao()
            .getAll()
            .subscribeOn(Schedulers.io())
    }

    private fun deleteNote(note: Note) {
        viewModel.deleteNote(note, {
            // nothing to do
        }, {
            it.printStackTrace()
        })
    }

    private fun updateNode(note: Note) {
        viewModel.updateNode(note, {
            // nothing to do
        }, {
            it.printStackTrace()
        })
    }
}
