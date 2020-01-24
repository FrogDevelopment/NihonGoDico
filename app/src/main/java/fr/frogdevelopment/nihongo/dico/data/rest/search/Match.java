package fr.frogdevelopment.nihongo.dico.data.rest.search;

import java.io.Serializable;

public class Match implements Serializable {

    private static final long serialVersionUID = -2986641105608998278L;

    public int start;
    public int end;

    public static Match of(int start, int end) {
        Match match = new Match();
        match.start = start;
        match.end = end;

        return match;
    }
}
