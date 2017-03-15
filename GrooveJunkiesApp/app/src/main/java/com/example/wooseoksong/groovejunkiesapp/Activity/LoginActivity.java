package com.example.wooseoksong.groovejunkiesapp.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.example.wooseoksong.groovejunkiesapp.BackgroundTask.LoginBT;
import com.example.wooseoksong.groovejunkiesapp.R;

import org.json.JSONObject;

/**
 * Created by wooseoksong on 2017. 3. 16..
 */

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        Button loginBtn = (Button)findViewById(R.id.loginBtn);
        Button registerBtn = (Button)findViewById(R.id.registerBtn);


        final Activity thisActivity = this;

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AutoCompleteTextView emailForm =
                        (AutoCompleteTextView)findViewById(R.id.email);
                AutoCompleteTextView passwordForm =
                        (AutoCompleteTextView)findViewById(R.id.password);



                String email = emailForm.getText().toString();
                String password = passwordForm.getText().toString();



                LoginBT loginBT = new LoginBT(
                        email, password,
                        "http://192.168.0.107:8080/appServer/loginAction");

                try{
                    JSONObject result = loginBT.execute().get();
                    if(result != null && result.get("result").equals("success")){
                        Toast.makeText(thisActivity, "로그인 성공!", Toast.LENGTH_SHORT).show();
                    }
                }catch(Exception e){e.printStackTrace();}
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {

        Intent intent = getIntent();

        if(intent != null){
            String result = intent.getStringExtra("result");
            if(result != null && result.equals("ok")){
                Toast.makeText(this, "회원가입 성공! 로그인을 해주세요 :)", Toast.LENGTH_SHORT);
            }
        }

        super.onResume();
    }
}
