package fr.frogdevelopment.nihongo.dico.search;

import java.io.Serializable;

public class Entry implements Serializable {

    public long sensSeq;
    public String kanji;
    public String kana;
    public String vocabulary;
    public double similarity;

}
