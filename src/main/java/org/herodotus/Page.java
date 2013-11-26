package org.herodotus;

import java.util.List;

public class Page {
	
	private String title;
	
	private long id;
	
	private List<Link> categories;
	
	private List<Link> outlinks;

	public String getTitle() {
		return title;
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
	

}
