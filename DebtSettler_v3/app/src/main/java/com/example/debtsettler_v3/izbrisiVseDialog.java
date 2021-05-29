package com.example.debtsettler_v3;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

public class izbrisiVseDialog extends AppCompatDialogFragment {

    RequestQueue requestQueue;

    String token;

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        requestQueue = Volley.newRequestQueue(getContext());

        // PRIDOBIVANJE TOKENA ZA AVTENTIKACIJO
        token = SharedPrefManager.getInstance(getActivity()).tokenValue();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Potrditev izbrisa")
                .setMessage("Ali res želite izbrisati izbrane izdelke iz nakupovalne liste?")
                .setPositiveButton("Da", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(context, "Izbrani izdelki:  \n" + data, Toast.LENGTH_SHORT).show();

                        String url = "https://debtsettler.herokuapp.com/api/seznam";

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


                        dialog.dismiss();
                        ((NakupovalnaLista) getActivity()).izbrisiVse();
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
}
