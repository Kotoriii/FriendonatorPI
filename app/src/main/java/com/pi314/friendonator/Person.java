package com.pi314.friendonator;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class Person implements Serializable {
    private HashMap<String, List<String>> interestList = new HashMap<String, List<String>>();
    private HashMap<String, String> getContactedByList = new HashMap<String, String>();
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public void setGetContactedByList(HashMap<String, String> getContactedByList) {
        this.getContactedByList = getContactedByList;
    }

    public void fillInterestList(String tittle, List<String> interests) {
        this.interestList.put(tittle, interests);
    }

    public void fillContactedList(String tittle, String contact) {
        this.getContactedByList.put(tittle, contact);
    }

    public List<String> interestsValue(String key) {
        return interestList.get(key);
    }

    public String contactedByValue(String key) {
        return getContactedByList.get(key);
    }
}