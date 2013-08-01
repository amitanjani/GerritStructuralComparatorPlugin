package com.imaginea.comparator.domain.uiNode;

import com.imaginea.comparator.repo.ComparatorUtil;

public class Import {
	private com.imaginea.comparator.domain.Import imprt;
	
	public Import(com.imaginea.comparator.domain.Import imprt) {
		this.imprt = imprt;
	}

	public com.imaginea.comparator.domain.Import getImprt() {
		return imprt;
	}

	public void setImprt(com.imaginea.comparator.domain.Import imprt) {
		this.imprt = imprt;
	}

	@Override
	public String toString() {
		String obj = null;
		if (this.imprt.getDiff() != ComparatorUtil.EXPECTEDDEFAULT)
			obj = this.imprt.getLine(0).getValue();
		else
			obj = this.imprt.getLine(1).getValue();
		return obj;
	}
}
