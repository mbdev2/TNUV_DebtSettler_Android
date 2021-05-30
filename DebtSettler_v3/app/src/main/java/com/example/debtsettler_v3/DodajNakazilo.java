package com.example.debtsettler_v3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.debtsettler_v3.model.Members;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DodajNakazilo extends AppCompatActivity {

    Button dodajNakaziloButton;
    Button prekliciNakaziloButton;

    Spinner prejemnik;
    EditText opisNakazila;
    EditText znesekNakazila;

    ArrayList<Members> members;

    String token;

    RequestQueue requestQueue;

    String idPrijav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dodaj_nakazilo);

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        //PRIDOBI ID PRIJAVLJENEGA UPORABNIKA
        idPrijavljenega();

        // PRIDOBIVANJE PODATKOV IZ PREJŠNJE AKTIVNOSTI (ID in imena članov)
        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("bundle");
        members = (ArrayList<Members>) args.getSerializable("members");

        for(int i=0; i<members.size(); i++) {
            if (members.get(i).getId().toString() == idPrijav) {
                members.remove(i);
            }
        }
        Log.e("Tukaj:", String.valueOf(members.size()));


        // PRIDOBIVANJE TOKENA ZA AVTENTIKACIJO
        token = SharedPrefManager.getInstance(this).tokenValue();

        //UREJANJE POGLEDA - transparentost ozadja, postavitev...

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width), (int)(height*0.75));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.BOTTOM;
        params.x = 0;
        params.y = -20;

        getWindow().setAttributes(params);

        // GUMBI
        dodajNakaziloButton = findViewById(R.id.potrdiNakaziloButton);
        prekliciNakaziloButton = findViewById(R.id.prekliciNakaziloButton);

        //TEKSTOVNA POLJA
        opisNakazila = findViewById(R.id.editTextOpisNakazila);
        znesekNakazila = findViewById(R.id.editTextZnesekNakazila);

        // SPINNER ZA IZBIRO PREJEMNIKA
        prejemnik = findViewById(R.id.spinnerMembers);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<Members> adapter = new ArrayAdapter<Members>(this, android.R.layout.simple_spinner_dropdown_item, members);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        prejemnik.setAdapter(adapter);

        prekliciNakaziloButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        dodajNakaziloButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Members koncniPrejemnik = (Members) prejemnik.getSelectedItem();
                //Log.e("Izbrani prejemniK:", koncniPrejemnik.getName());

                String znesekNakStr = znesekNakazila.getText().toString();
                String opisNakStr = opisNakazila.getText().toString();


                // Koda za klicanje API streznika
                String url2 = "https://debtsettler.herokuapp.com/api/poravnavadolga";

                StringRequest stringRequest = new StringRequest(
                        Request.Method.POST,
                        url2,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.i("Rest response", getString(R.string.nakazilo_uspesno));
                                Toast.makeText(getApplicationContext(), getString(R.string.nakazilo_uspesno), Toast.LENGTH_SHORT).show();
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
                        params.put("idUporabnikaB", koncniPrejemnik.getId().toString());
                        params.put("opisNakupa", opisNakStr);
                        params.put("cenaNakupa", znesekNakStr);

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
        });

    }


    public void idPrijavljenega() {
        String url = "https://debtsettler.herokuapp.com/api/users/pridobiid";
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("Rest response", "id prijavljenega pridobljen.");
                        idPrijav = response;
                        Log.e("Id prijavljenega:", idPrijav);
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Rest error", error.toString());
                        Log.e("TU SE MI ZATAKNE", "ZEP");
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                params.put("Authorization", "Bearer "+token);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}