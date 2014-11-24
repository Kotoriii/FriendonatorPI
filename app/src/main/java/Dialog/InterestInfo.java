package Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.pi314.friendonator.R;

import java.util.Arrays;

public class InterestInfo extends DialogFragment {
    private String title;
    private String message;
    private String icon;

    public void setInfo(String title, String message, String icon) {
        this.title = title;
        this.message = message;
        this.icon = icon;
    }

    // Set icon
    public Drawable setIcon() {
        int titleIndex = Arrays.asList(getResources().getStringArray(R.array.identifyInterests)).indexOf(icon);
        return getResources().obtainTypedArray(R.array.dialogeIcon).getDrawable(titleIndex);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setIcon(setIcon())
                .setTitle(title)
                .setMessage(message);
        return builder.create();
    }
}
