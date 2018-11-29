/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package documenttranslator.controllers;

import documenttranslator.forms.TranslateRequest;
import documenttranslator.processors.AbstractTask;
import documenttranslator.processors.BaseProcessor;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author joni.jinon
 */
public class TermListController implements BaseController {

	@FXML
	private VBox inputTermsContainer;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO
	}

	@Override
	public void onEnter(MainWindowController c, TranslateRequest tr) {
		
		this.inputTermsContainer.getChildren().clear();

		BaseProcessor p = tr.getProcessor();
		AbstractTask task = p.getReadFileTask(tr);
		
		VBox rootNode = new VBox();
		rootNode.setPadding(new Insets(10));
		rootNode.setSpacing(10);
		
		Label label = new Label("Reading file...");
		ProgressBar pBar = new ProgressBar();
		pBar.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
		pBar.prefWidthProperty().bind(rootNode.widthProperty());
		rootNode.getChildren().addAll(label, pBar);
		
		Scene scene = new Scene(rootNode);
		
		Stage loadingWindow = new Stage();
		loadingWindow.setTitle("Working...");
		loadingWindow.setScene(scene);
		loadingWindow.setWidth(300);
		loadingWindow.setHeight(100);
		loadingWindow.setResizable(false);
		loadingWindow.initModality(Modality.WINDOW_MODAL);
		
		loadingWindow.initOwner(c.getButtonNext().getScene().getWindow());
		
		task.setOnRunning(e -> {
			loadingWindow.showAndWait();
		});
		
		task.setOnSucceeded(e -> {
			this.addElements(tr);
			loadingWindow.close();
		});
		
		task.setOnFailed(e -> {
			loadingWindow.close();
		});
		
		new Thread(task).start();
		
	}

	@Override
	public void onBack(MainWindowController c, TranslateRequest tr) {
		c.setCurrentScreen(Screen.FileBasket);
	}

	@Override
	public boolean onValidate(MainWindowController c, TranslateRequest tr) {
		return true;
	}

	@Override
	public void onConfirm(MainWindowController c, TranslateRequest tr) {
		
		tr.getInterpreter().getDictionary().clear();
		this.inputTermsContainer.getChildren().forEach(hBox -> {
			
			ObservableList<Node> row = ((HBox)hBox).getChildren();
			
			TextArea terms = (TextArea)row.get(0);
			TextArea translated = (TextArea)row.get(1);
			
			List<String> termList = Arrays.asList(terms.getText().split("\n"));
			List<String> translatedList = new ArrayList<>(Arrays.asList(translated.getText().split("\n")));
			
			for (int s = 0, e = termList.size(); s < e; s++) {
				if(s == e - 1 && translatedList.size() == e - 1) {
					if(termList.get(s).equals("\u3000") || termList.get(s).equals(" ")) {
						translatedList.add(" ");
					} else {
						JOptionPane.showMessageDialog(null, "Insufficient translated terms!", "Error", JOptionPane.ERROR_MESSAGE);
						tr.getInterpreter().getDictionary().clear();
						return;
					}
				}
				tr.getInterpreter().getDictionary().put(termList.get(s), translatedList.get(s));
			}
			
		});
		
		c.setCurrentScreen(Screen.TranslateSettings);
	}

	@Override
	public void onExit(MainWindowController c, TranslateRequest tr) {

	}
	
	private void addElements(TranslateRequest tr) {
		
		List<HBox> termRows = new ArrayList<>();

		ArrayList<String> lineHolder = new ArrayList<>();
		int charCounter = 0;
		TextArea txtScannedTerms;
		TextArea txtTranslateTerms;
		List<String> termList = tr.getInterpreter().getExtractedTerms();

		for (int i = 0; i < termList.size(); i++) {

			boolean addNow = false;

			// If less than 5K chars, add to LineHolder
			if (charCounter + termList.get(i).length() <= 5000) {
				lineHolder.add(termList.get(i));
				charCounter += termList.get(i).length() + 1;

			} else { // Else Add

				addNow = true;

			}

			// If AddNow or Last Element
			if (addNow || i == termList.size() - 1) {

				txtScannedTerms = new TextArea();
				txtTranslateTerms = new TextArea();
				txtScannedTerms.scrollTopProperty().bindBidirectional(txtTranslateTerms.scrollTopProperty());

				txtScannedTerms.setText(String.join("\n", lineHolder));

				HBox row = new HBox();
				row.getChildren().addAll(txtScannedTerms, txtTranslateTerms);
				termRows.add(row);

				lineHolder.clear();
				charCounter = 0;

				lineHolder.add(termList.get(i));
				charCounter += termList.get(i).length();

			}

		}
		
		termRows.forEach(e -> {

			e.setSpacing(10);

			TextArea terms = (TextArea)e.getChildren().get(0);
			HBox.setHgrow(terms, Priority.ALWAYS);

			TextArea translations = (TextArea)e.getChildren().get(1);
			HBox.setHgrow(translations, Priority.ALWAYS);

			this.inputTermsContainer.getChildren().add(e);

		});
		
	}
	
}
