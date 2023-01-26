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

public class List_saran extends AppCompatActivity {

    SwipeRefreshLayout srl_main;
    ArrayList<String> array_nama,array_email,array_saran;
    ProgressDialog progressDialog;
    ListView listProses;
    ImageView ic_kembali;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_saran);

        //set variable sesuai dengan widget yang digunakan
        listProses = findViewById(R.id.LV);
        srl_main    = findViewById(R.id.swipe_container);
        progressDialog = new ProgressDialog(this);
        ic_kembali = findViewById(R.id.ic_kembali_listkontak);

        ic_kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(List_saran.this, AdminHome.class);
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
            public void run() {getData(); }
        },2000);
    }

    void initializeArray(){
        array_nama          = new ArrayList<String>();
        array_email         = new ArrayList<String>();
        array_saran         = new ArrayList<String>();


        // clear ini untuk menginilisasi array
        array_nama.clear();
        array_email.clear();
        array_saran.clear();
    }
    public void getData(){
        initializeArray();
        AndroidNetworking.get("https://tekajeapunya.com/kelompok_1/hicantik/getdataSaran.php")
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

                                    array_nama.add(jo.getString("nama"));
                                    array_email.add(jo.getString("email"));
                                    array_saran.add(jo.getString("saran"));
                                }


                                //Menampilkan data berbentuk adapter menggunakan class CLVDataUser
                                final CLV_Saran adapter = new CLV_Saran(List_saran.this,array_nama,array_email,array_saran);
                                //Set adapter to list
                                listProses.setAdapter(adapter);
                                //edit and delete
                                listProses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        Log.d("TestKlik",""+array_nama.get(position));
                                        Toast.makeText(List_saran.this, array_nama.get(position), Toast.LENGTH_SHORT).show();
                                    }
                                });


                            }else{
                                Toast.makeText(List_saran.this, "Gagal Mengambil Data", Toast.LENGTH_SHORT).show();

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
            Intent i = new Intent(List_saran.this,Saran.class);
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