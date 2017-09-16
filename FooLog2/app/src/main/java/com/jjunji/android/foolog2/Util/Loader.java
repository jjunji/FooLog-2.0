package com.jjunji.android.foolog2.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.jjunji.android.foolog2.CalendarFragment;
import com.jjunji.android.foolog2.Dialog.CustomDialog;
import com.jjunji.android.foolog2.model.DayList;
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

    public static void logInterceptor(){
        // okhttp log interceptor 사용
        logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        client = new OkHttpClient.Builder().addInterceptor(logging).build();

    }

    // day 날짜 클릭시 넘어온 해당 날짜의 정보 YYYYMMDD -> Get Day list 에 전송하는 값
    public static void setDialogNetwork(String day, String send_token, final ITask.createDialog createDialog){
        // 레트로핏 객체 정의
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        NetworkService service = retrofit.create(NetworkService.class);
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
                       Toast.makeText((Context) createDialog, "Nothing", Toast.LENGTH_SHORT).show();
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


}
