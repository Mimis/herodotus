package org.herodotus.domain;

import java.util.List;

public class Page {

	private long id;

	private String title;
	
	private String content;

	private String url;
	
	private List<Link> categories;
	
	private List<Link> outlinks;

	private String country;
	
	private String language;
	
	private String touched;
	
	
	private Location location;
	
	
	
	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public String getTouched() {
		return touched;
	}

	public void setTouched(String touched) {
		this.touched = touched;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getTitle() {
		return title;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public List<Link> getCategories() {
		return categories;
	}

	public void setCategories(List<Link> categories) {
		this.categories = categories;
	}

	public List<Link> getOutlinks() {
		return outlinks;
	}

	public void setOutlinks(List<Link> outlinks) {
		this.outlinks = outlinks;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Page [id=" + id + ", title=" + title + ", content=" + content
				+ ", url=" + url + ", categories=" + categories + ", outlinks="
				+ outlinks + "]";
	}

	
}
