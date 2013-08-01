package com.imaginea.comparator.domain.uiNode;

import com.imaginea.comparator.domain.node.DeclarationNode;
import com.imaginea.comparator.repo.ComparatorUtil;

public class Type {

	com.imaginea.comparator.domain.Type type;

	public Type(com.imaginea.comparator.domain.Type type) {
		this.type = type;
	}

	public com.imaginea.comparator.domain.Type getType() {
		return type;
	}

	public void setType(com.imaginea.comparator.domain.Type type) {
		this.type = type;
	}

	@Override
	public String toString() {
		String obj = null;
		System.out.println();
		if (type.getDiff() != ComparatorUtil.EXPECTEDDEFAULT) {
			if (this.type.getAbstractDeclarations(0).getName() == null)
				obj = "Line";
			else
				obj = ((DeclarationNode) this.type.getAbstractDeclarations(0)).getName();
		} else {
			if (this.type.getAbstractDeclarations(1).getName() == null)
				obj = "Line";
			else
				obj = ((DeclarationNode) this.type.getAbstractDeclarations(1)).getName();
		}
		return obj;
	}
}
