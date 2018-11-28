/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package documenttranslator.controllers;

import documenttranslator.SupportedExtensions;
import documenttranslator.forms.TranslateRequest;
import documenttranslator.interpreters.NihongoInterpreter;
import documenttranslator.processors.OfficeDocumentProcessor;
import documenttranslator.services.Spy;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author joni.jinon
 */
public class FileBasketController implements BaseController {

	@FXML
	private VBox rootNode;
	@FXML
	private Node inputDropBox;
	@FXML
	private Label labelDropBox;
	@FXML
	private TextField inputFileUrl;
	@FXML
	private Button buttonSelectFile;

	private final List<FileChooser.ExtensionFilter> supportedExtensions = new ArrayList<>();

	@Override
	public void initialize(URL url, ResourceBundle rb) {

		this.buttonSelectFile.setOnAction(e -> {
			this.selectFile();
		});

		final List<String> allExtensions = new ArrayList<>();
		final List<FileChooser.ExtensionFilter> individualExtensions = new ArrayList<>();

		for(SupportedExtensions e : SupportedExtensions.values()) {
			allExtensions.add("*" + e.getExt());
			individualExtensions.add(new FileChooser.ExtensionFilter(e.getDesc() + " (" + e.getExt() + ")", "*" + e.getExt()));
		}

		this.supportedExtensions.add(new FileChooser.ExtensionFilter("All Supported Files (" + String.join(", ", allExtensions) + ")", allExtensions));
		this.supportedExtensions.addAll(individualExtensions);

		this.inputDropBox.setOnDragOver((DragEvent event) -> {
			if (event.getGestureSource() != this.inputDropBox && event.getDragboard().hasFiles()) {
				/* allow for both copying and moving, whatever user chooses */
				event.acceptTransferModes(TransferMode.LINK);
			}
			event.consume();
		});

	}

	public TextField getInputFileUrl() {
		return inputFileUrl;
	}

	public void setInputFileUrl(TextField inputFileUrl) {
		this.inputFileUrl = inputFileUrl;
	}

	public Button getBtnSelectFile() {
		return buttonSelectFile;
	}

	public void setBtnSelectFile(Button buttonSelectFile) {
		this.buttonSelectFile = buttonSelectFile;
	}

	private void selectFile() {

		Spy.log().toConsole().asInfo("Initiated file selection");

		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Source File");
		fileChooser.getExtensionFilters().addAll(this.supportedExtensions);
		String currentDir = System.getProperty("user.home");
		fileChooser.setInitialDirectory(new File(currentDir));

		File source = fileChooser.showOpenDialog(this.buttonSelectFile.getScene().getWindow());
		this.acceptFile(source);

	}

	@Override
	public void onEnter(MainWindowController c, TranslateRequest tr) {
		this.inputFileUrl.setText("");

		if (tr.getInputFile() != null) {
			this.inputFileUrl.setText(tr.getInputFile().getAbsolutePath());
		}

		c.getButtonBack().setDisable(true);

		this.inputDropBox.setOnDragDropped((DragEvent event) -> {
			Dragboard db = event.getDragboard();
			File droppedFile = null;
			boolean success = false;

			if (db.hasFiles()) {
				droppedFile = db.getFiles().get(0);
				success = true;
			}
			
			event.setDropCompleted(success);
			event.consume();
			
			if(success) {
				this.acceptFile(droppedFile);
				this.onConfirm(c, tr);
			}
			
		});

	}

	@Override
	public void onBack(MainWindowController c, TranslateRequest tr) {

	}

	@Override
	public boolean onValidate(MainWindowController c, TranslateRequest tr) {

		if (this.inputFileUrl.getText().isEmpty()) {
			return false;
		} else if (!new File(this.inputFileUrl.getText()).exists()) {
			return false;
		}

		boolean supported = false;
		for (SupportedExtensions se : SupportedExtensions.values()) {
			if (this.inputFileUrl.getText().endsWith(se.getExt())) {
				supported = true;
				break;
			}
		}
		
		if(!supported) {
			JOptionPane.showMessageDialog(null, "File is not yet supported!", "Error", JOptionPane.ERROR_MESSAGE);
			this.inputFileUrl.clear();
		}

		return supported;
	}

	@Override
	public void onConfirm(MainWindowController c, TranslateRequest tr) {
		if(this.onValidate(c, tr)) {
			
			tr.setInputFile(new File(this.inputFileUrl.getText()));
			tr.setInterpreter(new NihongoInterpreter());

			switch(SupportedExtensions.parse(tr.getInputFile().getAbsolutePath())) {
				case docx:
				case ods:
				case pptx:
				case xlsx:
					tr.setProcessor(new OfficeDocumentProcessor());
					break;
			}

			c.setCurrentScreen(Screen.TermList);
			
		}
	}

	@Override
	public void onExit(MainWindowController c, TranslateRequest tr) {
		c.getButtonBack().setDisable(false);
	}

	private void acceptFile(File f) {
		if (f != null && f.exists()) {
			this.inputFileUrl.setText(f.getAbsolutePath());
		}
	}
	
}
