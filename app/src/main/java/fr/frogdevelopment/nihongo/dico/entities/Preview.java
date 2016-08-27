package fr.frogdevelopment.nihongo.dico.entities;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Preview {

	public String kanji;
	public String reading;
	public String gloss;

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.append("kanji", kanji)
				.append("reading", reading)
				.append("gloss", gloss)
				.toString();
	}

}
