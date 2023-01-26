package coms.example.asus.doctor_appointment;

import static coms.example.asus.doctor_appointment.R.id.ic_kembali_listkontak;
import static coms.example.asus.doctor_appointment.R.id.ic_tambahklinik;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class update_klinik extends AppCompatActivity {

    ImageView ictambahkontak;
    ImageView ickembali;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_klinik);

        ickembali = (ImageView) findViewById(R.id.ic_kembali_listkontak);
        ictambahkontak = (ImageView) findViewById(R.id.ic_tambahklinik);

        ictambahkontak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(update_klinik.this, add_clinick.class);
                startActivity(intent);
            }
        });
        ickembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(update_klinik.this, Homehicantik.class);
                startActivity(intent);
            }
        });

    }
}