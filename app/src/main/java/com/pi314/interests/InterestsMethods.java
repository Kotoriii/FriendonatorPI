package com.pi314.interests;

import android.content.Context;
import android.util.Log;

import com.pi314.friendonator.Person;
import com.pi314.friendonator.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import Database.Historial;
import Database.Intereses;
import Database.SQLiteHelper;
import Database.Superinteres;
import Database.TextoInteres;
import Database.Usuario;
import Database.Usuariointereses;
import GridView.GridObject;

public class InterestsMethods {



    public HashMap<Integer, List<Integer>> getInterestFromDataBase(Context context, int idUser) {
        // Make interests hashMap from Data Base to fill person interests using its id
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
        // Method to convert genres id to its respective string
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
        // Make optional interest text hashMap from Data Base to fill person interests text using its id
        SQLiteHelper db = SQLiteHelper.getInstance(context.getApplicationContext());
        HashMap<String, String> contactedByList = new HashMap<String, String>();
        String [] interest = context.getApplicationContext().getResources().getStringArray(R.array.identifyInterests);

        List<TextoInteres> allTexts = db.getAllInterestTexts(idUser);
        int count = 0;

        while (count < allTexts.size()) {
            for (TextoInteres t : allTexts) {
                contactedByList.put(interest[Integer.parseInt(t.getIdSuperInteres())], t.getTexto());
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

        if (userList.get(event) != null) {
            for (int value : userList.get(event)) {
                if (matchList.get(event) != null && matchList.get(event).contains(value)) {
                    matchInterest += 1;
                }
                userInterest++;
            }
        }

        resultInterest = (matchInterest/userInterest) * 100;

        return resultInterest;
    }

    public void insertInterests(Context context, Person person) {
        // Insert interest from person interest hash map
        if (!person.getDataBaseInterest().isEmpty()) {
            SQLiteHelper db = SQLiteHelper.getInstance(context.getApplicationContext());
            Log.i("-------------------", "" + db.deleteUserInterestData(person.getId()));
            for (int interest : person.getDataBaseInterest().keySet())
                for (int value : person.getDataBaseInterest().get(interest))
                    db.insertUserint(new Usuariointereses(String.valueOf(value + 1), person.getId()));
        }
    }

    public void insertText(Context context, Person person) {
        // Insert optional texts from person text hash map
        if (!person.getTextFieldInfo().isEmpty()) {
            SQLiteHelper db = SQLiteHelper.getInstance(context.getApplicationContext());
            String [] interestArray = context.getApplicationContext().getResources().getStringArray(R.array.identifyInterests);
            Log.i("-------------------", "" + db.deleteUserTextData(person.getId()));

            for (Map.Entry<String, String> entry : person.getTextFieldInfo().entrySet()) {
                TextoInteres text = new TextoInteres();
                text.setIdSuperInteres(String.valueOf(Arrays.asList(interestArray).indexOf(entry.getKey())));
                text.setUsuario(person.getId());
                text.setTexto(entry.getValue());
                db.insertTexto(text);
            }
        }
    }

    public Person createPerson(Context context, int userId) {
        // Create object person using its id as parameter
        SQLiteHelper db = SQLiteHelper.getInstance(context.getApplicationContext());
        Usuario userFromDataBase = db.getUserByID(userId);

        Person person = new Person();
        person.setName(userFromDataBase.getNombre());
        person.setId(userFromDataBase.getId());
        person.setDataBaseInterest(getInterestFromDataBase(context, userId));
        person.setGetTextFieldInfo(getTextsFromDataBase(context, userId));
        person.setGetContactedByList(getContactedByFromDataBase(context, userFromDataBase));

        return person;
    }

    public HashMap<String, String> getContactedByFromDataBase(Context context, Usuario user) {
        // Create contactedBy hash map from Data Base user information
        HashMap<String, String> contactedByList = new HashMap<String, String>();

        if (user.getNum() != null)
            contactedByList.put(context.getApplicationContext().getResources().getString(R.string.lblCellphone), user.getNum());
        if (user.getGplus() != null)
            contactedByList.put(context.getApplicationContext().getResources().getString(R.string.lblGoogle), user.getGplus());
        if (user.getFb() != null)
            contactedByList.put(context.getApplicationContext().getResources().getString(R.string.lblFacebook), user.getFb());
        if (user.getTwitter() != null)
            contactedByList.put(context.getApplicationContext().getResources().getString(R.string.lblTwitter), user.getTwitter());

        return contactedByList;
    }

    public void insertReceivedPerson(Context context, Person person, String idUsuario, int percentage) {
        // Insert received person via bluetooth into Data Base
        SQLiteHelper db = SQLiteHelper.getInstance(context.getApplicationContext());
        Usuario userToInsert = new Usuario();
        userToInsert.setId(person.getId());
        userToInsert.setNombre(person.getName());
        if (person.contactedByValue(context.getApplicationContext().getResources().getString(R.string.lblCellphone)) != null){
            userToInsert.setNum(person.contactedByValue(context.getApplicationContext().getResources().getString(R.string.lblCellphone)));
        }
        if (person.contactedByValue(context.getApplicationContext().getResources().getString(R.string.lblGoogle)) != null){
            userToInsert.setGplus(person.contactedByValue(context.getApplicationContext().getResources().getString(R.string.lblGoogle)));
        }
        if (person.contactedByValue(context.getApplicationContext().getResources().getString(R.string.lblFacebook)) != null){
            userToInsert.setFb(person.contactedByValue(context.getApplicationContext().getResources().getString(R.string.lblFacebook)));
        }
        if (person.contactedByValue(context.getApplicationContext().getResources().getString(R.string.lblTwitter)) != null){
            userToInsert.setTwitter(person.contactedByValue(context.getApplicationContext().getResources().getString(R.string.lblTwitter)));
        }

        db.insertUsuario(userToInsert);
        insertInterests(context, person);
        insertText(context, person);

        Historial historial = new Historial();
        historial.setIdMatch(person.getId());
        historial.setIdusuario(idUsuario);
        historial.setMatchPerc(String.valueOf(percentage));
        historial.setLatitud("0");
        historial.setLongitud("0");
        historial.setMatchName(person.getName());
        historial.setFecha(getDataTime());

        db.insertHistorial(historial);
    }

    public Person getLocalPropietor(Context contex){
        SQLiteHelper hlpr = SQLiteHelper.getInstance(contex);
        int idDePropietario = Integer.parseInt(hlpr.getLimbo1().getId());
        return this.createPerson(contex, idDePropietario);
    }

    private String getDataTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

}
