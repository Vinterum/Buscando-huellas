package com.example.buscandohuellas;

public class Place {
    String lugar, detalles_ubi, fecha;
    double latitud, longitud;
    Object timestamp;

    public Place(){}

    public Place(String lugar, String detalles_ubi, String fecha, double latitud, double longitud, Object timestamp) {
        this.lugar = lugar;
        this.detalles_ubi = detalles_ubi;
        this.fecha = fecha;
        this.latitud = latitud;
        this.longitud = longitud;
        this.timestamp = timestamp;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getDetalles_ubi() {
        return detalles_ubi;
    }

    public void setDetalles_ubi(String detalles_ubi) {
        this.detalles_ubi = detalles_ubi;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public Object getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Object timestamp) {
        this.timestamp = timestamp;
    }
}
