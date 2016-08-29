package fr.frogdevelopment.nihongo.dico;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.frogdevelopment.nihongo.dico.entities.Preview;

public class DicoAdapter extends ArrayAdapter<Preview> {

    private final LayoutInflater mInflater;

    public DicoAdapter(Activity context, List<Preview> items) {
        super(context, 0, items);

        mInflater = context.getLayoutInflater();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(android.R.layout.simple_list_item_2, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Preview item = getItem(position);
        String text = (StringUtils.isBlank(item.kanji) ? " " : item.kanji + "- ") + item.reading;
        holder.text1.setText(text);

        SpannableStringBuilder str = new SpannableStringBuilder(item.gloss);
        for (Pair<Integer, Integer> indices : item.matchIndices) {
            str.setSpan(new StyleSpan(Typeface.BOLD), indices.getLeft(), indices.getRight(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            str.setSpan(new ForegroundColorSpan(Color.RED), indices.getLeft(), indices.getRight(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        holder.text2.setText(str);

        return convertView;
    }

    class ViewHolder {

        @BindView(android.R.id.text1)
        TextView text1;
        @BindView(android.R.id.text2)
        TextView text2;

        private ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
