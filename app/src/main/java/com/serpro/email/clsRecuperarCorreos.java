package com.serpro.email;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class clsRecuperarCorreos extends AsyncTask<Void,Void,Void> {
    private Context context;
    private String email,emailConf,passwordConf,Message_ID;
    private Integer ddonde;

    public clsRecuperarCorreos(Context context, String emailConf, String passwordConf,Integer ddonde) {
        this.context = context;
        this.email = email;
        this.emailConf = emailConf;
        this.passwordConf = passwordConf;
        this.Message_ID = Message_ID;
    }

//    @Override
    protected void onProgressUpdate() {
        Log.d("chat","trabajando");
    }

    @Override
    protected void onPreExecute() {
        Log.d("chat","anted de ejecutar");
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        Toast.makeText(context, "Tarea finalizada!",Toast.LENGTH_SHORT).show();
        Log.d("chat","se termino la ejecucion");
    }

    @Override
    protected void onCancelled() {
        Toast.makeText(context, "Tarea cancelada!",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        ConexionSQLiteHelper conn;
        //hago la coneccion con la BD
        conn = new ConexionSQLiteHelper(context);

        MailService mailService = new MailService();
        try {
            mailService.login("imap.nauta.cu",emailConf, passwordConf);
            Integer cantidad = mailService.getMessageCount();
            Log.d("mail", "cargo los mensages: "+cantidad.toString());

            Message[] mensajes = mailService.getMessages();

            for (Message current : mensajes) {
                MimeBodyPart im = new MimeBodyPart();

                Long messageId = mailService.getUID_Folder().getUID(current); // get message Id of first message in the inbox
                // System.out.println("lol *****************"+ im.getContentID()+"" );
                String Message_Date    = substringBetween( Arrays.toString(current.getHeader("Date")) , "[","]");  ;
                String Message_ID      = substringBetween( Arrays.toString(current.getHeader("Message-ID")) , "[" ,  "]");
                String Message_From    = substringBetween( Arrays.toString(current.getHeader("From")) , "[" ,  "]");
                String Message_To      = substringBetween( Arrays.toString(current.getHeader("To")) , "[" ,  "]");
                String Message_Subject = substringBetween( Arrays.toString(current.getHeader("Subject")), "[" ,  "]");
                String Message_Content_Type = substringBetween( Arrays.toString(current.getHeader("Content-Type")) , "[" ,  "]");
                String Message_Text = Funciones.getText(current);
                //     User-Agent  : SerproApiClient
                //  String Message_UA = Arrays.toString(current.getHeader("User-Agent")) ;

                System.out.println("------------------------------------------------------------- " );
                System.out.println("1 Message_Date" + Message_Date);
                System.out.println("2 Message_ID:  " + Message_ID);
                System.out.println("3 Message_From: " + Message_From);
                System.out.println("4 Message_To: " + Message_To );
                System.out.println("5 Message_Subject: " + Message_Subject);
                System.out.println("6 Message_Content_Type: " + Message_Content_Type);
                System.out.println("7 Message_Text: " + Message_Text);
                System.out.println("------------------------------------------------------------- " );


                // System.out.println("lol *****************"+ im.getContentID()+"" );

                System.out.println("lol ------------------------------------------------------------- " );
                System.out.println("lol UID: " + messageId );
                System.out.println("lol ------------------------------------------------------------- " );
                String contentType = current.getContentType();

                //   String aa = mailService.getUID(Message message)
                String a =  current.getSubject();
                String b = "SerproApiClient";

                String reciverdate;
// son iguales
                if (a.equalsIgnoreCase(b)) {
                    System.out.println("lol message from: " + ((InternetAddress) current.getFrom()[0]).getAddress());
                    System.out.println("lol reciverdate: " + current.getSentDate().toString());
                    // System.out.println("lol header: " + current.getUID(current));
                    System.out.println("lol message " + Funciones.getText(current));
                    System.out.println("---------------------------------------------");
                    System.out.println("a y b son iguales");

                    reciverdate = current.getSentDate().toString();
                    conn.addMensaje(emailConf,((InternetAddress) current.getFrom()[0]).getAddress(),Funciones.getText(current),"", Message_ID,ddonde);
                }
            }
            mailService.logout();
        }catch (Exception e){
            Log.d("mail", "error: "+e);
        }

        return null;
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

    public static String substringBetween(String str, String open,  String close) {
        if (str == null || open == null || close == null) {
            return null;
        }

        int start = str.indexOf(open);
        if (start != -1) {
            int end = str.indexOf(close, start + open.length());
            if (end != -1) {
                return str.substring(start + open.length(), end);
            } else {

            }
        }
        return null;
    }

}