package ch.furrylittlefriends.hamsterhelper.ui.addhamster;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.List;

import ch.furrylittlefriends.hamsterhelper.R;
import ch.furrylittlefriends.hamsterhelper.model.Hamster;

/**
 * Created by fork on 30.09.14.
 */
public class HamsterSpinnerAdapter extends ArrayAdapter<Hamster>  {
    private final Context context;
    private final List<Hamster> hamsters;

    public HamsterSpinnerAdapter(Context context,  List<Hamster> hamsters) {
        super(context, android.R.layout.simple_spinner_item, hamsters);
        this.context = context;
        this.hamsters = hamsters;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView;
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(android.R.layout.simple_spinner_item, parent, false);
        } else {
            rowView = convertView;
        }

        Hamster hamster = hamsters.get(position);
        TextView nameTextView = (TextView) rowView.findViewById(android.R.id.text1);

        nameTextView.setText(hamster.getName());
        return rowView;
    }
}
