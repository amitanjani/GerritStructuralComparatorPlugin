package com.imaginea.gerritplugin.utils;


public class PatchDetail {
	
	private int patchSetId;
	
	private int changeId;
	
	private String fileName;
	
	private boolean baseSet;

	public PatchDetail(){}
	
	public PatchDetail( String url ){
		parseURL( url );
	}
	
	private void parseURL( String url ){
		if( null != url ){
			String[] changeDetails =url.split(",");
			if( changeDetails.length == 3 ){
				setPatchSetId( Integer.valueOf( changeDetails[1] ) );
				
				if( null != changeDetails[2] && (changeDetails[2].length() - 2) > 0){
					setFileName( changeDetails[2].substring(0, changeDetails[2].length()-2) );
				}
				
				if( null != changeDetails[2] && changeDetails[2].indexOf('^') >= 0 ){
					int index = changeDetails[2].indexOf('^');
					if( index+1 <= changeDetails[2].length() && changeDetails[2].charAt(index+1) == '1' ){
						setBaseSet(true);
					}else{
						setBaseSet(false);
					}
				}else{
					setBaseSet(true);
				}
				
				String tmpUrl = changeDetails[0];
				String[] tmpChangeId = tmpUrl.split("/");
				if( tmpChangeId.length == 5 ){
					setChangeId( Integer.valueOf(tmpChangeId[4]) );
				}
			}
		}
	}

	public int getPatchSetId() {
		return patchSetId;
	}

	public void setPatchSetId(int patchSetId) {
		this.patchSetId = patchSetId;
	}

	public int getChangeId() {
		return changeId;
	}

	public void setChangeId(int changeId) {
		this.changeId = changeId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public boolean isBaseSet() {
		return baseSet;
	}

	public void setBaseSet(boolean baseSet) {
		this.baseSet = baseSet;
	}
}
