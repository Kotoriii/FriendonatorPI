package com.pi314.friendonator;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User implements Parcelable {
    private HashMap<String, List<String>> interestList = new HashMap<String, List<String>>();
    private HashMap<String, String> getContactedByList = new HashMap<String, String>();

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel source, int flags) {
        final int n = interestList.size();
        if (n>0) {
            source.writeInt(n);
            for (Map.Entry<String, List<String>> entry : interestList.entrySet()) {
                source.writeString(entry.getKey());
                source.writeList(entry.getValue());
            }
        }

        final int m = interestList.size();
        if (m>0) {
            source.writeInt(m);
            for (Map.Entry<String, String> entry : getContactedByList.entrySet()) {
                source.writeString(entry.getKey());
                source.writeString(entry.getValue());
            }
        }

    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public User(Parcel source) {
        final int n = source.readInt();
        for (int i = 0; i < n; i++) {
            String key = source.readString();
            List<String> interests = new ArrayList<String>();
            source.readList(interests, ClassLoader.getSystemClassLoader());
            fillInterestList(key, interests);
        }

        final int m = source.readInt();
        for (int i = 0; i < m; i++) {
            String keyContact = source.readString();
            String contact = source.readString();
            fillContactedList(keyContact, contact);
        }

    }
}
