package fr.frogdevelopment.nihongo.dico.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.frogdevelopment.nihongo.dico.entities.Preview;

abstract class DicoAdapter extends ArrayAdapter<Preview> {

    private final LayoutInflater mInflater;

    protected DicoAdapter(Activity context, List<Preview> items) {
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

        handleFirstLine(holder.text1, item);
        handleSecondLine(holder.text2, item);

        return convertView;
    }

    protected void handleFirstLine(TextView textview, Preview item) {
        String pre = StringUtils.isBlank(item.kanji) ? " " : item.kanji + " - ";
        int start = pre.length();

        String text = pre + item.reading;
        int end = text.length();

        SpannableStringBuilder str = new SpannableStringBuilder(text);
        spanKanjiKana(str, start, end);
        textview.setText(str);
    }

    protected void handleSecondLine(TextView textview, Preview item) {
        textview.setText(item.gloss);
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

    protected void spanKanjiKana(SpannableStringBuilder str, int start, int end) {
        str.setSpan(new RelativeSizeSpan(0.7f), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    protected void spanMatchRegion(SpannableStringBuilder str, int start, int end) {
        str.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        str.setSpan(new ForegroundColorSpan(Color.RED), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }
}
