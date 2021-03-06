package com.serpro.email;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Setting extends AppCompatActivity {
    LinearLayout addCuentas;
    TextView nombreUsuarioCuenta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        getSupportActionBar().setTitle("Configuración");
        nombreUsuarioCuenta = findViewById(R.id.nombreUsuarioCuenta);

        try {
            SharedPreferences preferencias=getSharedPreferences("usuarioactivo", Context.MODE_PRIVATE);
            String nombreusuario = preferencias.getString("Nombre","");
            SharedPreferences.Editor editor=preferencias.edit();
            nombreUsuarioCuenta.setText(nombreusuario);

        }catch (Exception e){}


        addCuentas = findViewById(R.id.addCuentas);
        addCuentas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AdministrarCuentas.class);
                startActivity(intent);
            }
        });

    }

    @Override public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        MenuItem itemR,itemS;
        itemR = menu.findItem(R.id.menu_icreturn);
        itemS = menu.findItem(R.id.menu_setting);
        itemR.setVisible(true);
        itemS.setVisible(false);
        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem option_menu) {
        switch (option_menu.getItemId()) {
            case R.id.menu_icreturn:
                Intent intent;
                intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(option_menu);
        }
    }
}
