/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package documenttranslator.controllers;

import documenttranslator.forms.TranslateRequest;
import documenttranslator.processors.AbstractTask;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;

/**
 * FXML Controller class
 *
 * @author joni.jinon
 */
public class TranslateCompleteController implements BaseController {
	
	@FXML
	private Label labelStatus;
	@FXML
	private ProgressBar progressBar;
	@FXML
	private Button buttonOpenFile;
	@FXML
	private Button buttonOpenFolder;
	@FXML
	private HBox hboxPadding;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.progressBar.prefWidthProperty().bind(this.hboxPadding.widthProperty());
	}

	@Override
	public void onEnter(MainWindowController c, TranslateRequest tr) {
		
		c.getButtonNext().setText("Finish");
		c.getButtonNext().setDisable(true);
		c.getButtonCancel().setVisible(false);
		c.getButtonBack().setVisible(false);
		
		this.buttonOpenFile.setOnAction(e -> {
			try {
				Desktop.getDesktop().open(tr.getOutputFile());
			} catch (IOException ex) {
				System.out.println(ex);
			}
		});
		
		this.buttonOpenFolder.setOnAction(e -> {
			try {
				Desktop.getDesktop().open(tr.getOutputFile().getParentFile());
			} catch (IOException ex) {
				System.out.println(ex);
			}
		});
		
		this.progressBar.setVisible(true);
		this.buttonOpenFile.setDisable(true);
		this.buttonOpenFolder.setDisable(true);
		
		// Run Task
		AbstractTask task = tr.getProcessor().getFindAndReplaceTask(tr);
		
		this.progressBar.progressProperty().bind(task.progressProperty());
		this.labelStatus.textProperty().bind(task.messageProperty());
		
		task.setOnFailed(e -> {
			c.getButtonNext().setDisable(false);
		});
		
		task.setOnSucceeded(e -> {
			this.progressBar.setVisible(false);
			this.buttonOpenFile.setDisable(false);
			this.buttonOpenFolder.setDisable(false);
			c.getButtonNext().setDisable(false);
			c.getButtonNext().requestFocus();
		});
		
		new Thread(task).start();
		
	}

	@Override
	public void onBack(MainWindowController c, TranslateRequest tr) {
		c.setCurrentScreen(Screen.TranslateSettings);
	}

	@Override
	public boolean onValidate(MainWindowController c, TranslateRequest tr) {
		return true;
	}

	@Override
	public void onConfirm(MainWindowController c, TranslateRequest tr) {
		c.reset();
	}

	@Override
	public void onExit(MainWindowController c, TranslateRequest tr) {
		c.getButtonNext().setText("Next >");
		c.getButtonNext().setDisable(false);
		c.getButtonCancel().setVisible(true);
		c.getButtonBack().setVisible(true);
	}

}
