package com.pi314.friendonator;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SelectInterests extends DialogFragment {

    private String title;
    private String [] choices;
    private Person person;
    private List<Integer> interestList;
    private List<Integer> mirrorIntegerListForCancel;

    public void setOptions(String title, String [] choices, Person person){
        this.title = title;
        this.choices = choices;
        this.person = person;
    }

    // Set boolean array to fill checkbox
    public boolean [] putChecks() {
        boolean[] interestsChecked = new boolean[choices.length];

        for (int i = 0; i < choices.length; i++) {
            interestsChecked[i] = false;
        }

        mirrorIntegerListForCancel = new ArrayList<Integer>();
        interestList = new ArrayList<Integer>();

        if (!person.getDataBaseInterest().isEmpty()) {
            int titleIndex = Arrays.asList(getResources().getStringArray(R.array.identifyInterests)).indexOf(title);
            if (person.dataBaseValues(titleIndex + 1) != null) {
                for (int value : person.dataBaseValues(titleIndex + 1)) {
                    int index = indexGenre(titleIndex, value);
                    interestList.add(value);
                    interestsChecked[index] = true;
                }
                for (Integer i : interestList) {
                    mirrorIntegerListForCancel.add(i);
                }
            }
        }

        return interestsChecked;
    }

    /* The activity that creates an instance of this dialog fragment must
       implement this interface in order to receive event callbacks.
       Each method passes the DialogFragment in case the host needs to query it. */
    public interface NoticeDialogListener {
        public void onDialogPositiveClick(String tittle, List<Integer> interestList);
        public void onDialogNegativeClick(List<Integer> mirrorIntegerListForCancel, String tittle);
    }

    // Use this instance of the interface to deliver action events
    NoticeDialogListener mListener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NoticeDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Set the dialog title
        AlertDialog.Builder builder1 = builder.setTitle(title)
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
                .setMultiChoiceItems(choices, putChecks(),
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which,
                                                boolean isChecked) {
                                if (isChecked) {
                                    // If the user checked the item, add it to the selected items
                                    interestList.add(dataBaseID(title, which));
                                } else if (interestList.contains(dataBaseID(title, which))) {
                                    // Else, if the item is already in the array, remove it
                                    interestList.remove(interestList.indexOf(dataBaseID(title, which)));
                                }
                            }
                        })
                        // Set the action buttons
                .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // Return selected interest to the component that opened the dialog
                        mListener.onDialogPositiveClick(title, interestList);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogNegativeClick(mirrorIntegerListForCancel, title);
                    }
                });

        return builder.create();
    }

    public Integer dataBaseID(String title, int index) {
        int realIndex;
        int titleIndex = Arrays.asList(getResources().getStringArray(R.array.identifyInterests)).indexOf(title);

        if (titleIndex == 1)
            realIndex = index + 8;
        else if (titleIndex == 2)
            realIndex = index + 16;
        else if (titleIndex == 3)
            realIndex = index + 24;
        else if (titleIndex == 4)
            realIndex = index + 32;
        else if (titleIndex == 5)
            realIndex = index + 40;
        else if (titleIndex == 6)
            realIndex = index + 48;
        else if (titleIndex == 7)
            realIndex = index + 49;
        else
            realIndex = index;

        return realIndex;
    }

    public Integer indexGenre(int idSuperInt, int dataBaseID) {
        int index;

        if (idSuperInt == 1)
            index = dataBaseID - 8;
        else if (idSuperInt == 2)
            index = dataBaseID - 16;
        else if (idSuperInt == 3)
            index = dataBaseID - 24;
        else if (idSuperInt == 4)
            index = dataBaseID - 32;
        else if (idSuperInt == 5)
            index = dataBaseID - 40;
        else if (idSuperInt == 6)
            index = dataBaseID - 48;
        else if (idSuperInt == 7)
            index = dataBaseID - 49;
        else
            index = dataBaseID;

        return index;
    }
}
