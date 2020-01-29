package fr.frogdevelopment.nihongo.dico.data.rest;

import android.text.SpannableString;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

public class Entry implements Serializable {

    private static final long serialVersionUID = 1L;

    public String senseSeq;
    public String kanji;
    public String kana;
    public String vocabulary;

    @JsonIgnore
    public SpannableString kanjiSpannable;
    @JsonIgnore
    public SpannableString kanaSpannable;
    @JsonIgnore
    public SpannableString vocabularySpannable;
}
