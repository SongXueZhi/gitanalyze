package bugreportanalyze;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;

import bugreportanalyze.action.BRParse;
import bugreportanalyze.action.BRParseFactory;
import bugreportanalyze.action.BugzillaParse;

public class AppEnter {
/**
 * @author Song Xuezhi
 * @param args
 * @throws Exception
 */
	public static void main(String[] args) throws Exception {
		System.out.println("Please entering file path and bug report type，params use ','split");	
		BufferedReader reader=new BufferedReader(new InputStreamReader(System.in));
		String input=reader.readLine();
		String[] params =input.split(",");
		
		BRParse brParse=new BRParseFactory().getBRParse(params[1]);
		System.out.println("reading xml..... may need longtime");	
		brParse.searchCRB(params[0]);
	}
}
