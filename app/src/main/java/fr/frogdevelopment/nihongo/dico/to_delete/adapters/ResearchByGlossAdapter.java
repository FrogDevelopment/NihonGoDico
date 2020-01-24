package fr.frogdevelopment.nihongo.dico.to_delete.adapters;

import android.app.Activity;
import android.text.SpannableStringBuilder;
import android.widget.TextView;

import java.util.List;

import fr.frogdevelopment.nihongo.dico.data.rest.search.Match;
import fr.frogdevelopment.nihongo.dico.data.rest.search.Result;
import fr.frogdevelopment.nihongo.dico.data.rest.search.Search;

public class ResearchByGlossAdapter extends DicoAdapter {

    public ResearchByGlossAdapter(Activity activity, List<Search> items) {
        super(activity, items);
    }

    protected void handleSecondLine(TextView textview, Search item) {

        int offset = 0;
        SpannableStringBuilder str = new SpannableStringBuilder();
        int xx = 0;
        for (Result result : item.senses) {
            str.append(result.value);
            for (Match match : result.matches) {
                spanMatchRegion(str, offset + match.start, offset + match.end);
            }
            offset += result.value.length();

            xx++;
            if (xx < item.senses.size()) {
                str.append(", ");
                offset += ", ".length();
            }
        }

        textview.setText(str);
    }
}
