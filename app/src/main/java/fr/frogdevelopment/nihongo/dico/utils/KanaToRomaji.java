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
        KATAKANAS.put("ア", "a");
        KATAKANAS.put("イ", "i");
        KATAKANAS.put("ウ", "u");
        KATAKANAS.put("エ", "e");
        KATAKANAS.put("オ", "o");
        // column KA
        KATAKANAS.put("カ", "ka");
        KATAKANAS.put("キ", "ki");
        KATAKANAS.put("ク", "ku");
        KATAKANAS.put("ケ", "ke");
        KATAKANAS.put("コ", "ko");
        // column SA
        KATAKANAS.put("サ", "sa");
        KATAKANAS.put("シ", "shi");
        KATAKANAS.put("ス", "su");
        KATAKANAS.put("セ", "se");
        KATAKANAS.put("ソ", "so");
        // column TA
        KATAKANAS.put("タ", "ta");
        KATAKANAS.put("チ", "chi");
        KATAKANAS.put("ツ", "tsu");
        KATAKANAS.put("テ", "te");
        KATAKANAS.put("ト", "to");
        // column NA
        KATAKANAS.put("ナ", "na");
        KATAKANAS.put("ニ", "ni");
        KATAKANAS.put("ヌ", "nu");
        KATAKANAS.put("ネ", "ne");
        KATAKANAS.put("ノ", "no");
        // column HA
        KATAKANAS.put("ハ", "ha");
        KATAKANAS.put("ヒ", "hi");
        KATAKANAS.put("フ", "fu");
        KATAKANAS.put("ヘ", "he");
        KATAKANAS.put("ホ", "ho");
        // column MA
        KATAKANAS.put("マ", "ma");
        KATAKANAS.put("ミ", "mi");
        KATAKANAS.put("ム", "mu");
        KATAKANAS.put("メ", "me");
        KATAKANAS.put("モ", "mo");
        // column YA
        KATAKANAS.put("ヤ", "ya");
        KATAKANAS.put("ユ", "yu");
        KATAKANAS.put("ヨ", "yo");
        // column RA
        KATAKANAS.put("ラ", "ra");
        KATAKANAS.put("リ", "ri");
        KATAKANAS.put("ル", "ru");
        KATAKANAS.put("レ", "re");
        KATAKANAS.put("ロ", "ro");
        // column WA
        KATAKANAS.put("ワ", "wa");
        KATAKANAS.put("ヲ", "wo");
        // column N
        KATAKANAS.put("ン", "n");
        // column GA
        KATAKANAS.put("ガ", "ga");
        KATAKANAS.put("ギ", "gi");
        KATAKANAS.put("グ", "gu");
        KATAKANAS.put("ゲ", "ge");
        KATAKANAS.put("ゴ", "go");
        // column ZA
        KATAKANAS.put("ザ", "za");
        KATAKANAS.put("ジ", "ji");
        KATAKANAS.put("ズ", "zu");
        KATAKANAS.put("ゼ", "ze");
        KATAKANAS.put("ゾ", "zo");
        // column DA
        KATAKANAS.put("ダ", "da");
        KATAKANAS.put("ヂ", "ji");
        KATAKANAS.put("ヅ", "zu");
        KATAKANAS.put("デ", "de");
        KATAKANAS.put("ド", "do");
        // column BA
        KATAKANAS.put("バ", "ba");
        KATAKANAS.put("ビ", "bi");
        KATAKANAS.put("ブ", "bu");
        KATAKANAS.put("ベ", "be");
        KATAKANAS.put("ボ", "bo");
        // column PA
        KATAKANAS.put("パ", "pa");
        KATAKANAS.put("ピ", "pi");
        KATAKANAS.put("プ", "pu");
        KATAKANAS.put("ペ", "pe");
        KATAKANAS.put("ポ", "po");
        // column KYA
        KATAKANAS.put("キャ", "kya");
        KATAKANAS.put("キュ", "kyu");
        KATAKANAS.put("キョ", "kyo");
        // column SHA　
        KATAKANAS.put("シャ", "sha");
        KATAKANAS.put("シュ", "shu");
        KATAKANAS.put("ショ", "sho");
        // column CHA　
        KATAKANAS.put("チャ", "cha");
        KATAKANAS.put("チュ", "chu");
        KATAKANAS.put("チョ", "cho");
        // column NYA
        KATAKANAS.put("ニャ", "nya");
        KATAKANAS.put("ニュ", "nyu");
        KATAKANAS.put("ニョ", "nyo");
        // column MYA
        KATAKANAS.put("ミャ", "mya");
        KATAKANAS.put("ミュ", "myu");
        KATAKANAS.put("ミョ", "myo");
        // column HYA
        KATAKANAS.put("ヒャ", "hya");
        KATAKANAS.put("ヒュ", "hyu");
        KATAKANAS.put("ヒョ", "hyo");
        // column RYA
        KATAKANAS.put("リャ", "rya");
        KATAKANAS.put("リュ", "ryu");
        KATAKANAS.put("リョ", "ryo");
        // column GYA
        KATAKANAS.put("ギャ", "gya");
        KATAKANAS.put("ギュ", "gyu");
        KATAKANAS.put("ギョ", "gyo");
        // column JA
        KATAKANAS.put("ジャ", "ja");
        KATAKANAS.put("ジュ", "ju");
        KATAKANAS.put("ジョ", "jo");
        // column
        KATAKANAS.put("ティ", "ti");
        KATAKANAS.put("", "fi");
        KATAKANAS.put("ディ", "di");
        // column
        KATAKANAS.put("", "tsa");
        KATAKANAS.put("ツィ", "tsi");
        // column　DYA
        KATAKANAS.put("ヂャ", "dya");
        KATAKANAS.put("ヂュ", "dyu");
        KATAKANAS.put("ヂョ", "dyo");
        // column BYA
        KATAKANAS.put("ビャ", "bya");
        KATAKANAS.put("ビュ", "byu");
        KATAKANAS.put("ビョ", "byo");
        // column PYA
        KATAKANAS.put("ピャ", "pya");
        KATAKANAS.put("ピュ", "pyu");
        KATAKANAS.put("ピョ", "pyo");

        KATAKANAS.put("ファ", "fa");
        KATAKANAS.put("ウィ", "wi");
        KATAKANAS.put("トゥ", "tu");
        KATAKANAS.put("ドゥ", "du");
        KATAKANAS.put("デゥ", "dyu");
        KATAKANAS.put("ウェ", "we");
        KATAKANAS.put("シェ", "she");
        KATAKANAS.put("チェ", "che");
        KATAKANAS.put("ツェ", "tse");
        KATAKANAS.put("フェ", "fe");
        KATAKANAS.put("ジェ", "je");
        KATAKANAS.put("ウォ", "wo");
        KATAKANAS.put("ツォ", "tso");
        KATAKANAS.put("フォ", "fo");
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
