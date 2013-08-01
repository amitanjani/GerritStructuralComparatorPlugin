package com.imaginea.comparator.test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.imaginea.comparator.domain.ComparisonResult;
import com.imaginea.comparator.domain.Import;
import com.imaginea.comparator.domain.Package;
import com.imaginea.comparator.domain.Type;
import com.imaginea.comparator.repo.ComparatorImpl;


public class StructuralComparatorTest{

	@Test
	public final void assertPackageEqual() {
		ComparatorImpl comparator = new ComparatorImpl();
		ComparisonResult result = comparator.compare(
				loadFile("src/test/java/com/imaginea/comparator/test/StructuralComparatorTest.java"),
				loadFile("src/test/java/com/imaginea/comparator/test/StructuralComparatorTest.java"));
		com.imaginea.comparator.domain.Package pkg = result.getPkg();
		isPackageDifferent(pkg);

	}

	@Test
	public final void assertImportsEqual() {
		ComparatorImpl comparator = new ComparatorImpl();
		ComparisonResult result =comparator.compare(
				loadFile("src/test/java/com/imaginea/comparator/test/StructuralComparatorTest.java"),
				loadFile("src/test/java/com/imaginea/comparator/test/StructuralComparatorTest.java"));
		List<Import> imports = result.getImports();
		isImportsDifferent(imports);
	}
	
	@Test
	public final void assertTypesEqual() {
		ComparatorImpl comparator = new ComparatorImpl();
		ComparisonResult result = comparator.compare(
				loadFile("src/test/java/com/imaginea/comparator/test/StructuralComparatorTest.java"),
				loadFile("src/test/java/com/imaginea/comparator/test/StructuralComparatorTest.java"));
		List<Type> types = result.getTypes();
		isTypesDifferent(types);
	}


	@Test
	public final void assertJavaFileStructuresEqual() {
		ComparatorImpl comparator = new ComparatorImpl();
		ComparisonResult result = comparator.compare(
				loadFile("src/test/java/com/imaginea/comparator/test/StructuralComparatorTest.java"),
				loadFile("src/test/java/com/imaginea/comparator/test/StructuralComparatorTest.java"));
		isPackageDifferent(result.getPkg());
		isImportsDifferent(result.getImports());
		isTypesDifferent(result.getTypes());
	}
	

	private void isPackageDifferent(Package pkg) {
		if (pkg.getDiff() != 0)
			Assert.fail("Package is different: " + pkg);
	}

	void isImportsDifferent(List<Import> imports) {
		for (Import imp : imports)
			if (imp.getDiff() != 0)
				Assert.fail("Imports are different: " + imp);
	}
	
	private void isTypesDifferent(List<Type> types) {
		for (Type type : types)
			if (type.getDiff() != 0)
				Assert.fail("Imports are different: " + type);
	}

	
	private String loadFile( String filePath ){
		StringBuffer buffer = new StringBuffer();
		BufferedReader br = null;
		String sCurrentLine;
		try {
			br = new BufferedReader(new FileReader( filePath ));
			while ((sCurrentLine = br.readLine()) != null) {
				buffer.append(sCurrentLine+'\n');
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}  finally {
			try {
				if ( br != null )
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		if( null == buffer ){
			Assert.fail( "Error Occured in reading file" );
		}
		
		return buffer.toString().trim();
	}
}
