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
import org.json.JSONObject;

import java.util.ArrayList;

public class item_reservasi extends AppCompatActivity {

    SwipeRefreshLayout srl_main;
    ArrayList<String> array_nama_lengkap, array_treatment, array_dokter, array_tanggal, array_waktu, array_reservasi;
    ProgressDialog progressDialog;
    ListView listProses;
    ImageView ic_kembali;
    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_reservasi);


        listProses = findViewById(R.id.LV);
        srl_main = findViewById(R.id.swipe_container);
        progressDialog = new ProgressDialog(this);
        ic_kembali = findViewById(R.id.ic_kembali);

        ic_kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(item_reservasi.this, AdminHome.class);
                startActivity(intent);
            }
        });


        srl_main.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                scrollRefresh();
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

        scrollRefresh();
    }

    public void scrollRefresh() {
        progressDialog.setMessage("Mengambil Data.....");
        progressDialog.setCancelable(false);
        progressDialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getData();
            }
        }, 2000);
    }

    void initializeArray() {
        array_nama_lengkap = new ArrayList<String>();
        array_treatment = new ArrayList<String>();
        array_dokter = new ArrayList<String>();
        array_tanggal = new ArrayList<String>();
        array_waktu = new ArrayList<String>();
        array_reservasi = new ArrayList<String>();


        // clear ini untuk menginilisasi array
        array_nama_lengkap.clear();
        array_treatment.clear();
        array_dokter.clear();
        array_tanggal.clear();
        array_waktu.clear();
        array_reservasi.clear();
    }

    public void getData() {
        initializeArray();
        AndroidNetworking.get("https://tekajeapunya.com/kelompok_1/getReservasi.php")
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
                                for (int i = 0; i < ja.length(); i++) {
                                    JSONObject jo = ja.getJSONObject(i);

                                    array_nama_lengkap.add(jo.getString("nama_lengkap"));
                                    array_treatment.add(jo.getString("treatment"));
                                    array_dokter.add(jo.getString("dokter"));
                                    array_tanggal.add(jo.getString("tanggal"));
                                    array_waktu.add(jo.getString("waktu"));
                                    array_reservasi.add(jo.getString("nomor_antrian"));
                                }


                                //Menampilkan data berbentuk adapter menggunakan class CLVDataUser
                                final CLV_Reservasi adapter = new CLV_Reservasi(item_reservasi.this, array_nama_lengkap, array_treatment, array_dokter, array_tanggal, array_waktu, array_reservasi);
                                //Set adapter to list
                                listProses.setAdapter(adapter);
                                //edit and delete
                                listProses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        Log.d("TestKlik", "" + array_nama_lengkap.get(position));
                                        Toast.makeText(item_reservasi.this, array_nama_lengkap.get(position), Toast.LENGTH_SHORT).show();

                                        Intent i = new Intent(item_reservasi.this, Verifikasi.class);
                                        i.putExtra("nama_lengkap", array_nama_lengkap.get(position));
                                        i.putExtra("treatment", array_treatment.get(position));
                                        i.putExtra("dokter", array_dokter.get(position));
                                        i.putExtra("tanggal", array_tanggal.get(position));
                                        i.putExtra("waktu", array_waktu.get(position));
                                        i.putExtra("nomor_antrian", array_reservasi.get(position));

                                        startActivity(i);
                                    }
                                });


                            } else {
                                Toast.makeText(item_reservasi.this, "Gagal Mengambil Data", Toast.LENGTH_SHORT).show();

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }


                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add) {
            Intent i = new Intent(item_reservasi.this, reservasi.class);
            startActivityForResult(i, 1);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                scrollRefresh();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                scrollRefresh();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

