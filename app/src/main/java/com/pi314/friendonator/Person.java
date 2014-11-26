package com.pi314.friendonator;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Person implements Serializable {
    private HashMap<String, String> getContactedByList = new HashMap<String, String>();
    private HashMap<String, String> textFieldInfo = new HashMap<String, String>();
    private HashMap<Integer,List<Integer>> dataBaseInterest = new HashMap<Integer, List<Integer>>();

    private String name;
    private String id;
    private String email;
    private int eventId;

    private Date fecha_de_nacimiento = null;
    private int modo_de_cont_favorito;
    private Bitmap foto_perfil = null;
    private List<Person> historial = null;

    public Person() {
    }

    public Person(HashMap<String, String> getContactedByList,
                  HashMap<String, String> textFieldInfo, HashMap<Integer, List<Integer>> dataBaseInterest,
                  String name, String id, Date fecha_de_nacimiento, int modo_de_cont_favorito,
                  Bitmap foto_perfil, List<Person> historial) {

        this.getContactedByList = getContactedByList;
        this.textFieldInfo = textFieldInfo;
        this.dataBaseInterest = dataBaseInterest;
        this.name = name;
        this.id = id;
        this.fecha_de_nacimiento = fecha_de_nacimiento;
        this.modo_de_cont_favorito = modo_de_cont_favorito;
        this.foto_perfil = foto_perfil;
        this.historial = historial;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public Date getFecha_de_nacimiento() {
        return fecha_de_nacimiento;
    }

    public void setFecha_de_nacimiento(Date fecha_de_nacimiento) {
        this.fecha_de_nacimiento = fecha_de_nacimiento;
    }

    public int getModo_de_cont_favorito() {
        return modo_de_cont_favorito;
    }

    public void setModo_de_cont_favorito(int modo_de_cont_favorito) {
        this.modo_de_cont_favorito = modo_de_cont_favorito;
    }

    public Bitmap getFoto_perfil() {
        return foto_perfil;
    }

    public void setFoto_perfil(Bitmap foto_perfil) {
        this.foto_perfil = foto_perfil;
    }

    public HashMap<String, String> getGetContactedByList() {
        return getContactedByList;
    }

    public HashMap<String, String> getTextFieldInfo() {
        return textFieldInfo;
    }

    public void setGetTextFieldInfo(HashMap<String, String> textFieldInfo) {
        this.textFieldInfo = textFieldInfo;
    }

    public void setGetContactedByList(HashMap<String, String> getContactedByList) {
        this.getContactedByList = getContactedByList;
    }

    public HashMap<Integer, List<Integer>> getDataBaseInterest() {
        return dataBaseInterest;
    }

    public void setDataBaseInterest(HashMap<Integer, List<Integer>> dataBaseInterest) {
        this.dataBaseInterest = dataBaseInterest;
    }

    // Fill hash map
    public void fillContactedList(String tittle, String contact) {
        this.getContactedByList.put(tittle, contact);
    }

    public void fillTextFieldInfo(String tittle, String text) {
        this.textFieldInfo.put(tittle, text);
    }

    public void fillDataBaseInterests(Integer tittle, List<Integer> interests) {
        this.dataBaseInterest.put(tittle, interests);
    }

    // Get hash map values

    public String contactedByValue(String key) {
        return getContactedByList.get(key);
    }

    public String textValue(String key) {
        return textFieldInfo.get(key);
    }

    public List<Integer> dataBaseValues(Integer key) {
        return dataBaseInterest.get(key);
    }

    //temporal to String
    @Override
    public String toString() {
        return "Usuario{" +
                "nombre='" + this.name + '\'' +
                ", fecha_de_nacimiento=" + fecha_de_nacimiento +
                ", modo_de_cont_favorito=" + modo_de_cont_favorito +
                ", foto_perfil=" + foto_perfil +
                ", historial=" + historial +
                "Modos de contactol= " + getContactedByList +
                '}';
    }

    public void setId(int id_us) {
        this.id = Integer.toString(id_us);
    }
}
