package com.serpro.email.entidades;

public class Contactos {
    private Integer idContacto;
    private Integer idCuenta;
    private String nombre;
    private String email;
    private Integer estado;

    public Contactos() {
        this.idContacto = idContacto;
        this.idCuenta = idCuenta;
        this.nombre = nombre;
        this.email = email;
        this.estado = estado;
    }

    public Integer getIdContacto() {
        return idContacto;
    }

    public void setIdContacto(Integer idContacto) {
        this.idContacto = idContacto;
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

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }
}
