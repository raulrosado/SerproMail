package com.serpro.email;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.serpro.email.entidades.Contactos;
import com.serpro.email.entidades.Cuentas;
import com.serpro.email.utilidades.utilidades;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Personas#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Personas extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    ConexionSQLiteHelper conn;
    private String mParam1;
    private String mParam2;
    View vista;
    LinearLayout addNewContacto;
    RecyclerView recyclerListaContactos;
    ArrayList<Contactos> listaContactos;

    String nombre, email;
    Integer idCuenta;

    public Personas() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Personas.
     */
    // TODO: Rename and change types and number of parameters
    public static Personas newInstance(String param1, String param2) {
        Personas fragment = new Personas();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista = inflater.inflate(R.layout.fragment_personas, container, false);
        conn = new ConexionSQLiteHelper(getContext());//conexion  con la bd

        //VARIABLES GUARDADAS
        try {
            SharedPreferences preferencias = getActivity().getSharedPreferences("usuarioactivo", Context.MODE_PRIVATE);
            nombre = preferencias.getString("Nombre","");
            email = preferencias.getString("Email","");
            idCuenta = Integer.parseInt(preferencias.getString("idCuenta",""));
            SharedPreferences.Editor editor=preferencias.edit();
        }catch (Exception e){}

        recyclerListaContactos = vista.findViewById(R.id.recyclerListaContactos);
        //PARA MOSTRAR LAS PROPAGANDAS
        recyclerListaContactos = vista.findViewById(R.id.recyclerListaContactos);
        recyclerListaContactos.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));

        //cargo las cuentas
        loadContactos(idCuenta);

        addNewContacto = vista.findViewById(R.id.addNewContacto);
        addNewContacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), FormAddPersonCuenta.class);
                intent.putExtra("dvengo", 0);
                startActivity(intent);
            }
        });

        return vista;
    }

    private void loadContactos(Integer idCuenta) {
        SQLiteDatabase db = conn.getReadableDatabase();   //se conecta a la db
        Contactos contactos = null;
        listaContactos = new ArrayList<Contactos>();
        try {
            Cursor cursor = db.rawQuery("SELECT * FROM "+ utilidades.TABLA_CONTACTOS +" WHERE " + utilidades.Cont_idCuenta + "= "+idCuenta+" ",null);
            Log.d("mail","cantidad:" + cursor.getCount());
            if(cursor.getCount() > 0){
                while (cursor.moveToNext()) {
                    contactos = new Contactos();
                    contactos.setIdContacto(cursor.getInt(0));
                    contactos.setIdCuenta(cursor.getInt(1));
                    contactos.setNombre(cursor.getString(2));
                    contactos.setEmail(cursor.getString(3));
                    contactos.setEstado(cursor.getInt(4));
                    listaContactos.add(contactos);
                    AdapterContactos adapterContactos = new AdapterContactos(listaContactos, getContext());
                    adapterContactos.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.d("mail", "seleccionado:" + listaContactos.get(recyclerListaContactos.getChildAdapterPosition(v)).getIdContacto());
                            Intent intent = new Intent(getContext(), MensajesChat.class);
                            intent.putExtra("idSelected", listaContactos.get(recyclerListaContactos.getChildAdapterPosition(v)).getIdContacto());
                            intent.putExtra("idEmailSelect", listaContactos.get(recyclerListaContactos.getChildAdapterPosition(v)).getEmail());
                            intent.putExtra("idNombreSelect", listaContactos.get(recyclerListaContactos.getChildAdapterPosition(v)).getNombre());
                            startActivity(intent);
                        }
                    });
                    recyclerListaContactos.setAdapter(adapterContactos);
                }

            }else{
                Toast.makeText(getContext(), "No hay contactos", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Toast.makeText(getContext(),"Problema al buscar los contactos",Toast.LENGTH_LONG).show();
        }
    }
}
