package com.imaginea.comparator.repo;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Scanner;

import org.apache.log4j.Logger;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

public class JavaParser {

	private static org.apache.log4j.Logger log = Logger.getLogger(JavaParser.class);
	
	static CompilationUnit parse(char[] charArray) {
		log.debug("Executing CompilationUnit parse(char[] charArray)");
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setSource(charArray);
		@SuppressWarnings("rawtypes")
		Map options = JavaCore.getOptions();
		JavaCore.setComplianceOptions(JavaCore.VERSION_1_7, options);
		parser.setCompilerOptions(options);

		return (CompilationUnit) parser.createAST(null);
	}

	static CompilationUnit parse(File file) throws FileNotFoundException {
		log.debug("Executing CompilationUnit parse(File file)");
		Scanner scanner = new Scanner(file);
		StringBuffer theString = new StringBuffer( scanner.nextLine() );
		while (scanner.hasNextLine()){
			theString.append("\n").append( scanner.nextLine() );
		}
		scanner.close();
		char[] charArray = theString.toString().toCharArray();
		return parse(charArray);
	}

	static CompilationUnit parse(String fileName) throws FileNotFoundException {
		log.debug("Executing CompilationUnit parse(String fileName)");
		File file = new File(fileName);
		return parse(file);
	}
}
