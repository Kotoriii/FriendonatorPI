package com.pi314.friendonator;

/**
 * Created by grecia on 15/10/2014.
 */
public class ListaEntrada_History {
    private int Image;
    private String name;
    private String percentage;
    private int percentage1;

    public ListaEntrada_History(int image, String name, String percentage, int precentage1) {
        Image = image;
        this.name = name;
        this.percentage = percentage;
        percentage1 = precentage1;
    }

    public int getImage() {
        return Image;
    }

    public void setImage(int image) {
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

    public int getPercentage1() {
        return percentage1;
    }

    public void setPercentage1(int percentage1) {
        this.percentage1 = percentage1;
    }
}
