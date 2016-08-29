/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.nihongo.dico.utils;

import java.nio.charset.StandardCharsets;

/**
 * @see <a href="http://www.rikai.com/library/kanjitables/kanji_codes.unicode.shtml">kanji_codes.unicode</a>
 * @see <a href="http://en.wikipedia.org/wiki/Japanese_writing_system">Japanese_writing_system</a>
 */
public class InputUtils {

    public static final char TILD = 0xFF5E; // '~'
    public static final char WAVE_DASH = 0x301C; // '～'
    public static final char TOTEN = 0x3001; // '、'

    // Japanese-style punctuation ( 3000 - 303f)
    private static final int RANGE_PUNCTUATION_START = 0x3000;
    private static final int RANGE_PUNCTUATION_END = 0x303F;

    // Hiragana ( 3040 - 309f)
    private static final int RANGE_HIRAGANA_START = 0x3040;
    private static final int RANGE_HIRAGANA_END = 0x309F;

    // Katakana ( 30a0 - 30ff)
    private static final int RANGE_KATAKANA_START = 0x30A0;
    private static final int RANGE_KATAKANA_END = 0x30FF;

    // Full-width roman characters and half-width katakana ( ff00 - ffef)
    private static final int RANGE_ROMAN_START = 0xFF00;
    private static final int RANGE_ROMAN_END = 0xFFEF;

    //    CJK unified ideographs - Common and uncommon kanji ( 4e00 - 9fbf)
    private static final int RANGE_KANJI_START = 0x4E00;
    private static final int RANGE_KANJI_END = 0x9fbf;

    public static boolean isOnlyHiragana(final String input) {
        return isOnRange(input, RANGE_HIRAGANA_START, RANGE_HIRAGANA_END);
    }

    public static boolean isOnlyKatakana(final String input) {
        return isOnRange(input, RANGE_KATAKANA_START, RANGE_KATAKANA_END);
    }

    public static boolean isOnlyKanji(final String input) {
        return isOnRange(input, RANGE_KANJI_START, RANGE_KANJI_END);
    }

    private static boolean isOnRange(final String input, final int startRange, final int endRange) {
        for (char ch : input.toCharArray()) {
            if (ch >= RANGE_PUNCTUATION_START && ch <= RANGE_PUNCTUATION_END) { // PUNCTUATION
                continue;
            }

            if (ch < startRange || ch > endRange) {
                return false;
            }
        }
        return true;
    }

    @SuppressWarnings("UnnecessaryContinue")
    public static boolean isOnlyJapanese(String input) {
        boolean isOnlyJapanese = true;
        for (Character ch : input.toCharArray()) {
            if (ch >= RANGE_PUNCTUATION_START && ch <= RANGE_PUNCTUATION_END) { // PUNCTUATION
                continue;
            } else if (ch >= RANGE_ROMAN_START && ch <= RANGE_ROMAN_END) { // ROMAN
                continue;
            } else if (ch >= RANGE_HIRAGANA_START && ch <= RANGE_HIRAGANA_END) { // HIRAGANA
                continue;
            } else if (ch >= RANGE_KATAKANA_START && ch <= RANGE_KATAKANA_END) { // KATAKANA
                continue;
            } else if (ch >= RANGE_KANJI_START && ch <= RANGE_KANJI_END) { // KANJI
                continue;
            } else if (ch == TILD || ch == WAVE_DASH) { // todo http://stackoverflow.com/questions/14450187/having-trouble-with-japanese-character-in-android
                continue;
            } else {
                isOnlyJapanese = false;
                break;
            }
        }
        return isOnlyJapanese;
    }

    @SuppressWarnings("UnnecessaryContinue")
    public static boolean isOnlyKana(final String input) {
        boolean isOnlyKana = true;
        for (Character ch : input.toCharArray()) {
            if (ch >= RANGE_PUNCTUATION_START && ch <= RANGE_PUNCTUATION_END) { // PUNCTUATION
                continue;
            } else if (ch >= RANGE_ROMAN_START && ch <= RANGE_ROMAN_END) { // ROMAN
                continue;
            } else if (ch >= RANGE_HIRAGANA_START && ch <= RANGE_HIRAGANA_END) { // HIRAGANA
                continue;
            } else if (ch >= RANGE_KATAKANA_START && ch <= RANGE_KATAKANA_END) { // KATAKANA
                continue;
            } else {
                isOnlyKana = false;
                break;
            }
        }
        return isOnlyKana;
    }

