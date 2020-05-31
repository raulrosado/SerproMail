package com.serpro.email;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.util.LocaleData;
import android.os.Bundle;
import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Function;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialDialogs;
import com.serpro.email.entidades.Contactos;
import com.serpro.email.entidades.MensajesEmails;
import com.serpro.email.utilidades.utilidades;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.FlagTerm;


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
    String nombre, email, password, mensajetxt;
    TextView textView;
    ConexionSQLiteHelper conn;
    ImageView scan;
    String idCuenta;
    RecyclerView listaChatsActivos;
    ArrayList<MensajesEmails> listaMensajes;
    ArrayList<Contactos> listaContactos;
    ArrayList<String> chatactivos = new ArrayList<String>();

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

        conn = new ConexionSQLiteHelper(getContext());//conexion  con la bd
        textView = vista.findViewById(R.id.textView);
        scan = vista.findViewById(R.id.scan);
        listaChatsActivos = vista.findViewById(R.id.listaChatsActivos);
        //PARA MOSTRAR LAS PROPAGANDAS
        listaChatsActivos.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));

        Contactos contactos = null;
        listaContactos = new ArrayList<Contactos>();

        try {
            SharedPreferences preferencias = this.getActivity().getSharedPreferences("usuarioactivo", Context.MODE_PRIVATE);
            idCuenta = preferencias.getString("idCuenta", "");
            nombre = preferencias.getString("Nombre","");
            email = preferencias.getString("Email","");
            password = preferencias.getString("Password","");
            SharedPreferences.Editor editor=preferencias.edit();

            textView.setText(email + "--"+ password);
        }catch (Exception e){}

        //CARGO LOS CORREOS
