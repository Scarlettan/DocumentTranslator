/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package documenttranslator.interpreters;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Extracts words from Strings
 * 
 * @author joni.jinon
 */
public abstract class BaseInterpreter {

	private final List<String> extractedTerms = new ArrayList<>();
	private final LinkedHashMap<String, String> dictionary = new LinkedHashMap<>();

	public List<String> getExtractedTerms() {
		return extractedTerms;
	}

	public LinkedHashMap<String, String> getDictionary() {
		return dictionary;
	}

	/**
	 * Read string and extract terms to be translated
	 *
	 * @param s String to interpret
	 */
	public abstract void interpretString(String s);

}
