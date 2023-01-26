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

public class reservasi_hicantik extends AppCompatActivity {

    SwipeRefreshLayout srl_main;
    ArrayList<String> array_namalengkap,array_treatment,array_dokter, array_tanggal, array_waktu, array_nomorreservasi;
    ProgressDialog progressDialog;
    ListView listProses;
    ImageView ic_kembali;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservasi_hicantik);

        //set variable sesuai dengan widget yang digunakan
        listProses = findViewById(R.id.LVuser);
        srl_main    = findViewById(R.id.swipe_containeruser);
        progressDialog = new ProgressDialog(this);

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
            public void run() {getData(); }
        },2000);
    }

    void initializeArray(){
        array_namalengkap           = new ArrayList<String>();
        array_treatment             = new ArrayList<String>();
        array_dokter                = new ArrayList<String>();
        array_tanggal               = new ArrayList<String>();
        array_waktu                 = new ArrayList<String>();
        array_nomorreservasi        = new ArrayList<String>();


        // clear ini untuk menginilisasi array
        array_namalengkap.clear();
        array_treatment.clear();
        array_dokter.clear();
        array_tanggal.clear();
        array_waktu.clear();
        array_nomorreservasi.clear();
    }
    public void getData(){
        initializeArray();
        AndroidNetworking.get("https://tekajeapunya.com/kelompok_1/getReservasi.php")
                .setTag("Get Data")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();

                        try{
                            Boolean status = response.getBoolean("status");
                            if(status){
                                JSONArray ja = response.getJSONArray("result");
                                Log.d("respon",""+ja);
                                for(int i = 0 ; i < ja.length() ; i++){
                                    JSONObject jo = ja.getJSONObject(i);

                                    array_namalengkap.add(jo.getString("nama_lengkap"));
                                    array_treatment.add(jo.getString("treatment"));
                                    array_dokter.add(jo.getString("dokter"));
                                    array_tanggal.add(jo.getString("tanggal"));
                                    array_waktu.add(jo.getString("waktu"));
                                    array_nomorreservasi.add(jo.getString("nomor_antrian"));
                                }


                                //Menampilkan data berbentuk adapter menggunakan class CLVDataUser
                                final CLV_verifikasi adapter = new CLV_verifikasi(reservasi_hicantik.this,array_namalengkap,array_treatment,array_dokter, array_tanggal, array_waktu, array_nomorreservasi);
                                //Set adapter to list
                                listProses.setAdapter(adapter);
                                //edit and delete
                                listProses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        Log.d("TestKlik",""+array_namalengkap.get(position));
                                        Toast.makeText(reservasi_hicantik.this, array_namalengkap.get(position), Toast.LENGTH_SHORT).show();
                                    }
                                });


                            }else{
                                Toast.makeText(reservasi_hicantik.this, "Gagal Mengambil Data", Toast.LENGTH_SHORT).show();

                            }

                        }
                        catch (Exception e){
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
        getMenuInflater().inflate(R.menu.main,menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.action_add){
            Intent i = new Intent(reservasi_hicantik.this,reservasi_hicantik.class);
            startActivityForResult(i,1);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){
            if(resultCode==RESULT_OK){
                scrollRefresh();
            }else if(resultCode==RESULT_CANCELED){
                Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();
            }
        }

        if(requestCode==2){
            if(resultCode==RESULT_OK){
                scrollRefresh();
            }else if(resultCode==RESULT_CANCELED){
                Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();
            }
        }
    }
}