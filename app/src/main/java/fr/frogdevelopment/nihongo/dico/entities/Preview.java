package fr.frogdevelopment.nihongo.dico.entities;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class Preview {

	public String kanji;
	public String reading;
	public String gloss;
	public double similarity;

    public List<Pair<Integer,Integer>> matchIndices = new ArrayList<>();

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.append("kanji", kanji)
				.append("reading", reading)
				.append("gloss", gloss)
				.append("similarity", similarity)
				.toString();
	}

}
