package com.example.debtsettler_v3;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.debtsettler_v3.adapter.MembersAdapter;
import com.example.debtsettler_v3.model.Members;
import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.ColorPickerDialog;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    boolean shouldExecuteOnResume;

    RecyclerView membersRecyclerView;
    MembersAdapter membersAdapter;
    List<Members> membersList;
    ImageView addButton;
    ImageView historyButton;
    ImageView listButton;

    RequestQueue requestQueue;

    String EditBarva;

    String token;

    ArrayList<String> usersList = new ArrayList<>();
    ArrayList<String> zneskiList = new ArrayList<>();
    ArrayList<String> avtorBarve = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // NASTAVITEV NASLOVA V ACTION BARU:
        getSupportActionBar().setTitle("DebtSettler");

        // PRIDOBIVANJE TOKENA ZA AVTENTIKACIJO
        token = SharedPrefManager.getInstance(this).tokenValue();

        // PREVERJANJE ČE JE UPORABNIK PRIJAVLJEN ALI NE
        if(!SharedPrefManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(this, StartScreen.class));
        }

        shouldExecuteOnResume = false;

        membersRecyclerView = findViewById(R.id.memberRecycler);
        addButton = findViewById(R.id.addButton);
        historyButton = findViewById(R.id.historyButton);
        listButton = findViewById(R.id.listButton);
        membersList = new ArrayList<>();

        requestQueue = Volley.newRequestQueue(this);

    //============== dodajanje vseh gumbov v orodni vrstici =====================================================
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, DodajNakup.class);
                startActivity(i);
            }
        });

        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, zgodovina.class);
                startActivity(i);
            }
        });

        listButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, NakupovalnaLista.class);
                startActivity(i);
            }
        });
    //===============================================================================================================

        pridobiUporabnike();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(shouldExecuteOnResume){
            //Toast.makeText(getApplicationContext(), "RESUMEEEEEE!", Toast.LENGTH_SHORT).show();
            usersList.clear();
            zneskiList.clear();
            membersList.clear();
            avtorBarve.clear();
            pridobiUporabnike();
        } else{
            shouldExecuteOnResume = true;
        }

    }

    private void setMembersRecycler(List<Members> dataList) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        membersRecyclerView.setLayoutManager(layoutManager);
        membersAdapter = new MembersAdapter(this, dataList);
        membersRecyclerView.setAdapter(membersAdapter);
    }

    //======== Koda za klicanje API streznika  ======================================================================
    //     za pridobivanje podatkov o uporabnikih in njihovem stanju
    public void pridobiUporabnike() {
        String url = "https://debtsettler.herokuapp.com/api/users";

        JsonArrayRequest arrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Log.e("Rest response", response.toString());
                        Map<String, String> barveUpHashMap = new HashMap<String, String>();
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject obj = null;
                            try {
                                obj = response.getJSONObject(i);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                usersList.add(obj.getString("ime"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                zneskiList.add(obj.getString("stanjeDenarja"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                avtorBarve.add(obj.getString("barvaUporabnika"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                barveUpHashMap.put(obj.getString("_id"), obj.getString("barvaUporabnika"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        SharedPrefManager.getInstance(getApplicationContext())
                                .userColorsFill(
                                        barveUpHashMap
                                );

                        int vsota = 0;
                        for (int i = 0; i < zneskiList.size(); i++) {
                            vsota += Integer.parseInt(zneskiList.get(i));
                        }
                        double povprecje = (double) vsota / zneskiList.size();

                        for (int i = 0; i < usersList.size(); i++) {
                            double koncno = Math.round(Double.parseDouble(zneskiList.get(i)) - povprecje) / 100.0;
                            membersList.add(new Members(i, R.drawable.ic_baseline_person_24, usersList.get(i), koncno, avtorBarve.get(i)));
                        }
                        setMembersRecycler(membersList);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.buttonMenuOdjava:
                SharedPrefManager.getInstance(this).logout();
                finish();
                startActivity(new Intent(this, Registracija.class));
                break;

            case R.id.buttonMenuBarva:
                new ColorPickerDialog.Builder(MainActivity.this)
                        .setTitle("ColorPicker Dialog")
                        .setPreferenceName("MyColorPickerDialog")
                        .setPositiveButton("Potrdi",
                                new ColorEnvelopeListener() {
                                    @Override
                                    public void onColorSelected(ColorEnvelope envelope, boolean fromUser) {
                                        EditBarva = envelope.getHexCode();
                                        posodobiBarvo(EditBarva);
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
                break;
        }
        return true;
    }

    public void posodobiBarvo(String barva) {
        String url = "https://debtsettler.herokuapp.com/api/users/posodobibarvo";

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), "Barva uspešno posodobljena", Toast.LENGTH_LONG).show();
                        Log.i("Rest error", response.toString());
                        onResume();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("Rest error", error.toString());
                    }
                }){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("barvaUp", barva);

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

        //RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}
