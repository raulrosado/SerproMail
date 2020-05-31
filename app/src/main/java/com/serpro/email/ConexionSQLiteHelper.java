package com.serpro.email;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;
import com.serpro.email.utilidades.utilidades;

public class ConexionSQLiteHelper extends SQLiteOpenHelper {
  private static final String TAG = "SQLITE";
  private static final String DATABASE_NAME = "serpromail";

  public ConexionSQLiteHelper(Context context) {
    super(context, DATABASE_NAME, null, 1);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
      db.execSQL(utilidades.CREAR_CUENTAS);
      db.execSQL(utilidades.CREAR_CONTACTOS);
      db.execSQL(utilidades.CREAR_MENSAJES);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      db.execSQL("DROP TABLE IF EXISTS "+utilidades.TABLA_CUENTAS);
      db.execSQL("DROP TABLE IF EXISTS "+utilidades.TABLA_CONTACTOS);
      db.execSQL("DROP TABLE IF EXISTS "+utilidades.TABLA_MENSAJE);
  }

  public void vaciarCuentas(){
      SQLiteDatabase db = this.getWritableDatabase();
      db.execSQL("DROP TABLE IF EXISTS "+utilidades.TABLA_CUENTAS);
      db.execSQL(utilidades.CREAR_CUENTAS);
      db.close(); // Closing database connection
  }

    public void addCuenta(String nombre, String email, String password) {
        SQLiteDatabase db2 = this.getWritableDatabase();   //se conecta a la db
        long lid = 0;
        ContentValues values = new ContentValues();
        values.put(utilidades.Conf_nombre, nombre);
        values.put(utilidades.Conf_email, email);
        values.put(utilidades.Conf_password, password);
        values.put(utilidades.Conf_estado, 0);
        // Inserting Row
        lid = db2.insert(utilidades.TABLA_CUENTAS, null, values);
        db2.close(); // Closing database connection
        Log.d(TAG, "New cuenta inserted into sqlite: " + lid);
    }

    public void updateCuenta(String idCuenta, String nombre, String email, String password){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        String[] parametros = {idCuenta};
        values.put(utilidades.Conf_nombre, nombre);
        values.put(utilidades.Conf_email, email);
        values.put(utilidades.Conf_password, password);
        // update Row
        long lid = db.update(utilidades.TABLA_CUENTAS, values, utilidades.Conf_id+"=?",parametros);
        db.close(); // Closing database connection
        Log.d(TAG, "Update cuenta: " + lid);
    }

    public void selectCuenta(Integer idCuenta){
        SQLiteDatabase db = this.getWritableDatabase();
        Integer[] parametros = {idCuenta};

        ContentValues values = new ContentValues();
        values.put(utilidades.Conf_estado, 1);

        ContentValues values2 = new ContentValues();
        values2.put(utilidades.Conf_estado, 0);
        // desactivo todos
        long lid2 = db.update(utilidades.TABLA_CUENTAS, values2,null ,null);
        //selecciono el user
        long lid = db.update(utilidades.TABLA_CUENTAS, values, utilidades.Conf_id + "= '" + idCuenta + "' " ,null);
        db.close(); // Closing database connection
        Log.d(TAG, "Update estado user2: " + lid2);
        Log.d(TAG, "Update estado user: " + lid);
    }

    public void addContacto(Integer idCuenta,String nombre, String email) {
        SQLiteDatabase db2 = this.getWritableDatabase();   //se conecta a la db
        long lid = 0;
        ContentValues values = new ContentValues();
        values.put(utilidades.Cont_idCuenta, idCuenta);
        values.put(utilidades.Cont_nombre, nombre);
        values.put(utilidades.Cont_email, email);
        values.put(utilidades.Cont_estado, 0);
        // Inserting Row
        lid = db2.insert(utilidades.TABLA_CONTACTOS, null, values);
        db2.close(); // Closing database connection
        Log.d(TAG, "New contacto inserted into sqlite: " + lid);
    }

