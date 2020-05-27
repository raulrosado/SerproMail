package com.serpro.email;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import com.serpro.email.entidades.Cuentas;
import com.serpro.email.utilidades.utilidades;

import java.util.ArrayList;

public class AdministrarCuentas extends AppCompatActivity {
    ConexionSQLiteHelper conn;
    LinearLayout addNewCuenta;
    CardView cardNewCuenta;
    Button btnAdddCuenta;
    EditText edtnombre,edtemail,edtpassword;
    RecyclerView recyclerListaCuentas;
    ArrayList<Cuentas> lista;

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
        recyclerListaCuentas = findViewById(R.id.recyclerListaCuentas);

        //PARA MOSTRAR LAS PROPAGANDAS
        recyclerListaCuentas = (RecyclerView)findViewById(R.id.recyclerListaCuentas);
        recyclerListaCuentas.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        //cargo las cuentas
        loadCuentas();

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
                conn.addCuenta(edtnombre.getText().toString(), edtemail.getText().toString(), edtpassword.getText().toString());
                Toast.makeText(AdministrarCuentas.this, "Se agrego la cuenta", Toast.LENGTH_SHORT).show();
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

    private void loadCuentas() {
        SQLiteDatabase db = conn.getReadableDatabase();   //se conecta a la db
        Cuentas cuentas = null;
        lista = new ArrayList<Cuentas>();
        try {
            Cursor cursor = db.rawQuery("SELECT * FROM "+ utilidades.TABLA_CUENTAS,null);
            Log.d("mail","cantidad:" + cursor.getCount());
            if(cursor.getCount() > 0){
                while (cursor.moveToNext()) {
                    cuentas = new Cuentas();
                    cuentas.setIdCuenta(cursor.getInt(0));
                    cuentas.setNombre(cursor.getString(1));
                    cuentas.setEmail(cursor.getString(2));
                    cuentas.setPassword(cursor.getString(3));
                    cuentas.setEstado(cursor.getInt(4));
                    lista.add(cuentas);
                    AdapterCuentasAsistent adapterCuentasAsistent = new AdapterCuentasAsistent(lista, getApplicationContext());
                    adapterCuentasAsistent.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            conn.selectCuenta(lista.get(recyclerListaCuentas.getChildAdapterPosition(v)).getIdCuenta());
                            Toast.makeText(AdministrarCuentas.this, "seleccionado:" + lista.get(recyclerListaCuentas.getChildAdapterPosition(v)).getIdCuenta() , Toast.LENGTH_SHORT).show();
                            Log.d("mail","seleccionado:" + lista.get(recyclerListaCuentas.getChildAdapterPosition(v)).getIdCuenta() );

                            SharedPreferences preferencias = getSharedPreferences("usuarioactivo", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferencias.edit();
                            editor.putString("idCuenta", lista.get(recyclerListaCuentas.getChildAdapterPosition(v)).getIdCuenta().toString());
                            editor.putString("Nombre", lista.get(recyclerListaCuentas.getChildAdapterPosition(v)).getNombre());
                            editor.putString("Email", lista.get(recyclerListaCuentas.getChildAdapterPosition(v)).getEmail());
                            editor.putString("Password", lista.get(recyclerListaCuentas.getChildAdapterPosition(v)).getPassword());
                            editor.commit();


                            loadCuentas();

                        }
                    });
                    recyclerListaCuentas.setAdapter(adapterCuentasAsistent);
                }

            }else{
                Toast.makeText(this, "No hay cuentas", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"Problema al buscar las cuentas",Toast.LENGTH_LONG).show();
        }
    }

}
