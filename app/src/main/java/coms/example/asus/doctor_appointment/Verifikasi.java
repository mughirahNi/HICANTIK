package coms.example.asus.doctor_appointment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class Verifikasi extends AppCompatActivity {

    EditText ETnama, ETjenis, ETdokter, ETtanggal, ETwaktu, ETnomor;
    String nama_lengkap, treatment, dokter, tanggal, waktu, nomor_antrian;
    ProgressDialog progressDialog;
    Button BTNreservasi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifikasi);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_kembali);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //Intent, get data from main
        nama_lengkap         = getIntent().getStringExtra("nama_lengkap");
        treatment            = getIntent().getStringExtra("treatment");
        dokter               = getIntent().getStringExtra("dokter");
        tanggal              = getIntent().getStringExtra("tanggal");
        waktu                = getIntent().getStringExtra("waktu");



        ETnama              = findViewById(R.id.etnama);
        ETjenis             = findViewById(R.id.edJenis);
        ETdokter            = findViewById(R.id.etdok);
        ETtanggal           = findViewById(R.id.edttanggal);
        ETwaktu             = findViewById(R.id.edtwaktu);
        ETnomor             = findViewById(R.id.reser);
        BTNreservasi        = findViewById(R.id.Reservasi);

        ETnama.setText(nama_lengkap);
        ETjenis.setText(treatment);
        ETdokter.setText(dokter);
        ETtanggal.setText(tanggal);
        ETwaktu.setText(waktu);
        ETnomor.setText(nomor_antrian);


        progressDialog = new ProgressDialog(this);


        BTNreservasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(Verifikasi.this)
                        .setMessage("Apakah kamu ingin melakukan virifikasi ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                progressDialog.setMessage("Mohon tunggu...");
                                progressDialog.setCancelable(false);
                                progressDialog.show();

                                nama_lengkap = ETnama.getText().toString();
                                treatment = ETjenis.getText().toString();
                                dokter = ETdokter.getText().toString();
                                tanggal = ETtanggal.getText().toString();
                                waktu = ETwaktu.getText().toString();
                                nomor_antrian = ETnomor.getText().toString();

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        validatingData();
                                    }
                                }, 1000);
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        }).show();

            }

            private void validatingData() {
                if (nama_lengkap.equals("") || treatment.equals("") || dokter.equals("") || tanggal.equals("") || waktu.equals("") || nomor_antrian.equals("")) {
                    progressDialog.dismiss();
                    Toast.makeText(Verifikasi.this, "Data tidak boleh kosong", Toast.LENGTH_SHORT).show();
                } else {
                    updateData();
                }
            }

            void updateData() {
                AndroidNetworking.post("https://tekajeapunya.com/kelompok_1/update_reservasi.php")
                        .addBodyParameter("nama_lengkap", "" + nama_lengkap)
                        .addBodyParameter("treatment", "" + treatment)
                        .addBodyParameter("dokter", "" + dokter)
                        .addBodyParameter("tanggal", "" + tanggal)
                        .addBodyParameter("waktu", "" + waktu)
                        .addBodyParameter("nomor_antrian", "" + nomor_antrian)
                        .setPriority(Priority.MEDIUM)
                        .setTag("Menambah Nomor Antrian")
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                progressDialog.dismiss();
                                Log.d("cekUpdate", "" + response);
                                try {
                                    Boolean status = response.getBoolean("status");
                                    String pesan = response.getString("result");
                                    Toast.makeText(Verifikasi.this, "" + pesan, Toast.LENGTH_SHORT).show();
                                    Log.d("status", "" + status);

                                    if (status) {
                                        new AlertDialog.Builder(Verifikasi.this)
                                                .setMessage("Nomor Antrian Berhasil Ditambahkan")
                                                .setCancelable(false)
                                                .setPositiveButton("Kembali", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        Intent i = new Intent(Verifikasi.this, item_reservasi.class);
                                                        startActivity(i);
                                                        Verifikasi.this.finish();
                                                    }
                                                })
                                                .show();
                                    } else {
                                        new AlertDialog.Builder(Verifikasi.this)
                                                .setMessage("Gagal mengupdate data !")
                                                .setPositiveButton("Kembali", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        Intent i = getIntent();
                                                        setResult(RESULT_CANCELED, i);
                                                        Verifikasi.this.finish();
                                                    }
                                                })
                                                .setCancelable(false)
                                                .show();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(ANError anError) {
                                Log.d("Cannot update your data", "" + anError.getErrorBody());
                            }
                        });
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.delete,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.action_delete){
            new android.app.AlertDialog.Builder(Verifikasi.this)
                    .setMessage("Apakah kamu ingin menghapus data  "+nama_lengkap+"?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            progressDialog.setMessage("Menghapus...");
                            progressDialog.setCancelable(false);
                            progressDialog.show();

                            AndroidNetworking.post("https://tekajeapunya.com/kelompok_1/hapus_verifikasi.php")
                                    .addBodyParameter("nama_lengkap",""+nama_lengkap)
                                    .setPriority(Priority.MEDIUM)
                                    .build()
                                    .getAsJSONObject(new JSONObjectRequestListener() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            progressDialog.dismiss();
                                            try {
                                                Boolean status = response.getBoolean("status");
                                                Log.d("status",""+status);
                                                String result = response.getString("result");
                                                if(status){
                                                    new android.app.AlertDialog.Builder(Verifikasi.this)
                                                            .setMessage("Data telah dihapus!")
                                                            .setCancelable(false)
                                                            .setPositiveButton("Kembali", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    Intent i = new Intent(Verifikasi.this, item_reservasi.class);
                                                                    startActivity(i);
                                                                    Verifikasi.this.finish();
                                                                }
                                                            })
                                                            .show();

                                                }else{
                                                    Toast.makeText(Verifikasi.this, ""+result, Toast.LENGTH_SHORT).show();
                                                }
                                            }catch (Exception e){
                                                e.printStackTrace();
                                            }
                                        }

                                        @Override
                                        public void onError(ANError anError) {
                                            anError.printStackTrace();
                                        }
                                    });
                        }
                    })
                    .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .show();


        }
        return super.onOptionsItemSelected(item);
    }

}

