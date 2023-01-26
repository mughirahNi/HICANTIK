package coms.example.asus.doctor_appointment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import coms.example.asus.doctor_appointment.AdminHome;
import coms.example.asus.doctor_appointment.R;

public class item_treatment extends AppCompatActivity {

    SwipeRefreshLayout srl_main;
    ArrayList<String> array_jenis_treatment, array_deskripsi, array_biaya, array_photo;
    ProgressDialog progressDialog;
    ListView listProses;
    ImageView ic_kembali, ic_tambah;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_treatment);

        listProses = findViewById(R.id.LV3);
        srl_main = findViewById(R.id.swipe_container3);
        progressDialog = new ProgressDialog(this);

        ic_kembali = findViewById(R.id.ic_kembali);
        ic_tambah = findViewById(R.id.ic_tambahtreatment);

        ic_kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(item_treatment.this, AdminHome.class);
                startActivity(intent);
            }
        });

        ic_tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(item_treatment.this, add_treatment.class);
                startActivity(i);
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
            public void run() {getData();}
        }, 2000);
    }



    void initializerArray(){
        array_jenis_treatment = new ArrayList<String>();
        array_deskripsi = new ArrayList<String>();
        array_biaya = new ArrayList<String>();
        array_photo = new ArrayList<String>();

        // clear ini untuk menginilisasi array
        array_jenis_treatment.clear();
        array_deskripsi.clear();
        array_biaya.clear();
        array_photo.clear();

    }
    public void getData(){
        initializerArray();
        AndroidNetworking.get("https://tekajeapunya.com/kelompok_1/getTreatment.php")
                .setTag("Get Data")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();

                        try {

                            Boolean status = response.getBoolean("status");
                            if (status){
                                JSONArray ja = response.getJSONArray("result");
                                Log.d("respon", ""+ja);
                                for (int i = 0 ; i < ja.length() ; i++){
                                    JSONObject jo = ja.getJSONObject(i);
                                    array_jenis_treatment.add(jo.getString("jenis_treatment"));
                                    array_deskripsi.add(jo.getString("deskripsi"));
                                    array_biaya.add(jo.getString("biaya"));

                                    //add this code
                                    array_photo.add(jo.getString("photo"));


                                }

                                final CLV_Treatment adapter = new CLV_Treatment(item_treatment.this,array_jenis_treatment,array_deskripsi,array_biaya, array_photo);
                                //Set adapter to list
                                listProses.setAdapter(adapter);

                                //edit and delete
                                listProses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        Log.d("TestKlik",""+array_jenis_treatment.get(position));
                                        Toast.makeText(item_treatment.this, array_jenis_treatment.get(position), Toast.LENGTH_SHORT).show();

                                        //Setelah proses koneksi keserver selesai, maka aplikasi akan berpindah class
                                        //DataActivity.class dan membawa/mengirim data-data hasil query dari server.
                                        Intent i = new Intent(item_treatment.this, update_treatment.class);
                                        i.putExtra("jenis_treatment",array_jenis_treatment.get(position));
                                        i.putExtra("deskripsi",array_deskripsi.get(position));
                                        i.putExtra("biaya",array_biaya.get(position));
                                        i.putExtra("photo",array_photo.get(position));

                                        startActivity(i);
                                    }
                                });


                            }else{
                                Toast.makeText(item_treatment.this, "Gagal Mengambil Data", Toast.LENGTH_SHORT).show();


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