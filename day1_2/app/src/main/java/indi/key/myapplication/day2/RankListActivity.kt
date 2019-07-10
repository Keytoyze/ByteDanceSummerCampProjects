/*
 * Copyright (c) 2019 Chen Mouxiang <cmx_1007@foxmail.com>
 * All Rights Reserved.
 */

package indi.key.myapplication.day2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import indi.key.myapplication.R
import kotlinx.android.synthetic.main.activity_rank_list.*

class RankListActivity : AppCompatActivity() {

    private val adapter = RankAdapter()

    private val viewModel: RankViewModel by lazy {
        ViewModelProviders.of(this)[RankViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rank_list)

        rankingList.layoutManager = LinearLayoutManager(this)
        rankingList.adapter = adapter
        viewModel.getRankingData().observe(this,
            Observer<List<RankItem>> { t ->
                t?.let {
                    adapter.setList(t)
                }
            })
        viewModel.fetch()
    }
}
