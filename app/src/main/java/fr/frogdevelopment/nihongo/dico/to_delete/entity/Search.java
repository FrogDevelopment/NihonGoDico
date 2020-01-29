package fr.frogdevelopment.nihongo.dico.to_delete.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Search implements Serializable {

    private static final long serialVersionUID = 7444239611921721030L;

    public long seq;
    public Result kanji;
    public Result kana;
    public long senseId;
    public List<Result> senses = new ArrayList<>();
    public double similarity;

}
