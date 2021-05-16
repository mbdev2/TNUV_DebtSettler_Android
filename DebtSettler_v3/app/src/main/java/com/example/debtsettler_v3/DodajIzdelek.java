package com.example.debtsettler_v3;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DodajIzdelek extends AppCompatActivity {

    Button dodajIzdelek;
    Button prekliciIzdelek;

    EditText nazivIzdelka;
    EditText opisIzdelka;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //UREJANJE POGLEDA - transparentost ozadja, postavitev...
        setContentView(R.layout.activity_dodaj_izdelek);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width), (int)(height*0.4));

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
                Toast.makeText(getApplicationContext(), "DELAAAAAAAAA", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
