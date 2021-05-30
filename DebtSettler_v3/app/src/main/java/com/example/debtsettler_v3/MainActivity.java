package com.example.debtsettler_v3;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.debtsettler_v3.adapter.MembersAdapter;
import com.example.debtsettler_v3.model.Members;

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

    ArrayList<String> usersList = new ArrayList<>();
    ArrayList<String> zneskiList = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // NASTAVITEV NASLOVA V ACTION BARU:
        getSupportActionBar().setTitle("DebtSettler");

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
            posodobiUporabnike();
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
                            membersList.add(new Members(i, R.drawable.person2, usersList.get(i), koncno));
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

    public void posodobiUporabnike() {
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
                            membersList.set(i, new Members(i, R.drawable.person2, usersList.get(i), koncno));
                        }
                        membersAdapter.notifyDataSetChanged();

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
        }
        return true;
    }

}
