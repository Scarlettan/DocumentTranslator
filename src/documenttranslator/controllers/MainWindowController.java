/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package documenttranslator.controllers;

import documenttranslator.forms.TranslateRequest;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author joni.jinon
 */
public class MainWindowController implements Initializable {

	@FXML
	private BorderPane mainWindow;
	@FXML
	private Button buttonBack;
	@FXML
	private Button buttonCancel;
	@FXML
	private Button buttonNext;

	private Screen currentScreen;
	
	private TranslateRequest translateRequest = new TranslateRequest();

	@Override
	public void initialize(URL url, ResourceBundle rb) {

		this.buttonCancel.setOnAction(e -> ((Stage) this.buttonCancel.getScene().getWindow()).close());
		this.buttonBack.setOnAction(e -> this.currentScreen.getController().onBack(this, translateRequest));
		this.buttonNext.setOnAction(e -> {
			if(this.currentScreen.getController().onValidate(this, translateRequest)) {
				this.currentScreen.getController().onConfirm(this, translateRequest);
			}
		});

		this.reset();

	}
	
	public void reset() {
		this.translateRequest = new TranslateRequest();
		this.setCurrentScreen(Screen.FileBasket);
	}

	public Button getButtonBack() {
		return buttonBack;
	}

	public Button getButtonCancel() {
		return buttonCancel;
	}

	public Button getButtonNext() {
		return buttonNext;
	}

	public Screen getCurrentScreen() {
		return currentScreen;
	}

	public void setCurrentScreen(Screen currentScreen) {
		if(this.currentScreen != null) {
			this.currentScreen.getController().onExit(this, translateRequest);
		}
		this.currentScreen = currentScreen;
		this.mainWindow.setCenter(this.currentScreen.getRootNode());
		this.currentScreen.getController().onEnter(this, translateRequest);
	}

}
