package com.imaginea.comparator.domain;

import java.sql.Timestamp;


public class DraftMessage {

	private String message;
	
	private int side;
	
	private int line;
	
	private String status;
	
	private String author;
	
	private Timestamp writtenOn;
	
	private String uuid;
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public int getSide() {
		return side;
	}
	
	public void setSide(int side) {
		this.side = side;
	}
	
	public int getLine() {
		return line;
	}
	
	public void setLine(int line) {
		this.line = line;
	}
	
	public String getStatus() {
		return status;
	}
	
	public String getAuthor() {
		return author;
	}
	
	public void setAuthor(String author) {
		this.author = author;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public Timestamp getWrittenOn() {
		return writtenOn;
	}
	
	public void setWrittenOn(Timestamp writtenOn) {
		this.writtenOn = writtenOn;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
}
