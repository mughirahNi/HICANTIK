package coms.example.asus.doctor_appointment;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class reservasi extends AppCompatActivity {

    EditText ETnama_lengkap, ETreatment, ETdokter, ETtanggal, ETwaktu;
    String nama_lengkap, treatment, dokter, tanggal, waktu;
    Button BtnReservasi;
    DatePickerDialog datePickerDialog;
    SimpleDateFormat dateFormatter;
    AlertDialog.Builder builder;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservasi);

        ETnama_lengkap = findViewById(R.id.etnama);
        ETreatment = findViewById(R.id.edJenis);
        ETdokter = findViewById(R.id.etdok);
        ETtanggal = findViewById(R.id.edttanggal);
        ETwaktu = findViewById(R.id.edtwaktu);
        BtnReservasi = findViewById(R.id.Reservasi);
        builder = new AlertDialog.Builder(this);


        dateFormatter = new SimpleDateFormat("dd-MM-yyyy");

        ETtanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog();
            }
        });

        progressDialog = new ProgressDialog(this);
        BtnReservasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("Mengirim Data Anda...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                nama_lengkap = ETnama_lengkap.getText().toString();
                treatment = ETreatment.getText().toString();
                tanggal = ETtanggal.getText().toString();
                waktu = ETwaktu.getText().toString();
                dokter = ETdokter.getText().toString();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        validasiData();
                    }
                }, 1000);

            }
        });
    }

    private void showDateDialog() {
        Calendar calendar = Calendar.getInstance();

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayMonth);
                ETtanggal.setText(dateFormatter.format(newDate.getTime()));


            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();

    }

    void validasiData() {
        if (nama_lengkap.equals("") || treatment.equals("") || dokter.equals("") || tanggal.equals("") || waktu.equals("")) {
            progressDialog.dismiss();
            Toast.makeText(reservasi.this, "Periksa kembali data yang anda masukkan !", Toast.LENGTH_SHORT).show();
        } else {
            kirimData();
        }
    }

    private void kirimData() {
        AndroidNetworking.post("https://tekajeapunya.com/kelompok_1/reservasi.php")
                .addBodyParameter("nama_lengkap", "" + nama_lengkap)
                .addBodyParameter("treatment", "" + treatment)
                .addBodyParameter("dokter", "" + dokter)
                .addBodyParameter("tanggal", "" + tanggal)
                .addBodyParameter("waktu", "" + waktu)
                .setPriority(Priority.MEDIUM)
                .setTag("Mengirim Data Anda")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        Log.d("cekTambah", "" + response);
                        try {
                            Boolean status = response.getBoolean("status");
                            String pesan = response.getString("result");
                            Toast.makeText(reservasi.this, "" + pesan, Toast.LENGTH_SHORT).show();
                            Log.d("status", "" + status);
                            if (status) {
                                new AlertDialog.Builder(reservasi.this)
                                        .setMessage("Mohon Tunggu Konfirmasi Dari Klinik !")
                                        .setCancelable(false)
                                        .setPositiveButton("Kembali", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent page;
                                                page = new Intent(reservasi.this, Homehicantik.class);
                                                startActivity(page);
                                            }
                                        })
                                        .show();
                            } else {
                                new AlertDialog.Builder(reservasi.this)
                                        .setMessage("Gagal Menambahkan Data !")
                                        .setPositiveButton("Kembali", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                //Intent i = getIntent();
                                                //setResult(RESULT_CANCELED,i);
                                                //add_mahasiswa.this.finish();
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
                        Log.d("ErrorTambahData", "" + anError.getErrorBody());
                    }
                });
    }
}



