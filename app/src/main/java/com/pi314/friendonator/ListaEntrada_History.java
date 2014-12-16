package com.pi314.friendonator;

/**
 * Created by grecia on 15/10/2014.
 */
public class ListaEntrada_History {
    private int id;
    private String Image;
    private String name;
    private String percentage;


    public ListaEntrada_History(String imagePath, String name, String percentage, int _id) {
        Image = imagePath;
        this.name = name;
        this.percentage = percentage;
        id = _id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }
}
