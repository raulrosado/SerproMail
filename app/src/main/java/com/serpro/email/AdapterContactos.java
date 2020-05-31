package com.serpro.email;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.serpro.email.entidades.Contactos;
import com.serpro.email.entidades.Cuentas;

import java.util.ArrayList;
import java.util.List;

class AdapterContactos extends RecyclerView.Adapter<AdapterContactos.ViewHolderDatos> implements View.OnClickListener{
    List<Contactos> listaContactos;
    Context context;
    private View.OnClickListener listener;
    ConexionSQLiteHelper conn;      //conexion a la bd

    public AdapterContactos(ArrayList<Contactos> listaContactos, Context context) {
        this.listaContactos = listaContactos;
        this.context = context;
        conn = new ConexionSQLiteHelper(context);
    }

    @NonNull
    @Override
    public ViewHolderDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listacontactos,null,false);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(layoutParams);
        view.setOnClickListener(this);
        return new AdapterContactos.ViewHolderDatos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolderDatos holder, final int position) {
        holder.nombreCuentas.setText(listaContactos.get(position).getNombre());
//
//        holder.linearCuentas.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("mail", "seleccionado:" + listaContactos.get(position).getIdContacto());
//            }
//        });
//
//        holder.linearCuentas.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                holder.estadoCuentas.setVisibility(View.VISIBLE);
//                return false;
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return listaContactos.size();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if(listener != null){
            listener.onClick(view);
        }
    }

    public class ViewHolderDatos extends RecyclerView.ViewHolder {

        TextView nombreCuentas;
        TextView letraNombre;
        ImageView estadoCuentas;
        LinearLayout linearCuentas;

        public ViewHolderDatos(@NonNull View itemView) {
            super(itemView);
            nombreCuentas = (TextView) itemView.findViewById(R.id.nombreCuentas);
            letraNombre = (TextView) itemView.findViewById(R.id.letraNombre);
            linearCuentas = (LinearLayout) itemView.findViewById(R.id.linearCuentas);
            estadoCuentas = (ImageView) itemView.findViewById(R.id.estadoCuentas);
        }
    }
}
