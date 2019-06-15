package com.example.kd.ata;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

public class splash_screen extends AppCompatActivity {
    ProgressBar pb2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
         pb2=(ProgressBar)findViewById(R.id.pb2);
        startService(new Intent(this, ScreenOnService.class));
         pb2.setProgressBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
        pb2.setMax(20);
        Runnable r =new Runnable(){
            int i=1;
            @Override
            public void run() {
                while(i<21) {
                    try {
                        Thread.sleep(100);
                        pb2.setProgress(i);
                        i++;
                    } catch (InterruptedException e) {
                        e.printStackTrace();

                    }
                }
                Intent intent= new Intent(splash_screen.this,homepage_main.class);
                startActivity(intent);
                finish();
            }
        };

        new Thread(r).start();/*
        Thread mythread=new Thread(){
            @Override
            public void run() {
                try {
                    sleep(2000);
                    Intent intent= new Intent(splash_screen.this,homepage_main.class);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        mythread.start();*/
    }


}
