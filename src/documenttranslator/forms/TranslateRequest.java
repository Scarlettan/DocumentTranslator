/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package documenttranslator.forms;

import documenttranslator.interpreters.BaseInterpreter;
import documenttranslator.processors.BaseProcessor;
import java.io.File;

/**
 *
 * @author joni.jinon
 */
public class TranslateRequest {

	private File inputFile;
	private File outputFile;
	private BaseInterpreter interpreter;
	private BaseProcessor processor;

	public File getInputFile() {
		return inputFile;
	}

	public void setInputFile(File inputFile) {
		this.inputFile = inputFile;
		String outputFileName = this.inputFile.getAbsolutePath().replace(this.inputFile.getName(), "[Translated] " + this.inputFile.getName());
		this.outputFile = new File(outputFileName);
	}

	public File getOutputFile() {
		return outputFile;
	}

	public void setOutputFile(File outputFile) {
		this.outputFile = outputFile;
	}

	public BaseInterpreter getInterpreter() {
		return interpreter;
	}

	public void setInterpreter(BaseInterpreter interpreter) {
		this.interpreter = interpreter;
	}

	public BaseProcessor getProcessor() {
		return processor;
	}

	public void setProcessor(BaseProcessor processor) {
		this.processor = processor;
	}

}
