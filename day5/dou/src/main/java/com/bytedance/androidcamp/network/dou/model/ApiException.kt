/*
 * Copyright (c) 2019 Chen Mouxiang <cmx_1007@foxmail.com>
 * All Rights Reserved.
 */

package com.bytedance.androidcamp.network.dou.model

import android.util.AndroidRuntimeException
import java.lang.Exception

class ApiException: AndroidRuntimeException {
    constructor() : super()
    constructor(name: String?) : super(name)
    constructor(name: String?, cause: Throwable?) : super(name, cause)
    constructor(cause: Exception?) : super(cause)
}