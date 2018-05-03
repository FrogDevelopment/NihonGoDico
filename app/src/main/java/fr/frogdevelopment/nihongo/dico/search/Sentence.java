package fr.frogdevelopment.nihongo.dico.search;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class Sentence implements Serializable {

    private static final long serialVersionUID = 6791663241781942819L;

    public Result japanese;
    public Result translation;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Sentence sentence = (Sentence) o;

        return new EqualsBuilder()
                .append(japanese.value, sentence.japanese.value)
                .append(translation.value, sentence.translation.value)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(japanese.value)
                .append(translation.value)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("japanese", japanese)
                .append("translation", translation)
                .toString();
    }
}
