package coms.example.asus.doctor_appointment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class info_kami extends AppCompatActivity {

    ImageView backdash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info);

        backdash = findViewById(R.id.back);

        backdash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(info_kami.this, Homehicantik.class);
                startActivity(i);

            }
        });
    }
}