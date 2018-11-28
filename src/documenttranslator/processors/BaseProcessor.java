/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package documenttranslator.processors;

import documenttranslator.forms.TranslateRequest;

/**
 *
 * @author joni.jinon
 */
public abstract class BaseProcessor {
	
	public BaseProcessor() {

	}

	public abstract AbstractTask getReadFileTask(TranslateRequest tr);

	public abstract AbstractTask getFindAndReplaceTask(TranslateRequest tr);

}
