package com.frogdevelopment.nihongo.dico.to_delete;

import androidx.appcompat.app.AppCompatActivity;

public class OldMainActivity extends AppCompatActivity {

    private static final int LOADER_INIT          = 0;
    private static final int LOADER_DICO_ID_KANJI = 100;
    private static final int LOADER_DICO_ID_KANA  = 200;
    private static final int LOADER_DICO_ID_GLOSS = 300;

    private static final String REGEX_SEARCH_SPLIT = "\\+|!|\\?";

    private String query;

//    private void launchQueryFor(String query) {
////         https://developer.android.com/guide/topics/search/adding-recent-query-suggestions.html
//        SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this, NihonGoDicoContentProvider.AUTHORITY, NihonGoDicoContentProvider.MODE);
//        suggestions.saveRecentQuery(query, null);
//
//        // fixme proposer la possibilité d'effacer l'history
////            suggestions.clearHistory();
//
//        Bundle args = new Bundle();
//        args.putString("query", query.trim());
//
//        int loaderId;
//        if (InputUtils.containsKanji(query)) {
//            loaderId = LOADER_DICO_ID_KANJI;
//        } else if (InputUtils.isOnlyKana(query)) {
//            loaderId = LOADER_DICO_ID_KANA;
//        } else {
//            loaderId = LOADER_DICO_ID_GLOSS;
//        }
//    }

//    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
//
//        final Uri uri;
////        switch (id) {
////            case LOADER_DICO_ID_KANJI:
////                uri = NihonGoDicoContentProvider.URI_SEARCH_KANJI;
////                break;
////            case LOADER_DICO_ID_KANA:
////                uri = NihonGoDicoContentProvider.URI_SEARCH_KANA;
////                break;
////            case LOADER_DICO_ID_GLOSS:
////                uri = NihonGoDicoContentProvider.URI_SEARCH_GLOSS;
////                break;
////            default:
////                return null;
////        }
//
//        query = args.getString("query", "");
//
//        // query by word1+word2+...
//        String[] searches = query.split(REGEX_SEARCH_SPLIT);
//
//        char charAt;
//        StringBuilder selection = new StringBuilder();
//        // if other words, check either include (+) or exclude (-) from query
//        for (String search : searches) { // start 1, as first word already proceed
//            if (TextUtils.isEmpty(search)) {
//                continue;
//            }
//
//            search = search.trim();
//
//            // check the character in front of word to know if inclusion or exclusion
//            int startOfWord = query.indexOf(search);
//
//            if (startOfWord > 0) {
//                do {
//                    charAt = query.charAt(--startOfWord);
//                } while (Character.isWhitespace(charAt));
//
//                switch (charAt) {
//                    case '?': // OR
//                        selection.append(" OR ");
//                        break;
//                    case '!': // NOT
//                        selection.append(" -");
//                        break;
//                }
//            }
//
//            if (search.split("\\s").length > 1 || search.contains("-")) { // phrase query or double-word => enclosing in double quotes
//                selection.append("\"").append(cleanWord(search)).append("\"");
//            } else {
//                selection.append(cleanWord(search));
//            }
//        }
//
//        return new CursorLoader(this, uri, null, selection.toString(), null, null);
//    }

//    private static String cleanWord(String word) {
//        return word
//                .trim() // remove leading and trailing spaces
//                .replace("'", "''") // replace ['] by [''] for sql syntax
//                ; // todo other ?
//    }

//    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
//        int loaderId = loader.getId();
//        if (data.getCount() > 0) {
//
//            List<Pattern> patterns = new ArrayList<>();
//            String[] searches = query.split(REGEX_SEARCH_SPLIT);
//
//            for (String search : searches) {
//                search = search.replace("*", ""); // pattern without the * char
//                // check the character in front of word to know if inclusion or exclusion
//                int indexOfWord = query.indexOf(search);
//                if (indexOfWord > 0) {
//                    char charAt;
//                    do {
//                        charAt = query.charAt(--indexOfWord);
//                    } while (Character.isWhitespace(charAt));
//
//                    if (charAt == '?') {
//                        patterns.add(Pattern.compile(Pattern.quote(search.trim().toLowerCase())));
//                    }
//                } else {
//                    patterns.add(Pattern.compile(Pattern.quote(search.trim().toLowerCase())));
//                }
//            }
//
//            List<Preview> previews = new ArrayList<>();
//            while (data.moveToNext()) {
//                Preview preview = new Preview();
//                preview.kanji = data.getString(0);
//                preview.reading = data.getString(1);
//                preview.gloss = data.getString(2);
//                preview.sense_id = data.getLong(3);
//                preview.favorite = data.getInt(4) == 1;
//
//                String text;
//                switch (loaderId) {
//                    case LOADER_DICO_ID_KANJI:
//                        text = preview.kanji;
//                        break;
//                    case LOADER_DICO_ID_KANA:
//                        text = preview.reading;
//                        break;
//                    case LOADER_DICO_ID_GLOSS:
//                    default:
//                        text = preview.gloss;
//                }
//
//                computeSimilarity(patterns, preview, text);
//
//                previews.add(preview);
//            }
//
//            // sort by descending similarity score
//            Collections.sort(previews, (p1, p2) -> Double.compare(p2.similarity, p1.similarity));
//
//            // adapter by research type
//            switch (loaderId) {
//                case LOADER_DICO_ID_KANJI:
//                    mAdapter = new ResearchByKanjiAdapter(this, previews);
//                    break;
//                case LOADER_DICO_ID_KANA:
//                    mAdapter = new ResearchByKanaAdapter(this, previews);
//                    break;
//                case LOADER_DICO_ID_GLOSS:
//                default:
//                    mAdapter = new ResearchByGlossAdapter(this, previews);
//            }
//
//            mListView.setAdapter(mAdapter);
//
//            SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this, NihonGoDicoContentProvider.AUTHORITY, NihonGoDicoContentProvider.MODE);
//            suggestions.saveRecentQuery(query, String.valueOf(previews.size() + " results"));
//
//            mListView.setVisibility(View.VISIBLE);
//            mTipsView.setVisibility(View.GONE);
//        } else {
//            Snackbar.make(findViewById(R.id.activity_main), R.string.no_results, Snackbar.LENGTH_LONG).show();
//        }
//
//        data.close();
//        getLoaderManager().destroyLoader(loaderId);
//
//        mProgressBar.setVisibility(View.INVISIBLE);
//        mSearchView.clearFocus();
//    }

}
