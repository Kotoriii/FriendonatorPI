package com.pi314.friendonator;

import java.util.Comparator;

/**
 * Created by grecia on 15/10/2014.
 */
public class OrderArrayListByMatch implements Comparator{
    @Override
    public int compare(Object l1, Object l2) {
        ListaEntrada_History dd1 = (ListaEntrada_History)l1;// where FBFriends_Obj is your object class
        ListaEntrada_History dd2 = (ListaEntrada_History)l2;

        return dd2.getPercentage().compareToIgnoreCase(dd1.getPercentage());
    }
}
