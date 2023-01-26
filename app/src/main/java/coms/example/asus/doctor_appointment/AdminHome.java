package coms.example.asus.doctor_appointment;

import static coms.example.asus.doctor_appointment.R.id.drawer_layout1;

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
import android.view.View;
import android.widget.RelativeLayout;

import java.util.ArrayList;

public class AdminHome extends AppCompatActivity {
    DrawerLayout drawerLayout;
    RelativeLayout daftar_klinik, Daftar_treatment, Saran, reservasi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        drawerLayout = findViewById(R.id.drawer_layout1);
        daftar_klinik = findViewById(R.id.add_klinik);
        Daftar_treatment = findViewById(R.id.addtreatment);
        Saran= findViewById(R.id.saran);
        reservasi = findViewById(R.id.list_reservasi);


        daftar_klinik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AdminHome.this, Klinik.class);
                startActivity(i);
            }
        });

        Daftar_treatment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AdminHome.this, item_treatment.class);
                startActivity(i);
            }
        });

        Saran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AdminHome.this, List_saran.class);
                startActivity(i);
            }
        });

        reservasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AdminHome.this, Dokter.class);
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
        redirectActivity(this, item_reservasi.class);
    }

    public void click_info(View view) {redirectActivity(this, Admin_info.class);}

    public void ClickKeluar(View view){
        keluar(this);
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