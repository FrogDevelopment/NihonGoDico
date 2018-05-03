package fr.frogdevelopment.nihongo.dico.search;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Result implements Serializable, Comparable<Result> {

    private static final long serialVersionUID = -8196673041691485655L;

    public String value;
    public List<Match> matches = new ArrayList<>();

    static Result of(String value) {
        Result result = new Result();
        result.value = value;

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Result result = (Result) o;

        return new EqualsBuilder()
                .append(value, result.value)
                .append(matches, result.matches)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(value)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("value", value)
                .append("matches", matches)
                .toString();
    }

    @Override
    public int compareTo(Result o) {
        return new CompareToBuilder()
                .append(this.value, o.value)
                .toComparison();
    }
}
