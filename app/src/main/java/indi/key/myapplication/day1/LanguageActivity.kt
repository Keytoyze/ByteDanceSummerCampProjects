/*
 * Copyright (c) 2019 Chen Mouxiang <cmx_1007@foxmail.com>
 * All Rights Reserved.
 */

package indi.key.myapplication.day1

import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import indi.key.myapplication.R
import indi.key.myapplication.databinding.ActivityLanguageBinding
import kotlinx.android.synthetic.main.activity_language.*

class LanguageActivity : AppCompatActivity() {

    companion object {
        val TAG: String = LanguageActivity::class.java.simpleName
    }

    // use ViewModel to process callbacks.
    private val model: LanguageActivityViewModel by lazy {
        LanguageActivityViewModel(
            false, {
                log("click submit button!")
                requestSubmit()
            }, {
                log("click language: #$it!")
            }, {
                log("click OS: #$it!")
            }
        )
    }

    private var binding: ActivityLanguageBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // use databinding to decouple
        binding = (DataBindingUtil.setContentView(this, R.layout.activity_language) as ActivityLanguageBinding)
            .apply {
                model = this@LanguageActivity.model
            }
    }

    private fun requestSubmit() {
        log("The user ${editText.text.trim()} requests submitting...")
        model.isSubmitting = true
        binding?.model = model // rebind
        submit()
    }

    private fun submit() {
        Thread {
            // submitting...
            Thread.sleep(2000)
            runOnUiThread {
                complete() // should run on ui thread
            }
        }.start()
    }

    private fun complete() {
        log("Finish submitting")
        model.isSubmitting = false
        binding?.model = model // rebind
    }

    private fun log(content: String) {
        Log.i(TAG, content)
        Toast.makeText(this, content, Toast.LENGTH_SHORT).show()
    }
}
