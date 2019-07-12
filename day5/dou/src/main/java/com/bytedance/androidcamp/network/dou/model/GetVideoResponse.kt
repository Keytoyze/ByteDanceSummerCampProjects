/*
 * Copyright (c) 2019 Chen Mouxiang <cmx_1007@foxmail.com>
 * All Rights Reserved.
 */

package com.bytedance.androidcamp.network.dou.model

import com.google.gson.annotations.SerializedName

data class GetVideoResponse(
        var feeds: List<Video>//List<VideoFeed>
)