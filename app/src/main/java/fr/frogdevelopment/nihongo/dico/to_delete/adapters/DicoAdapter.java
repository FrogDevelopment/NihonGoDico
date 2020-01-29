package fr.frogdevelopment.nihongo.dico.to_delete.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import fr.frogdevelopment.nihongo.dico.R;
import fr.frogdevelopment.nihongo.dico.to_delete.entity.Result;
import fr.frogdevelopment.nihongo.dico.to_delete.entity.Search;

public abstract class DicoAdapter extends ArrayAdapter<Search> {

    private final LayoutInflater mInflater;

    DicoAdapter(Activity activity, List<Search> items) {
        super(activity, 0, items);

        mInflater = activity.getLayoutInflater();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.row_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Search item = getItem(position);

        handleFirstLine(holder.firstLine, item);
        handleSecondLine(holder.secondLine, item);

        return convertView;
    }

    protected void handleFirstLine(TextView textview, Search item) {
        String pre = TextUtils.isEmpty(item.kanji.value) ? " " : item.kanji.value + " - ";
        int start = pre.length();

        String text = pre + item.kana.value;
        int end = text.length();

        SpannableStringBuilder str = new SpannableStringBuilder(text);
        spanKanjiKana(str, start, end);
        textview.setText(str);
    }

    protected void handleSecondLine(TextView textview, Search item) {
        List<String> gloss = new ArrayList<>();

        for (Result result : item.senses) {
            gloss.add(result.value);
        }

        textview.setText(TextUtils.join(", ", gloss));
    }

    void spanKanjiKana(SpannableStringBuilder str, int start, int end) {
        str.setSpan(new RelativeSizeSpan(0.7f), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    void spanMatchRegion(SpannableStringBuilder str, int start, int end) {
        str.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        str.setSpan(new ForegroundColorSpan(Color.RED), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private class ViewHolder {

        private final TextView firstLine;
        private final TextView secondLine;

        private ViewHolder(View view) {
            firstLine = view.findViewById(R.id.row_first_line);
            secondLine = view.findViewById(R.id.row_second_line);
        }
    }
}
