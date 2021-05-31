package com.example.debtsettler_v3;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.example.debtsettler_v3.model.Members;

import java.io.UnsupportedEncodingException;
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



        // PRIDOBIVANJE PODATKOV IZ PREJŠNJE AKTIVNOSTI (ID in imena članov)
        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("bundle");
        members = (ArrayList<Members>) args.getSerializable("members");


        // PRIDOBIVANJE TOKENA ZA AVTENTIKACIJO
        token = SharedPrefManager.getInstance(this).tokenValue();

        //PRIDOBI ID PRIJAVLJENEGA UPORABNIKA
        try {
            String[] split = token.split("\\.");
            idPrijav = getJson(split[1]).substring(8, 32);
        } catch (UnsupportedEncodingException e) {
            //Error
            Log.e("ID-from-token-error", e.toString());
        }

        int index = -1;
        for(int i=0; i<members.size(); i++) {

            if (members.get(i).get_Id().equals(idPrijav)) {
                index = i;
            }
        }
        if (index >= 0) {
            members.remove(index);
        }


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

        znesekNakazila.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    dodajNakaziloButton.performClick();
                    return true;
                }
                return false;
            }
        });

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

                String znesekNakStr = znesekNakazila.getText().toString().replace(',', '.');
                String opisNakStr = opisNakazila.getText().toString();


                if (opisNakStr.matches("") && znesekNakStr.matches("")) {
                    Toast.makeText(getApplicationContext(), getString(R.string.opozorilo_prazna_polja), Toast.LENGTH_SHORT).show();
                } else if (!znesekNakStr.matches("\\d+(.|,)\\d{0,2}")) {
                    Toast.makeText(getApplicationContext(), getString(R.string.opozorilo_napacen_znesek), Toast.LENGTH_SHORT).show();
                } else {
                    double d = Double.parseDouble(znesekNakStr);
                    String znesekCenti = String.valueOf((int)Math.round(d*100));


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
                            }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("idUporabnikaB", koncniPrejemnik.get_Id());
                            params.put("opisNakupa", opisNakStr);
                            params.put("cenaNakupa", znesekCenti);

                            return params;
                        }

                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("Content-Type", "application/x-www-form-urlencoded");
                            params.put("Authorization", "Bearer " + token);
                            return params;
                        }
                    };
                    requestQueue.add(stringRequest);
                }
            }
        });

    }

    private static String getJson(String strEncoded) throws UnsupportedEncodingException {
        byte[] decodedBytes = Base64.decode(strEncoded, Base64.URL_SAFE);
        return new String(decodedBytes, "UTF-8");
    }

}