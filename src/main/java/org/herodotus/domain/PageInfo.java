package org.herodotus.domain;

public class PageInfo {
	
	private  long id;
	private  String language;
	private String touched; // date of This page was last modified on
	private String first_paragraph;
	private Location location; //longtitude and langtitude
	
	
	
	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public String getFirst_paragraph() {
		return first_paragraph;
	}

	public void setFirst_paragraph(String first_paragraph) {
		this.first_paragraph = first_paragraph;
	}

	public String getTouched() {
		return touched;
	}

	public String getLanguage() {
		return language;
	}

	public long getId() {
		return id;
	}

	public void setTouched(String touched) {
		this.touched = touched;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setLanguage(String language) {
		this.language = language;
	}
	
}
