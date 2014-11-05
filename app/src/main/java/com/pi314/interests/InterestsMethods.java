package com.pi314.interests;

import com.pi314.friendonator.Person;
import com.pi314.friendonator.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Database.SQLiteHelper;
import Database.Superinteres;
import Database.Usuario;
import Database.Usuariointereses;
import GridView.GridObject;

public class InterestsMethods {

    public HashMap<String, List<String>> getInterestFromDataBase(String [] interest, SQLiteHelper db) {
        // Data base should be a parameter, will be included on project update
        List<Integer> interestId = new ArrayList<Integer>(); // From data base, can be received by parameter
        List<Integer> valuesId = new ArrayList<Integer>(); // From data base, can be received by parameter
        List<String> interestsList = new ArrayList<String>();
        HashMap<String, List<String>> userInterests = new HashMap<String, List<String>>();

        for (Superinteres spi : db.getAllSuperinter()) {
            interestId.add(Integer.parseInt(spi.getId()));
        }

        int count = 0;
        while (count < interestId.size()) {
            userInterests.put(interest[interestId.get(count)+1], null); // Get data from db, will send interestId
            count ++;
        }

        return userInterests;
    }

    public double getMatchPercentage(Person user, Person match) {
        int userInterest = 0;
        int matchInterest = 0;
        int matchValueCount = 0;
        double percentage;
        double resultInterest = 0.0;
        HashMap<Integer, List<Integer>> userList = user.getDataBaseInterest();
        HashMap<Integer, List<Integer>> matchList = match.getDataBaseInterest();

        for (int interest : userList.keySet()) {
            if(matchList.containsKey(interest)) {
                for (int value : userList.get(interest)) {
                    if (matchList.get(interest).contains(value)) {
                        matchInterest ++;
                    }
                    userInterest ++;
                }
                resultInterest += (matchInterest/userInterest) * 100;
                matchValueCount ++;
            }
            matchInterest = 0;
            userInterest = 0;
        }

        percentage = resultInterest/matchValueCount;

        return percentage;
    }

    public int getPercentage(int event, Person user, Person match) {
        int percentage;
        double resultInterest = 0.0;
        HashMap<Integer, List<Integer>> userList = user.getDataBaseInterest();
        HashMap<Integer, List<Integer>> matchList = match.getDataBaseInterest();
        boolean special = false;

        switch (event) {
            case 1:
                if (userList.containsKey(event) && matchList.containsKey(event))
                    special = true;
                break;
            case 2:
                if (userList.containsKey(event) && matchList.containsKey(event))
                    special = true;
                break;
            case 3:
                if (userList.containsKey(event) && matchList.containsKey(event))
                    special = true;
                break;
            default:
                special = false;
                break;
        }

        if (special)
            resultInterest = specialMatchResult(event, user, match);
        else
            resultInterest = getMatchPercentage(user, match);

        percentage = (int) Math.floor(resultInterest);

        return percentage;
    }

    public double specialMatchResult(int event, Person user, Person match) {
        int userInterest = 0;
        int matchInterest = 0;
        double resultInterest;
        HashMap<Integer, List<Integer>> userList = user.getDataBaseInterest();
        HashMap<Integer, List<Integer>> matchList = match.getDataBaseInterest();

        for (int value : userList.get(event)) {
            if (matchList.get(event).contains(value)) {
                matchInterest++;
            }
            userInterest++;
        }

        resultInterest = (matchInterest/userInterest) * 100;

        return resultInterest;
    }

    public void insertInterests(SQLiteHelper db, Person person) {
        if (!person.getInterestList().isEmpty()) {
            for (String interest : person.getInterestList().keySet())
                for (String value : person.getInterestList().get(interest))
                    db.insertUserint(new Usuariointereses(value, person.getId()));
        }
    }

    public void insertContactedByInfo(SQLiteHelper db, Person person) {
        if (!person.getGetContactedByList().isEmpty()) {
            Usuario user = new Usuario();
            for (Map.Entry<String, String> entry : person.getGetContactedByList().entrySet()) {

            }

        }
    }
}
