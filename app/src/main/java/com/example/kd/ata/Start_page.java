package com.example.kd.ata;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class Start_page extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);
        Thread splashThread = new Thread()
        {
            @Override
            public void run()
            {
                try {
                    int waited = 0;
                    while (waited < 1000)
                    {
                        sleep(100);
                        waited += 100;
                    }
                } catch (InterruptedException e)
                {
                    // do nothing
                } finally
                {
                    finish();
                    Intent i = new Intent(Start_page.this,homepage_main.class);
                    startActivity(i);
                }
            }
        };
        splashThread.start();
    }
    }





