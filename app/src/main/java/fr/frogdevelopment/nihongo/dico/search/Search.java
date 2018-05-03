package fr.frogdevelopment.nihongo.dico.search;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Search implements Serializable {

    private static final long serialVersionUID = 7444239611921721030L;

    public long seq;
    public Result kanji;
    public Result kana;
    public long senseId;
    public List<Result> senses = new ArrayList<>();
    public double similarity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Search search = (Search) o;

        return new EqualsBuilder()
                .append(seq, search.seq)
                .append(senseId, search.senseId)
                .append(kanji, search.kanji)
                .append(kana, search.kana)
                .append(senses, search.senses)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(seq)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("seq", seq)
                .append("kanji", kanji)
                .append("kana", kana)
                .append("senseId", senseId)
                .append("senses", senses)
                .toString();
    }
}
