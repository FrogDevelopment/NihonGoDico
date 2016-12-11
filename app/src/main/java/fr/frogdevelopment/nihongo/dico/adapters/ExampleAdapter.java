package fr.frogdevelopment.nihongo.dico.adapters;

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

import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

import fr.frogdevelopment.nihongo.dico.entities.Example;

public class ExampleAdapter extends ArrayAdapter<Example> {

	private final LayoutInflater      mInflater;

	public ExampleAdapter(Activity context, List<Example> items) {
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

		Example item = getItem(position);
		SpannableStringBuilder str = new SpannableStringBuilder(item.japanese);
		for (Pair<Integer, Integer> indices : item.matchIndices) {
			spanMatchRegion(str, indices.getLeft(), indices.getRight());
		}
		holder.text1.setText(str);
		holder.text2.setText(item.translation);

		return convertView;
	}

	private void spanMatchRegion(SpannableStringBuilder str, int start, int end) {
		str.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		str.setSpan(new ForegroundColorSpan(Color.RED), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	}

	private class ViewHolder {

		private final TextView text1;
		private final TextView text2;

		private ViewHolder(View view) {
			text1 = (TextView) view.findViewById(android.R.id.text1);
			text2 = (TextView) view.findViewById(android.R.id.text2);
		}
	}
}
