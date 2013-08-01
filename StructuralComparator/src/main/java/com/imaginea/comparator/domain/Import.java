package com.imaginea.comparator.domain;

import com.imaginea.comparator.domain.node.ImportNode;
import com.imaginea.comparator.repo.ComparatorUtil;

public class Import {
	private short diff = ComparatorUtil.MODIFIEDDEFAULT;
	private ImportNode[] lines = new ImportNode[2];

	public short getDiff() {
		return diff;
	}

	public void setDiff(short diff) {
		this.diff = diff;
	}

	public ImportNode getLine(int lineNum) {
		return lines[lineNum];
	}

	public void setLines(ImportNode line, int lineNum) {
		lines[lineNum] = line;
	}
	
	@Override
	public String toString() {
		return "ImportNode [isImportSame=" + getDiff() + ", ActualImport=" + lines[0] + ", " + "ExpectedImport=" + lines[1] + "]";
	}

	public ImportNode[] getLines() {
		return lines;
	}

	public void setLines(ImportNode[] lines) {
		this.lines = lines;
	}

}
