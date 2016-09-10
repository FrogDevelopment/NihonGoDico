package fr.frogdevelopment.nihongo.dico.entities;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Details {

	public String pos;
	public String field;
	public String misc;
	public String info;
	public String dial;
	public String gloss;

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.append("pos", pos)
				.append("field", field)
				.append("misc", misc)
				.append("info", info)
				.append("dial", dial)
				.append("gloss", gloss)
				.toString();
	}
}
