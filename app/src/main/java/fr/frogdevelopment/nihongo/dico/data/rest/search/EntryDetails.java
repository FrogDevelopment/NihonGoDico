package fr.frogdevelopment.nihongo.dico.data.rest.search;

import java.io.Serializable;
import java.util.Set;

public class EntryDetails implements Serializable {

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
