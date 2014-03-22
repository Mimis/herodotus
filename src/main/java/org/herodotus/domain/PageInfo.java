package org.herodotus.domain;

public class PageInfo {
	
	private final long id;
	private final String language;

	public PageInfo(long id, String language) {
		this.id = id;
		this.language = language;
	}
	
	public String getLanguage() {
		return language;
	}

	public long getId() {
		return id;
	}

}
