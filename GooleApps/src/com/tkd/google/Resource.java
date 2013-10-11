package com.tkd.google;

public class Resource {
	private String id;
	private String name;
	private String description;
	private String type;
	private String email;
	
	public Resource(String id, String name, String description, String type) {
		super();
		this.id = id.replace(" ", "_").replace("'", "");
		this.name = id.replace("-WB", "");
		//this.name = name.replace(" ", "_").replace("'", "");
		this.description = description;
		this.type = type;
		//this.email = "imaginea.com_"+ this.id +"@resource.calendar.google.com";
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String domain) {
		//this.email = domain + "_"+ this.name +"@resource.calendar.google.com";
		this.email = domain + "_"+ this.id +"@resource.calendar.google.com";
	}
	
	@Override
	public String toString() {
		return "Resource [id=" + id + ", name=" + name + ", description="
				+ description + ", type=" + type + ", email=" + email + "]";
	}
}
