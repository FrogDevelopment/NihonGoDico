package fr.frogdevelopment.nihongo.dico.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.frogdevelopment.nihongo.dico.R;
import fr.frogdevelopment.nihongo.dico.entities.Details;

public class DetailsAdapter extends ArrayAdapter<Details> {

    private final LayoutInflater mInflater;

    public DetailsAdapter(Activity context, List<Details> items) {
        super(context, 0, items);

        mInflater = context.getLayoutInflater();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.details_list_items, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Details item = getItem(position);

        holder.pos.setText(item.pos);
        holder.field.setText(item.field);
        holder.misc.setText(item.misc);
        holder.dial.setText(item.dial);
        holder.gloss.setText(item.glos);

        return convertView;
    }

    class ViewHolder {

        @BindView(R.id.details_pos)
        TextView pos;
        @BindView(R.id.details_field)
        TextView field;
	    @BindView(R.id.details_misc)
	    TextView misc;
	    @BindView(R.id.details_dial)
	    TextView dial;
	    @BindView(R.id.details_gloss)
	    TextView gloss;

        private ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
