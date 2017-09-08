package com.jjunji.android.foolog2.Util;


import com.jjunji.android.foolog2.model.Join;
import com.jjunji.android.foolog2.model.Login;
import com.jjunji.android.foolog2.model.LoginResult;

import retrofit2.Call;
import retrofit2.http.Body;

import retrofit2.http.POST;


/**
 * Created by jhjun on 2017-08-01.
 */

public interface NetworkService {

    public static final String BASE_URL = "http://api.foolog.xyz/";

    @POST("member/")
    Call<Join> createUser(@Body Join join);

    @POST("member/login/")
    Call<LoginResult> createLogin(@Body Login login);

}