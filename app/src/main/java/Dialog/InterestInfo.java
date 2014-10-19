package Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

public class InterestInfo extends DialogFragment {
    private String title;
    private String message;

    public void setInfo(String title, String message) {
        this.title = title;
        this.message = message;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title)
                .setMessage(message);
        return builder.create();
    }
}
