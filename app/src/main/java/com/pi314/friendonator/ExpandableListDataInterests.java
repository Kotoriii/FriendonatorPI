package com.pi314.friendonator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListDataInterests {
    public static HashMap<String, List<String>> getData() {
        HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();

        List<String> music = new ArrayList<String>();
        music.add("Rock");
        music.add("Jazz");
        music.add("Blues");
        music.add("Classical");
        music.add("Hip Hop");
        music.add("Electronic");
        music.add("Pop");
        music.add("Romantic");
        music.add("Latin American");

        List<String> movies = new ArrayList<String>();
        movies.add("Action");
        movies.add("Thriller");
        movies.add("Romantic");
        movies.add("Comedy");
        movies.add("Fantasy");
        movies.add("Historical");
        movies.add("Horror");
        movies.add("Science Fiction");

        List<String> literature = new ArrayList<String>();
        literature.add("Novel");
        literature.add("Drama");
        literature.add("Poem");
        literature.add("Romance");
        literature.add("Comedy");
        literature.add("Fiction");
        literature.add("Fantasy");
        literature.add("Mythology");

        List<String> art = new ArrayList<String>();
        art.add("Portrait");
        art.add("Landscape");
        art.add("Conceptual");
        art.add("Modernism");
        art.add("Criticism");
        art.add("Neoclassic");
        art.add("Classic");
        art.add("Expressionism");

        List<String> sports = new ArrayList<String>();
        sports.add("Baseball");
        sports.add("Basketball");
        sports.add("Tennis");
        sports.add("Archery");
        sports.add("Cycling");
        sports.add("Dance");
        sports.add("Gymnastics");
        sports.add("Climbing");
        sports.add("Boxing");
        sports.add("Mix Martial Arts (MMA)");

        List<String> videoGames = new ArrayList<String>();
        videoGames.add("Action");
        videoGames.add("Adventure");
        videoGames.add("Shooter");
        videoGames.add("RPG");
        videoGames.add("Racing");
        videoGames.add("Fighting");
        videoGames.add("Strategy");
        videoGames.add("Casual");

        List<String> saveChanges = new ArrayList<String>();

        expandableListDetail.put("Music", music);
        expandableListDetail.put("Save Changes", saveChanges);
        expandableListDetail.put("Movies", movies);
        expandableListDetail.put("Literature", literature);
        expandableListDetail.put("Art", art);
        expandableListDetail.put("Sports", sports);
        expandableListDetail.put("Video Games", videoGames);

        return expandableListDetail;
    }
}
