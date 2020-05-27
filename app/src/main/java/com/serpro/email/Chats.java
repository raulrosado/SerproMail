package com.serpro.email;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialDialogs;

import javax.mail.Message;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Chats#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Chats extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
//
    View vista;
    String nombre, email, password;
    TextView textView;
    Button button;



    public Chats() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Chats.
     */
    // TODO: Rename and change types and number of parameters
    public static Chats newInstance(String param1, String param2) {
        Chats fragment = new Chats();
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
        vista = inflater.inflate(R.layout.fragment_chats, container, false);
        textView = vista.findViewById(R.id.textView);
        button = vista.findViewById(R.id.button);


        try {
            SharedPreferences preferencias = this.getActivity().getSharedPreferences("usuarioactivo", Context.MODE_PRIVATE);
            nombre = preferencias.getString("Nombre","");
            email = preferencias.getString("Email","");
            password = preferencias.getString("Password","");
            SharedPreferences.Editor editor=preferencias.edit();

            textView.setText(email + "--"+ password);
        }catch (Exception e){}

        //CARGO LOS CORREOS
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MailService mailService = new MailService();
                try {

                    mailService.login("imap.nauta.cu",email, password);
                    Integer cantidad = mailService.getMessageCount();

                    textView.setText("cantidad de correos: "+ cantidad.toString());
                    Toast.makeText(getContext(), "cantidad de mensajes: "+ cantidad.toString(), Toast.LENGTH_SHORT).show();
                    Log.d("mail", "cargo los mensages: "+cantidad.toString());

                    Message[] mensajes = mailService.getMessages();
                    for (int i=0;i<mensajes.length;i++)
                    {
                        System.out.println("From:"+mensajes[i].getFrom()[0].toString());
                        System.out.println("Subject:"+mensajes[i].getSubject());

                        Log.d("mail", "From:"+mensajes[i].getFrom()[0].toString());
                        Log.d("mail", "Subject:"+mensajes[i].getSubject());
                    }

                    mailService.logout();
                }catch (Exception e){
                    Log.d("mail", "error: "+e);
                }

            }
        });


        return vista;
    }

}
