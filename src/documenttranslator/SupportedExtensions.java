/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package documenttranslator;

/**
 *
 * @author joni.jinon
 */
public enum SupportedExtensions {
	xlsx(".xlsx", "MS Excel 2007"),
	pptx(".pptx", "MS Powerpoint 2007"),
	docx(".docx", "MS Word Document"),
	ods(".ods", "OpenDocument Spreadsheet"),
	;

	private final String extension;
	private final String description;

	private SupportedExtensions(String extension, String description) {
		this.extension = extension;
		this.description = description;
	}

	public String getExt() {
		return extension;
	}

	public String getDesc() {
		return description;
	}
	
	public static boolean isSupported(String filename) {
		return parse(filename) != null;
	}
	
	public static SupportedExtensions parse(String filename) {
		if(filename != null && filename.contains(".")) {
			String ext = filename.substring(filename.lastIndexOf('.'));
			for (SupportedExtensions se : values()) {
				if(ext.equals(se.getExt())) {
					return se;
				}
			}
		}
		return null;
	}

}
