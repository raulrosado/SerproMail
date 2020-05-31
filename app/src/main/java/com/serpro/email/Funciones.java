package com.serpro.email;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.serpro.email.utilidades.utilidades;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimePart;

public class Funciones {


    /**
     *  Email Address Validation
     */
    public static boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     *  Hide Keyboard
     */
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    public  static void guardarSharedInformacion(Activity activity, String nombre, String email, String idCuenta)
    {
        SharedPreferences preferencias = activity.getSharedPreferences("usuarioactivo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferencias.edit();
        editor.putString("idCuenta", idCuenta);
        editor.putString("Nombre", nombre);
        editor.putString("Email", email);
        editor.commit();
    }
//
//    public static String loadServerConfig(Context context) {
//        ConexionSQLiteHelper conn;
//        //hago la coneccion con la BD
//        conn = new ConexionSQLiteHelper(context);
//
//        SQLiteDatabase db = conn.getReadableDatabase();   //se conecta a la db
//        String dev ="";
//
//        try {
//            Cursor cursor = db.rawQuery("SELECT * FROM "+ utilidades.TABLA_CONFIGURACION,null);
//            if(cursor.getCount() > 0){
//                while (cursor.moveToNext()) {
//                    dev = cursor.getString(1);
//                }
//            }else{
//               // Toast.makeText(context, "Esta vacia la bd", Toast.LENGTH_SHORT).show();
//            }
//
//        }catch (Exception e){
//            Toast.makeText(context,"Problema al buscar las configuraciones",Toast.LENGTH_LONG).show();
//        }
//        return dev;
//    }


    public static String loadFecha(){
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat sdf;
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }
    public static String loadHora(){
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat sdf;
        sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(date);
    }

//    public static void showToastSuccess(Activity context) {
//        LayoutInflater inflater = context.getLayoutInflater();
//        View layout = inflater.inflate(R.layout.recuadro_acept, (ViewGroup) context.findViewById(R.id.recuadroSuccess));
//        Toast toast = new Toast(context);
//        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
//        toast.setDuration(Toast.LENGTH_LONG);
//        toast.setView(layout);
//        toast.show();
//    }
//
//    public static void showToastError(Activity context) {
//        LayoutInflater inflater = context.getLayoutInflater();
//        View layout = inflater.inflate(R.layout.recuadro_error, (ViewGroup) context.findViewById(R.id.recuadroError));
//        Toast toast = new Toast(context);
//        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
//        toast.setDuration(Toast.LENGTH_LONG);
//        toast.setView(layout);
//        toast.show();
//    }

//    Folder folder = imapStore.getFolder("INBOX"); // get reference for inbox folder
//    UIDFolder uf = (UIDFolder)folder; // cast folder to UIDFolder interface
//    folder.open(Folder.READ_ONLY); // open folder
//    Message messages[] = folder.getMessages(); // get all messages
//    Long messageId = uf.getUID(messages[0]); // get message Id of first message in the inbox

    public  static void loadEmailServer(Context context, String email, String password){
        ConexionSQLiteHelper conn;
        //hago la coneccion con la BD
        conn = new ConexionSQLiteHelper(context);

        MailService mailService = new MailService();
        try {
            mailService.login("imap.nauta.cu",email, password);
            Integer cantidad = mailService.getMessageCount();
            Log.d("mail", "cargo los mensages: "+cantidad.toString());

            Message[] mensajes = mailService.getMessages();


  //          Session session = Session.getDefaultInstance(props, null);
 //           Store store = session.getStore("imaps");
  //          store.connect("imap.gmail.com", "myemail@gmail.com", "password");

 //           Folder inbox = store.getFolder("Inbox");
 //           inbox.open(Folder.READ_ONLY);
 //           Message messages[] = inbox.getMessages();

 //           for(Message message:messages) {
  //              MimeMessage im = new MimeMessage(session, message.getContent());
  //              im.getFrom();
  //              im.getMessageID();
   //         }
//              Session sessions =  mailService.getSession();

            for (Message current : mensajes) {

                Session De_Sesion = mailService.getSession();

                Log.d("polo", String.valueOf(De_Sesion));

                MimeBodyPart im = new MimeBodyPart();

                Long messageId = mailService.getUID_Folder().getUID(current); // get message Id of first message in the inbox

               // System.out.println("lol *****************"+ im.getContentID()+"" );

                System.out.println("lol ------------------------------------------------------------- " );
                System.out.println("lol UID: " + messageId );
                System.out.println("lol ------------------------------------------------------------- " );
                String contentType = current.getContentType();

             //   String aa = mailService.getUID(Message message)

                String a =  current.getSubject();
                String b = "SerproApiClient";
// son iguales
                if (a.equalsIgnoreCase(b)) {
                    System.out.println("lol message from: " + ((InternetAddress) current.getFrom()[0]).getAddress());
                    System.out.println("lol reciverdate: " + current.getReceivedDate().toString());
                   // System.out.println("lol header: " + current.getUID(current));
                    System.out.println("lol message " + Funciones.getText(current));
                    System.out.println("---------------------------------------------");
                    System.out.println("a y b son iguales");
                    conn.addMensaje(email,((InternetAddress) current.getFrom()[0]).getAddress(),Funciones.getText(current),"");
                }
            }
//
//
////                        Object content = current.getContent();
////                        if (content instanceof MimeMultipart) {
////                            System.out.println("lol <<<<<<<<<MULTIPART>>>>>>>>>");
////                            for (int i = 0; i < ((MimeMultipart) content).getCount(); i++) {
////
////                                System.out.println("lol sub contentType "+((MimeMultipart) content).getBodyPart(i).getContentType());
////                                System.out.println("lol " + getText(((MimeMultipart) content).getBodyPart(i)));
////                                String c = getText(((MimeMultipart) content).getBodyPart(i));
////                                System.out.println("lol message "+i+" " + c);
////                            }
////                        } else {
////                            System.out.println("lol <<<<<<<<<<<NO MULTIPART>>>>>>>>>>");
////                            System.out.println("lol message "+getText((Part)content));
////                        }
//                    }
//
//                    for (int i=0;i<mensajes.length;i++)
//                    {
//
//                        Log.d("mail", "From:"+mensajes[i].getFrom()[0].toString());
//                        Log.d("mail", "Subject:"+mensajes[i].getSubject());
//
//                        if(mensajes[i].getFrom()[0].toString() == "raulrosado91@nauta.cu"){
//                            Log.d("mio", "estos mensajes son mios");
//                        }
//
//                        if(mensajes[i].getSubject()=="......"){
//                            Log.d("mail", "imprimio0ooooo");
//
//                        }
//                    }
            mailService.logout();
        }catch (Exception e){
            Log.d("mail", "error: "+e);
        }
    }

    private static String getText(Part p) throws MessagingException, IOException {
        if (p.isMimeType("text/*")) {
            System.out.println("lol mime text/*");
            String s = (String)p.getContent();
//            textIsHtml = p.isMimeType("text/html");
            return s;
        }

        if (p.isMimeType("multipart/alternative")) {
            // prefer html text over plain text
            Multipart mp = (Multipart)p.getContent();
            String text = null;
            for (int i = 0; i < mp.getCount(); i++) {
                Part bp = mp.getBodyPart(i);
                if (bp.isMimeType("text/plain")) {
                    if (text == null)
                        text = getText(bp);
                    continue;
                } else if (bp.isMimeType("text/html")) {
                    String s = getText(bp);
                    if (s != null){
                        return s;
                    }
                } else {
                    return getText(bp);
                }
            }
            return text;
        } else if (p.isMimeType("multipart/*")) {
            Multipart mp = (Multipart)p.getContent();
            for (int i = 0; i < mp.getCount(); i++) {
                String s = getText(mp.getBodyPart(i));
                if (s != null)
                    return s;
            }
        }
        return null;
    }

    public static String getContent(BodyPart bodyPart) throws IOException, MessagingException {

        Object o = bodyPart.getContent();

        if (o instanceof MimeMultipart){
            System.out.println("lol bodypart is multipart ");
            MimeMultipart mimeMultipart = (MimeMultipart) bodyPart.getContent();
            for (int j = 0; j < mimeMultipart.getCount(); j++) {
                getContent(mimeMultipart.getBodyPart(j));
            }
        }
        else if (o instanceof String) {
            System.out.println("lol content string");
            return (String) o;
        } else if (null != bodyPart.getDisposition() && bodyPart.getDisposition().equalsIgnoreCase(Part.ATTACHMENT)) {
            System.out.println("lol content not string");
            return "file "+bodyPart.getContent();
        }

        return null;
    }

}
