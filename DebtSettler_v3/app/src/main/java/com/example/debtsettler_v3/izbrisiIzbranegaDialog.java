package com.example.debtsettler_v3;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.android.volley.AuthFailureError;
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
import java.util.HashMap;
import java.util.Map;

public class izbrisiIzbranegaDialog extends AppCompatDialogFragment {

    ArrayList<String> data;
    Context context;

    RequestQueue requestQueue;

    String token;

    public izbrisiIzbranegaDialog(Context context, ArrayList<String>  data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        requestQueue = Volley.newRequestQueue(context);

        // PRIDOBIVANJE TOKENA ZA AVTENTIKACIJO
        token = SharedPrefManager.getInstance(getActivity()).tokenValue();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Potrditev izbrisa")
                .setMessage("Ali res želite izbrisati izbrane izdelke iz nakupovalne liste?")
                .setPositiveButton("Da", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(context, "Izbrani izdelki:  \n" + data, Toast.LENGTH_SHORT).show();

                        for (int i=0; i<data.size(); i++) {
                            izbrisiIzdelek(data.get(i));
                            //Log.e("Indeksi za izbris:", data.get(i));
                        }
                        dialog.dismiss();
                        ((NakupovalnaLista) getActivity()).izbrisiIzbrane();
                    }
                })
                .setNegativeButton("Ne", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        return builder.create();
    }


    public void izbrisiIzdelek(String idIzdelka) {
        String url = "https://debtsettler.herokuapp.com/api/seznam/" + idIzdelka;

        JsonArrayRequest arrayRequest = new JsonArrayRequest(
                Request.Method.DELETE,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i("Rest response:", response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Rest error", error.toString());
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                params.put("Authorization", "Bearer " + token);
                return params;
            }
        };

        requestQueue.add(arrayRequest);
    }

}
