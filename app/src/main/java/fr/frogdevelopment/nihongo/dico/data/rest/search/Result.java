package fr.frogdevelopment.nihongo.dico.data.rest.search;

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
    public int compareTo(Result o) {
        return value.compareTo(o.value);
    }
}
