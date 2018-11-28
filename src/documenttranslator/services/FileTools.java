/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package documenttranslator.services;

import java.io.File;
import java.util.function.Consumer;

/**
 *
 * @author joni.jinon
 */
public class FileTools {

	public static void readFilesRecursive(File target, Consumer<File> c) {

		if (target.exists()) {

			if (target.isDirectory()) {
				for (File f : target.listFiles()) {
					FileTools.readFilesRecursive(f, c);
				}
			} else if (target.isFile()) {
				c.accept(target);
			}

		}

	}

	public static void deleteFilesRecursive(File target) {

		if (target.exists()) {
			if (target.isDirectory()) {
				for (File f : target.listFiles()) {
					deleteFilesRecursive(f);
				}
			}
			target.delete();
		}

	}

	public static void copyFile(File source, File destination) {

	}

}
