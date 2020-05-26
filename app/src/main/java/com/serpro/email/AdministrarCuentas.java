package com.serpro.email;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.google.android.material.dialog.MaterialDialogs;

public class AdministrarCuentas extends AppCompatActivity {
    ConexionSQLiteHelper conn;
    LinearLayout addNewCuenta;
    CardView cardNewCuenta;
    Button btnAdddCuenta;
    EditText edtnombre,edtemail,edtpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrar_cuentas);

        conn = new ConexionSQLiteHelper(getApplicationContext());//conexion  con la bd

        addNewCuenta = findViewById(R.id.addNewCuenta);
        cardNewCuenta = findViewById(R.id.cardNewCuenta);
        btnAdddCuenta = findViewById(R.id.btnAdddCuenta);
        edtnombre = findViewById(R.id.edtnombre);
        edtemail = findViewById(R.id.edtemail);
        edtpassword = findViewById(R.id.edtpassword);

        addNewCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("mail","el dialog");
                cardNewCuenta.setVisibility(View.VISIBLE);
            }
        });

        btnAdddCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!edtnombre.toString().isEmpty() || !edtemail.toString().isEmpty()|| !edtpassword.toString().isEmpty()) {
                    if (Funciones.isValidEmailAddress(edtemail.toString())) {
                        conn.addCuenta(edtnombre.toString(), edtemail.toString(), edtpassword.toString());
                        Toast.makeText(AdministrarCuentas.this, "Se agrego la cuenta", Toast.LENGTH_SHORT).show();
                    }
                }
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
                intent = new Intent(getApplicationContext(), Setting.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(option_menu);
        }
    }

}
