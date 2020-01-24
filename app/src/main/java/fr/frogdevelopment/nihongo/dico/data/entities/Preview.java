package fr.frogdevelopment.nihongo.dico.data.entities;

import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class Preview {

	public long sense_id;
	public String kanji;
	public String reading;
	public String gloss;
	public double similarity;
	public boolean favorite;

    public List<Pair<Integer,Integer>> matchIndices = new ArrayList<>();

}
