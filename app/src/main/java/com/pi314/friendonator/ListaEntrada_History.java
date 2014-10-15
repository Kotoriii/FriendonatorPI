package com.pi314.friendonator;

/**
 * Created by grecia on 15/10/2014.
 */
public class ListaEntrada_History {
    private int Image;
    private String name;
    private String percentage;

    public ListaEntrada_History(int image, String name, String percentage) {
        Image = image;
        this.name = name;
        this.percentage = percentage;
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

    public int getPercentageInt() {
        int percentageMatch = Integer.parseInt(percentage);
        return percentageMatch;
    }
}
