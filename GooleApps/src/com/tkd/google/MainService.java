package com.tkd.google;

public class MainService {
	public static void main(String[] args) throws Exception{
		 try {
			 GoogleResourceManagement grm = new GoogleResourceManagement("kumar");
			 grm.execute();
		 } catch (Exception ex) {
			 System.out.println(ex.getMessage());
		 }
	}
}