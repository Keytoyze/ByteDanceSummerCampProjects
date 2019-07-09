/*
 * Copyright (c) 2019 Chen Mouxiang <cmx_1007@foxmail.com>
 * All Rights Reserved.
 */

package indi.key.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import indi.key.myapplication.day1.LanguageActivity
import indi.key.myapplication.day2.RankListActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        day_1_button.setOnClickListener {
            startActivity(Intent(this, LanguageActivity::class.java))
        }

        day_2_button.setOnClickListener {
            startActivity(Intent(this, RankListActivity::class.java))

        }
    }
}
