package bugreportanalyze;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;

import bugreportanalyze.action.BRParsing;

public class AppEnter {

	public static void main(String[] args) throws Exception {
		BRParsing brParsing=new BRParsing();
		System.out.println("entering file path......");	
		BufferedReader reader=new BufferedReader(new InputStreamReader(System.in));
		String xmlPath=reader.readLine();
		System.out.println("reading xml..... may need longtime");	
		brParsing.printDesc(xmlPath);
	}
}
