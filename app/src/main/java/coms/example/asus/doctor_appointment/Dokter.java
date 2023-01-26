package coms.example.asus.doctor_appointment;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Dokter extends AppCompatActivity {

    SwipeRefreshLayout srl_main;
    ArrayList<String> array_namadokter, array_keahlian, array_kontak, array_jadwal, array_alamatklinik, array_photo;
    ProgressDialog progressDialog;
    ListView listProses;
    ImageView ic_kembali, ic_tambah;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dokter);

        ///set variable sesuai dengan widget yang digunakan
        listProses = findViewById(R.id.LV);
        srl_main = findViewById(R.id.swipe_container);
        progressDialog = new ProgressDialog(this);

        ic_kembali = findViewById(R.id.ic_back);
        ic_tambah = findViewById(R.id.ic_tambahklinik);

        ic_kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dokter.this, AdminHome.class);
                startActivity(intent);
            }
        });

        ic_tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dokter.this, Add_Doctor.class);
                startActivity(intent);
            }
        });


        srl_main.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srollRefresh();
                srl_main.setRefreshing(false);
            }
        });
        // Scheme colors for animation
        srl_main.setColorSchemeColors(
                getResources().getColor(android.R.color.holo_blue_bright),
                getResources().getColor(android.R.color.holo_green_light),
                getResources().getColor(android.R.color.holo_orange_light),
                getResources().getColor(android.R.color.holo_red_light)

        );

        srollRefresh();

    }

    public void srollRefresh() {
        progressDialog.setMessage("Mengambil Data....");
        progressDialog.setCancelable(false);
        progressDialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {getData(); }
        }, 2000);
    }


    void initializerArray() {
        array_namadokter   = new ArrayList<String>();
        array_keahlian      = new ArrayList<String>();
        array_kontak        = new ArrayList<String>();
        array_jadwal        = new ArrayList<String>();
        array_alamatklinik   = new ArrayList<String>();
        array_photo         = new ArrayList<String>();

        // clear ini untuk menginilisasi array
        array_namadokter.clear();
        array_keahlian.clear();
        array_kontak.clear();
        array_jadwal.clear();
        array_alamatklinik.clear();
        array_photo.clear();

    }

    public void getData() {
        initializerArray();
        AndroidNetworking.get("https://tekajeapunya.com/kelompok_1/getDokter.php")
                .setTag("Get Data")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();

                        try {

                            Boolean status = response.getBoolean("status");
                            if (status) {
                                JSONArray ja = response.getJSONArray("result");
                                Log.d("respon", "" + ja);
                                for (int i = 0 ; i < ja.length(); i++) {
                                    JSONObject jo = ja.getJSONObject(i);


                                    array_namadokter.add(jo.getString("nama_dokter"));
                                    array_keahlian.add(jo.getString("keahlian"));
                                    array_kontak.add(jo.getString("kontak"));
                                    array_jadwal.add(jo.getString("jadwal"));
                                    array_alamatklinik.add(jo.getString("alamat_klinik"));

                                    //add this code
                                    array_photo.add(jo.getString("photo"));

                                }

                                final CLV_Dokter adapter = new CLV_Dokter(Dokter.this, array_namadokter, array_keahlian, array_kontak, array_jadwal, array_alamatklinik, array_photo);
                                //Set adapter to list
                                listProses.setAdapter(adapter);

                                //edit and delete
                                listProses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        Log.d("TestKlik", "" + array_namadokter.get(position));
                                        Toast.makeText(Dokter.this, array_namadokter.get(position), Toast.LENGTH_SHORT).show();

                                        //Setelah proses koneksi keserver selesai, maka aplikasi akan berpindah class
                                        //DataActivity.class dan membawa/mengirim data-data hasil query dari server.
                                        Intent i = new Intent(Dokter.this, Update_Dokter.class);

                                        i.putExtra("nama_dokter", array_namadokter.get(position));
                                        i.putExtra("keahlian", array_keahlian.get(position));
                                        i.putExtra("kontak", array_kontak.get(position));
                                        i.putExtra("jadwal", array_jadwal.get(position));
                                        i.putExtra("alamat_klinik", array_alamatklinik.get(position));
                                        i.putExtra("photo", array_photo.get(position));

                                        startActivity(i);
                                    }
                                });


                            } else {
                                Toast.makeText(Dokter.this, "Gagal Mengambil Data", Toast.LENGTH_SHORT).show();

                            }
                        }

                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });




    }


}

