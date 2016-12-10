package fr.frogdevelopment.nihongo.dico.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @see <a href='https://github.com/nicolas-raoul/jakaroma'>Jakaroma</a>
 */
public class KanaToRomaji {

    private static final Map<String, String> KATAKANAS = new HashMap<>();
    private static final Map<String, String> HIRAGANAS = new HashMap<>();

    // static init
    static {
        // KATAKANA
        // column A
        KATAKANAS.put("\u30a2", "a");
        KATAKANAS.put("\u30a4", "i");
        KATAKANAS.put("\u30a6", "u");
        KATAKANAS.put("\u30a8", "e");
        KATAKANAS.put("\u30aa", "o");
        // column KA
        KATAKANAS.put("\u30ab", "ka");
        KATAKANAS.put("\u30ad", "ki");
        KATAKANAS.put("\u30af", "ku");
        KATAKANAS.put("\u30b1", "ke");
        KATAKANAS.put("\u30b3", "ko");
        // column SA
        KATAKANAS.put("\u30b5", "sa");
        KATAKANAS.put("\u30b7", "shi");
        KATAKANAS.put("\u30b9", "su");
        KATAKANAS.put("\u30bb", "se");
        KATAKANAS.put("\u30bd", "so");
        // column TA
        KATAKANAS.put("\u30bf", "ta");
        KATAKANAS.put("\u30c1", "chi");
        KATAKANAS.put("\u30c4", "tsu");
        KATAKANAS.put("\u30c6", "te");
        KATAKANAS.put("\u30c8", "to");
        // column NA
        KATAKANAS.put("\u30ca", "na");
        KATAKANAS.put("\u30cb", "ni");
        KATAKANAS.put("\u30cc", "nu");
        KATAKANAS.put("\u30cd", "ne");
        KATAKANAS.put("\u30ce", "no");
        // column HA
        KATAKANAS.put("\u30cf", "ha");
        KATAKANAS.put("\u30d2", "hi");
        KATAKANAS.put("\u30d5", "fu");
        KATAKANAS.put("\u30d8", "he");
        KATAKANAS.put("\u30db", "ho");
        // column MA
        KATAKANAS.put("\u30de", "ma");
        KATAKANAS.put("\u30df", "mi");
        KATAKANAS.put("\u30e0", "mu");
        KATAKANAS.put("\u30e1", "me");
        KATAKANAS.put("\u30e2", "mo");
        // column YA
        KATAKANAS.put("\u30e4", "ya");
        KATAKANAS.put("\u30e6", "yu");
        KATAKANAS.put("\u30e8", "yo");
        // column RA
        KATAKANAS.put("\u30e9", "ra");
        KATAKANAS.put("\u30ea", "ri");
        KATAKANAS.put("\u30eb", "ru");
        KATAKANAS.put("\u30ec", "re");
        KATAKANAS.put("\u30ed", "ro");
        // column WA
        KATAKANAS.put("\u30ef", "wa");
        KATAKANAS.put("\u30f2", "wo");
        // column N
        KATAKANAS.put("\u30f3", "n");
        // column GA
        KATAKANAS.put("\u30ac", "ga");
        KATAKANAS.put("\u30ae", "gi");
        KATAKANAS.put("\u30b0", "gu");
        KATAKANAS.put("\u30b2", "ge");
        KATAKANAS.put("\u30b4", "go");
        // column ZA
        KATAKANAS.put("\u30b6", "za");
        KATAKANAS.put("\u30b8", "ji");
        KATAKANAS.put("\u30ba", "zu");
        KATAKANAS.put("\u30bc", "ze");
        KATAKANAS.put("\u30be", "zo");
        // column DA
        KATAKANAS.put("\u30c0", "da");
        KATAKANAS.put("\u30c2", "ji");
        KATAKANAS.put("\u30c5", "zu");
        KATAKANAS.put("\u30c7", "de");
        KATAKANAS.put("\u30c9", "do");
        // column BA
        KATAKANAS.put("\u30d0", "ba");
        KATAKANAS.put("\u30d3", "bi");
        KATAKANAS.put("\u30d6", "bu");
        KATAKANAS.put("\u30d9", "be");
        KATAKANAS.put("\u30dc", "bo");
        // column PA
        KATAKANAS.put("\u30d1", "pa");
        KATAKANAS.put("\u30d4", "pi");
        KATAKANAS.put("\u30d7", "pu");
        KATAKANAS.put("\u30da", "pe");
        KATAKANAS.put("\u30dd", "po");
        // column KYA
        KATAKANAS.put("\u30ad\u30e3", "kya");
        KATAKANAS.put("\u30ad\u30e5", "kyu");
        KATAKANAS.put("\u30ad\u30e7", "kyo");
        // column SHA　
        KATAKANAS.put("\u30b7\u30e3", "sha");
        KATAKANAS.put("\u30b7\u30e5", "shu");
        KATAKANAS.put("\u30b7\u30e7", "sho");
        // column CHA　
        KATAKANAS.put("\u30c1\u30e3", "cha");
        KATAKANAS.put("\u30c1\u30e5", "chu");
        KATAKANAS.put("\u30c1\u30e7", "cho");
        // column NYA
        KATAKANAS.put("\u30cb\u30e3", "nya");
        KATAKANAS.put("\u30cb\u30e5", "nyu");
        KATAKANAS.put("\u30cb\u30e7", "nyo");
        // column MYA
        KATAKANAS.put("\u30df\u30e3", "mya");
        KATAKANAS.put("\u30df\u30e5", "myu");
        KATAKANAS.put("\u30df\u30e7", "myo");
        // column HYA
        KATAKANAS.put("\u30d2\u30e3", "hya");
        KATAKANAS.put("\u30d2\u30e5", "hyu");
        KATAKANAS.put("\u30d2\u30e7", "hyo");
        // column RYA
        KATAKANAS.put("\u30ea\u30e3", "rya");
        KATAKANAS.put("\u30ea\u30e5", "ryu");
        KATAKANAS.put("\u30ea\u30e7", "ryo");
        // column GYA
        KATAKANAS.put("\u30ae\u30e3", "gya");
        KATAKANAS.put("\u30ae\u30e5", "gyu");
        KATAKANAS.put("\u30ae\u30e7", "gyo");
        // column JA
        KATAKANAS.put("\u30b8\u30e3", "ja");
        KATAKANAS.put("\u30b8\u30e5", "ju");
        KATAKANAS.put("\u30b8\u30e7", "jo");
        // column
        KATAKANAS.put("\u30c6\u30a3", "ti");
        KATAKANAS.put("", "fi");// FIXME
        KATAKANAS.put("\u30c7\u30a3", "di");
        // column
        KATAKANAS.put("", "tsa");// FIXME
        KATAKANAS.put("\u30c4\u30a3", "tsi");
        // column　DYA
        KATAKANAS.put("\u30c2\u30e3", "dya");
        KATAKANAS.put("\u30c2\u30e5", "dyu");
        KATAKANAS.put("\u30c2\u30e7", "dyo");
        // column BYA
        KATAKANAS.put("\u30d3\u30e3", "bya");
        KATAKANAS.put("\u30d3\u30e5", "byu");
        KATAKANAS.put("\u30d3\u30e7", "byo");
        // column PYA
        KATAKANAS.put("\u30d4\u30e3", "pya");
        KATAKANAS.put("\u30d4\u30e5", "pyu");
        KATAKANAS.put("\u30d4\u30e7", "pyo");

        KATAKANAS.put("\u30d5\u30a1", "fa");
        KATAKANAS.put("\u30a6\u30a3", "wi");
        KATAKANAS.put("\u30c8\u30a5", "tu");
        KATAKANAS.put("\u30c9\u30a5", "du");
        KATAKANAS.put("\u30c7\u30a5", "dyu");
        KATAKANAS.put("\u30a6\u30a7", "we");
        KATAKANAS.put("\u30b7\u30a7", "she");
        KATAKANAS.put("\u30c1\u30a7", "che");
        KATAKANAS.put("\u30c4\u30a7", "tse");
        KATAKANAS.put("\u30d5\u30a7", "fe");
        KATAKANAS.put("\u30b8\u30a7", "je");
        KATAKANAS.put("\u30a6\u30a9", "wo");
        KATAKANAS.put("\u30c4\u30a9", "tso");
        KATAKANAS.put("\u30d5\u30a9", "fo");
        // tild
        KATAKANAS.put("\u30fc", "-");

        // HIRAGANA

        // column A
        HIRAGANAS.put("\u3042", "a");
        HIRAGANAS.put("\u3044", "i");
        HIRAGANAS.put("\u3046", "u");
        HIRAGANAS.put("\u3048", "e");
        HIRAGANAS.put("\u304a", "o");
        // column KA
        HIRAGANAS.put("\u304b", "ka");
        HIRAGANAS.put("\u304d", "ki");
        HIRAGANAS.put("\u304f", "ku");
        HIRAGANAS.put("\u3051", "ke");
        HIRAGANAS.put("\u3053", "ko");
        // column SA
        HIRAGANAS.put("\u3055", "sa");
        HIRAGANAS.put("\u3057", "shi");
        HIRAGANAS.put("\u3059", "su");
        HIRAGANAS.put("\u305b", "se");
        HIRAGANAS.put("\u305d", "so");
        // column TA
        HIRAGANAS.put("\u305f", "ta");
        HIRAGANAS.put("\u3061", "chi");
        HIRAGANAS.put("\u3064", "tsu");
        HIRAGANAS.put("\u3066", "te");
        HIRAGANAS.put("\u3068", "to");
        // column NA
        HIRAGANAS.put("\u306a", "na");
        HIRAGANAS.put("\u306b", "ni");
        HIRAGANAS.put("\u306c", "nu");
        HIRAGANAS.put("\u306d", "ne");
        HIRAGANAS.put("\u306e", "no");
        // column HA
        HIRAGANAS.put("\u306f", "ha");
        HIRAGANAS.put("\u3072", "hi");
        HIRAGANAS.put("\u3075", "fu");
        HIRAGANAS.put("\u3078", "he");
        HIRAGANAS.put("\u307b", "ho");
        // column MA
        HIRAGANAS.put("\u307e", "ma");
        HIRAGANAS.put("\u307f", "mi");
        HIRAGANAS.put("\u3080", "mu");
        HIRAGANAS.put("\u3081", "me");
        HIRAGANAS.put("\u3082", "mo");
        // column YA
        HIRAGANAS.put("\u3084", "ya");
        HIRAGANAS.put("\u3086", "yu");
        HIRAGANAS.put("\u3088", "yo");
        // column RA
        HIRAGANAS.put("\u3089", "ra");
        HIRAGANAS.put("\u308a", "ri");
        HIRAGANAS.put("\u308b", "ru");
        HIRAGANAS.put("\u308c", "re");
        HIRAGANAS.put("\u308d", "ro");
        // column WA
        HIRAGANAS.put("\u308f", "wa");
        HIRAGANAS.put("\u3092", "wo");
        // column N
        HIRAGANAS.put("\u3093", "n");
        // column GA
        HIRAGANAS.put("\u304c", "ga");
        HIRAGANAS.put("\u304e", "gi");
        HIRAGANAS.put("\u3050", "gu");
        HIRAGANAS.put("\u3052", "ge");
        HIRAGANAS.put("\u3054", "go");
        // column ZA
        HIRAGANAS.put("\u3056", "za");
        HIRAGANAS.put("\u3058", "ji");
        HIRAGANAS.put("\u305a", "zu");
        HIRAGANAS.put("\u305c", "ze");
        HIRAGANAS.put("\u305e", "zo");
        // column DA
        HIRAGANAS.put("\u3060", "da");
        HIRAGANAS.put("\u3058", "ji");
        HIRAGANAS.put("\u305a", "zu");
        HIRAGANAS.put("\u3067", "de");
        HIRAGANAS.put("\u3069", "do");
        // column BA
        HIRAGANAS.put("\u3070", "ba");
        HIRAGANAS.put("\u3073", "bi");
        HIRAGANAS.put("\u3076", "bu");
        HIRAGANAS.put("\u3079", "be");
        HIRAGANAS.put("\u307c", "bo");
        // column PA
        HIRAGANAS.put("\u3071", "pa");
        HIRAGANAS.put("\u3074", "pi");
        HIRAGANAS.put("\u3077", "pu");
        HIRAGANAS.put("\u307a", "pe");
        HIRAGANAS.put("\u307d", "po");
        // column KYA
        HIRAGANAS.put("\u304d\u3083", "kya");
        HIRAGANAS.put("\u304d\u3085", "kyu");
        HIRAGANAS.put("\u304d\u3087", "kyo");
        // column SHA
        HIRAGANAS.put("\u3057\u3083", "sha");
        HIRAGANAS.put("\u3057\u3085", "shu");
        HIRAGANAS.put("\u3057\u3087", "sho");
        // column CHA
        HIRAGANAS.put("\u3061\u3083", "cha");
        HIRAGANAS.put("\u3061\u3085", "chu");
        HIRAGANAS.put("\u3061\u3087", "cho");
        // column NYA
        HIRAGANAS.put("\u306b\u3083", "nya");
        HIRAGANAS.put("\u306b\u3085", "nyu");
        HIRAGANAS.put("\u306b\u3087", "nyo");
        // column NYA
        HIRAGANAS.put("\u307f\u3083", "nya");
        HIRAGANAS.put("\u307f\u3085", "nyu");
        HIRAGANAS.put("\u307f\u3087", "nyo");
        // column HYA
        HIRAGANAS.put("\u3072\u3083", "hya");
        HIRAGANAS.put("\u3072\u3085", "hyu");
        HIRAGANAS.put("\u3072\u3087", "hyo");
        // column RYA
        HIRAGANAS.put("\u308a\u3083", "rya");
        HIRAGANAS.put("\u308a\u3085", "ryu");
        HIRAGANAS.put("\u308a\u3087", "ryo");
        // column GYA
        HIRAGANAS.put("\u304e\u3083", "gya");
        HIRAGANAS.put("\u304e\u3085", "gyu");
        HIRAGANAS.put("\u304e\u3087", "gyo");
        // column JA
        HIRAGANAS.put("\u3058\u3083", "ja");
        HIRAGANAS.put("\u3058\u3085", "ju");
        HIRAGANAS.put("\u3058\u3087", "jo");
        // column BYA
        HIRAGANAS.put("\u3073\u3083", "bya");
        HIRAGANAS.put("\u3073\u3085", "byu");
        HIRAGANAS.put("\u3073\u3087", "byo");
        // column PYA
        HIRAGANAS.put("\u3074\u3083", "pya");
        HIRAGANAS.put("\u3074\u3085", "pyu");
        HIRAGANAS.put("\u3074\u3087", "pyo");
        // tild
        HIRAGANAS.put("\u30fc", "-");
    }

