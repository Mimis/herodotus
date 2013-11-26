package org.herodotus.domain;

public class Link {
	
	private String title;
	

	/**
	 * @param title
	 */
	public Link(String title) {
		super();
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Link [title=" + title + "]";
	}

	

}
