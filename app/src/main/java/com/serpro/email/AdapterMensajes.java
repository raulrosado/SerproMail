package com.serpro.email;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.serpro.email.entidades.Contactos;
import com.serpro.email.entidades.MensajesEmails;

import java.util.ArrayList;
import java.util.List;

class AdapterMensajes extends RecyclerView.Adapter<AdapterMensajes.ViewHolderDatos> implements View.OnClickListener{
    List<MensajesEmails> listaMensajes;
    Context context;
    private View.OnClickListener listener;
    ConexionSQLiteHelper conn;      //conexion a la bd
    String idCuentamia;

    public AdapterMensajes(ArrayList<MensajesEmails> listaMensajes, Context context, String idCuenta) {
        this.listaMensajes = listaMensajes;
        this.context = context;
        conn = new ConexionSQLiteHelper(context);
        idCuentamia = idCuenta;
    }

    @NonNull
    @Override
    public ViewHolderDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chatyo,null,false);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(layoutParams);
        view.setOnClickListener(this);
        return new AdapterMensajes.ViewHolderDatos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolderDatos holder, final int position) {
        holder.mensajeyo.setText(listaMensajes.get(position).getMensaje());
        String a = listaMensajes.get(position).getIdCuenta();
        if (a.equalsIgnoreCase(idCuentamia)) {
            holder.mensajeyo.setBackgroundResource(R.drawable.radius_blue_chat);
        }else{
            holder.mensajeyo.setBackgroundResource(R.drawable.radius_space_chat);
            holder.mContenedor.setGravity(View.LAYOUT_DIRECTION_LTR);
        }

        conn.changeMensajesStatus(listaMensajes.get(position).getIdMensaje());
//
//        holder.linearCuentas.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("mail", "seleccionado:" + listaMensajes.get(position).getIdContacto());
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
        return listaMensajes.size();
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

        TextView mensajeyo;
        RelativeLayout mContenedor;

        public ViewHolderDatos(@NonNull View itemView) {
            super(itemView);
            mensajeyo = (TextView) itemView.findViewById(R.id.mensajeyo);
            mContenedor = itemView.findViewById(R.id.mContenedor);
        }
    }
}
