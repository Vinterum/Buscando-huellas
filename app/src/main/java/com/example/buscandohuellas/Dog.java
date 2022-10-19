package com.example.buscandohuellas;

public class Dog {
    String nombre, color, comportamiento, contacto, detalles_ap,
            detalles_salud,edad, raza, sexo, tamano, imageUrl;
    Object timestamp;

    public Dog(){}

    public Dog(String nombre, String color, String comportamiento, String contacto,
               String detalles_ap, String detalles_salud, String edad,
               String raza, String sexo, String tamano, String imageUrl, Object timestamp) {
        this.nombre = nombre;
        this.color = color;
        this.comportamiento = comportamiento;
        this.contacto = contacto;
        this.detalles_ap = detalles_ap;
        this.detalles_salud = detalles_salud;
        this.edad = edad;
        this.raza = raza;
        this.sexo = sexo;
        this.tamano = tamano;
        this.imageUrl = imageUrl;
        this.timestamp = timestamp;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getComportamiento() {
        return comportamiento;
    }

    public void setComportamiento(String comportamiento) {
        this.comportamiento = comportamiento;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public String getDetalles_ap() {
        return detalles_ap;
    }

    public void setDetalles_ap(String detalles_ap) {
        this.detalles_ap = detalles_ap;
    }

    public String getDetalles_salud() {
        return detalles_salud;
    }

    public void setDetalles_salud(String detalles_salud) {
        this.detalles_salud = detalles_salud;
    }

    public String getEdad() {
        return edad;
    }

    public void setEdad(String edad) {
        this.edad = edad;
    }

    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getTamano() {
        return tamano;
    }

    public void setTamano(String tamano) {
        this.tamano = tamano;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Object getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Object timestamp) {
        this.timestamp = timestamp;
    }
}
