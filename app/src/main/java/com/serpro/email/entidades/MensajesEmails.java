package com.serpro.email.entidades;

public class MensajesEmails {
    private Integer idMensaje;
    private String idCuenta;
    private String idTo;
    private String mensaje;
    private String adjunto;
    private Integer estado;
    private String reciverdate;

    public MensajesEmails() {
        this.idMensaje = idMensaje;
        this.idCuenta = idCuenta;
        this.idTo = idTo;
        this.mensaje = mensaje;
        this.adjunto = adjunto;
        this.estado = estado;
        this.reciverdate = reciverdate;
    }

    public Integer getIdMensaje() {
        return idMensaje;
    }

    public void setIdMensaje(Integer idMensaje) {
        this.idMensaje = idMensaje;
    }

    public String getIdCuenta() {
        return idCuenta;
    }

    public void setIdCuenta(String idCuenta) {
        this.idCuenta = idCuenta;
    }

    public String getIdTo() {
        return idTo;
    }

    public void setIdTo(String idTo) {
        this.idTo = idTo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getAdjunto() {
        return adjunto;
    }

    public void setAdjunto(String adjunto) {
        this.adjunto = adjunto;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public String getReciverdate() {
        return reciverdate;
    }

    public void setReciverdate(String reciverdate) {
        this.reciverdate = reciverdate;
    }
}