//        loadEmails();
        Funciones.loadEmailServer(getContext(),email,password);

        //cargo los char activos
        loadCharActivos(email);
        mostrarContacto(contactos);

        return vista;
    }


    private void mostrarContacto(Contactos contactos) {
        if(chatactivos.isEmpty()){
            Log.d("chatA", "vacio");
        }else {
            for (int i = 0; i < chatactivos.size(); i++) {
                Log.d("chatA", chatactivos.get(i) + " | ");
                loadContactos(chatactivos.get(i),contactos);
            }

            Log.d("chatAA", "cantidad de contactos a mostrar"+listaContactos.size());
            AdapterContactos adapterContactos = new AdapterContactos(listaContactos, getContext());
            adapterContactos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("mail", "seleccionado:" + listaContactos.get(listaChatsActivos.getChildAdapterPosition(v)).getIdContacto());
                    Intent intent = new Intent(getContext(), MensajesChat.class);
                    intent.putExtra("idSelected", listaContactos.get(listaChatsActivos.getChildAdapterPosition(v)).getIdContacto());
                    intent.putExtra("idEmailSelect", listaContactos.get(listaChatsActivos.getChildAdapterPosition(v)).getEmail());
                    intent.putExtra("idNombreSelect", listaContactos.get(listaChatsActivos.getChildAdapterPosition(v)).getNombre());
                    startActivity(intent);
                }
            });
            listaChatsActivos.setAdapter(adapterContactos);

        }

    }

    private void loadContactos(String emailCuenta,Contactos contactos) {
        SQLiteDatabase db = conn.getReadableDatabase();   //se conecta a la db
        try {
            Cursor cursor = db.rawQuery("SELECT * FROM "+ utilidades.TABLA_CONTACTOS +" WHERE "+ utilidades.Cont_email +" = '" + emailCuenta +"' ",null);
            if(cursor.getCount() > 0){
                while (cursor.moveToNext()) {
                    contactos = new Contactos();
                    contactos.setIdContacto(cursor.getInt(0));
                    contactos.setIdCuenta(cursor.getInt(1));
                    contactos.setNombre(cursor.getString(2));
                    contactos.setEmail(cursor.getString(3));
                    contactos.setEstado(cursor.getInt(4));
                    Log.d("chatR", cursor.getString(3));
                    listaContactos.add(contactos);
                }
            }else{
                Toast.makeText(getContext(), "No hay contactos", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Toast.makeText(getContext(),"Problema al buscar los contactos",Toast.LENGTH_LONG).show();
        }
    }
//
//    public void loadEmails(){
//        MailService mailService = new MailService();
//        try {
//            mailService.login("imap.nauta.cu",email, password);
//            Integer cantidad = mailService.getMessageCount();
//
//            textView.setText("cantidad de correos: "+ cantidad.toString());
//            Toast.makeText(getContext(), "cantidad de mensajes: "+ cantidad.toString(), Toast.LENGTH_SHORT).show();
//            Log.d("mail", "cargo los mensages: "+cantidad.toString());
//
//            Message[] mensajes = mailService.getMessages();
//
//            for (Message current : mensajes) {
//                String contentType = current.getContentType();
//
//                String a =  current.getSubject();
//                String b = "SerproApiClient";
//// son iguales
//                if (a.equalsIgnoreCase(b)) {
//                    System.out.println("lol message from: " + ((InternetAddress) current.getFrom()[0]).getAddress());
//                    System.out.println("lol titulo: " + current.getSubject());
//                    System.out.println("lol message " + getText(current));
//                    System.out.println("---------------------------------------------");
//                    System.out.println("a y b son iguales");
//
//                    conn.addMensaje(email,((InternetAddress) current.getFrom()[0]).getAddress(),getText(current),"");
//                }
//            }
////
////
//////                        Object content = current.getContent();
//////                        if (content instanceof MimeMultipart) {
//////                            System.out.println("lol <<<<<<<<<MULTIPART>>>>>>>>>");
//////                            for (int i = 0; i < ((MimeMultipart) content).getCount(); i++) {
//////
//////                                System.out.println("lol sub contentType "+((MimeMultipart) content).getBodyPart(i).getContentType());
//////                                System.out.println("lol " + getText(((MimeMultipart) content).getBodyPart(i)));
//////                                String c = getText(((MimeMultipart) content).getBodyPart(i));
//////                                System.out.println("lol message "+i+" " + c);
//////                            }
//////                        } else {
//////                            System.out.println("lol <<<<<<<<<<<NO MULTIPART>>>>>>>>>>");
//////                            System.out.println("lol message "+getText((Part)content));
//////                        }
////                    }
////
////                    for (int i=0;i<mensajes.length;i++)
////                    {
////
////                        Log.d("mail", "From:"+mensajes[i].getFrom()[0].toString());
////                        Log.d("mail", "Subject:"+mensajes[i].getSubject());
////
////                        if(mensajes[i].getFrom()[0].toString() == "raulrosado91@nauta.cu"){
////                            Log.d("mio", "estos mensajes son mios");
////                        }
////
////                        if(mensajes[i].getSubject()=="......"){
////                            Log.d("mail", "imprimio0ooooo");
////
////                        }
////                    }
//            mailService.logout();
//        }catch (Exception e){
//            Log.d("mail", "error: "+e);
//        }
//    }
//
//    private String getText(Part p) throws MessagingException, IOException {
//        if (p.isMimeType("text/*")) {
//            System.out.println("lol mime text/*");
//            String s = (String)p.getContent();
////            textIsHtml = p.isMimeType("text/html");
//            return s;
//        }
//
//        if (p.isMimeType("multipart/alternative")) {
//            // prefer html text over plain text
//            Multipart mp = (Multipart)p.getContent();
//            String text = null;
//            for (int i = 0; i < mp.getCount(); i++) {
//                Part bp = mp.getBodyPart(i);
//                if (bp.isMimeType("text/plain")) {
//                    if (text == null)
//                        text = getText(bp);
//                    continue;
//                } else if (bp.isMimeType("text/html")) {
//                    String s = getText(bp);
//                    if (s != null){
//                        return s;
//                    }
//                } else {
//                    return getText(bp);
//                }
//            }
//            return text;
//        } else if (p.isMimeType("multipart/*")) {
//            Multipart mp = (Multipart)p.getContent();
//            for (int i = 0; i < mp.getCount(); i++) {
//                String s = getText(mp.getBodyPart(i));
//                if (s != null)
//                    return s;
//            }
//        }
//        return null;
//    }
//
//    public String getContent(BodyPart bodyPart) throws IOException, MessagingException {
//
//        Object o = bodyPart.getContent();
//
//        if (o instanceof MimeMultipart){
//            System.out.println("lol bodypart is multipart ");
//            MimeMultipart mimeMultipart = (MimeMultipart) bodyPart.getContent();
//            for (int j = 0; j < mimeMultipart.getCount(); j++) {
//                getContent(mimeMultipart.getBodyPart(j));
//            }
//        }
//        else if (o instanceof String) {
//            System.out.println("lol content string");
//            return (String) o;
//        } else if (null != bodyPart.getDisposition() && bodyPart.getDisposition().equalsIgnoreCase(Part.ATTACHMENT)) {
//            System.out.println("lol content not string");
//            return "file "+bodyPart.getContent();
//        }
//
//        return null;
//    }

    public void loadCharActivos(String idemail){
        SQLiteDatabase db = conn.getReadableDatabase();   //se conecta a la db
        MensajesEmails mensajesEmails = null;
        listaMensajes = new ArrayList<MensajesEmails>();
        try {
            Cursor cursor = db.rawQuery("SELECT * FROM "+ utilidades.TABLA_MENSAJE +" WHERE " + utilidades.M_idCuenta + "= '"+email
                    +"' OR " + utilidades.M_idTo + "= '"+email+"'" ,null);
            if(cursor.getCount() > 0){
                while (cursor.moveToNext()) {
                    Log.d("chatO", idemail +"-------" + cursor.getString(2).toString());

                    String a =cursor.getString(2).toString();

                    if (a.equalsIgnoreCase(idemail)) {}else{
                        if (chatactivos.contains(cursor.getString(2).toString())) {
                        } else {
                            chatactivos.add(cursor.getString(2).toString());
                        }
                    }
                }
            }else{
                Toast.makeText(getContext(), "No hay mensajes en la bd", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Toast.makeText(getContext(),"Problema al buscar los mensajes",Toast.LENGTH_LONG).show();
        }
    }

}
