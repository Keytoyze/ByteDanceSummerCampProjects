/*
 * Copyright (c) 2019 Chen Mouxiang <cmx_1007@foxmail.com>
 * All Rights Reserved.
 */

package com.bytedance.androidcamp.network.dou.model

import com.google.gson.annotations.SerializedName
import java.util.*

data class VideoFeed(
        @SerializedName("student_id")
        var studentId: String,
        @SerializedName("user_name")
        var userName: String,
        @SerializedName("image_url")
        var imageUrl: String,
        @SerializedName("_id")
        var id: String,
        @SerializedName("video_url")
        var vedeoUrl: String,
        @SerializedName("createdAt")
        var createdAt: Date,
        @SerializedName("updatedAt")
        var updatedAt: Date
)