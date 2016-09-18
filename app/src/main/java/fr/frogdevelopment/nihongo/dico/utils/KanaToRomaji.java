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
		KATAKANAS.put("ディ", "di");
		// column
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
		// tild
		KATAKANAS.put("ー", "-");

		// HIRAGANA

		// column A
		HIRAGANAS.put("あ", "a");
		HIRAGANAS.put("い", "i");
		HIRAGANAS.put("う", "u");
		HIRAGANAS.put("え", "e");
		HIRAGANAS.put("お", "o");
		// column KA
		HIRAGANAS.put("か", "ka");
		HIRAGANAS.put("き", "ki");
		HIRAGANAS.put("く", "ku");
		HIRAGANAS.put("け", "ke");
		HIRAGANAS.put("こ", "ko");
		// column SA
		HIRAGANAS.put("さ", "sa");
		HIRAGANAS.put("し", "shi");
		HIRAGANAS.put("す", "su");
		HIRAGANAS.put("せ", "se");
		HIRAGANAS.put("そ", "so");
		// column TA
		HIRAGANAS.put("た", "ta");
		HIRAGANAS.put("ち", "chi");
		HIRAGANAS.put("つ", "tsu");
		HIRAGANAS.put("て", "te");
		HIRAGANAS.put("と", "to");
		// column NA
		HIRAGANAS.put("な", "na");
		HIRAGANAS.put("に", "ni");
		HIRAGANAS.put("ぬ", "nu");
		HIRAGANAS.put("ね", "ne");
		HIRAGANAS.put("の", "no");
		// column HA
		HIRAGANAS.put("は", "ha");
		HIRAGANAS.put("ひ", "hi");
		HIRAGANAS.put("ふ", "fu");
		HIRAGANAS.put("へ", "he");
		HIRAGANAS.put("ほ", "ho");
		// column MA
		HIRAGANAS.put("ま", "ma");
		HIRAGANAS.put("み", "mi");
		HIRAGANAS.put("む", "mu");
		HIRAGANAS.put("め", "me");
		HIRAGANAS.put("も", "mo");
		// column YA
		HIRAGANAS.put("や", "ya");
		HIRAGANAS.put("ゆ", "yu");
		HIRAGANAS.put("よ", "yo");
		// column RA
		HIRAGANAS.put("ら", "ra");
		HIRAGANAS.put("り", "ri");
		HIRAGANAS.put("る", "ru");
		HIRAGANAS.put("れ", "re");
		HIRAGANAS.put("ろ", "ro");
		// column WA
		HIRAGANAS.put("わ", "wa");
		HIRAGANAS.put("を", "wo");
		// column N
		HIRAGANAS.put("ん", "n");
		// column GA
		HIRAGANAS.put("が", "ga");
		HIRAGANAS.put("ぎ", "gi");
		HIRAGANAS.put("ぐ", "gu");
		HIRAGANAS.put("げ", "ge");
		HIRAGANAS.put("ご", "go");
		// column ZA
		HIRAGANAS.put("ざ", "za");
		HIRAGANAS.put("じ", "ji");
		HIRAGANAS.put("ず", "zu");
		HIRAGANAS.put("ぜ", "ze");
		HIRAGANAS.put("ぞ", "zo");
		// column DA
		HIRAGANAS.put("だ", "da");
		HIRAGANAS.put("じ", "ji");
		HIRAGANAS.put("ず", "zu");
		HIRAGANAS.put("で", "de");
		HIRAGANAS.put("ど", "do");
		// column BA
		HIRAGANAS.put("ば", "ba");
		HIRAGANAS.put("び", "bi");
		HIRAGANAS.put("ぶ", "bu");
		HIRAGANAS.put("べ", "be");
		HIRAGANAS.put("ぼ", "bo");
		// column PA
		HIRAGANAS.put("ぱ", "pa");
		HIRAGANAS.put("ぴ", "pi");
		HIRAGANAS.put("ぷ", "pu");
		HIRAGANAS.put("ぺ", "pe");
		HIRAGANAS.put("ぽ", "po");
		// column KYA fixme à vérifier
		HIRAGANAS.put("きゃ", "kya");
		HIRAGANAS.put("きゅ", "kyu");
		HIRAGANAS.put("きょ", "kyo");
		// column SHA　fixme à vérifier
		HIRAGANAS.put("きゃ", "sha");
		HIRAGANAS.put("しゅ", "shu");
		HIRAGANAS.put("しょ", "sho");
		// column CHA　fixme à vérifier
		HIRAGANAS.put("ちゃ", "cha");
		HIRAGANAS.put("ちゅ", "chu");
		HIRAGANAS.put("ちょ", "cho");
		// column NYA　fixme à vérifier
		HIRAGANAS.put("にゃ", "nya");
		HIRAGANAS.put("にゅ", "nyu");
		HIRAGANAS.put("にょ", "nyo");
		// column NYA　fixme à vérifier
		HIRAGANAS.put("みゃ", "nya");
		HIRAGANAS.put("みゅ", "nyu");
		HIRAGANAS.put("みょ", "nyo");
		// column HYA　fixme à vérifier
		HIRAGANAS.put("ひゃ", "hya");
		HIRAGANAS.put("ひゅ", "hyu");
		HIRAGANAS.put("ひょ", "hyo");
		// column RYA　fixme à vérifier
		HIRAGANAS.put("りゃ", "rya");
		HIRAGANAS.put("りゅ", "ryu");
		HIRAGANAS.put("りょ", "ryo");
		// column GYA　fixme à vérifier
		HIRAGANAS.put("ぎゃ", "gya");
		HIRAGANAS.put("ぎゅ", "gyu");
		HIRAGANAS.put("ぎょ", "gyo");
		// column JA
		HIRAGANAS.put("じゃ", "ja");
		HIRAGANAS.put("じゅ", "ju");
		HIRAGANAS.put("じょ", "jo");
		// column BYA
		HIRAGANAS.put("びゃ", "bya");
		HIRAGANAS.put("びゅ", "byu");
		HIRAGANAS.put("びょ", "byo");
		// column PYA
		HIRAGANAS.put("ぴゃ", "pya");
		HIRAGANAS.put("ぴゅ", "pyu");
		HIRAGANAS.put("ぴょ", "pyo");
		// tild
		HIRAGANAS.put("ー", "-");
	}

	public static String convert(String value) {
		if (InputUtils.isOnlyHiragana(value)) {
			return doConvert(HIRAGANAS, value);
		} else if (InputUtils.isOnlyKatakana(value)) {
			return doConvert(KATAKANAS, value);
		} else {
			return value;
			// fixme handle both together
//			StringBuilder t = new StringBuilder();
//			for (int i = 0; i < value.length(); i++) {
//				if (i <= value.length() - 2) {
//					if (KATAKANAS.containsKey(value.substring(i, i + 2))) {
//						t.append(KATAKANAS.get(value.substring(i, i + 2)));
//						i++;
//					} else if (KATAKANAS.containsKey(value.substring(i, i + 1))) {
//						t.append(KATAKANAS.get(value.substring(i, i + 1)));
//					} else if (value.charAt(i) == 'ッ') {
//						t.append(KATAKANAS.get(value.substring(i + 1, i + 2)).charAt(0));
//					} else {
//						t.append(value.charAt(i));
//					}
//				} else {
//					if (KATAKANAS.containsKey(value.substring(i, i + 1))) {
//						t.append(KATAKANAS.get(value.substring(i, i + 1)));
//					} else {
//						t.append(value.charAt(i));
//					}
//				}
//			}
//			return t.toString();
		}
	}

	private static String doConvert(Map<String, String> map, String value) {
		StringBuilder t = new StringBuilder();
		for (int i = 0; i < value.length(); i++) {
			if (i <= value.length() - 2) {
				if (map.containsKey(value.substring(i, i + 2))) {
					t.append(map.get(value.substring(i, i + 2)));
					i++;
				} else if (map.containsKey(value.substring(i, i + 1))) {
					t.append(map.get(value.substring(i, i + 1)));
				} else if (value.charAt(i) == 'ッ' || value.charAt(i) == 'っ') {
					t.append(map.get(value.substring(i + 1, i + 2)).charAt(0));
				} else {
					t.append(value.charAt(i));
				}
			} else {
				if (map.containsKey(value.substring(i, i + 1))) {
					t.append(map.get(value.substring(i, i + 1)));
				} else {
					t.append(value.charAt(i));
				}
			}
		}
		return t.toString();
	}

}
