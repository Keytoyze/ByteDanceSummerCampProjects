/*
 * Copyright (c) 2019 Chen Mouxiang <cmx_1007@foxmail.com>
 * All Rights Reserved.
 */

package indi.key.myapplication.day2

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import indi.key.myapplication.R
import indi.key.myapplication.databinding.RankingItemBinding
import indi.key.myapplication.extension.inflateDataBinding

class RankAdapter : RecyclerView.Adapter<RankAdapter.RankViewHolder>() {

    private var data: List<RankItem> = ArrayList()

    class RankViewHolder(
        val rankingItemBinding: RankingItemBinding
    ) : RecyclerView.ViewHolder(rankingItemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankViewHolder {
        return RankViewHolder(
            parent.inflateDataBinding(R.layout.ranking_item)
        )
    }

    fun setList(data: List<RankItem>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: RankViewHolder, position: Int) {
        holder.rankingItemBinding.model = data[position]
    }
}