package com.serpro.email.utilidades;

public class utilidades {
    public static final String TABLA_CUENTAS = "cuentas";
    public static final String Conf_id = "idCuenta";
    public static final String Conf_nombre = "nombre";
    public static final String Conf_email = "email";
    public static final String Conf_password = "password";
    public static final String Conf_estado = "estado";

    public static final String TABLA_CONTACTOS = "contactos";
    public static final String Cont_idContacto = "idContacto";
    public static final String Cont_idCuenta = "idCuenta";
    public static final String Cont_nombre = "nombre";
    public static final String Cont_email = "email";
    public static final String Cont_password = "password";
    public static final String Cont_estado = "estado";

    public static final String TABLA_MENSAJE = "mensaje";
    public static final String M_idMensaje = "idMensaje";
    public static final String M_idCuenta = "idCuenta";
    public static final String M_idTo = "idTo";
    public static final String M_mensaje = "mensaje";
    public static final String M_adjunto = "adjunto";
    public static final String M_estado = "estado";
    public static final String M_reciverdate = "reciverdate";

    public static final String CREAR_CUENTAS = "CREATE TABLE "+TABLA_CUENTAS+" ("+Conf_id+" INTEGER PRIMARY KEY, "+Conf_nombre+" TEXT, "+Conf_email+" TEXT, "+Conf_password+" TEXT, "+Conf_estado+" INTEGER)";
    public static final String CREAR_CONTACTOS = "CREATE TABLE "+TABLA_CONTACTOS+" ("+Cont_idContacto+" INTEGER PRIMARY KEY, "+Cont_idCuenta+" INTEGER, "+Cont_nombre+" TEXT, "+Cont_email+" TEXT, "+Cont_estado+" INTEGER)";
    public static final String CREAR_MENSAJES = "CREATE TABLE "+TABLA_MENSAJE+" ("+M_idMensaje+" INTEGER PRIMARY KEY, "+M_idCuenta+" TEXT, "+M_idTo+" TEXT, "+M_mensaje+" TEXT, "+M_adjunto+" TEXT, "+M_estado+" INTEGER, "+M_reciverdate+" TEXT)";

}
