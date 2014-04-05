package org.herodotus.domain;

import java.util.List;

public class Page {

	private long id;  //the wikipage id

	private String title; 
	
	private String summary;

	private String url;  //wikipedia oage url
	
	private String dbpedia_url;  //dbpedia page url

	private String country;
	
	private String language;
	
	private String touched; // date of This page was last modified on

	private List<String> categories;
	
	private List<String> categoriesDbPediaURL;

	
	private List<String> outlinks; //page's outlinks to other wikipedia pages(ENTITIES) - internal links

	private GeoLocation geoLocation;
	
	private long inLinkCounter;

	private long outLinkCounter;
	
	private String photoCollectionUrl;
	
	private List<String> locationList;//country,city,street

	private List<String>  thubnailsList;

	private List<String>  typesList;

	private List<String> externalLinkList;

	private List<String> websitesList;

	private List<String>  redirectsList;
	
	
	
	public String getDbpedia_url() {
		return dbpedia_url;
	}

	public void setDbpedia_url(String dbpedia_url) {
		this.dbpedia_url = dbpedia_url;
	}

	public List<String> getRedirectsList() {
		return redirectsList;
	}

	public void setRedirectsList(List<String> redirectsList) {
		this.redirectsList = redirectsList;
	}

	public int countNonEmptyArrayListFields(){
		int c=0;
		if(!websitesList.isEmpty())c++;
		if(!externalLinkList.isEmpty())c++;
		if(!typesList.isEmpty())c++;
		if(!thubnailsList.isEmpty())c++;
		if(!locationList.isEmpty())c++;
		if(!outlinks.isEmpty())c++;
		if(!categories.isEmpty())c++;
		return c;
	}
	
	public GeoLocation getGeoLocation() {
		return geoLocation;
	}

	public void setGeoLocation(GeoLocation geoLocation) {
		this.geoLocation = geoLocation;
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



	
	
	public List<String> getCategoriesDbPediaURL() {
		return categoriesDbPediaURL;
	}

	public void setCategoriesDbPediaURL(List<String> categoriesDbPediaURL) {
		this.categoriesDbPediaURL = categoriesDbPediaURL;
	}

	public List<String> getCategories() {
		return categories;
	}

	public void setCategories(List<String> categories) {
		this.categories = categories;
	}

	public List<String> getOutlinks() {
		return outlinks;
	}

	public void setOutlinks(List<String> outlinks) {
		this.outlinks = outlinks;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public List<String> getLocationList() {
		return locationList;
	}

	public void setLocationList(List<String> locationList) {
		this.locationList = locationList;
	}

	public List<String> getThubnailsList() {
		return thubnailsList;
	}

	public void setThubnailsList(List<String> thubnailsList) {
		this.thubnailsList = thubnailsList;
	}

	public List<String> getTypesList() {
		return typesList;
	}

	public void setTypesList(List<String> typesList) {
		this.typesList = typesList;
	}

	public List<String> getExternalLinkList() {
		return externalLinkList;
	}

	public void setExternalLinkList(List<String> externalLinkList) {
		this.externalLinkList = externalLinkList;
	}

	public long getInLinkCounter() {
		return inLinkCounter;
	}


	public void setInLinkCounter(long inLinkCounter) {
		this.inLinkCounter = inLinkCounter;
	}

	public long getOutLinkCounter() {
		return outLinkCounter;
	}

	public void setOutLinkCounter(long outLinkCounter) {
		this.outLinkCounter = outLinkCounter;
	}

	public String getPhotoCollectionUrl() {
		return photoCollectionUrl;
	}

	public void setPhotoCollectionUrl(String photoCollectionUrl) {
		this.photoCollectionUrl = photoCollectionUrl;
	}

	public List<String> getWebsitesList() {
		return websitesList;
	}

	public void setWebsitesList(List<String> websitesList) {
		this.websitesList = websitesList;
	}


	@Override
	public String toString() {
		return "Page [\nid=" + id + ", \ntitle=" + title + ", \nsummary=" + summary
				+ ", \nurl=" + url + ", \ncategories=" + categories + ", \noutlinks="
				+ outlinks + ", \ncountry=" + country + ", \nlanguage=" + language
				+ ", \ntouched=" + touched + ", \ngeoLocation=" + geoLocation
				+ ", \ninLinkCounter=" + inLinkCounter + ", \noutLinkCounter="
				+ outLinkCounter + ", \nphotoCollectionUrl=" + photoCollectionUrl
				+ ", \nlocationList=" + locationList + ", \nthubnailsList="
				+ thubnailsList + ", \ntypesList=" + typesList
				+ ", \nexternalLinkList=" + externalLinkList + ", \nwebsitesList="
				+ websitesList + "]";
	}
	

}
