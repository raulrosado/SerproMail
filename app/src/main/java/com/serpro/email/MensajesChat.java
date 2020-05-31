package com.serpro.email;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.serpro.email.entidades.Contactos;
import com.serpro.email.entidades.MensajesEmails;
import com.serpro.email.utilidades.utilidades;

import java.io.IOException;
import java.security.AlgorithmConstraints;
import java.util.ArrayList;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMultipart;

public class MensajesChat extends AppCompatActivity implements View.OnClickListener {
    ImageView ivProfile,ivSend;
    ConexionSQLiteHelper conn;
    String nombre, email, password, mensajetxt, idEmailSelect, idNombreSelect,idCuenta;
    Integer idUserSelect;
    TextView nameUser,emailUser;
    EditText edText;
    RecyclerView recyclerMensajes;
    ArrayList<MensajesEmails> listaMensajes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensajes_chat);
        getSupportActionBar().hide();

        conn = new ConexionSQLiteHelper(getApplicationContext());//conexion  con la bd
        nameUser = findViewById(R.id.nameUser);
        emailUser = findViewById(R.id.emailUser);
        edText = findViewById(R.id.edText);

        recyclerMensajes = findViewById(R.id.recyclerMensajes);
        //PARA MOSTRAR LAS PROPAGANDAS
        recyclerMensajes.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));

        try {
            SharedPreferences preferencias = getSharedPreferences("usuarioactivo", Context.MODE_PRIVATE);
            idCuenta = preferencias.getString("idCuenta", "");
            nombre = preferencias.getString("Nombre","");
            email = preferencias.getString("Email","");
            password = preferencias.getString("Password","");
            SharedPreferences.Editor editor=preferencias.edit();
            nameUser.setText(nombre);

            Log.d("mailEmail",email);
        }catch (Exception e){}



        idUserSelect = getIntent().getExtras().getInt("idSelected");
        idEmailSelect = getIntent().getExtras().getString("idEmailSelect");
        idNombreSelect = getIntent().getExtras().getString("idNombreSelect");

        nameUser.setText(idNombreSelect);
        emailUser.setText(idEmailSelect);
        Toast.makeText(this, "select:" + idEmailSelect, Toast.LENGTH_SHORT).show();

        ivProfile = findViewById(R.id.ivProfile);
        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        ivSend = findViewById(R.id.ivSend);
        ivSend.setOnClickListener(this);

        //CARGO LOS MENSAJES
        loadMensajesUser(idEmailSelect);

    }

    private void loadMensajesUser(String idEmailSelect) {
        SQLiteDatabase db = conn.getReadableDatabase();   //se conecta a la db
        MensajesEmails mensajesEmails = null;
        listaMensajes = new ArrayList<MensajesEmails>();
        try {
            Cursor cursor = db.rawQuery("SELECT * FROM "+ utilidades.TABLA_MENSAJE +" WHERE " + utilidades.M_idCuenta + "= '"+email
                            +"' AND " + utilidades.M_idTo + "= '"+idEmailSelect+"' OR "+ utilidades.M_idCuenta +" = "+"'" + idEmailSelect +"' AND "+ utilidades.M_idTo + "= '"+email+"'" ,null);
            Log.d("mail","cantidad:" + cursor.getCount());
            Log.d("mailE", email +" y "+idEmailSelect);
            if(cursor.getCount() > 0){
                while (cursor.moveToNext()) {
                    mensajesEmails = new MensajesEmails();
                    mensajesEmails.setIdMensaje(cursor.getInt(0));
                    mensajesEmails.setIdCuenta(cursor.getString(1));
                    mensajesEmails.setIdTo(cursor.getString(2));
                    mensajesEmails.setMensaje(cursor.getString(3));
                    mensajesEmails.setEstado(cursor.getInt(4));
                    listaMensajes.add(mensajesEmails);
                    AdapterMensajes adapterMensajes = new AdapterMensajes(listaMensajes, getApplicationContext(),email);
                    adapterMensajes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.d("mail", "seleccionado:" + listaMensajes.get(recyclerMensajes.getChildAdapterPosition(v)).getIdMensaje());
//                            Intent intent = new Intent(getContext(), MensajesChat.class);
//                            intent.putExtra("idSelected", listaContactos.get(recyclerListaContactos.getChildAdapterPosition(v)).getIdContacto());
//                            intent.putExtra("idEmailSelect", listaContactos.get(recyclerListaContactos.getChildAdapterPosition(v)).getEmail());
//                            intent.putExtra("idNombreSelect", listaContactos.get(recyclerListaContactos.getChildAdapterPosition(v)).getNombre());
//                            startActivity(intent);
                        }
                    });
                    recyclerMensajes.setAdapter(adapterMensajes);
                }

            }else{
                Toast.makeText(getApplicationContext(), "No hay mensajes", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"Problema al buscar los mensajes",Toast.LENGTH_LONG).show();
        }

    }

    private void EnviarCorreo() {
        String sCorreo = idEmailSelect;
        String sAsunto = "SerproApiClient";
        String sMensaje = edText.getText().toString().trim();
        clsEnviaCorreo objCorreo = new clsEnviaCorreo(getApplicationContext(), sCorreo, sAsunto, sMensaje, email,password);
        objCorreo.execute();
        conn.addMensaje(email,idEmailSelect,sMensaje,"");
        edText.setText("");
        Funciones.loadEmailServer(getApplicationContext(),email,password);
        loadMensajesUser(idEmailSelect);
    }

    @Override
    public void onClick(View v) {
        EnviarCorreo();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

}
