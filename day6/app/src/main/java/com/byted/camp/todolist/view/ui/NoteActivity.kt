package com.byted.camp.todolist.view.ui

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatRadioButton
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast

import com.byted.camp.todolist.R
import com.byted.camp.todolist.db.AppDataBase
import com.byted.camp.todolist.model.Priority
import com.byted.camp.todolist.model.State
import com.byted.camp.todolist.db.TodoContract.TodoNote
import com.byted.camp.todolist.db.TodoDbHelper
import com.byted.camp.todolist.model.Note
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_note.*
import java.util.*


class NoteActivity : AppCompatActivity() {

    private var editText: EditText? = null
    private var addBtn: Button? = null
    private var radioGroup: RadioGroup? = null
    private var lowRadio: AppCompatRadioButton? = null

//    private var dbHelper: TodoDbHelper? = null
//    private var database: SQLiteDatabase? = null

    private val selectedPriority: Priority
        get() {
            when (radioGroup!!.checkedRadioButtonId) {
                R.id.btn_high -> return Priority.High
                R.id.btn_medium -> return Priority.Medium
                else -> return Priority.Low
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)
        setTitle(R.string.take_a_note)

//        dbHelper = TodoDbHelper(this)
//        database = dbHelper!!.writableDatabase

        editText = findViewById(R.id.edit_text)
        editText!!.isFocusable = true
        editText!!.requestFocus()
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager?.showSoftInput(editText, 0)
        radioGroup = findViewById(R.id.radio_group)
        lowRadio = findViewById(R.id.btn_low)
        lowRadio!!.isChecked = true

        btn_add.setOnClickListener(View.OnClickListener {
            val content = editText!!.text
            if (TextUtils.isEmpty(content)) {
                Toast.makeText(this@NoteActivity,
                        "No content to add", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
            saveNote2Database(content.toString().trim { it <= ' ' },
                    selectedPriority)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Toast.makeText(this@NoteActivity,
                        "Note added", Toast.LENGTH_SHORT).show()
                    setResult(Activity.RESULT_OK)
                    finish()
                }, {
                    it.printStackTrace()
                    Toast.makeText(this@NoteActivity,
                        "Error", Toast.LENGTH_SHORT).show()
                    finish()
                })
        })
    }

    override fun onDestroy() {
        super.onDestroy()
//        database!!.close()
//        database = null
//        dbHelper!!.close()
//        dbHelper = null
    }

    private fun saveNote2Database(content: String, priority: Priority): Single<Long> {
//        if (database == null || TextUtils.isEmpty(content)) {
//            return false
//        }
//        val values = ContentValues()
//        values.put(TodoNote.COLUMN_CONTENT, content)
//        values.put(TodoNote.COLUMN_STATE, State.TODO.intValue)
//        values.put(TodoNote.COLUMN_DATE, System.currentTimeMillis())
//        values.put(TodoNote.COLUMN_PRIORITY, priority.intValue)
//        val rowId = database!!.insert(TodoNote.TABLE_NAME, null, values)
//        return rowId != -1L
        return AppDataBase.getInstance(this)
            .todoDao()
            .insert(Note(content = content, priority = priority))
            .subscribeOn(Schedulers.io())
    }
}
