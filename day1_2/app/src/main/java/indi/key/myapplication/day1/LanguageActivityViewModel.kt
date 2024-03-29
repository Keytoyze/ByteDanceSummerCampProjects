/*
 * Copyright (c) 2019 Chen Mouxiang <cmx_1007@foxmail.com>
 * All Rights Reserved.
 */

package indi.key.myapplication.day1

data class LanguageActivityViewModel(
    var isSubmitting: Boolean,
    var onButtonClickListener: () -> Unit,
    var onLanguageClick: (Int) -> Unit,
    var onOSClick: (Int) -> Unit
)