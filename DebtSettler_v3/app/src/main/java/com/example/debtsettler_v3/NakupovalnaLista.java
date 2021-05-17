package com.example.debtsettler_v3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.debtsettler_v3.adapter.ItemsAdapter;
import com.example.debtsettler_v3.model.Items;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NakupovalnaLista extends AppCompatActivity {

    RecyclerView itemListRecyclerView;
    ItemsAdapter itemListAdapter;
    List<Items> itemList;
    ImageView addItemButton;
    ImageView deleteItemsButton;
    ImageView deleteAllButton;

    RequestQueue requestQueue;

    ArrayList<String> itemNameList = new ArrayList<>();
    ArrayList<String> itemDescriptionList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nakupovalna_lista);

        itemListRecyclerView = findViewById(R.id.itemListRecycler);
        addItemButton = findViewById(R.id.addItemButton);
        deleteItemsButton = findViewById(R.id.deleteItemButton);
        deleteAllButton = findViewById(R.id.deleteAllButton);
        itemList = new ArrayList<>();

        requestQueue = Volley.newRequestQueue(this);

        //============== dodajanje vseh gumbov v orodni vrstici =====================================================
        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "Jap tukaj sem", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(NakupovalnaLista.this, DodajIzdelek.class);
                startActivity(i);
            }
        });

        deleteItemsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        deleteAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //===============================================================================================================

        pridobiIzdelke();

    }


    private void setItemListRecycler(List<Items> dataList) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        itemListRecyclerView.setLayoutManager(layoutManager);
        itemListAdapter = new ItemsAdapter(this, dataList);
        itemListRecyclerView.setAdapter(itemListAdapter);
    }

    //======== Koda za klicanje API streznika  ======================================================================
    //     za pridobivanje podatkov o uporabnikih in njihovem stanju
    public void pridobiIzdelke() {
        String url = "https://debtsettler.herokuapp.com/api/seznam";

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
                                itemNameList.add(obj.getString("naslov"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                itemDescriptionList.add(obj.getString("opis"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        for (int i = 0; i < itemNameList.size(); i++) {
                            itemList.add(new Items(i, itemNameList.get(i), itemDescriptionList.get(i), false));
                        }
                        setItemListRecycler(itemList);
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