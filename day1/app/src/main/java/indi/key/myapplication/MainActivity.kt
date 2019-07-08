/*
 * Copyright (c) 2019 Chen Mouxiang <cmx_1007@foxmail.com>
 * All Rights Reserved.
 */

package indi.key.myapplication

import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import indi.key.myapplication.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        val TAG: String = MainActivity::class.java.simpleName
    }

    // use ViewModel to process callbacks.
    private val model: MainActivityViewModel by lazy {
        MainActivityViewModel(
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

    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // use databinding to decouple
        binding = (DataBindingUtil.setContentView(this, R.layout.activity_main) as ActivityMainBinding)
            .apply {
                model = this@MainActivity.model
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
