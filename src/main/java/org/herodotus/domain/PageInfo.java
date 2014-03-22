package org.herodotus.domain;

public class PageInfo {
	
	private  long id;
	private  String language;
	private String touched; // date of This page was last modified on
	
	
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
