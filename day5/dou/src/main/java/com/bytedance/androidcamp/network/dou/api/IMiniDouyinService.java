package com.bytedance.androidcamp.network.dou.api;

import com.bytedance.androidcamp.network.dou.model.GetVideoResponse;
import com.bytedance.androidcamp.network.dou.model.PostVideoResponse;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface IMiniDouyinService {
    // DONE 7: Define IMiniDouyinService
    String BASE_URL = "http://test.androidcamp.bytedance.com/mini_douyin/invoke/";

    @Multipart
    @POST("video")
    Single<PostVideoResponse> postVideo(
            @Query("student_id") String studentId,
            @Query("user_name") String userName,
            @Part MultipartBody.Part image,
            @Part MultipartBody.Part video
    );

    @GET("video")
    Single<GetVideoResponse> getVideos();
}
