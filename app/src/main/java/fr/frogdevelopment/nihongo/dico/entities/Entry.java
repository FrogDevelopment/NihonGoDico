package fr.frogdevelopment.nihongo.dico.entities;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.HashSet;
import java.util.Set;

public class Entry {

	public       String     kanji   = "";
	public       String     reading = "";
	public final Set<Sense> senses  = new HashSet<>();

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.append("kanji", kanji)
				.append("reading", reading)
				.append("senses", senses)
				.toString();
	}

	public static Entry fromString(String value) {
		Entry entry = new Entry();

		String[] values = value.split("\\|", 3);

		entry.kanji = values[0];
		entry.reading = values[1];

		String[] senses = values[2].split("\\|");

		for (String sense : senses) {
			entry.senses.add(Sense.fromString(sense));
		}

		return entry;
	}
}
