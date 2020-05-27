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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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

}
