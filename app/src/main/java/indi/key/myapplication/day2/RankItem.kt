/*
 * Copyright (c) 2019 Chen Mouxiang <cmx_1007@foxmail.com>
 * All Rights Reserved.
 */

package indi.key.myapplication.day2

data class RankItem(
    var order: Int,
    var title: String,
    var hotValue: Double,
    var isNew: Boolean = false,
    var isHot: Boolean = false,
    var isRecommended: Boolean = false
) {
    fun getHotString() = String.format("%.1fw", hotValue)

    fun isBold() = order <= 3

    fun getOrderString() = "$order."
}