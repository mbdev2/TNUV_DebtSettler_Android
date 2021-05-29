package com.example.debtsettler_v3;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.debtsettler_v3.adapter.ZgodovinaAdapter;
import com.example.debtsettler_v3.model.Nakupi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class zgodovina extends AppCompatActivity {

    RecyclerView zgodovinaRecyclerView;
    ZgodovinaAdapter zgodovinaAdapter;
    List<Nakupi> zgodovinaNakupovList;

    RequestQueue requestQueue;

    ArrayList<String> avtorList = new ArrayList<>();
    ArrayList<String> cenaList = new ArrayList<>();
    ArrayList<String> trgovinaList = new ArrayList<>();
    ArrayList<String> opisList = new ArrayList<>();
    ArrayList<String> datumList = new ArrayList<>();
    Map<String, String> barveUp = new HashMap();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zgodovina);

        // NASTAVITEV NASLOVA V ACTION BARU:
        getSupportActionBar().setTitle("Zgodovina nakupov");

        if(!SharedPrefManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(this, Registracija.class));
        }

        String token = SharedPrefManager.getInstance(this).tokenValue();
        barveUp= SharedPrefManager.getInstance(this).userColorsReturn();
        zgodovinaRecyclerView = findViewById(R.id.zgodovinaRecycler);
        zgodovinaNakupovList = new ArrayList<>();

        requestQueue = Volley.newRequestQueue(this);

        pridobiZgodovino();
    }

    private void setZgodovinaRecycler(List<Nakupi> dataList) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        zgodovinaRecyclerView.setLayoutManager(layoutManager);
        zgodovinaAdapter = new ZgodovinaAdapter(this, dataList);
        zgodovinaRecyclerView.setAdapter(zgodovinaAdapter);
    }

    public void pridobiZgodovino() {
        String url = "https://debtsettler.herokuapp.com/api/nakupi";
        JsonArrayRequest arrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Log.e("Rest response", response.toString());
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject obj = null;
                            try {
                                obj = response.getJSONObject(i);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                avtorList.add(obj.getString("avtor"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                cenaList.add(obj.getString("cenaNakupa"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                trgovinaList.add(obj.getString("imeTrgovine"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                opisList.add(obj.getString("opisNakupa"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                String datum = obj.getString("datumNakupa");
                                DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                                datumList.add(String.valueOf(df.parse(datum)));
                            } catch (JSONException | ParseException e) {
                                e.printStackTrace();
                            }
                        }


                        for (int i = avtorList.size()-1; i > 0; i--) {
                            zgodovinaNakupovList.add(new Nakupi(i, trgovinaList.get(i), opisList.get(i), datumList.get(i), cenaList.get(i), avtorList.get(i), barveUp.get(avtorList.get(i))));
                        }
                        setZgodovinaRecycler(zgodovinaNakupovList);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Rest error", error.toString());
                    }
                }
        );

        requestQueue.add(arrayRequest);
    }
}
