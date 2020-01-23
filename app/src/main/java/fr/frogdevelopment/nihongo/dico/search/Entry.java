package fr.frogdevelopment.nihongo.dico.search;

import android.text.SpannableString;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

public class Entry implements Serializable {

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
