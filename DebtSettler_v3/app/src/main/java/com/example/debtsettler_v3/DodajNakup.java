package com.example.debtsettler_v3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.debtsettler_v3.model.Members;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class DodajNakup extends AppCompatActivity {

    Button dodajButton;
    Button prekliciButton;

    EditText trgovina;
    EditText opis;
    EditText znesek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //UREJANJE POGLEDA - transparentost ozadja, postavitev...
        setContentView(R.layout.activity_dodaj_nakup);

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
        dodajButton = findViewById(R.id.buttonDodaj);
        prekliciButton = findViewById(R.id.buttonPreklici);

        //TEKSTOVNA POLJA
        trgovina = findViewById(R.id.editTextImeTrgovine);
        opis = findViewById(R.id.editTextOpisNakupa);
        znesek = findViewById(R.id.editTextZnesekNakupa);

        prekliciButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        dodajButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String trgovinaStr = trgovina.getText().toString();
                String znesekStr = znesek.getText().toString();
                String opisStr = opis.getText().toString();

                String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI1ZmMyYjc4ZjRiZTI1NjM4NmM2Yjk2ZWQiLCJlbWFpbCI6Im1hcmsuYnJlem5pa0BnbWFpbC5jb20iLCJpbWUiOiJNYXJrIiwiZXhwIjozMTcxMjU1OTgwNzEsImlhdCI6MTYyMDQ4NDM3NH0.rsRtVofftjNEuau4CXZCp2Qcs3RC54E7vYjXhrNEA78";

                if (trgovinaStr.matches("") && znesekStr.matches("")) {
                    Toast.makeText(getApplicationContext(), getString(R.string.opozorilo_prazna_polja), Toast.LENGTH_SHORT).show();
                }
                else if (!znesekStr.matches("\\d+(.|,)\\d{0,2}")) {
                    Toast.makeText(getApplicationContext(), getString(R.string.opozorilo_napacen_znesek), Toast.LENGTH_SHORT).show();
                }
                else {
                    double d=Double.parseDouble(znesekStr);
                    String znesekCenti = String.valueOf(d*100);

                    // Koda za klicanje API streznika

                    String url = "https://debtsettler.herokuapp.com/api/dodajnakup";

                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

                    StringRequest stringRequest = new StringRequest(
                            Request.Method.POST,
                            url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.i("Rest response", getString(R.string.sporocilo_nakup_dodan));
                                    Toast.makeText(getApplicationContext(), getString(R.string.sporocilo_nakup_dodan), Toast.LENGTH_SHORT).show();
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
                                    params.put("imeTrgovine", trgovinaStr);
                                    params.put("opisNakupa", opisStr);
                                    params.put("cenaNakupa", znesekCenti);

                                    return params;
                                }

                                @Override
                                public Map<String, String> getHeaders() throws AuthFailureError {
                                    Map<String,String> params = new HashMap<String, String>();
                                    params.put("Content-Type","application/x-www-form-urlencoded");
                                    params.put("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI1ZmMyYjc4ZjRiZTI1NjM4NmM2Yjk2ZWQiLCJlbWFpbCI6Im1hcmsuYnJlem5pa0BnbWFpbC5jb20iLCJpbWUiOiJNYXJrIiwiZXhwIjozMTcxMjU1OTgwNzEsImlhdCI6MTYyMDQ4NDM3NH0.rsRtVofftjNEuau4CXZCp2Qcs3RC54E7vYjXhrNEA78");
                                    return params;
                                }
                            };
                    requestQueue.add(stringRequest);

                }
            }
        });

    }
}
