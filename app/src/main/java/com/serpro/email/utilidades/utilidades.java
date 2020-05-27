package com.serpro.email.utilidades;

public class utilidades {
    public static final String TABLA_CUENTAS = "cuentas";
    public static final String Conf_id = "idCuenta";
    public static final String Conf_nombre = "nombre";
    public static final String Conf_email = "email";
    public static final String Conf_password = "password";
    public static final String Conf_estado = "estado";

    public static final String CREAR_CUENTAS = "CREATE TABLE "+TABLA_CUENTAS+" ("+Conf_id+" INTEGER PRIMARY KEY, "+Conf_nombre+" TEXT, "+Conf_email+" TEXT, "+Conf_password+" TEXT, "+Conf_estado+" INTEGER)";

}
