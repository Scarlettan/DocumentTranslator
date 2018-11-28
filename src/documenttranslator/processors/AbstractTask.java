/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package documenttranslator.processors;

import documenttranslator.forms.TranslateRequest;
import java.util.ArrayList;
import java.util.List;
import javafx.concurrent.Task;

/**
 *
 * @author joni.jinon
 */
public abstract class AbstractTask extends Task<Void> {

	private final TranslateRequest translateRequest;
	private final List<String> logger = new ArrayList<>();

	public AbstractTask(TranslateRequest tr) {
		this.translateRequest = tr;
	}

	public final TranslateRequest getTranslateRequest() {
		return translateRequest;
	}

	public final void addLog(String s) {
		this.logger.add(s);
	}

	public final String getLog() {
		return String.join(System.lineSeparator(), this.logger);
	}

}
