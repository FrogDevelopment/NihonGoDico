package fr.frogdevelopment.nihongo.dico.search;

import android.text.SpannableStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

public class Entry implements Serializable {

    public String senseSeq;
    public String kanji;
    public String kana;
    public String vocabulary;
    public double similarity;

    @JsonIgnore
    public SpannableStringBuilder vocabularySpannable;
}