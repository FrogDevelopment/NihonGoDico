package com.frogdevelopment.nihongo.dico.data.search;

import com.frogdevelopment.nihongo.dico.data.entities.EntrySearch;
import com.frogdevelopment.nihongo.dico.utils.Input;

import static java.nio.charset.StandardCharsets.UTF_8;

public class ComputeSimilarity {

    static void computeSimilarity(final EntrySearch row, final String query, final Input input) {
        final String value;
        switch (input) {
            case KANJI:
            case KANA:
                if (input == Input.KANJI) {
                    value = row.getKanji();
                } else {
                    value = row.getKana();
                }

                // compute similarity
                row.setSimilarity(computeSimilarity(query, value));

                // if only 1 word and match => has to be first multiple values => fixme ???? don't remember why ô0 !!
                if (row.getSimilarity() == 1) {
                    row.setSimilarity(2);
                }
                break;
            default:
                value = row.getVocabulary();
                final double similarity = computeSimilarity(query, value);
                if (similarity > row.getSimilarity()) {
                    row.setSimilarity(similarity);
                }
                break;
        }
    }

    // http://rosettacode.org/wiki/Levenshtein_distance#Java
    private static double computeSimilarity(final String s1, final String s2) {
        String s1_ = new String(s1.getBytes(UTF_8));
        String s2_ = new String(s2.getBytes(UTF_8));

        if (s1_.length() < s2_.length()) { // s1 should always be bigger
            final String swap = s1_;
            s1_ = s2_;
            s2_ = swap;
        }
        final int bigLen = s1_.length();
        if (bigLen == 0) {
            return 1.0; /* both strings are zero length */
        }
        return (bigLen - computeEditDistance(s1_, s2_)) / (double) bigLen;
    }

    // http://en.wikibooks.org/wiki/Algorithm_Implementation/Strings/Levenshtein_distance#Java
    private static int computeEditDistance(final String s1, final String s2) {

        final int len0 = s1.length() + 1;
        final int len1 = s2.length() + 1;

        // the array of distances
        int[] oldCost = new int[len0]; // j -2
        int[] cost = new int[len0]; // j-1
        int[] newCost = new int[len0]; // j

        // initial cost of skipping prefix in String s1
        for (int i = 0; i < len0; i++) {
            cost[i] = i;
        }

        // dynamically computing the array of distances

        // transformation cost for each letter in s2
        for (int j = 1; j < len1; j++) {

            // initial cost of skipping prefix in String s2
            newCost[0] = j - 1;

            // transformation cost for each letter in s1
            for (int i = 1; i < len0; i++) {

                // matching current letters in both strings
                final int match = s1.charAt(i - 1) == s2.charAt(j - 1) ? 0 : 1;

                // computing cost for each transformation
                final int costDelete = newCost[i - 1] + 1; // deletion
                final int costInsert = cost[i] + 1; // insertion
                final int costReplace = cost[i - 1] + match; // substitution

                // keep minimum cost
                newCost[i] = Math.min(Math.min(costInsert, costDelete), costReplace);

                // http://fr.wikipedia.org/wiki/Distance_de_Damerau-Levenshtein
                if (i > 1 && j > 1 && s1.charAt(i - 1) == s2.charAt(j - 2) && s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    final int costTransposition = oldCost[i - 2] + match; // transposition

                    newCost[i] = Math.min(newCost[i], costTransposition);
                }
            }

            // swap cost/new cost arrays
            oldCost = cost;
            final int[] swap = cost;
            cost = newCost;
            newCost = swap;
        }

        // the distance is the cost for transforming all letters in both strings
        return cost[len0 - 1];
    }
}
