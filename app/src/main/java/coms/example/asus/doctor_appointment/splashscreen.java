package coms.example.asus.doctor_appointment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class splashscreen extends AppCompatActivity {
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        img = findViewById(R.id.logo);
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.drop_from_top);
        img.startAnimation(anim);

        SessionManager sessionManager = new SessionManager(getApplicationContext());

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                sessionManager.checkLogin2();
                finish();
            }
        },5000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(splashscreen.this, onboarding.class));
                finish();
            }
        },3000);
    }
}



