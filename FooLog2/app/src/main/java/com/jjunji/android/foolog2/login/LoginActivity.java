package com.jjunji.android.foolog2.login;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jjunji.android.foolog2.MainActivity;
import com.jjunji.android.foolog2.R;
import com.jjunji.android.foolog2.util.NetworkService;
import com.jjunji.android.foolog2.util.SharedPreferencesDb;
import com.jjunji.android.foolog2.model.Login;
import com.jjunji.android.foolog2.model.LoginResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/*
- model -
Join : 회원가입시 사용
Login : 로그인시 서버로 email, password 전송
LoginResult : 로그인 -> 토큰 생성 -> 토큰 저장용
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private final int REQ_PERMISSION = 100;
    private EditText txtEmail, txtPassword;
    private Button btnLogin, btnSignUp;
    private Intent intent;
    static String token; // 가입 시 생성되는 token 저장
    private int pk;
    private Login login;
    private String email, nickName;
   // private String loginId, loginPwd;  // SharedPreferences 사용을 위한 id, pwd 선언

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
        //setSharedPreferences(); // SharedPreferences 정의
        checkStorage();  // 저장된 키 값이 있는지 확인
        setOnClickListener(); // 버튼 클릭 이벤트 설정

        // 마시멜로 이상버전에서만 런타임 권한체크
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission();
        }else {
            init();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkPermission(){
        //1 권한체크 - 특정권한이 있는지 시스템에 물어본다
        if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
            init();
        }else{
            // 2. 권한이 없으면 사용자에 권한을 달라고 요청
            String permissions[] = { Manifest.permission.WRITE_EXTERNAL_STORAGE
                    , Manifest.permission.CAMERA };
            requestPermissions(permissions ,REQ_PERMISSION); // -> 권한을 요구하는 팝업이 사용자 화면에 노출된다
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQ_PERMISSION){
            // 3.1 사용자가 승인을 했음
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                init();
                // 3.2 사용자가 거절 했음
            }else{
                Toast.makeText(this, "권한요청을 승인하셔야 앱을 사용할 수 있습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void init() {
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtPassword = (EditText) findViewById(R.id.txtPassword1);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);
    }

    private void setOnClickListener(){
        btnLogin.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
    }

    // 저장된 키 값이 있다면 자동 로그인
    private void checkStorage() {
        if(SharedPreferencesDb.getId(this, "loginId") != null && SharedPreferencesDb.getPwd(this, "loginPwd") != null) {
            String id = SharedPreferencesDb.getId(this, "loginId");
            Toast.makeText(LoginActivity.this, id +"님 자동로그인 입니다.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    // 저장된 키 값이 없다면 로그인 정보 저장
    private void setStorage(){
        if(SharedPreferencesDb.getId(this, "loginId") == null && SharedPreferencesDb.getPwd(this, "loginPwd") == null){
            SharedPreferencesDb.setId(this, "loginId", txtEmail.getText().toString());  // email 저장.
            SharedPreferencesDb.setPwd(this, "loginPwd", txtPassword.getText().toString()); // 비밀번호 저장.
            SharedPreferencesDb.setToken(this, "token", token); // 토큰 저장.
            SharedPreferencesDb.setToken(this, "nickName", nickName);
        }
    }

    private void model_setLogin(){
        String email = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();

        // Login 클래스는 로그인용으로만 사용됨.
        login = new Login();
        login.email = email;
        login.password = password;
    }

    private void setNetwork(){
        // 레트로핏 객체 정의
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.foolog.xyz/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        // 실제 서비스 인터페이스 생성.
        NetworkService service = retrofit.create(NetworkService.class);
        // 서비스 호출
        Call<LoginResult> call = service.createLogin(login);  // 응답 body는 LoginResult.class를 이용.
        call.enqueue(new Callback<LoginResult>() {
            @Override
            public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                // 전송결과가 정상이면
                Log.e("Write","in ====== onResponse");
                if(response.isSuccessful()){
                    LoginResult loginResult = response.body(); // 로그인시 생성되는 객체는 loginResult 형태.
                    email = loginResult.user.email;
                    nickName = loginResult.user.nickname;
                    token = loginResult.key;
                    pk = loginResult.user.pk;
                    Log.e("LoginActivity", "token =====================" +token);
                    Log.e("LoginActivity", "pk=========================" + pk);
                    Toast.makeText(getBaseContext(),"로그인", Toast.LENGTH_SHORT).show();

                    setStorage();

                    intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }else{
                    int statusCode = response.code();
                    Log.i("MyTag", "응답코드 ============= "+statusCode);
                    //Log.i("MyTag", "응답코드 ============= "+response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<LoginResult> call, Throwable t) {
                Log.e("MyTag","error==========="+t.getMessage());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            // 로그인
            case R.id.btnLogin :
                model_setLogin();
                setNetwork();
                break;

            // 회원가입 페이지로 이동
            case R.id.btnSignUp :
                intent = new Intent(this, SignUpActivity.class);
                startActivity(intent);
                break;
        }
    }
}

