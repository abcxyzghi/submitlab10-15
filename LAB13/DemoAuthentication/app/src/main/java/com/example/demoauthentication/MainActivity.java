package com.example.demoauthentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.airbnb.lottie.LottieAnimationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LottieAnimationView animationView = findViewById(R.id.animationView);

        animationView.setAnimation("loading.json");
        animationView.loop(true);
        animationView.playAnimation();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, ChoiceLoginActivity.class);

                startActivity(intent);

                finish();
            }
        }, 3000);
    }
}