package com.xing.countdownview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.xing.lib.CountDownView;

public class MainActivity extends AppCompatActivity implements CountDownView.OnCountDownViewListener {

    CountDownView view;
    CountDownView view1;
    CountDownView view2;
    CountDownView view3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        view = findViewById(R.id.id_view);
        view1 = findViewById(R.id.id_view1);
        view2 = findViewById(R.id.id_view2);
        view3 = findViewById(R.id.id_view4);

        view2.setCountDownListener(this);
    }

    public void start(View v){
        view.start();
        view1.start();
        view2.start();
        view3.start();
    }

    @Override
    public void onFinished() {
        Toast.makeText(this,"finished", Toast.LENGTH_SHORT).show();

    }
}
