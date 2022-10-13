package ipr2;

import java.io.File;
import java.util.Scanner;
import java.util.concurrent.Callable;

public class FileScanner implements Callable<String> {
	private File file;
	private String sequence;

	public FileScanner(File file, String sequence) {
	    this.file = file;
	    this.sequence = sequence;
	}

	@Override
	public String call() throws Exception {
	    Scanner sc = null;
	    try {
	        sc = new Scanner(file);
	        while (sc.hasNextLine()) {
	            String line = sc.nextLine();
	            if (line.contains(sequence)) return file.getAbsolutePath();
	        }
	    } catch (Exception e) {
	        throw e;
	    } finally {
	        sc.close();
	    }
	    return null;
	}
}
