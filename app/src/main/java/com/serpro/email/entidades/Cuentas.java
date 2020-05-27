package com.serpro.email.entidades;

public class Cuentas {
    private Integer idCuenta;
    private String nombre;
    private String email;
    private String password;
    private Integer estado;

    public Cuentas() {
        this.idCuenta = idCuenta;
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.estado = estado;

    }

    public Integer getIdCuenta() {
        return idCuenta;
    }

    public void setIdCuenta(Integer idCuenta) {
        this.idCuenta = idCuenta;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }
}
