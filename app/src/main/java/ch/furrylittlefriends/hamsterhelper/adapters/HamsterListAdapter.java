package ch.furrylittlefriends.hamsterhelper.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ch.furrylittlefriends.hamsterhelper.R;
import ch.furrylittlefriends.hamsterhelper.model.Hamster;

/**
 * Created by fork on 30.08.14.
 */
public class HamsterListAdapter extends ArrayAdapter<Hamster>{

    private Context context;
    private List<Hamster> hamsterList;

    public HamsterListAdapter(Context context, List<Hamster> hamsterList) {
        super(context, R.layout.hamster_list_row, hamsterList);
        this.context = context;
        this.hamsterList = hamsterList;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.hamster_list_row, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.label);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        textView.setText(hamsterList.get(position).getName());

        return rowView;
    }
}
