package coms.example.asus.doctor_appointment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONObject;

public class Saran extends AppCompatActivity {

    EditText ETNama,ETemail, ETsaran;
    String nama,email,saran;
    Button BTNSubmit;
    ProgressDialog progressDialog;
    ImageView img_kembali;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saran);

        ETNama = findViewById(R.id.etnama);
        ETemail = findViewById(R.id.edtemail);
        ETsaran = findViewById(R.id.etsrn);
        BTNSubmit = findViewById(R.id.btnsrn);
        img_kembali = findViewById(R.id.bck_saran);

        img_kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Saran.this, Homehicantik.class);
                startActivity(intent);
            }
        });
        progressDialog = new ProgressDialog(this);
        BTNSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("Mengirim saran Anda...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                nama = ETNama.getText().toString();
                email = ETemail.getText().toString();
                saran = ETsaran.getText().toString();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() { validasiData();}
                }, 1000);
            }
        });


    }void validasiData(){
        if(nama.equals("") || email.equals("") || saran.equals("")){
            progressDialog.dismiss();
            Toast.makeText(Saran.this, "Periksa kembali data yang anda masukkan !", Toast.LENGTH_SHORT).show();
        }else {
            kirimData();
        }
    }
    void kirimData(){
        AndroidNetworking.post("https://tekajeapunya.com/kelompok_1/hicantik/addsaran.php")
                .addBodyParameter("nama",""+nama)
                .addBodyParameter("email",""+email)
                .addBodyParameter("saran",""+saran)
                .setPriority(Priority.MEDIUM)
                .setTag("Mengirim Saran")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        Log.d("cekTambah",""+response);
                        try {
                            Boolean status = response.getBoolean("status");
                            String pesan = response.getString("result");
                            Toast.makeText(Saran.this, ""+pesan, Toast.LENGTH_SHORT).show();
                            Log.d("status",""+status);
                            if(status){
                                new AlertDialog.Builder(Saran.this)
                                        .setMessage("Pesan Anda Terkirim !")
                                        .setCancelable(false)
                                        .setPositiveButton("Kembali", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent page;
                                                page = new Intent(Saran.this,Homehicantik.class);
                                                startActivity(page);
                                            }
                                        })
                                        .show();
                            }
                            else{
                                new AlertDialog.Builder(Saran.this)
                                        .setMessage("Gagal Menambahkan Saran !")
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
                        }catch (Exception e){
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("ErrorTambahData",""+anError.getErrorBody());
                    }
                });
    }
}





