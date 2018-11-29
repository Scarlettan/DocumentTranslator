/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package documenttranslator;

import documenttranslator.services.FileTools;
import java.io.File;
import java.time.LocalDateTime;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author joni.jinon
 */
public class DocumentTranslator extends Application {

	@Override
	public void start(Stage stage) throws Exception {

		Parent root = FXMLLoader.load(getClass().getResource("/documenttranslator/fxml/MainWindow.fxml"));

		Scene scene = new Scene(root);

		stage.setTitle("Document Translator");
		stage.setScene(scene);
		stage.setMinWidth(800);
		stage.setMinHeight(600);
		stage.setWidth(800);
		stage.setHeight(600);
		stage.show();

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			FileTools.deleteFilesRecursive(new File(Prefs.DIR_APP));
		}));
	}

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}

}
