package fr.frogdevelopment.nihongo.dico.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
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

		List<String> lexicos = new ArrayList<>();
		if (StringUtils.isNotBlank(item.pos)) {
			lexicos.add(item.pos);
		}
		if (StringUtils.isNotBlank(item.field)) {
			lexicos.add(item.field);
		}
		if (StringUtils.isNotBlank(item.misc)) {
			lexicos.add(item.misc);
		}
		if (StringUtils.isNotBlank(item.dial)) {
			lexicos.add(item.dial);
		}

		if (lexicos.isEmpty()) {
			holder.lexicos.setText("");
			holder.lexicos.setVisibility(View.GONE);
		} else {
			holder.lexicos.setText(TextUtils.join(" / ", lexicos)); // fixme rendre clickable => afficher liste lexique
			holder.lexicos.setVisibility(View.VISIBLE);
		}

		holder.info.setText(item.info);
		if (StringUtils.isBlank(item.info)) {
			holder.info.setVisibility(View.GONE);
		} else {
			holder.info.setVisibility(View.VISIBLE);
		}

		holder.gloss.setText(item.gloss);

		return convertView;
	}

	class ViewHolder {

		@BindView(R.id.details_lexicos)
		TextView lexicos;
		@BindView(R.id.details_info)
		TextView info;
		@BindView(R.id.details_gloss)
		TextView gloss;

		private ViewHolder(View view) {
			ButterKnife.bind(this, view);
		}
	}

}
