package GridView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pi314.friendonator.R;

import java.util.ArrayList;

public class GridCustomAdapter extends ArrayAdapter<GridObject> {
    public GridCustomAdapter(Context context, ArrayList<GridObject> interest) {
        super(context, 0, interest);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        GridObject gridInterest = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.gridview_text, parent, false);
        }
        // Lookup view for data population
        TextView interestTitle = (TextView) convertView.findViewById(R.id.interestTitle);
        TextView interestGenres = (TextView) convertView.findViewById(R.id.interestGenres);
        // Populate the data into the template view using the data object
        interestTitle.setText(gridInterest.getTitle());
        interestGenres.setText(gridInterest.getGenres());
        // Return the completed view to render on screen
        return convertView;
    }
}