    public static boolean containsJapanese(String input) {
        boolean containsJapanese = false;
        for (char ch : input.toCharArray()) {
            if (ch >= RANGE_HIRAGANA_START && ch <= RANGE_HIRAGANA_END) { // HIRAGANA
                containsJapanese = true;
                break;
            } else if (ch >= RANGE_KATAKANA_START && ch <= RANGE_KATAKANA_END) { // KATAKANA
                containsJapanese = true;
                break;
            } else if (ch >= RANGE_KANJI_START && ch <= RANGE_KANJI_END) { // KANJI
                containsJapanese = true;
                break;
            }
        }
        return containsJapanese;
    }

    public static boolean containsNoJapanese(String input) {
        return !containsJapanese(input);
    }

    public static boolean containsKanji(String input) {
        boolean containsKanji = false;
        for (char ch : input.toCharArray()) {
            if (ch >= RANGE_KANJI_START && ch <= RANGE_KANJI_END) { // KANJI
                containsKanji = true;
                break;
            }
        }
        return containsKanji;
    }

    // http://rosettacode.org/wiki/Levenshtein_distance#Java
    public static double computeSimilarity(String s1, String s2) {
        s1 = new String(s1.getBytes(StandardCharsets.UTF_8));
        s2 = new String(s2.getBytes(StandardCharsets.UTF_8));

        if (s1.length() < s2.length()) { // s1 should always be bigger
            String swap = s1;
            s1 = s2;
            s2 = swap;
        }
        int bigLen = s1.length();
        if (bigLen == 0) {
            return 1.0; /* both strings are zero length */
        }
        return (bigLen - computeEditDistance(s1, s2)) / (double) bigLen;
    }

    // http://en.wikibooks.org/wiki/Algorithm_Implementation/Strings/Levenshtein_distance#Java
    public static int computeEditDistance(String s0, String s1) {

        int len0 = s0.length() + 1;
        int len1 = s1.length() + 1;

        // the array of distances
        int[] oldcost = new int[len0]; // j -2
        int[] cost = new int[len0]; // j-1
        int[] newcost = new int[len0]; // j

        // initial cost of skipping prefix in String s0
        for (int i = 0; i < len0; i++) {
            cost[i] = i;
        }

        // dynamicaly computing the array of distances

        // transformation cost for each letter in s1
        for (int j = 1; j < len1; j++) {

            // initial cost of skipping prefix in String s1
            newcost[0] = j - 1;

            // transformation cost for each letter in s0
            for (int i = 1; i < len0; i++) {

                // matching current letters in both strings
                int match = s0.charAt(i - 1) == s1.charAt(j - 1) ? 0 : 1;

                // computing cost for each transformation
                int cost_delete = newcost[i - 1] + 1; // deletion
                int cost_insert = cost[i] + 1; // insertion
                int cost_replace = cost[i - 1] + match; // substitution

                // keep minimum cost
                newcost[i] = Math.min(Math.min(cost_insert, cost_delete), cost_replace);

                // http://fr.wikipedia.org/wiki/Distance_de_Damerau-Levenshtein
                if (i > 1 && j > 1 && s0.charAt(i - 1) == s1.charAt(j - 2) && s0.charAt(i - 1) == s1.charAt(j - 1)) {
                    int cost_transposition = oldcost[i - 2] + match; // transposition

                    newcost[i] = Math.min(newcost[i], cost_transposition);
                }
            }

            // swap cost/newcost arrays
            oldcost = cost;
            int[] swap = cost;
            cost = newcost;
            newcost = swap;
        }

        // the distance is the cost for transforming all letters in both strings
        return cost[len0 - 1];
    }

}
