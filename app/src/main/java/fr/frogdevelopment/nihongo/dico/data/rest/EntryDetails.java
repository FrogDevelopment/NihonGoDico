package fr.frogdevelopment.nihongo.dico.data.rest;

import java.io.Serializable;
import java.util.Set;

public class EntryDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    public int entrySeq;
    public String kanji;
    public String kana;
    public String reading;
    public Set<String> pos;
    public Set<String> field;
    public Set<String> misc;
    public String info;
    public Set<String> dial;
    public String gloss;
}
