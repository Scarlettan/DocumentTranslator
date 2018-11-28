/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package documenttranslator.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 *
 * @author joni.jinon
 */
public class FileZipper {
	
	private final ArrayList<String> fileList = new ArrayList<>();
	private final File sourceFile;
	private final File targetFile;

	public FileZipper(File sourceFile, File targetFile) {
		this.sourceFile = sourceFile;
		this.targetFile = targetFile;
		this.listFiles(this.sourceFile);
	}
	
	private void listFiles(File parent) {
		
		if(parent.isFile()){
			this.fileList.add(parent.getAbsolutePath().replace(this.sourceFile.getAbsolutePath(), ""));
		}
		else if(parent.isDirectory()){
			for (File f : parent.listFiles()) {
				this.listFiles(f);
			}
		}
		
	}
	
	private void writeArchive() {
		
		byte[] buffer = new byte[1024];
		
		try{
			
			FileOutputStream fos = new FileOutputStream(this.targetFile);
			try (ZipOutputStream zos = new ZipOutputStream(fos)) {
				
				Spy.log().toConsole()
						//.toJobFile(logFileName)
						.asInfo("Archive to : " + this.targetFile.getAbsolutePath());
				
				for(String file : this.fileList){
					
					Spy.log().toConsole()
						//.toJobFile(logFileName)
						.asInfo("File Added : " + file);
					
					ZipEntry ze = new ZipEntry(file.substring(1));
					zos.putNextEntry(ze);
					
					try (FileInputStream in = new FileInputStream(this.sourceFile.getAbsolutePath() + file)) {
						int len;
						while ((len = in.read(buffer)) > 0) {
							zos.write(buffer, 0, len);
						}
					}
				}
				
				zos.closeEntry();
				
			}
			
		}catch(IOException e){
			
			//Spy.log().toConsole().toJobFile(logFileName).asException(e);
			
		}
		
	}
	
	public static void zip(File source, File target) {
		
		Spy.log().toConsole()
				//.toJobFile(logFileName)
				.asInfo("Initiated File Archiving");
		
		// Create Instance
		FileZipper zp = new FileZipper(source, target);
		
		// Zip
		zp.writeArchive();
		
	}
	
	
	
	public static void unzip(File source, File destination) {
		
		Spy.log().toConsole()
				//.toJobFile(logFileName)
				.asInfo("Initiated File Extraction");
		
		String replaceRoot = destination.getAbsolutePath();
		
		try (ZipFile sourceFile = new ZipFile(source)) {
			
			Enumeration<?> enu = sourceFile.entries();
			while (enu.hasMoreElements()) {
				
				ZipEntry zipEntry = (ZipEntry)enu.nextElement();
				
				String name = zipEntry.getName();
				//long size = zipEntry.getSize();
				//long compressedSize = zipEntry.getCompressedSize();
				//System.out.printf("name: %-20s | size: %6d | compressed size: %6d\n", name, size, compressedSize);
				
				// Do we need to create a directory ?
				File file = new File(destination + File.separator + name);
				if (name.endsWith(File.separator)) {
					
					Spy.log().toConsole()
							//.toJobFile(logFileName)
							.asInfo(Spy.padRight("Creating", 10) + " : "
							+ file.getAbsolutePath().replace(replaceRoot, ""));
					
					file.mkdirs();
					
					continue;
					
				}
				
				File parent = file.getParentFile();
				if(!parent.exists()) {
					Spy.log().toConsole()
							//.toJobFile(logFileName)
							.asInfo(Spy.padRight("Creating", 10) + " : "
							+ parent.getAbsolutePath().replace(replaceRoot, ""));
					
					parent.mkdirs();
				}
				
				
				
				FileOutputStream fos;
				try (InputStream is = sourceFile.getInputStream(zipEntry)) {
					fos = new FileOutputStream(file);
					byte[] bytes = new byte[1024];
					int length;
					while ((length = is.read(bytes)) >= 0) {
						fos.write(bytes, 0, length);
					}
				}
				fos.close();
				
				Spy.log().toConsole()
						//.toJobFile(logFileName)
						.asInfo(Spy.padRight("Extracting", 10) + " : "
						+ file.getAbsolutePath().replace(replaceRoot, ""));
				
			}
			
		} catch (IOException e) {
			
			//Spy.log().toConsole().toJobFile(logFileName).asException(e);
			
		}
		
	}
	
}