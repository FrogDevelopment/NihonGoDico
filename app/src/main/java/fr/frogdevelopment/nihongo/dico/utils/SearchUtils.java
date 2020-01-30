package fr.frogdevelopment.nihongo.dico.utils;

public class SearchUtils {

//    public static void computeSimilarity(String query, List<Pattern> patterns, Preview preview, String text) {
//        // keep max similarity
//        preview.similarity = 0;
//        String[] values = text.split(", ");
//        for (String value : values) {
//            double v = InputUtils.computeSimilarity(query, value);
//            if (v > preview.similarity) {
//                preview.similarity = v;
//            }
//        }
//
//        // if only 1 word and match => has to be first multiple values
//        if (values.length == 1 && preview.similarity == 1) {
//            preview.similarity = 2;
//        }
//
//        // get all regions which match
//        for (Pattern pattern : patterns) {
//            Matcher matcher = pattern.matcher(text.toLowerCase());
//            while (matcher.find()) {
//                preview.matchIndices.add(Pair.of(matcher.start(), matcher.end()));
//            }
//        }
//    }
}
