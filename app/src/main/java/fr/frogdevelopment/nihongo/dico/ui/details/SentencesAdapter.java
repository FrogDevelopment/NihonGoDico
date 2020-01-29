package fr.frogdevelopment.nihongo.dico.ui.details;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import java.util.List;

import fr.frogdevelopment.nihongo.dico.R;
import fr.frogdevelopment.nihongo.dico.data.rest.Sentence;

public class SentencesAdapter extends ArrayAdapter<Sentence> {

    private final LayoutInflater mInflater;
    private final Typeface kanjiFont;

    public SentencesAdapter(Context context, List<Sentence> items) {
        super(context, 0, items);

        mInflater = LayoutInflater.from(context);
        this.kanjiFont = ResourcesCompat.getFont(context, R.font.sawarabi_mincho);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(android.R.layout.simple_list_item_2, parent, false);
            holder = new ViewHolder(convertView);
            holder.text1.setTypeface(kanjiFont);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

		Sentence item = getItem(position);
        holder.text1.setText(item.japanese);
        holder.text2.setText(item.translation);

        return convertView;
    }

    private static class ViewHolder {

        private final TextView text1;
        private final TextView text2;

        private ViewHolder(View view) {
            text1 = view.findViewById(android.R.id.text1);
            text2 = view.findViewById(android.R.id.text2);
        }
    }
}
