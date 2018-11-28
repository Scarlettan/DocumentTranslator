/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package documenttranslator.controllers;

import documenttranslator.services.FxmlManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

/**
 *
 * @author joni.jinon
 */
public enum Screen {
	FileBasket(FxmlManager.FILE_BASKET),
	TermList(FxmlManager.TERM_LIST),
	TranslateSettings(FxmlManager.TRANSLATE_SETTINGS),
	TranslateComplete(FxmlManager.TRANSLATE_COMPLETE)
	;

	private final Node rootNode;
	private final BaseController controller;

	private Screen(String fxmlLocation) {
		FXMLLoader loader = FxmlManager.getLoader(fxmlLocation);
		this.rootNode = loader.getRoot();
		this.controller = loader.getController();
	}

	public Node getRootNode() {
		return rootNode;
	}

	public BaseController getController() {
		return controller;
	}

}
