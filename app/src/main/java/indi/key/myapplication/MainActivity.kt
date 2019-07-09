/*
 * Copyright (c) 2019 Chen Mouxiang <cmx_1007@foxmail.com>
 * All Rights Reserved.
 */

package indi.key.myapplication

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import indi.key.myapplication.day1.LanguageActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        day_1_button.setOnClickListener {
            startActivity(Intent(this, LanguageActivity::class.java))
        }
    }
}
