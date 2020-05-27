package com.serpro.email;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.serpro.email.entidades.Cuentas;
import java.util.ArrayList;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

class AdapterCuentasAsistent extends RecyclerView.Adapter<AdapterCuentasAsistent.ViewHolderDatos> implements View.OnClickListener{
    List<Cuentas> listaCuentas;
    Context context;
    private View.OnClickListener listener;
    ConexionSQLiteHelper conn;      //conexion a la bd

    public AdapterCuentasAsistent(ArrayList<Cuentas> listaCuentas, Context context) {
        this.listaCuentas = listaCuentas;
        this.context = context;
        conn = new ConexionSQLiteHelper(context);
    }

    @NonNull
    @Override
    public ViewHolderDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listacuentasadmin,null,false);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(layoutParams);
        view.setOnClickListener(this);
        return new AdapterCuentasAsistent.ViewHolderDatos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolderDatos holder, final int position) {
        holder.nombreCuentas.setText(listaCuentas.get(position).getNombre());
        holder.emailCuentas.setText(listaCuentas.get(position).getEmail());

        if(listaCuentas.get(position).getEstado() == 1){
            holder.estadoCuentas.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return listaCuentas.size();
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

        TextView nombreCuentas,emailCuentas;
        TextView letraNombre;
        ImageView estadoCuentas;
        LinearLayout linearCuentas;

        public ViewHolderDatos(@NonNull View itemView) {
            super(itemView);
            nombreCuentas = (TextView) itemView.findViewById(R.id.nombreCuentas);
            emailCuentas = (TextView) itemView.findViewById(R.id.emailCuentas);

            letraNombre = (TextView) itemView.findViewById(R.id.letraNombre);
            linearCuentas = (LinearLayout) itemView.findViewById(R.id.linearCuentas);
            estadoCuentas = (ImageView) itemView.findViewById(R.id.estadoCuentas);
        }
    }
}
