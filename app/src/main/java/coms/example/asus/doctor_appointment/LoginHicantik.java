package coms.example.asus.doctor_appointment;

import static coms.example.asus.doctor_appointment.R.id.adm;
import static coms.example.asus.doctor_appointment.R.id.edtemail;
import static coms.example.asus.doctor_appointment.R.id.edtemailuser;

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

public class LoginHicantik extends AppCompatActivity {

    EditText Email, Password, Repassword;
    Button Login;
    ProgressDialog progressDialog;
    TextView Tvadmin;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_hicantik);
        //preferences
        sessionManager = new SessionManager(getApplicationContext());

        Login = findViewById(R.id.loginuser);
        Email = findViewById(R.id.edtemailuser);
        Password = findViewById(R.id.password);
        Repassword= findViewById(R.id.Repassword);
        progressDialog = new ProgressDialog(this);
        Tvadmin = findViewById(adm);

        Tvadmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginHicantik.this, LoginAdmin.class);
                startActivity(i);
            }
        });

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = Email.getText().toString();
                String password = Password.getText().toString();
                String repassword = Repassword.getText().toString();

                Checlogin(email, password, repassword);
            }
        });

    }

    public void Checlogin(String email, String password, String repassword) {
        if (checkNetworkConnection()) {
            progressDialog.setMessage("Mencoba masuk...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, DbContact.SERVER_LOGIN_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String resp = jsonObject.getString("server_response");
                                if (resp.equals("[{\"status\":\"OK\"}]")) {
                                    sessionManager.createSession(Email.getText().toString());

                                    Toast.makeText(getApplicationContext(), "Login Berhasil", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(LoginHicantik.this, Homehicantik.class);
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
                    params.put("email", email);
                    params.put("password", password);
                    params.put("repassword", repassword);
                    return params;

                }
            };

            VolleyConnection.getInstance(LoginHicantik.this).addToRequestQue(stringRequest);

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
