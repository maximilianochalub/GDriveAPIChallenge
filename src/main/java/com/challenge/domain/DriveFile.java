package com.challenge.domain;

public class DriveFile { // representa un archivo nuevo con contenido creado en el drive
	
	private String id;
	private String title; 
	private String description;

	public DriveFile() { }
	
	public void setId(String p_id) {
		
		this.id = p_id;
		
	}
	
	public String getId() {
		
		return this.id;
		
	}
	
	public String getTitle() {
		
		return this.title;
		
	}
	
	public String getDescription() {
		
		return this.description;
		
	}

}