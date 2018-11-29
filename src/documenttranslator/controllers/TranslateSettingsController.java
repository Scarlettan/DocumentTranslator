/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package documenttranslator.controllers;

import documenttranslator.SupportedExtensions;
import documenttranslator.forms.TranslateRequest;
import documenttranslator.services.Spy;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

/**
 * FXML Controller class
 *
 * @author joni.jinon
 */
public class TranslateSettingsController implements BaseController {

	@FXML
	private TextField inputOutputFile;
	@FXML
	private Button buttonSelectOutputFile;
	@FXML
	private VBox moreSettingsPane;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

	@Override
	public void onEnter(MainWindowController c, TranslateRequest tr) {
		this.inputOutputFile.setText(tr.getOutputFile().getAbsolutePath());

		c.getButtonNext().setText("Translate");
		c.getButtonNext().requestFocus();
		
		this.buttonSelectOutputFile.setOnAction(e -> this.selectOutputFile(tr));
		
		this.moreSettingsPane.getChildren().clear();
		tr.getProcessor().onLoadMoreSettings(this.moreSettingsPane);
		
	}

	@Override
	public void onBack(MainWindowController c, TranslateRequest tr) {
		c.setCurrentScreen(Screen.TermList);
	}

	@Override
	public boolean onValidate(MainWindowController c, TranslateRequest tr) {
		return true;
	}

	@Override
	public void onConfirm(MainWindowController c, TranslateRequest tr) {
		tr.setOutputFile(new File(this.inputOutputFile.getText()));
		c.setCurrentScreen(Screen.TranslateComplete);
	}

	@Override
	public void onExit(MainWindowController c, TranslateRequest tr) {
		c.getButtonNext().setText("Next >");
	}

	private void selectOutputFile(TranslateRequest tr) {
		
		Spy.log().toConsole().asInfo("Initiated file translation and save");

		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save File As...");
		
		for (SupportedExtensions se : SupportedExtensions.values()) {
			if(tr.getInputFile().getName().endsWith(se.getExt())) {
				fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(se.getDesc(), se.getExt()));
				break;
			}
		}
		
		String initialDir = System.getProperty("user.home");
		if(tr.getOutputFile().getParentFile().exists() && tr.getOutputFile().getParentFile().isDirectory()) {
			initialDir = tr.getOutputFile().getParentFile().getAbsolutePath();
		}
		
		fileChooser.setInitialFileName(tr.getOutputFile().getName());
		fileChooser.setInitialDirectory(new File(initialDir));

		File target = fileChooser.showSaveDialog(this.inputOutputFile.getScene().getWindow());

		if (target != null) {
			this.inputOutputFile.setText(target.getAbsolutePath());
		}
	}
	

}
