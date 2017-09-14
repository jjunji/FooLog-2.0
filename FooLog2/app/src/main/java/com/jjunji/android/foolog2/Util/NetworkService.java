package com.jjunji.android.foolog2.Util;


import com.jjunji.android.foolog2.model.AllList;
import com.jjunji.android.foolog2.model.DayList;
import com.jjunji.android.foolog2.model.Delete;
import com.jjunji.android.foolog2.model.Join;
import com.jjunji.android.foolog2.model.Login;
import com.jjunji.android.foolog2.model.LoginResult;
import com.jjunji.android.foolog2.model.TagList;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;

import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;


/**
 * Created by jhjun on 2017-08-01.
 */

public interface NetworkService {

    public static final String BASE_URL = "http://api.foolog.xyz/";

    @POST("member/")
    Call<Join> createUser(@Body Join join);

    @POST("member/login/")
    Call<LoginResult> createLogin(@Body Login login);

    @DELETE("post/{pk}/")
    Call<Delete> deletePost(@Header("Authorization") String send_token,
                            @Path("pk") String pk);

    // stats/?start=20170811&end=20170814
    //@GET("stats/?start={start}&end={end}")
    //Call<TagList[]> createTagList(@Path("start") int start, @Path("end") int end);
    @GET("stats/")
    Call<List<TagList>> createTagList(
            @Header("Authorization") String send_token,
            @Query("start") String start,
            @Query("end") String end
    );

    // Post Day list
    @GET("post/day/{day}/")
    Call<List<DayList>> createDayList(@Path("day") String day, @Header("Authorization") String send_token); // 토큰 값만 전송 & api 뒤에 날짜입력

    @GET("post/")
    Call<List<AllList>> getAllList(@Header("Authorization") String send_token);


}