package coms.example.asus.doctor_appointment;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Registerhicantik extends AppCompatActivity {
    EditText Username, Email, Phone, Password, Repassword;
    Button BTNregister;
    TextView TVlogin;
    String username, email,phone, password, repassword;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registerhicantik);

        BTNregister = findViewById(R.id.btnloginuser);
        Username = findViewById(R.id.etuser);
        Email = findViewById(R.id.edtemailuser);
        Phone = findViewById(R.id.edtphone);
        Password = findViewById(R.id.passwordlogin);
        Repassword = findViewById(R.id.repassword1);
        TVlogin = findViewById(R.id.login);
        progressDialog = new ProgressDialog(this);

        TVlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Registerhicantik.this, LoginHicantik.class);
                startActivity(i);
            }
        });


        BTNregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = Username.getText().toString();
                String email = Email.getText().toString();
                String phone = Phone.getText().toString();
                String password = Password.getText().toString();
                String repassword = Repassword.getText().toString();
                Checklogin(username, email, phone, password, repassword);
            }
        });

    }

    public void Checklogin(final String username, String email, String phone, String password, String repassword) {
        if (checkNetworkConnection()) {
            progressDialog.setMessage("Mencoba masuk...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, DbContact.SERVER_REGISTER_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String resp = jsonObject.getString("server_response");
                                if (resp.equals("[{\"status\":\"OK\"}]")) {
                                    Toast.makeText(getApplicationContext(), "Register Berhasil", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(Registerhicantik.this, LoginHicantik.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(getApplicationContext(), "Try Again!", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("username", username);
                    params.put("email", email);
                    params.put("phone", phone);
                    params.put("password", password);
                    params.put("repassword", repassword);
                    return params;

                }
            };

            VolleyConnection.getInstance(Registerhicantik.this).addToRequestQue(stringRequest);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressDialog.cancel();
                }
            }, 1000);
        } else {
            Toast.makeText(getApplicationContext(), "Tidak Ada koneksi Internet", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean checkNetworkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());

    }
}
