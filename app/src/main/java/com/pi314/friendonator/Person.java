package com.pi314.friendonator;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class Person implements Serializable {
    private HashMap<String, List<String>> interestList = new HashMap<String, List<String>>();
    private HashMap<String, String> getContactedByList = new HashMap<String, String>();
    private HashMap<String, String> textFieldInfo = new HashMap<String, String>();
    private HashMap<Integer,List<Integer>> dataBaseInterest = new HashMap<Integer, List<Integer>>();
    private String name;
    private String id;

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

    public HashMap<String, List<String>> getInterestList() {
        return interestList;
    }

    public void setInterestList(HashMap<String, List<String>> interestList) {
        this.interestList = interestList;
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
    public void fillInterestList(String tittle, List<String> interests) {
        this.interestList.put(tittle, interests);
    }

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
    public List<String> interestsValue(String key) {
        return interestList.get(key);
    }

    public String contactedByValue(String key) {
        return getContactedByList.get(key);
    }

    public String textValue(String key) {
        return textFieldInfo.get(key);
    }

    public List<Integer> dataBaseValues(Integer key) {
        return dataBaseInterest.get(key);
    }

}