    public static String convert(String value) {
        if (InputUtils.isOnlyHiragana(value)) {
            return doConvert(value, HIRAGANAS, '\u3063');
        } else if (InputUtils.isOnlyKatakana(value)) {
            return doConvert(value, KATAKANAS, '\u30c3');
        } else {
            // fixme handle both together
            return value;
        }
    }

    private static String doConvert(String value, Map<String, String> map, char littleTsu) {
        StringBuilder romanji = new StringBuilder();

        int length = value.length();
        int minus1Character = length - 2;

        String substring;
        for (int i = 0; i < length; i++) {
            if (i <= minus1Character) {
                // KYA, SHA, NYA ...
                substring = value.substring(i, i + 2);
                if (map.containsKey(substring)) {
                    romanji.append(map.get(substring));
                    // increment 1 more as we take 2 characters
                    i++;
                } else {
                    // A, KA, SA, TA ...
                    substring = value.substring(i, i + 1);
                    if (map.containsKey(substring)) {
                        romanji.append(map.get(substring));
                    } else {
                        // LITTLE TSU
                        if (value.charAt(i) == littleTsu) {
                            // skip little tsu and take next character
                            substring = value.substring(i + 1, i + 2);
                            // add only consonant
                            romanji.append(map.get(substring).charAt(0));
                        } else {
                            // UNKNOWN
                            romanji.append(value.charAt(i));
                        }
                    }
                }
            } else { // last character
                // A, KA, SA, TA ...
                substring = value.substring(i, i + 1);
                if (map.containsKey(substring)) {
                    romanji.append(map.get(substring));
                } else {
                    // UNKNOWN
                    romanji.append(value.charAt(i));
                }
            }
        }

        return romanji.toString();
    }

}
