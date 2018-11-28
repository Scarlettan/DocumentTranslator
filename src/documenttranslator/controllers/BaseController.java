/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package documenttranslator.controllers;

import documenttranslator.forms.TranslateRequest;
import javafx.fxml.Initializable;

/**
 *
 * @author joni.jinon
 */
public interface BaseController extends Initializable {
	public void onEnter(MainWindowController c, TranslateRequest tr);
	public void onBack(MainWindowController c, TranslateRequest tr);
	public boolean onValidate(MainWindowController c, TranslateRequest tr);
	public void onConfirm(MainWindowController c, TranslateRequest tr);
	public void onExit(MainWindowController c, TranslateRequest tr);
}
