package com.tkd.google;

import java.util.HashMap;
import java.util.Map;

public class Account {	
	private String name;
	private String email;
	private String password;
	private String domain;
	private String applName;
	private static final Map<String, Resource> resources = new HashMap<String, Resource>();	
	static{
//		resources.put("Test'HareNew", new Resource("Test'HareNew-Hyd-2nd Floor-Meeting-12-LCD-WB","Test'HareNew","MidTown 6-3-348, Road No. 1, Banjara Hills, Hyderabad, AP, India, 500035","Meeting"));
		resources.put("O'Hare", new Resource("O'Hare-Hyd-2nd Floor-Meeting-12-LCD-WB","O'Hare","MidTown 6-3-348, Road No. 1, Banjara Hills, Hyderabad, AP, India, 500035","Meeting"));
		resources.put("Narita", new Resource("Narita-Hyd-2nd Floor-Meeting-4-WB","Narita","MidTown 6-3-348, Road No. 1, Banjara Hills, Hyderabad, AP, India, 500036","Meeting"));
		resources.put("Broadway", new Resource("Broadway-Hyd-2nd Floor-Meeting-4-WB","Broadway","MidTown 6-3-348, Road No. 1, Banjara Hills, Hyderabad, AP, India, 500034","Meeting"));
		resources.put("Broadway", new Resource("Broadway-Hyd-2nd Floor-Meeting-4-WB","Broadway","MidTown 6-3-348, Road No. 1, Banjara Hills, Hyderabad, AP, India, 500034","Meeting"));
		resources.put("Shinjuka", new Resource("Shinjuka-Hyd-2nd Floor-Meeting-4-WB","Shinjuka","MidTown 6-3-348, Road No. 1, Banjara Hills, Hyderabad, AP, India, 500035","Meeting"));
		resources.put("Schipol", new Resource("Schipol-Hyd-2nd Floor-Meeting-7-WB","Schipol","MidTown 6-3-348, Road No. 1, Banjara Hills, Hyderabad, AP, India, 500036","Meeting"));
		resources.put("Kings Cross", new Resource("Kings Cross-Hyd-3rd Floor-Meeting-4-WB","Kings Cross","MidTown 6-3-348, Road No. 1, Banjara Hills, Hyderabad, AP, India, 500037","Meeting"));
		resources.put("Shibuya", new Resource("Shibuya-Hyd-3rd Floor-Meeting-4-WB","Shibuya","MidTown 6-3-348, Road No. 1, Banjara Hills, Hyderabad, AP, India, 500038","Meeting"));
		resources.put("Grand Central", new Resource("Grand Central-Hyd-3rd Floor-Meeting-4-WB","Grand Central","MidTown 6-3-348, Road No. 1, Banjara Hills, Hyderabad, AP, India, 500039","Meeting"));
		resources.put("Union Station", new Resource("Union Station-Hyd-3rd Floor-Meeting-6-WB","Union Station","MidTown 6-3-348, Road No. 1, Banjara Hills, Hyderabad, AP, India, 500040","Meeting"));
		resources.put("Gare du Nord", new Resource("Gare du Nord-Hyd-3rd Floor-Meeting-12-WB","Gare du Nord","MidTown 6-3-348, Road No. 1, Banjara Hills, Hyderabad, AP, India, 500041","Meeting"));
		resources.put("Piazza Navona", new Resource("Piazza Navona-Hyd-4th Floor-Meeting-4-WB","Piazza Navona","MidTown 6-3-348, Road No. 1, Banjara Hills, Hyderabad, AP, India, 500042","Meeting"));
		resources.put("Piccadilly Circus", new Resource("Piccadilly Circus-Hyd-4th Floor-Meeting-4-WB","Piccadilly Circus","MidTown 6-3-348, Road No. 1, Banjara Hills, Hyderabad, AP, India, 500043","Meeting"));
		resources.put("Trafalgar Square", new Resource("Trafalgar Square-Hyd-4th Floor-Meeting-4-WB","Trafalgar Square","MidTown 6-3-348, Road No. 1, Banjara Hills, Hyderabad, AP, India, 500044","Meeting"));
		resources.put("Connaught Place", new Resource("Connaught Place-Hyd-4th Floor-Meeting-6-WB","Connaught Place","MidTown 6-3-348, Road No. 1, Banjara Hills, Hyderabad, AP, India, 500045","Meeting"));
		resources.put("Times Square", new Resource("Times Square-Hyd-4th Floor-Meeting-12-WB","Times Square","MidTown 6-3-348, Road No. 1, Banjara Hills, Hyderabad, AP, India, 500046","Meeting"));
		resources.put("Wall Street", new Resource("Wall Street-Hyd-5th Floor-Meeting-4-WB","Wall Street","MidTown 6-3-348, Road No. 1, Banjara Hills, Hyderabad, AP, India, 500047","Meeting"));
		resources.put("Silicon Valley", new Resource("Silicon Valley-Hyd-5th Floor-Meeting-4-WB","Silicon Valley","MidTown 6-3-348, Road No. 1, Banjara Hills, Hyderabad, AP, India, 500048","Meeting"));
		resources.put("Canary Wharf", new Resource("Canary Wharf-Hyd-5th Floor-Meeting-12-WB","Canary Wharf","MidTown 6-3-348, Road No. 1, Banjara Hills, Hyderabad, AP, India, 500049","Meeting"));
		resources.put("Bond Street", new Resource("Bond Street-Hyd-5th Floor-Meeting-4-WB","Bond Street","MidTown 6-3-348, Road No. 1, Banjara Hills, Hyderabad, AP, India, 500050","Meeting"));
		resources.put("Nariman Point", new Resource("Nariman Point-Hyd-5th Floor-Meeting-6-WB","Nariman Point","MidTown 6-3-348, Road No. 1, Banjara Hills, Hyderabad, AP, India, 500051","Meeting"));
		resources.put("Ghirardelli Square", new Resource("Ghirardelli Square-Hyd-5th Floor-Meeting-8-WB","Ghirardelli Square","MidTown 6-3-348, Road No. 1, Banjara Hills, Hyderabad, AP, India, 500052","Meeting"));
		resources.put("Charing Cross", new Resource("Charing Cross-Hyd-5th Floor-Training-16-WB","Charing Cross","MidTown 6-3-348, Road No. 1, Banjara Hills, Hyderabad, AP, India, 500053","Training"));
		resources.put("Lexington Avenue", new Resource("Lexington Avenue-Hyd-4th Floor-Training-16-WB","Lexington Avenue","MidTown 6-3-348, Road No. 1, Banjara Hills, Hyderabad, AP, India, 500054","Training"));
		resources.put("Penn Station", new Resource("Penn Station-Hyd-3rd Floor-Training-20-WB","Penn Station","MidTown 6-3-348, Road No. 1, Banjara Hills, Hyderabad, AP, India, 500055","Training"));		
	}

	public Account(String name, String email, String password, String domain,
			String applName) {
		this.name = name;
		this.email = email;
		this.password = password;
		this.domain = domain;
		this.applName = applName;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public static Map<String, Resource> getResources() {
		return resources;
	}

	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public String getApplName() {
		return applName;
	}
	public void setApplName(String applName) {
		this.applName = applName;
	}
	@Override
	public String toString() {
		return "Account [name=" + name + ", email=" + email + ", password="
				+ password + ", domain=" + domain + ", applName=" + applName
				+ "]";
	}	
}
