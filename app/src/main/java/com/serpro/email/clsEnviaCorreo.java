package com.serpro.email;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class clsEnviaCorreo extends AsyncTask<Void,Void,Void> {
    private Context contexto;
    private Session De_Sesion;

    private String A_Correo;
    private String A_Asunto;
    private String A_Mensaje;
    private String emailCuentaSelect;
    private String passwordCuentaSelect;

    private ProgressDialog progreso;

    public clsEnviaCorreo(Context cContexto, String cCorreo, String cAsunto, String cMensaje , String emailCuentaSelect, String passwordCuentaSelect) {
        this.contexto = cContexto;
        this.A_Correo = cCorreo;
        this.A_Asunto = cAsunto;
        this.A_Mensaje = cMensaje;
        this.emailCuentaSelect = emailCuentaSelect;
        this.passwordCuentaSelect = passwordCuentaSelect;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
//        progreso = ProgressDialog.show(contexto, "Enviando mensaje", "Espere...", false, false);
        Log.d("mail", "Enviando el correo, Espere......."+ emailCuentaSelect);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        //progreso.dismiss();
        Toast.makeText(contexto, "Mensaje enviado", Toast.LENGTH_LONG).show();
    }

    @Override
    protected Void doInBackground(Void... params) {
        Properties props = new Properties();

        /*Configuraciones según el proveedor de Correo electrónico que enviará el Mensaje*/
        /*=========================================================================================
                                         PARA GMAIL
         Requisito: se debe activar "Permitir que aplicaciones menos seguras accedan a tu cuenta"
     https://www.google.com/settings/security/lesssecureapps
            ===========================================================================================*/
        props.put("mail.smtp.host", "smtp.nauta.cu");
        props.put("mail.smtp.socketFactory.port", "25");
      //  props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "25");
        /*=========================================================================================
                                         PARA OUTLOOK (ANTES HOTMAIL)
    ===========================================================================================*/
        /*props.put("mail.smtp.host", "smtp-mail.outlook.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.starttls.enable","true");
        props.put("mail.smtp.auth", "true");*/
       /*=========================================================================================
                                         PARA YAHOO
         Requisito: se debe activar "Permitir aplicaciones que utilicen un inicio de sesión menos seguro"
         https://login.yahoo.com/account/security#other-apps?lang=es-ES
    ===========================================================================================*/
        /*props.put("mail.smtp.host", "smtp.mail.yahoo.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.debug", "true");
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.starttls.enable","true");
        props.put("mail.smtp.auth", "true");*/

        De_Sesion = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailCuentaSelect, passwordCuentaSelect);
            }
        });

        try {
            MimeMessage mm = new MimeMessage(De_Sesion);
            mm.setFrom(new InternetAddress(emailCuentaSelect));
            mm.addRecipient(Message.RecipientType.TO, new InternetAddress(A_Correo));
            mm.setSubject(A_Asunto);
            mm.setText(A_Mensaje);
            Transport.send(mm);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return null;
    }
}