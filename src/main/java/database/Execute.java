package database;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Scanner;

public class Execute {
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		/* Linux
		 * processBuilder.command("bash", "-c", "");
		 * Windows
		 * processBuilder.command("cmd.exe", "/c", ""); */
		
		// Setting environment
		ProcessBuilder pb = new ProcessBuilder();
        Map<String, String> map = pb.environment();
        String PATH = map.get("PATH") +
                File.pathSeparator +"/Users/qihao/apache-maven-3.6.1/bin" +
                File.pathSeparator +"/Library/Java/JavaVirtualMachines/jdk1.8.0_211.jdk/Contents/Home";
        map.put("PATH",PATH);
        String JAVA_HOME = "/Library/Java/JavaVirtualMachines/jdk1.8.0_211.jdk/Contents/Home";
        map.put("JAVA_HOME",JAVA_HOME);
        
        String command = "mvn -o -Dtest=TestGenericObjectPool#testNoInvalidateNPE -Drat.skip=true surefire:test";
        Scanner sc = new Scanner(System.in);
        System.out.print("Version: ");
		
		while(sc.hasNextLine()) {
	    	String input = sc.nextLine();
	        switch(input) {
	        	case "fix": {
	        		System.out.print(executCommand("cd fix", "fix", pb));
	        		System.out.print(executCommand("pwd", "fix", pb));
	        		System.out.println("Running tests...");
	        		System.out.print(executCommand(command, "fix", pb));
	        		System.out.println("Done.");
	        		break;
	        	}
	        	case "regression": {
	        		System.out.print(executCommand("cd regression", "regression", pb));
	        		System.out.print(executCommand("pwd", "regression", pb));
	        		System.out.println("Running tests...");
	        		System.out.print(executCommand(command, "regression", pb));
	        		System.out.println("Done.");
	        		break;
	        	}
	        	case "working": {
	        		System.out.print(executCommand("cd working", "working", pb));
	        		System.out.print(executCommand("pwd", "working", pb));
	        		System.out.println("Running tests...");
	        		System.out.print(executCommand(command, "working", pb));
	        		System.out.println("Done.");
	        		break;
	        	}
	        	default:
	        		System.out.print("Error");
        	}
        }
    }
	
	public static String executCommand(String cmd, String version, ProcessBuilder pb) {
        pb.directory(new File("/Users/qihao/Desktop/database_v1/pool/"+version));
        pb.command("bash", "-c", cmd);
        
        String result = "";
        
        try {
            Process process = pb.start();
            InputStreamReader inputStr = new InputStreamReader(process.getInputStream());
            BufferedReader bufferReader = new BufferedReader(inputStr);
            String line;
            while ((line = bufferReader.readLine()) != null) {
                result += line + "\n";
            }
            int exitCode = process.waitFor();
            if(exitCode != 0 && exitCode != 1) {
                System.out.println("\nExited with error code : " + exitCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        return result;
	}
}