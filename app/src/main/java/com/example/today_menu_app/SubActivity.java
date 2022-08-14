package com.example.today_menu_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.today_menu_app.network.CallRetrofit;

public class SubActivity extends AppCompatActivity {

    private Button btn_main;
    private Button btn_done;
    private EditText et_request;
    private String day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);
        et_request=(EditText)findViewById(R.id.et_request);

        Intent intent = getIntent();

        day=intent.getStringExtra("date");

        Button btn_main=(Button) findViewById(R.id.btn_main);
        btn_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        Button btn_done=(Button) findViewById(R.id.btn_done);
        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = et_request.getText().toString();
                CallRetrofit.post_suggestions(content, day);
                Toast.makeText(getApplicationContext(),"전송 완료!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}