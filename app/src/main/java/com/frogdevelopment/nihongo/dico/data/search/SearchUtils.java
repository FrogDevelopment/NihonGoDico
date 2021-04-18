package com.frogdevelopment.nihongo.dico.data.search;

import com.frogdevelopment.nihongo.dico.utils.Input;

import static com.frogdevelopment.nihongo.dico.utils.InputUtils.containsKanji;
import static com.frogdevelopment.nihongo.dico.utils.InputUtils.isOnlyKana;
import static java.lang.Character.isWhitespace;
import static org.apache.commons.lang3.StringUtils.isBlank;

public class SearchUtils {

    private static final String REGEX_SEARCH_SPLIT = "\\+|!|\\?";

    private SearchUtils() {
    }

    static Input getInputType(final String query) {
        final Input input;
        if (containsKanji(query)) {
            input = Input.KANJI;
        } else if (isOnlyKana(query)) {
            input = Input.KANA;
        } else {
            input = Input.ROMAJI;
        }
        return input;
    }

    static String toFtsQuery(final String query) {

        // query by word1+word2+...
        final String[] searches = query.split(REGEX_SEARCH_SPLIT);

        final StringBuilder stringBuilder = new StringBuilder();
        // if other words, check either include (+) or exclude (-) from query
        for (String search : searches) { // start 1, as first word already proceed
            if (isBlank(search)) {
                continue;
            }

            search = search.trim();

            // check the character in front of word to know if inclusion or exclusion
            int startOfWord = query.indexOf(search);

            if (startOfWord > 0) {
                char charAt;
                do {
                    charAt = query.charAt(--startOfWord);
                } while (isWhitespace(charAt));

                switch (charAt) {
                    case '?': // OR
                        stringBuilder.append(" OR ");
                        break;
                    case '!': // NOT
                        stringBuilder.append(" -");
                        break;
                    default:
                        //
                }
            }

            // phrase query or double-word => enclosing in double quotes
            if (search.split("\\s").length > 1 || search.contains("-")) {
                stringBuilder.append("\"").append(cleanWord(search)).append("\"");
            } else {
                stringBuilder.append(cleanWord(search));
            }
        }

        return stringBuilder.toString();
    }

    private static String cleanWord(final String word) {
        return word
                .trim() // remove leading and trailing spaces
                .replaceAll("'", "''") // replace ['] by [''] for sql syntax
                ; // todo other ?
    }
}
