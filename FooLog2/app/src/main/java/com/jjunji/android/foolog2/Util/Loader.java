package com.jjunji.android.foolog2.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.widget.Toast;

import com.jjunji.android.foolog2.Calendar.CalendarAdapter;
import com.jjunji.android.foolog2.CalendarFragment;
import com.jjunji.android.foolog2.Dialog.CustomDialog;
import com.jjunji.android.foolog2.ListRecyclerViewAdapter;
import com.jjunji.android.foolog2.model.AllList;
import com.jjunji.android.foolog2.model.DayList;
import com.jjunji.android.foolog2.model.TagList;
import com.jjunji.android.foolog2.util.ITask.createDialog;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jhjun on 2017-09-02.
 */
// public static void..
// public void ..

public class Loader {
    public static final String BASE_URL = "http://api.foolog.xyz/";
    static HttpLoggingInterceptor logging;
    static OkHttpClient client;
    static NetworkService service;

    public static void logInterceptor(){
        // okhttp log interceptor 사용
        logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        client = new OkHttpClient.Builder().addInterceptor(logging).build();
    }

    public static void initNetwork(){
        // 레트로핏 객체 정의
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        service = retrofit.create(NetworkService.class);
    }

    // day 날짜 클릭시 넘어온 해당 날짜의 정보 YYYYMMDD -> Get Day list 에 전송하는 값
    public static void setDialogNetwork(String day, String send_token, final ITask.createDialog createDialog){
        logInterceptor();
        initNetwork();

        Call<List<DayList>> call = service.createDayList(day, send_token);
        call.enqueue(new Callback<List<DayList>>() {
            @Override
            public void onResponse(Call<List<DayList>> call, Response<List<DayList>> response) {
                // 전송결과가 정상이면
                Log.e("Write", "in ====== onResponse");
                if (response.isSuccessful()) {
                    List<DayList> dayListBody = response.body();
                    if (dayListBody.size() != 0) {
                        createDialog.showDialog(dayListBody);
                    } else {
                       createDialog.noDialog(dayListBody);
                    }

                } else {
                    int statusCode = response.code();
                    Log.i("CalendarFragment", "응답코드 ============= " + statusCode);
                }
            }

            @Override
            public void onFailure(Call<List<DayList>> call, Throwable t) {
                Log.e("MyTag", "error===========" + t.getMessage());
            }
        });
    }

    // 인자로 토큰, start,end -> 날짜 범위(start ~ end) 에 작성한 글에 포함된 항목별 태그 개수
    public static void setTagNetwork(String start, String end, String send_token, final ITask.settingAdapter settingAdapter){
        settingAdapter.showProgress();

        logInterceptor();
        initNetwork();

        Call<List<TagList>> call = service.createTagList(send_token, start, end);

        call.enqueue(new Callback<List<TagList>>() {
            @Override
            public void onResponse(Call<List<TagList>> call, Response<List<TagList>> response) {
                settingAdapter.disProgress();
                List<TagList> tagList = response.body();
                settingAdapter.setAdapter(tagList);
            }

            @Override
            public void onFailure(Call<List<TagList>> call, Throwable t) {
                Log.e("CalendarFragment", "error===============" + t.getMessage());
            }
        });
    }

    // 인자로 day(특정 한 날짜) -> 선택한 날짜에 해당하는 post Data
    public static void allFoologNetwork(String send_token, final ITask.createAllFoolog createAllFoolog){
        Call<List<AllList>> call = service.getAllList(send_token);
        call.enqueue(new Callback<List<AllList>>() {
            @Override
            public void onResponse(Call<List<AllList>> call, Response<List<AllList>> response) {
                // 전송결과가 정상이면
                Log.e("Write","in ====== onResponse");
                if(response.isSuccessful()){
                    List<AllList> allLists = response.body();
                    createAllFoolog.showAllList(allLists);

                }else{
                    int statusCode = response.code();
                    Log.i("ShowListFragment", "응답코드 ============= " + statusCode);
                }
            }

            @Override
            public void onFailure(Call<List<AllList>> call, Throwable t) {
                Log.e("MyTag","error==========="+t.getMessage());
            }
        });
    }

}
