package com.serpro.email;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Person;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

public class FormAddPersonCuenta extends AppCompatActivity {
    ConexionSQLiteHelper conn;
    TextInputLayout lTextPassword;
    EditText edtnombre,edtemail,edtpassword;
    String nombre, email;
    String idCuenta;
    Button addNewPerson;
    Integer dvengo = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_add_person_cuenta);
        conn = new ConexionSQLiteHelper(getApplicationContext());//conexion  con la bd
        edtemail = findViewById(R.id.edtemail);
        edtnombre = findViewById(R.id.edtnombre);
        edtpassword = findViewById(R.id.edtpassword);
        addNewPerson = findViewById(R.id.addNewPerson);
        lTextPassword = findViewById(R.id.lTextPassword);

        if(getIntent().getExtras().getInt("dvengo") == 1){
            dvengo = 1;
            lTextPassword.setVisibility(View.VISIBLE);
        }

        try {
            SharedPreferences preferencias = getSharedPreferences("usuarioactivo", Context.MODE_PRIVATE);
            idCuenta = preferencias.getString("idCuenta", "");
            nombre = preferencias.getString("Nombre","");
            email = preferencias.getString("Email","");
            SharedPreferences.Editor editor=preferencias.edit();
        }catch (Exception e){}

        addNewPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dvengo == 1){
                    conn.addCuenta(getApplicationContext(),edtnombre.getText().toString(), edtemail.getText().toString(), edtpassword.getText().toString());
                    Toast.makeText(FormAddPersonCuenta.this, "Se agrego la cuenta", Toast.LENGTH_SHORT).show();

                }else{
                    if(idCuenta == null){
                        Toast.makeText(FormAddPersonCuenta.this, "Deve agregar una cuenta", Toast.LENGTH_SHORT).show();
                    }else {
                        conn.addContacto(idCuenta, edtnombre.getText().toString(), edtemail.getText().toString());
                        Toast.makeText(FormAddPersonCuenta.this, "Se agrego el contacto", Toast.LENGTH_SHORT).show();
                    }
                }

                Intent intent = null;

                switch (getIntent().getExtras().getInt("dvengo")){
                    case 1:
                        intent = new Intent(getApplicationContext(), AdministrarCuentas.class);
                        break;
                    case 0:
                        intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("dvengo", 2);
                        break;
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
                Intent intent = null;

                switch (getIntent().getExtras().getInt("dvengo")){
                    case 1:
                        intent = new Intent(getApplicationContext(), AdministrarCuentas.class);
                        break;
                    case 0:
                        intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("dvengo", 2);
                        break;
                }

                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(option_menu);
        }
    }
}
