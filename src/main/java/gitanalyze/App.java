package gitanalyze;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import gitanalyze.action.ReportRegressionBug;

/**
 * find regression bug from git repos
 * author sxz 2020，2.6
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException
    {
    	BufferedReader scanner = new BufferedReader(new InputStreamReader(System.in));
    	System.out.println("entering repoPath.....");
    	String repoPath =scanner.readLine();
    	System.out.println("process.....");
    	ReportRegressionBug  report =new ReportRegressionBug();
    	report.searchRegressionBug(repoPath);
    	
    	
    }
}
