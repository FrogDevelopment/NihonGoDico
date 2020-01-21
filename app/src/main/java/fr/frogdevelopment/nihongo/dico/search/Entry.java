package fr.frogdevelopment.nihongo.dico.search;

import androidx.annotation.NonNull;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class Entry implements Serializable {

    public long sensSeq;
    public String kanji;
    public String kana;
    public String vocabulary;
    public double similarity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Entry search = (Entry) o;

        return new EqualsBuilder()
                .append(sensSeq, search.sensSeq)
                .append(kanji, search.kanji)
                .append(kana, search.kana)
                .append(vocabulary, search.vocabulary)
                .append(similarity, search.similarity)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(sensSeq)
                .toHashCode();
    }

    @NonNull
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("sensSeq", sensSeq)
                .append("kanji", kanji)
                .append("kana", kana)
                .append("vocabulary", vocabulary)
                .append("similarity", similarity)
                .toString();
    }
}