    public void addMensaje(String idCuenta,String idTo,String mensaje, String adjunto) {
        Integer count =0;
        long lid = 0;
        SQLiteDatabase db = this.getReadableDatabase();   //se conecta a la db
        try {
          Cursor cursor = db.rawQuery("SELECT "+ utilidades.M_mensaje +" FROM " + utilidades.TABLA_MENSAJE + " WHERE "+utilidades.M_mensaje+" = '" + mensaje
                  + "' AND "+utilidades.M_idTo+" = '"+idTo+"' AND "+utilidades.M_idCuenta+" = '"+idCuenta+"' ", null);
          Log.d(TAG, "cantidad: "+cursor.getCount());
          count = cursor.getCount();
          if(cursor.moveToNext()){
              Log.d(TAG, "error ya esta en la bd");

          }else{
        //agrego
              SQLiteDatabase db2 = this.getWritableDatabase();   //se conecta a la db
              lid = 0;
              ContentValues values = new ContentValues();
              values.put(utilidades.M_idCuenta, idTo);
              values.put(utilidades.M_idTo, idCuenta);
              values.put(utilidades.M_mensaje, mensaje);
              values.put(utilidades.M_adjunto, adjunto);
              values.put(utilidades.M_estado, 0);
              // Inserting Row
              lid = db2.insert(utilidades.TABLA_MENSAJE, null, values);
              db2.close(); // Closing database connection
              Log.d(TAG, "New email inserted into sqlite: " + lid);

          }
        }catch (SQLException e){  }
    }

