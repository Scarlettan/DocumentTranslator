/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package documenttranslator.services;

import java.io.IOException;
import java.util.HashMap;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

/**
 *
 * @author joni.jinon
 */
public class FxmlManager {

	public static final String FXML_BASE = "/documenttranslator/fxml";
	public static final String MAIN_WINDOW = FXML_BASE + "/MainWindow.fxml";
	public static final String FILE_BASKET = FXML_BASE + "/FileBasket.fxml";
	public static final String TERM_LIST = FXML_BASE + "/TermList.fxml";
	public static final String TRANSLATE_SETTINGS = FXML_BASE + "/TranslateSettings.fxml";
	public static final String TRANSLATE_COMPLETE = FXML_BASE + "/TranslateComplete.fxml";

	private static final HashMap<String, FXMLLoader> FXML_LIST = new HashMap<>();

	public static FXMLLoader getLoader(String url) {
		if (!FXML_LIST.containsKey(url)) {
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(FxmlManager.class.getResource(url));
				loader.load();
				FXML_LIST.put(url, loader);
			} catch (IOException ex) {
				System.out.println(ex);
			}
		}
		return FXML_LIST.get(url);
	}

	public static Node getRoot(String url) {
		return getLoader(url).getRoot();
	}

	public static Object getController(String url) {
		return getLoader(url).getController();
	}

}
