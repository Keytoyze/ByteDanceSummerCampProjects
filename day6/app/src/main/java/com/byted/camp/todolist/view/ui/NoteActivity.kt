package com.byted.camp.todolist.view.ui

import android.app.Activity
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.byted.camp.todolist.R
import com.byted.camp.todolist.model.Note
import com.byted.camp.todolist.model.Priority
import com.byted.camp.todolist.viewmodel.TodoViewModel
import kotlinx.android.synthetic.main.activity_note.*


class NoteActivity : AppCompatActivity() {

    private val viewModel: TodoViewModel by lazy {
        ViewModelProviders.of(this)[TodoViewModel::class.java]
    }

    private val selectedPriority: Priority
        get() {
            return when (radio_group.checkedRadioButtonId) {
                R.id.btn_high -> Priority.High
                R.id.btn_medium -> Priority.Medium
                else -> Priority.Low
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)
        setTitle(R.string.take_a_note)

        edit_text.run {
            isFocusable = true
            requestFocus()
            (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                .showSoftInput(this, 0)
        }

        btn_low.isChecked = true

        btn_add.setOnClickListener(View.OnClickListener {
            val content = edit_text.text
            if (TextUtils.isEmpty(content)) {
                Toast.makeText(this@NoteActivity,
                    "No content to add", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
            viewModel.insert(Note(content = content.toString().trim { it <= ' ' },
                priority = selectedPriority), {
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
}
