package com.pi314.friendonator;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SelectInterests extends DialogFragment {

    private List<String> listBySelectedInterest;
    private String tittle;
    private String [] choices;
    private Person person;
    private List<String> mirrorListForCancel;

    public void setOptions(String tittle, String [] choices, Person person){
        this.tittle = tittle;
        this.choices = choices;
        this.person = person;
    }

    public boolean [] putChecks() {
        boolean[] interestsChecked = new boolean[choices.length];
        for (int i = 0; i < choices.length; i++) {
            interestsChecked[i] = false;
        }
        listBySelectedInterest = new ArrayList<String>();
        mirrorListForCancel = new ArrayList<String>();
        if (!person.getInterestList().isEmpty()) {
            if (person.interestsValue(tittle) != null) {
                for (String value : person.interestsValue(tittle)) {
                    if (Arrays.asList(choices).contains(value)) {
                        int index = Arrays.asList(choices).indexOf(value);
                        listBySelectedInterest.add(value);
                        interestsChecked[index] = true;
                    }
                }
            }
            for (String s : listBySelectedInterest) {
                mirrorListForCancel.add(s);
            }
        }
        return interestsChecked;
    }

    /* The activity that creates an instance of this dialog fragment must
             * implement this interface in order to receive event callbacks.
             * Each method passes the DialogFragment in case the host needs to query it. */
    public interface NoticeDialogListener {
        public void onDialogPositiveClick(List<String> listBySelectedInterest, String tittle);
        public void onDialogNegativeClick(List<String> listBySelectedInterest, String tittle);
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
        AlertDialog.Builder builder1 = builder.setTitle(tittle)
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
                .setMultiChoiceItems(choices, putChecks(),
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which,
                                                boolean isChecked) {
                                if (isChecked) {
                                    // If the user checked the item, add it to the selected items
                                    listBySelectedInterest.add(choices[which]);
                                } else if (listBySelectedInterest.contains(choices[which])) {
                                    // Else, if the item is already in the array, remove it
                                    listBySelectedInterest.remove(listBySelectedInterest.indexOf(choices[which]));
                                }
                            }
                        })
                        // Set the action buttons
                .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked accept, so save the mSelectedItems results somewhere
                        // or return them to the component that opened the dialog
                        mListener.onDialogPositiveClick(listBySelectedInterest, tittle);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogNegativeClick(mirrorListForCancel, tittle);
                    }
                });

        return builder.create();
    }
}
