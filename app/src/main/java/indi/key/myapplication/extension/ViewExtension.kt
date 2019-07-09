/*
 * Copyright (c) 2019 Chen Mouxiang <cmx_1007@foxmail.com>
 * All Rights Reserved.
 */

package indi.key.myapplication.extension

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

fun <T : ViewDataBinding> ViewGroup.inflateDataBinding(layoutRes: Int): T {
    return DataBindingUtil.inflate(
        LayoutInflater.from(context),
        layoutRes, this, false
    )
}

fun <T : ViewDataBinding> Activity.setBindingContentView(layoutRes: Int): T {
    return DataBindingUtil.setContentView(this, layoutRes)
}
