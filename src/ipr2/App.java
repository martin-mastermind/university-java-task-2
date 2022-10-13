package ipr2;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class App {
	private String sequence;
	
	private List<Future<String>> futureList;
	private ExecutorService executorService;
	
	public App(int threads, String sequence) {
		this.sequence = sequence;
		
		futureList = new ArrayList<Future<String>>();
		executorService = Executors.newFixedThreadPool(threads);
	}
	
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		Scanner scanner = new Scanner(System.in);
		
		System.out.print("Enter catalog path: ");
		String path = scanner.nextLine();
		
		System.out.print("Enter symbols sequence: ");
		String sequence = scanner.nextLine();
		
		System.out.print("Enter threads amount: ");
		int threads = scanner.nextInt();
		
		scanner.close();
		
		File dir = new File(path);
		if(!dir.isDirectory()) {
			System.out.println("Entered path is not directory...");
			return;
		}
		
		App app = new App(threads, sequence);
		app.lookDir(dir);
		
		for (Future<String> future : app.futureList) {
			String result = future.get();
			if(result == null) continue;
			
	        System.out.println(result);
	    }

		app.executorService.shutdown();
	    while(!app.executorService.isTerminated()) {
	        System.out.println("Not terminated yet, sleeping");
	        Thread.sleep(1000);
	    }
	    System.out.println("App closed");
	}
	
	public void lookDir(File dir) {
		File[] listFiles = dir.listFiles();
		if(listFiles == null) return;
		
		for(File item : listFiles){
            if(item.isDirectory()){
                lookDir(item);
                continue;
            }
            
            futureList.add(executorService.submit(new FileScanner(item, sequence)));
        }
	}
}
