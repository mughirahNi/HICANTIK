package coms.example.asus.doctor_appointment;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class Homehicantik extends AppCompatActivity {
    DrawerLayout drawerLayout;
    RelativeLayout fitur_booking, fitur_info, fitur_kirimSaran, fitur_infodokter;
    private RecyclerView recyclerView;
    private InfoAdapter infoAdapter;
    SessionManager sessionManager;
    TextView User;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homehicantik);

        drawerLayout = findViewById(R.id.drawer_layout);
        fitur_booking = findViewById(R.id.fitur_booking);
        fitur_info = findViewById(R.id.fitur_info);
        fitur_kirimSaran = findViewById(R.id.fitursaran);
        fitur_infodokter = findViewById(R.id.fiturdetail);
        User = findViewById(R.id.namauser);
        sessionManager = new SessionManager(getApplicationContext());

        HashMap<String, String> user = sessionManager.getUserDetails();
        String name = user.get(SessionManager.kunci_email);
        User.setText(Html.fromHtml("<b>" + name + "</b>"));

        ArrayList<StaticInfoModel> item = new ArrayList<>();
        item.add(new StaticInfoModel(R.drawable.doctor, "pilih dokter kecantikan terbaik"));
        item.add(new StaticInfoModel(R.drawable.satu, "kamu dapat melakukan konsultasi dengan dokter pilihan kamu dan dapatkan perawatan terbaik"));
        item.add(new StaticInfoModel(R.drawable.onboard3, "lakukan reservasi online dengan mudah"));

        recyclerView = findViewById(R.id.tvinfo_dashboard);
        infoAdapter = new InfoAdapter(item);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(infoAdapter);

        fitur_booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Homehicantik.this, Klinik_User.class);
                startActivity(i);
            }
        });

        fitur_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Homehicantik.this, info_kami.class);
                startActivity(i);
            }
        });

        fitur_kirimSaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Homehicantik.this, Saran.class);
                startActivity(i);
            }
        });

        fitur_infodokter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Homehicantik.this, info_Dokter.class);
                startActivity(i);
            }
        });

    }
    public void ClickNavMenu (View view) {
        openDrawer(drawerLayout);
    }

    public static void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer((GravityCompat.START));
    }

    private static void closDrawer(DrawerLayout drawerLayout) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }

    }
    public void Clickdasboard(View view) {

        recreate();
    }

    public void Clickprofil(View view) {
        redirectActivity(this, reservasi_hicantik.class);
    }

    public void ClickMyprofil(View view) { redirectActivity(this, Pengguna.class);}

    public void ClickKeluar(View view){

        sessionManager.logout();
    }

    private void keluar(Activity activity) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setTitle("Keluar");
        builder.setMessage("Apakah Anda Yakin Ingin Keluar dari aplikasi ?");

        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                activity.finishAffinity();

                System.exit(0);

            }
        });
        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

            }
        });
        builder.show();
    }

    protected void onPause(){
        super.onPause();

        closDrawer(drawerLayout);
    }

    private void redirectActivity(Activity activity, Class aClass) {

        Intent intent = new Intent(activity,aClass);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);

    }
}