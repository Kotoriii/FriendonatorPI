package com.pi314.interests;

import android.content.Context;
import android.util.Log;

import com.pi314.friendonator.Person;
import com.pi314.friendonator.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Database.Intereses;
import Database.SQLiteHelper;
import Database.Superinteres;
import Database.TextoInteres;
import Database.Usuario;
import Database.Usuariointereses;
import GridView.GridObject;

public class InterestsMethods {

    public HashMap<Integer, List<Integer>> getInterestFromDataBase(Context context, int idUser) {
        SQLiteHelper db = SQLiteHelper.getInstance(context.getApplicationContext());
        List<Integer> superId = new ArrayList<Integer>();
        List<Integer> valuesId = new ArrayList<Integer>();
        HashMap<Integer, List<Integer>> interests = new HashMap<Integer, List<Integer>>();

        for (Superinteres spi : db.getAllSuperinter()) {
            superId.add(Integer.parseInt(spi.getId()));
        }

        List<Intereses> getAllUserInterests = db.getAllUserInterests(idUser);
        int count = 0;

        while (count < superId.size()) {
            for (Intereses i : getAllUserInterests) {
                if (superId.get(count) == Integer.parseInt(i.getIdsuperinteres())) {
                    valuesId.add(Integer.parseInt(i.getId()) - 1);
                }
            }
            if (!valuesId.isEmpty()) {
                interests.put(superId.get(count), valuesId);
                valuesId = new ArrayList<Integer>();
            }
            count ++;
        }

        return interests;
    }

    public String getInterestsStrings(Context context,int interest, List<Integer> value) {
        String [] values = new String[8];
        int subtractRealID = 0;

        if (interest == 1) {
            values = context.getApplicationContext().getResources().getStringArray(R.array.music);
            subtractRealID = 0;
        }
        else if (interest == 2) {
            values = context.getApplicationContext().getResources().getStringArray(R.array.literature);
            subtractRealID = 8;
        }
        else if (interest == 3) {
            values = context.getApplicationContext().getResources().getStringArray(R.array.movies);
            subtractRealID = 16;
        }
        else if (interest == 4) {
            values = context.getApplicationContext().getResources().getStringArray(R.array.art);
            subtractRealID = 24;
        }
        else if (interest == 5) {
            values = context.getApplicationContext().getResources().getStringArray(R.array.tvShows);
            subtractRealID = 32;
        }
        else if (interest == 6) {
            values = context.getApplicationContext().getResources().getStringArray(R.array.sports);
            subtractRealID = 40;
        }
        else if (interest == 7) {
            values = context.getApplicationContext().getResources().getStringArray(R.array.science);
            subtractRealID = 48;
        }
        else if (interest == 8) {
            values = context.getApplicationContext().getResources().getStringArray(R.array.lookingFor);
            subtractRealID = 49;
        }

        String forReturn = "";
        int count = 0;

        for (Integer v : value) {
            if(count < value.size()-1) {
                forReturn += values[v - subtractRealID] + ", ";
                count += 1;
            } else
                forReturn += values[v - subtractRealID] + ".";
        }

        return forReturn;
    }

    public HashMap<String, String> getTextsFromDataBase(Context context, int idUser) {
        SQLiteHelper db = SQLiteHelper.getInstance(context.getApplicationContext());
        HashMap<String, String> contactedByList = new HashMap<String, String>();
        String [] interest = context.getApplicationContext().getResources().getStringArray(R.array.identifyInterests);

        List<TextoInteres> allTexts = db.getAllInterestTexts(idUser);
        int count = 0;

        while (count < allTexts.size()) {
            for (TextoInteres t : allTexts) {
                contactedByList.put(interest[Integer.parseInt(t.getIdTexto())], t.getTexto());
            }
            count ++;
        }

        return contactedByList;
    }

    public double getMatchPercentage(Person user, Person match) {
        double userInterest = 0.0;
        double matchInterest = 0.0;
        double matchValueCount = 0.0;
        double percentage;
        double resultInterest = 0.0;
        HashMap<Integer, List<Integer>> userList = user.getDataBaseInterest();
        HashMap<Integer, List<Integer>> matchList = match.getDataBaseInterest();

        for (int interest : userList.keySet()) {
            if(matchList.containsKey(interest)) {
                for (int value : userList.get(interest)) {
                    if (matchList.get(interest).contains(value)) {
                        matchInterest += 1;
                    }
                    userInterest += 1;
                }
                resultInterest += (matchInterest/userInterest) * 100;
                matchValueCount ++;
            }
            matchInterest = 0.0;
            userInterest = 0.0;
        }

        percentage = resultInterest/matchValueCount;

        return percentage;
    }

    public int getPercentage(int event, Person user, Person match) {
        int percentage;
        double resultInterest;
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
        double userInterest = 0.0;
        double matchInterest = 0.0;
        double resultInterest;
        HashMap<Integer, List<Integer>> userList = user.getDataBaseInterest();
        HashMap<Integer, List<Integer>> matchList = match.getDataBaseInterest();

        for (int value : userList.get(event)) {
            if (matchList.get(event).contains(value)) {
                matchInterest += 1;
            }
            userInterest++;
        }

        resultInterest = (matchInterest/userInterest) * 100;

        return resultInterest;
    }

    public void insertInterests(Context context, Person person) {
        if (!person.getDataBaseInterest().isEmpty()) {
            SQLiteHelper db = SQLiteHelper.getInstance(context.getApplicationContext());
            int testing = db.deleteUserInterestData();
            Log.i("===> ", "raw deleted " + testing);
            for (int interest : person.getDataBaseInterest().keySet())
                for (int value : person.getDataBaseInterest().get(interest))
                    db.insertUserint(new Usuariointereses(String.valueOf(value + 1), person.getId()));
        }
    }

    public void insertText(Context context, Person person) {
        if (!person.getGetContactedByList().isEmpty()) {
            SQLiteHelper db = SQLiteHelper.getInstance(context.getApplicationContext());
            String [] interestArray = context.getApplicationContext().getResources().getStringArray(R.array.identifyInterests);

            for (Map.Entry<String, String> entry : person.getTextFieldInfo().entrySet()) {
                TextoInteres text = new TextoInteres();
                text.setIdTexto(String.valueOf(Arrays.asList(interestArray).indexOf(entry.getKey())));
                text.setUsuario(person.getId());
                text.setTexto(entry.getValue());
                db.insertTexto(text);
            }
        }
    }

}
