package com.imaginea.comparator.domain.node;

import java.util.ArrayList;
import java.util.List;

public class MethodDeclarationNode extends DeclarationNode {
	private boolean isConstructor;
	private String returnType;
	private String parameters;
	private List<String> thrownExceptions;
	private List<Object> blockofStatements = new ArrayList<Object>();

	public boolean isConstructor() {
		return isConstructor;
	}

	public void setConstructor(boolean isConstructor) {
		this.isConstructor = isConstructor;
	}

	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	public String getParameters() {
		return parameters;
	}

	public void setParameters(String parameters) {
		this.parameters = parameters;
	}

	public List<String> getThrownExceptions() {
		return thrownExceptions;
	}

	public void setThrownExceptions(List<String> thrownExceptions) {
		this.thrownExceptions = thrownExceptions;
	}

	public List<Object> getBlockofStatements() {
		return blockofStatements;
	}

	public void setBlockofStatements(List<Object> blockofStatements) {
		this.blockofStatements = blockofStatements;
	}

	@Override
	public String toString() {
		return "MethodNode [MethodName=" + getName() + ", Modifiers=" + getModifiers() + ", " + "Paramaters=" + getParameters() + ", "
				+ "ReturnType=" + getReturnType() + ", " + "ThownExceptions=" + getThrownExceptions() + "BlockStatements="
				+ getBlockofStatements() + "ChildDeclarations=" + getChildDeclarations() + "]";
	}

}
