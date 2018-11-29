/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package documenttranslator.processors;

import documenttranslator.forms.TranslateRequest;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Reads files and extracts Strings
 * Replaces Strings and writes file
 * 
 * @author joni.jinon
 */
public abstract class BaseProcessor {
	
	public BaseProcessor() {

	}
	
	public void onLoadMoreSettings(VBox moreSettingsPane) {
		moreSettingsPane.getChildren().add(new Label("No additional configuration for this module."));
	}

	public abstract AbstractTask getReadFileTask(TranslateRequest tr);

	public abstract AbstractTask getFindAndReplaceTask(TranslateRequest tr);

}
