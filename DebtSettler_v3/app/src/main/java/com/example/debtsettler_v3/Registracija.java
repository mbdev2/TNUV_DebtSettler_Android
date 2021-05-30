package com.example.debtsettler_v3;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.ColorPickerDialog;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Registracija extends AppCompatActivity implements View.OnClickListener {
    private EditText editTextIme;
    private EditText editTextEmail;
    private EditText editTextGeslo;
    private String editTextBarva;
    private Button buttonRegister;
    private ProgressDialog progressDialog;
    private TextView textViewLogin;
    View colorPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registracija);

        // NASTAVITEV NASLOVA in BACK BUTTONA V ACTION BARU:
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Registrirajte se");

        if(SharedPrefManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(this, MainActivity.class));
            return;
        }


        editTextIme=(EditText) findViewById(R.id.editTextIme);
        editTextEmail=(EditText) findViewById(R.id.editTextEmail);
        editTextGeslo=(EditText) findViewById(R.id.editTextGeslo);
        textViewLogin = (TextView) findViewById(R.id.textViewLogin);

        buttonRegister= (Button) findViewById(R.id.buttonRegister);

        progressDialog = new ProgressDialog(this);

        buttonRegister.setOnClickListener(this);
        textViewLogin.setOnClickListener(this);


        // COLOR PICKER
        colorPreview = findViewById(R.id.colorPreview);
        colorPreview.setBackgroundColor(getResources().getColor(R.color.ButtonBlue));
        colorPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ColorPickerDialog.Builder(Registracija.this)
                        .setTitle("ColorPicker Dialog")
                        .setPreferenceName("MyColorPickerDialog")
                        .setPositiveButton("Potrdi",
                                new ColorEnvelopeListener() {
                                    @Override
                                    public void onColorSelected(ColorEnvelope envelope, boolean fromUser) {
                                        colorPreview.setBackgroundColor(envelope.getColor());
                                        editTextBarva = envelope.getHexCode();
                                    }
                                })
                        .setNegativeButton("Prekliči",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                })
                        .attachAlphaSlideBar(true) // the default value is true.
                        .attachBrightnessSlideBar(true)  // the default value is true.
                        .setBottomSpace(12) // set a bottom space between the last slidebar and buttons.
                        .show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    private void registerUser(){
        String ime = editTextIme.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String geslo = editTextGeslo.getText().toString().trim();

        progressDialog.setMessage("Registracija uporabnika poteka...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            SharedPrefManager.getInstance(getApplicationContext())
                                    .userLogin(
                                            jsonObject.getString("eTVtoken")
                                    );
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        } catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.hide();
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("Rest error", error.toString());
                    }
                }){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError{
                Map<String,String> params = new HashMap<String, String>();
                params.put("ime", ime);
                params.put("email", email);
                params.put("geslo", geslo);
                params.put("barvaUporabnika", editTextBarva);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onClick(View view){
        if (view == buttonRegister){
            registerUser();
        }
        if (view == textViewLogin){
            startActivity(new Intent(this, Login.class));
        }
    }
}
