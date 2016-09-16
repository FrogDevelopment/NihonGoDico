package fr.frogdevelopment.nihongo.dico.entities;

import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class Example {

	public final String japanese;
	public final String translation;

	public List<Pair<Integer,Integer>> matchIndices = new ArrayList<>();

	public Example(String japanese, String translation) {
		this.japanese = japanese;
		this.translation = translation;
	}
}
