/*
 * Copyright (c) 2019 Chen Mouxiang <cmx_1007@foxmail.com>
 * All Rights Reserved.
 */

package indi.key.myapplication.day2

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RankViewModel : ViewModel() {

    // TODO: use Repository to fetch data from API. Here we use static text
    // to feed consumer.

    private val liveData = MutableLiveData<List<RankItem>>()

    fun getRankingData(): LiveData<List<RankItem>> {
        return liveData
    }

    private val staticData: List<RankItem> by lazy {
        listOf(
            RankItem(1, "那个跑赢地震波的四川人", 266.0, isNew = true),
            RankItem(2, "消防员用衣服擦车的动作真帅", 251.8, isHot = true),
            RankItem(3, "出阁宴退场时新娘跪父母恩", 234.1),
            RankItem(4, "舒淇摔倒", 221.0, isHot = true),
            RankItem(5, "最强男子变身术", 196.1, isNew = true),
            RankItem(6, "女朋友烧的土豆像陨石", 182.6),
            RankItem(7, "游乐王子上快乐大本营", 48.2),
            RankItem(8, "中国孕妇泰国坠崖案翻转", 33.7),
            RankItem(9, "大爷溜的宠物从不让人失望", 27.6),
            RankItem(10, "为好生活battle", 26.5, isRecommended = true),
            RankItem(11, "林允约你晒美貌", 26.0, isRecommended = true),
            RankItem(12, "曾轶可", 25.5)
        )
    }

    fun fetch() {
        liveData.postValue(staticData)
    }
}