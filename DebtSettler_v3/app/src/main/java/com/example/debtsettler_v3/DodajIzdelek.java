package com.example.debtsettler_v3;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class DodajIzdelek extends AppCompatActivity {

    Button dodajIzdelek;
    Button prekliciIzdelek;

    EditText nazivIzdelka;
    EditText opisIzdelka;

    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // PRIDOBIVANJE TOKENA ZA AVTENTIKACIJO
        token = SharedPrefManager.getInstance(this).tokenValue();

        //UREJANJE POGLEDA - transparentost ozadja, postavitev...
        setContentView(R.layout.activity_dodaj_izdelek);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width), (int)(height*0.65));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.BOTTOM;
        params.x = 0;
        params.y = -20;

        getWindow().setAttributes(params);

        // GUMBI
        dodajIzdelek = findViewById(R.id.buttonDodajIzdelek);
        prekliciIzdelek = findViewById(R.id.buttonPrekliciIzdelek);

        // TEKSTOVNA POLJA
        nazivIzdelka = findViewById(R.id.editTextNazivIzdelka);
        opisIzdelka = findViewById(R.id.editTextOpisIzdelka);

        prekliciIzdelek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        dodajIzdelek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String izdelekStr = nazivIzdelka.getText().toString();
                String opisStr = opisIzdelka.getText().toString();


                if (izdelekStr.matches("")) {
                    Toast.makeText(getApplicationContext(), getString(R.string.opozorilo_prazna_polja), Toast.LENGTH_SHORT).show();
                }
                else {

                    // Koda za klicanje API streznika

                    String url = "https://debtsettler.herokuapp.com/api/seznam";

                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

                    StringRequest stringRequest = new StringRequest(
                            Request.Method.POST,
                            url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.i("Rest response", getString(R.string.sporocilo_izdelek_dodan));
                                    Toast.makeText(getApplicationContext(), getString(R.string.sporocilo_izdelek_dodan), Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e("Rest error", error.toString());
                                }
                            }){
                        @Override
                        protected Map<String,String> getParams(){
                            Map<String,String> params = new HashMap<String, String>();
                            params.put("naslov", izdelekStr);
                            if (opisStr.matches("")) {
                                params.put("opis", " ");
                            } else {
                                params.put("opis", opisStr);
                            }


                            return params;
                        }

                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String,String> params = new HashMap<String, String>();
                            params.put("Content-Type","application/x-www-form-urlencoded");
                            params.put("Authorization", "Bearer " + token);
                            return params;
                        }
                    };
                    requestQueue.add(stringRequest);

                }
            }
        });
    }
}