    public void changeMensajesStatus(Integer idMensaje){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        String[] parametros = {idMensaje.toString()};
        values.put(utilidades.M_estado, 1);
        // update Row
        long lid = db.update(utilidades.TABLA_MENSAJE, values, utilidades.M_idMensaje+"=?",parametros);
        db.close(); // Closing database connection
        Log.d(TAG, "Update mensaje: " + lid);
    }
//
//  //funcion para agregar las fotos a la db del celular
//  public void addProducto(Integer Id_articulo,String articulo,Integer duracion){
//    //vacio la tabla de productos
//    SQLiteDatabase db = this.getWritableDatabase();
//    ContentValues values = new ContentValues();
//    values.put(utilidades.Id_articulos,Id_articulo);
//    values.put(utilidades.articulo,articulo);
//    values.put(utilidades.duracion,duracion);
//
//    // Inserting Row
//    db.insert(utilidades.TABLA_PRODUCTOS, null, values);
//    db.close(); // Closing database connection
//  }
//
//  public void addUser(Integer id,String nombre,String apellidos,String email,String ci,Integer roles,Integer login, String provincia, String municipio) {
//    SQLiteDatabase db = this.getWritableDatabase();
//    ContentValues values = new ContentValues();
//    values.put(utilidades.User_id, id);
//    values.put(utilidades.User_nombre, nombre);
//    values.put(utilidades.User_apellidos, apellidos);
//    values.put(utilidades.User_email, email);
//    values.put(utilidades.User_ci, ci);
//    values.put(utilidades.User_roles, roles);
//    values.put(utilidades.User_login, login);
//    values.put(utilidades.User_provincia, provincia);
//    values.put(utilidades.User_municipio, municipio);
//    // Inserting Row
//    long lid = db.insert(utilidades.TABLA_USUARIO, null, values);
//    db.close(); // Closing database connection
//    Log.d(TAG, "New user inserted into sqlite: " + lid);
//  }
//
//  public void updateInfoUser(String nombre,String apellidos,String email,String ci, String provincia, String municipio) {
//    SQLiteDatabase db = this.getWritableDatabase();
//    ContentValues values = new ContentValues();
//
//    values.put(utilidades.User_nombre, nombre);
//    values.put(utilidades.User_apellidos, apellidos);
//    values.put(utilidades.User_email, email);
//    values.put(utilidades.User_ci, ci);
//    values.put(utilidades.User_provincia, provincia);
//    values.put(utilidades.User_municipio, municipio);
//
//    long lid = db.update(utilidades.TABLA_USUARIO, values, null,null);
//    db.close(); // Closing database connection
//  }
//
//  public void logout(){
//    SQLiteDatabase db = this.getReadableDatabase();
//    // update Row
//    //long lid = db.update(utilidades.TABLA_USUARIO, values, null,null);
//    long lid = db.delete(utilidades.TABLA_USUARIO,null,null);
//    long lidm = db.delete(utilidades.TABLA_MENSAJES,null,null);
//    db.close(); // Closing database connection
//    Log.d(TAG, "Update login: " + lid);
//  }
//
//  public void updateuserlogin(String iduser){
//    SQLiteDatabase db = this.getReadableDatabase();
//    ContentValues values = new ContentValues();
//    String[] parametros = {iduser};
//    values.put(utilidades.User_login, 1);
//    // update Row
//    long lid = db.update(utilidades.TABLA_USUARIO, values, utilidades.User_id+"=?",parametros);
//    db.close(); // Closing database connection
//    Log.d(TAG, "Update login: " + lid);
//  }

//
//
//  //funcion para agregar los mensajes a la bd
//  public void addMensajes(Integer toUser, Integer fromUser,String mensaje,Integer estado){
//    SQLiteDatabase db = this.getWritableDatabase();
//    ContentValues values = new ContentValues();
//    values.put(utilidades.men_toUser,toUser);
//    values.put(utilidades.men_fromUser,fromUser);
//    values.put(utilidades.men_mensaje,mensaje);
//    values.put(utilidades.men_estado,estado);
//    // Inserting Row
//    db.insert(utilidades.TABLA_MENSAJES, null, values);
//    db.close(); // Closing database connection
//  }
//
//  //Checkeo por ci
//  public void addRevision(Context context, Integer idTrabajador, String ci, Integer rev_articulo, String provincia, String municipio, String conexion){
//    Integer count =0;
//    long lid = 0;
//    SQLiteDatabase db = this.getReadableDatabase();   //se conecta a la db
//    try {
//      Cursor cursor = db.rawQuery("SELECT "+ utilidades.rev_ci +" FROM " + utilidades.TABLA_REVISION + " WHERE ci = '" + ci
//              + "' AND "+utilidades.rev_rev_articulo+" = '"+rev_articulo+"' ", null);
//      Log.d(TAG, "cantidad: "+cursor.getCount());
//      count = cursor.getCount();
//      if(cursor.moveToNext()){
//          Log.d(TAG, "error el insertar la libreta");
//        if(conexion == "null") {
//          //MUESTRO UN MENSAGE CUANDO AGREGE LOS PRODUCTOS A LA BD
//          AlertDialog.Builder alerta = new AlertDialog.Builder(context);
//          alerta.setMessage("Esta persona ya compro.").setNegativeButton("Aceptar", null).create().show();
//        }
//      }else{
//    //agrego
//        SQLiteDatabase db2 = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put(utilidades.rev_idTrab,idTrabajador);
//        values.put(utilidades.rev_ci,ci);
//        values.put(utilidades.rev_rev_articulo,rev_articulo);
//        values.put(utilidades.rev_provincia,provincia);
//        values.put(utilidades.rev_municipio,municipio);
//
//        // Inserting Row
//        lid = db2.insert(utilidades.TABLA_REVISION, null, values);
//        db2.close(); // Closing database connection
//        Log.d(TAG, "inserta nueva libreta: "+count.toString());
//        if(conexion == "null") {
//          //MUESTRO UN MENSAGE CUANDO AGREGE LOS PRODUCTOS A LA BD
//          AlertDialog.Builder alerta = new AlertDialog.Builder(context);
//          alerta.setMessage("Esta persona puede comprar.").setNegativeButton("Aceptar", null).create().show();
//        }
//      }
//    }catch (SQLException e){  }
//  }
//
//  //Checkeo por LIBRETA
//  public void addLibreta(Context context,Integer rev_articulo,String nucleo, String oficina, String bodega, String provincia, String municipio,String conexion){
//    Integer count =0;
//    long lid = 0;
//    SQLiteDatabase db = this.getReadableDatabase();   //se conecta a la db
//    try {
//      Cursor cursor = db.rawQuery("SELECT "+ utilidades.lib_nucleo +" FROM " + utilidades.TABLA_LIBRETA + " WHERE "+utilidades.lib_nucleo+" = '" + nucleo
//              + "' AND "+utilidades.lib_oficina+" = '"+oficina+"' AND "+utilidades.lib_bodega+" = '"+bodega+"'", null);
//      Log.d(TAG, "cantidad: "+cursor.getCount());
//      count = cursor.getCount();
//
//      if(cursor.moveToNext()){
//        Log.d(TAG, "error el insertar la revision");
//        if(conexion == "null") {
//          //MUESTRO UN MENSAGE CUANDO AGREGE LOS PRODUCTOS A LA BD
//          AlertDialog.Builder alerta = new AlertDialog.Builder(context);
//          alerta.setMessage("Esta persona ya compro.").setNegativeButton("Aceptar", null).create().show();
//        }
//      }else{
//        //agrego
//        SQLiteDatabase db2 = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put(utilidades.lib_rev_articulo,rev_articulo);
//        values.put(utilidades.lib_nucleo,nucleo);
//        values.put(utilidades.lib_oficina,oficina);
//        values.put(utilidades.lib_bodega,bodega);
//        values.put(utilidades.lib_provincia,provincia);
//        values.put(utilidades.lib_municipio,municipio);
//        // Inserting Row
//        db2.insert(utilidades.TABLA_LIBRETA, null, values);
//        db2.close(); // Closing database connection
//        Log.d(TAG, "inserta nueva libreta: "+count.toString());
//        if(conexion == "null") {
//          //MUESTRO UN MENSAGE CUANDO AGREGE LOS PRODUCTOS A LA BD
//          AlertDialog.Builder alerta = new AlertDialog.Builder(context);
//          alerta.setMessage("Esta persona puede comprar.").setNegativeButton("Aceptar", null).create().show();
//        }
//      }
//    }catch (SQLException e){    }
//  }
//
//
//  /***********************************************
//   *    TRABAJO OFFLINE
//   *
//   * @param offline
//   */
//  public void updateConfigOffLine2(String offline){
//      SQLiteDatabase db = this.getWritableDatabase();
//      ContentValues values = new ContentValues();
//      values.put(utilidades.Conf_offline, offline);
//      // update Row
//      long lid = db.update(utilidades.TABLA_CONFIGURACION, values, null,null);
//      db.close(); // Closing database connection
//      Log.d(TAG, "Update config offline: " + offline);
//  }
//
//  public Integer newCola() {
//    //agrego
//    SQLiteDatabase db2 = this.getWritableDatabase();
//    SQLiteDatabase db = this.getReadableDatabase();
//    ContentValues values = new ContentValues();
//    Integer res = 0;
//
//    values.put(utilidades.colas_horaIn,Funciones.loadHora());
//    values.put(utilidades.colas_horaOff,"");
//    values.put(utilidades.colas_fecha,Funciones.loadFecha());
//    values.put(utilidades.colas_estado,1);
//    // Inserting Row
//    db2.insert(utilidades.TABLA_COLAS, null, values);
//
//    Cursor cursor = db.rawQuery("SELECT "+ utilidades.colas_id +" FROM " + utilidades.TABLA_COLAS + " WHERE "+utilidades.colas_estado+" = '" + 1 + "' ", null);
//    if(cursor.getCount() > 0){
//      while (cursor.moveToNext()) {
//        res = cursor.getInt(0);
//      }
//    }
//    db.close(); // Closing database connection
//    Log.d(TAG, "inserta nueva cola:");
//    return res;
//  }
//
//    public void terminarCola() {
//            SQLiteDatabase db = this.getWritableDatabase();
//            ContentValues values = new ContentValues();
//            values.put(utilidades.colas_estado, 0);
//            values.put(utilidades.colas_horaOff, Funciones.loadHora());
//            // update Row
//            long lid = db.update(utilidades.TABLA_COLAS, values, null,null);
//            db.close(); // Closing database connection
//            Log.d(TAG, "Update colas offline: ");
//    }
//
//    public void updateReintentosUser(String ciUser,Integer idCola, Integer reintentos){
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        String[] parametros = {ciUser};
//        values.put(utilidades.RevOff_reintentos, reintentos);
//        // update Row
//        long lid = db.update(utilidades.TABLA_REVISIONOFFLINE, values,
//                utilidades.RevOff_ci + "= '" + ciUser + "'" + " AND " + utilidades.RevOff_idCola + "= '" + idCola + "' " ,null);
//        db.close(); // Closing database connection
//        Log.d(TAG, "Update reintentos user: " + lid);
//    }
//    public void updateReintentosLibreta(String nucleo,String oficina,String bodega,Integer idCola, Integer reintentos){
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put(utilidades.libOff_reintentos, reintentos);
//        // update Row
//        long lid = db.update(utilidades.TABLA_LIBRETAOFFLINE, values,
//                utilidades.libOff_nucleo + "= '" + nucleo + "' AND "+ utilidades.libOff_oficina+"= '"+oficina+"' AND "+ utilidades.libOff_bodega+"= '"+bodega +"' AND " + utilidades.libOff_idCola + "= '" + idCola + "' " ,null);
//        db.close(); // Closing database connection
//        Log.d(TAG, "Update reintentos libretas: " + lid);
//    }
//
//    public void delRevizion(Integer idRev){
//        SQLiteDatabase db = this.getWritableDatabase();
//        String[] parametro = {idRev.toString()};
//        long lid = db.delete(utilidades.TABLA_REVISIONOFFLINE,utilidades.RevOff_id + "=?",parametro);
//        db.close(); // Closing database connection
//        Log.d(TAG, "Elimino persona: " + lid);
//    }
//
//    public void delColaRevizion(String idCola){
//        SQLiteDatabase db = this.getWritableDatabase();
//        String[] parametro = {idCola.toString()};
//        db.delete(utilidades.TABLA_COLAS,utilidades.colas_id + "=?",parametro);
//        long lid = db.delete(utilidades.TABLA_REVISIONOFFLINE,utilidades.RevOff_id + "=?",parametro);
//        long lid2 = db.delete(utilidades.TABLA_LIBRETAOFFLINE,utilidades.libOff_idCola+ "=?",parametro);
//        db.close(); // Closing database connection
//        Log.d(TAG, "Elimino persona: " + lid);
//    }
}
