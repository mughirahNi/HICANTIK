package coms.example.asus.doctor_appointment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;

public class Pengguna extends AppCompatActivity {
    ImageView ic_back;
    TextView Tv_email;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengguna);

        ic_back = findViewById(R.id.icBack);
        Tv_email = findViewById(R.id.TVemail);
        sessionManager = new SessionManager(getApplicationContext());

        HashMap<String, String> user = sessionManager.getUserDetails();
        String name = user.get(SessionManager.kunci_email);
        Tv_email.setText(Html.fromHtml("<b>" + name + "</b>"));
        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Homehicantik.class));
                finish();
            }
        });
    }
}