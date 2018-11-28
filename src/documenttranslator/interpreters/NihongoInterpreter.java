/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package documenttranslator.interpreters;

import documenttranslator.services.Spy;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author joni.jinon
 */
public class NihongoInterpreter extends BaseInterpreter {

	// ([\"'])(?:(?=(\\\\?))\\2.)*?\\1
	// OR
	// >[^>]+<
	//private final Pattern inQuotesOrTagsPattern = Pattern.compile("([\"'])(?:(?=(\\\\?))\\2.)*?\\1|>[^>]+<");
	private final Pattern inQuotesOrTagsPattern = Pattern.compile(">[^>]+<");

	private final Pattern patternSpreadSheetReference = Pattern.compile(
			"([!])([$]?)([A-Z]+)([$]?)([0-9]+)([:])([$]?)([A-Z]+)([$]?)([0-9]+)");

	@Override
	public void interpretString(String s) {

		Spy.log().toConsole()
				//.toJobFile(this.logFileName)
				.asInfo("Initialized Term Extraction");

		Matcher m = this.inQuotesOrTagsPattern.matcher(s);
		String term;
		String[] chunks;
		while (m.find()) {
			// Get Term
			term = m.group();
			// Exclude brackets
			term = term.substring(1, term.length() - 1);
			// Remove Spreadsheet References
			term = term.replaceAll(this.patternSpreadSheetReference.pattern(), "");
			// Unescape htmlentities
			//term = StringEscapeUtils.unescapeHtml4(term);
			term = this.htmlDecodeExcelSpecialChars(term);
			// Replace doube byte space with single byte
			term = term.replace("" + '\u3000' + '\u3000', " ");
			// Trim, then replace all whitespace with single space
			term = term.trim().replaceAll("\\s", " ");

			chunks = term.split("\\s");
			for (String c : chunks) {
				if (this.hasJapaneseChar(c)) {

					// Log
					Spy.log().toConsole()
							//.toJobFile(this.logFileName)
							.asInfo(Spy.padRight("---- Found", 15) + " : " + c);

					// Add to target terms
					if(!this.getExtractedTerms().contains(c)) {
						this.getExtractedTerms().add(c);
					}

				}
			}

		}

//		Spy.log().toConsole()
//				.toJobFile(this.logFileName)
//				.asInfo("Total Terms Found: " + this.totalTermsFound)
//				.asInfo("Total Terms In Target Set: " + this.targetTerms.size());
	}

	private String htmlEncodeExcelSpecialChars(String s) {
		return s.replace("<", "&lt;")
				.replace(">", "&gt;")
				.replace("&", "&amp;");
	}

	private String htmlDecodeExcelSpecialChars(String s) {
		return s.replace("&lt;", "<")
				.replace("&gt;", ">")
				.replace("&amp;", "&");
	}

	private boolean isJapaneseChar(char c) {
		return (Character.UnicodeBlock.of(c) == Character.UnicodeBlock.HIRAGANA)
				|| (Character.UnicodeBlock.of(c) == Character.UnicodeBlock.KATAKANA)
				|| (Character.UnicodeBlock.of(c) == Character.UnicodeBlock.KATAKANA_PHONETIC_EXTENSIONS)
				|| (Character.UnicodeBlock.of(c) == Character.UnicodeBlock.KANA_SUPPLEMENT)
				|| (Character.UnicodeBlock.of(c) == Character.UnicodeBlock.CJK_COMPATIBILITY)
				|| (Character.UnicodeBlock.of(c) == Character.UnicodeBlock.CJK_COMPATIBILITY_FORMS)
				|| (Character.UnicodeBlock.of(c) == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS)
				|| (Character.UnicodeBlock.of(c) == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS_SUPPLEMENT)
				|| (Character.UnicodeBlock.of(c) == Character.UnicodeBlock.CJK_RADICALS_SUPPLEMENT)
				|| (Character.UnicodeBlock.of(c) == Character.UnicodeBlock.CJK_STROKES)
				|| (Character.UnicodeBlock.of(c) == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION)
				|| (Character.UnicodeBlock.of(c) == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS)
				|| (Character.UnicodeBlock.of(c) == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A)
				|| (Character.UnicodeBlock.of(c) == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B)
				|| (Character.UnicodeBlock.of(c) == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_C)
				|| (Character.UnicodeBlock.of(c) == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_D)
				|| (Character.UnicodeBlock.of(c) == Character.UnicodeBlock.ENCLOSED_CJK_LETTERS_AND_MONTHS) //|| (Character.UnicodeBlock.of(c) == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS)
				;
	}

	private boolean hasJapaneseChar(String text) {
		for (char c : text.toCharArray()) {
			if (this.isJapaneseChar(c)) {
				return true;
			}
		}
		return false;
	}

	private boolean isAlphaNum(char c) {
		int i = (int) c;
		return (i >= 65 && i <= 90) || (i >= 97 && i <= 122) || (i >= 48 && i <= 57);
	}

}
