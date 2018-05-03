package fr.frogdevelopment.nihongo.dico.search;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class Match implements Serializable {

    private static final long serialVersionUID = -2986641105608998278L;

    public int start;
    public int end;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("start", start)
                .append("end", end)
                .toString();
    }

    public static Match of(int start, int end) {
        Match match = new Match();
        match.start = start;
        match.end = end;

        return match;
    }
}
