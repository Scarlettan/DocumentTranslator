/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package documenttranslator.processors;

import documenttranslator.Prefs;
import documenttranslator.forms.TranslateRequest;
import documenttranslator.services.FileTools;
import documenttranslator.services.FileZipper;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;

/**
 *
 * @author joni.jinon
 */
public class OfficeDocumentProcessor extends BaseProcessor {

	private final Pattern excelSymbolsPattern = Pattern.compile("[ ]+([!$:'])[ ]+");
	private final CheckBox optionReplaceIncase = new CheckBox("Replace \"In case\" with \" \"");
	private final CheckBox optionTrimSymbols = new CheckBox("Trim MS Excel Symbols ( $, !, : , ' )");
	private File tempWorkingDirectory;

	public OfficeDocumentProcessor() {
		
	}

	@Override
	public AbstractTask getReadFileTask(TranslateRequest tr) {
		return new ReadFileTask(tr);
	}

	@Override
	public AbstractTask getFindAndReplaceTask(TranslateRequest tr) {
		return new FindAndReplaceTask(tr);
	}
	
	@Override
	public void onLoadMoreSettings(VBox moreSettingsPane) {
		this.optionReplaceIncase.setSelected(true);
		this.optionTrimSymbols.setSelected(true);
		moreSettingsPane.getChildren().addAll(this.optionReplaceIncase, this.optionTrimSymbols);
	}

	private String htmlEncodeExcelSpecialChars(String s) {
		return s.replace("<", "&lt;")
				.replace(">", "&gt;")
				.replace("&", "&amp;");
	}

	private String htmlDecodeExcelSpecialChars(String s) {
		return s.replace("&lt;", "<")
				.replace("&gt;", ">")
				.replace("&amp;", "&");
	}

	private class ReadFileTask extends AbstractTask {

		public ReadFileTask(TranslateRequest tr) {
			super(tr);
		}

		@Override
		protected Void call() throws Exception {

			// Prepare Input File
			File sourceFile = this.getTranslateRequest().getInputFile();

			// Create Temporary Directory
			String tempWorkingDirName = Prefs.DIR_JOBS + Prefs.DIR_JOBPREFIX + System.nanoTime();
			tempWorkingDirectory = new File(tempWorkingDirName);
			tempWorkingDirectory.mkdirs();

			if (sourceFile.exists() && tempWorkingDirectory.exists()) {

				// Unzip Source
				FileZipper.unzip(sourceFile, tempWorkingDirectory);

				// Extract Terms
				this.getTranslateRequest().getInterpreter().getExtractedTerms().clear();

				FileTools.readFilesRecursive(tempWorkingDirectory, f -> {

					if (!f.getName().endsWith(".xml")) {
						return;
					}

					// Get file contents and insert to string
					String fileContents = "";
					try {
						fileContents = String.join(" ", Files.readAllLines(f.toPath(), StandardCharsets.UTF_8));
					} catch (IOException e) {
						System.out.println(e);
					}

					// Extract Terms From String
					this.getTranslateRequest().getInterpreter().interpretString(fileContents);

				});

				// Arrange
				Collections.sort(this.getTranslateRequest().getInterpreter().getExtractedTerms(), (x, y) -> Integer.compare(y.length(), x.length()));

			} else {

			}

			return null;

		}

	}

	private class FindAndReplaceTask extends AbstractTask {

		private final List<File> filesToProcess = new ArrayList<>();
		private int findAndReplaceCount;
		private int totalWorkCount;

		public FindAndReplaceTask(TranslateRequest tr) {
			super(tr);
		}

		@Override
		protected Void call() throws Exception {
			
			// Prepare
			addLog("Scanning files to read...");
			FileTools.readFilesRecursive(tempWorkingDirectory, f -> {
				if (f.getName().endsWith(".xml")) {
					addLog("\tFound: " + f.getName());
					this.filesToProcess.add(f);
				}
			});

			this.findAndReplaceCount = this.filesToProcess.size() * this.getTranslateRequest().getInterpreter().getDictionary().size();
			this.totalWorkCount = this.findAndReplaceCount;

			// Start
			int progressCount = 0;

			this.updateMessage("Task Started");
			this.updateProgress(progressCount, this.totalWorkCount);

			addLog("Processing files...");
			for (int in = 0, fi = this.filesToProcess.size(); in < fi; in++) {
				File f = this.filesToProcess.get(in);
				addLog("\tFile: " + f.getName());

				final Path path = f.toPath();
				final Charset charset = StandardCharsets.UTF_8;

				// Read file contents
				String content = new String(Files.readAllBytes(path), charset);

				// Find and Replace
				for (Map.Entry<String, String> entry : this.getTranslateRequest().getInterpreter().getDictionary().entrySet()) {

					String textFind = htmlEncodeExcelSpecialChars(entry.getKey());
					String textReplace = htmlEncodeExcelSpecialChars(entry.getValue());
					
					if(optionReplaceIncase.isSelected() && textReplace.equals("In case")) {
						textReplace = " ";
					}
					if(optionTrimSymbols.isSelected()) {
						textReplace = textReplace.replaceAll(excelSymbolsPattern.pattern(), "$1");
					}
					
					content = content.replace(textFind, textReplace);

					addLog("\t\tReplacing: " + textFind);
					addLog("\t\tWith: " + textReplace);
					this.updateMessage("Replacing: " + textFind + " with " + textReplace);
					this.updateProgress(progressCount++, this.totalWorkCount);
				}

				// Write
				try {
					addLog("\t\tWriting: " + f.getName());
					Files.write(path, content.getBytes(charset));
				} catch (IOException ex) {
					addLog("\t\tERROR: " + ex.getMessage());
					System.out.println(ex);
				}
				
				addLog("\tDone!");

			}

			addLog("Saving output file...");
			FileZipper.zip(tempWorkingDirectory, this.getTranslateRequest().getOutputFile());
			
			addLog("Cleaning up temp files...");
			FileTools.deleteFilesRecursive(tempWorkingDirectory);

			addLog("Completed!");
			this.updateMessage("Completed!");

			return null;

		}

	}

}
