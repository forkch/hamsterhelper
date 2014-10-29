package ch.furrylittlefriends.hamsterhelper.ui.hamsterlist;

import android.app.ListActivity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;

import ch.furrylittlefriends.hamsterhelper.R;
import ch.furrylittlefriends.hamsterhelper.model.Hamster;
import ch.furrylittlefriends.hamsterhelper.util.HamsterImageHelper;

/**
 * Created with love by fork on 30.08.14.
 */
public class HamsterListAdapter extends ArrayAdapter<Hamster> {

    private static final String TAG = HamsterListAdapter.class.getSimpleName();
    private final ListActivity context;
    private final HamsterListRowListener hamsterListRowListener;
    private final DateTimeFormatter dateTimeFormatter;


    public HamsterListAdapter(ListActivity context, List<Hamster> hamsterList, HamsterListRowListener hamsterListRowListener) {
        super(context, R.layout.hamster_list_row, hamsterList);
        this.context = context;
        this.hamsterListRowListener = hamsterListRowListener;
        dateTimeFormatter = DateTimeFormat.forPattern(context.getString(R.string.birthday_date_format));
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View rowView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.hamster_list_row, parent, false);
        } else {
            rowView = convertView;
        }
        TextView nameTextView = (TextView) rowView.findViewById(R.id.hamsterName);
        TextView birthdayTextview = (TextView) rowView.findViewById(R.id.hamsterBirthday);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        TextView parents = (TextView) rowView.findViewById(R.id.hamsterParents);
        Hamster hamster = getItem(position);

        HamsterImageHelper.setHamsterImage(context, imageView, hamster);

        nameTextView.setText(hamster.getName());

        if (hamster.getMother() != null && hamster.getFather() != null) {
            parents.setText(hamster.getMother().getName() + " & " + hamster.getFather().getName());
        } else {
            parents.setText(context.getString(R.string.not_available));
        }

        DateTime birthday = hamster.getBirthday();
        if (birthday != null) {
            birthdayTextview.setText(context.getString(R.string.birthday_label) + " " + birthday.toLocalDate().toString(dateTimeFormatter));
        } else {
            birthdayTextview.setText(context.getString(R.string.no_birthday_set));
        }


        ImageButton deleteButton = (ImageButton) rowView.findViewById(R.id.deleteHamsterButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hamsterListRowListener.onDelete(view);
            }
        });
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hamsterListRowListener.onHamsterClick(position);
            }
        });
        return rowView;
    }


    public interface HamsterListRowListener {
        public void onDelete(View view);

        void onHamsterClick(int position);
    }
}
