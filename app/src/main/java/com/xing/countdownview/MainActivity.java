package com.xing.countdownview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.xing.lib.CountDownView;

public class MainActivity extends AppCompatActivity {

    CountDownView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        view = findViewById(R.id.id_view);

    }

    public void start(View v){
        view.start();

    }

}
