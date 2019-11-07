package com.anioncode.workpower.Model;

public class ModelEvent {
    String Nazwa;
    int ID;
    String Data;
    String Priorytet;

    public ModelEvent(String nazwa, String data) {
        Nazwa = nazwa;
        Data = data;
    }

    public ModelEvent( int ID,String nazwa, String data, String priorytet) {
        Nazwa = nazwa;
        this.ID = ID;
        Data = data;
        Priorytet = priorytet;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getNazwa() {
        return Nazwa;
    }

    public void setNazwa(String nazwa) {
        Nazwa = nazwa;
    }

    public String getData() {
        return Data;
    }

    public void setData(String data) {

        Data = data;
    }

    public String getPriorytet() {
        return Priorytet;
    }

    public void setPriorytet(String priorytet) {
        Priorytet = priorytet;
    }
}
