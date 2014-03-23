package org.herodotus.domain;

import java.util.List;

public class PageInfo {
	
	private  long id;
	private  String language;
	private String touched; // date of This page was last modified on
	private String summary;
	private GeoLocation geoLocation; //longtitude and langtitude
	
	
	private List<String> locationList;//country,city,street
	private List<String>  thubnailsList;
	private List<String>  typesList;
	private List<String> externalLinkList;
	
	private long inLinkCounter;
	private long outLinkCounter;
	private String photoCollectionUrl;
	private List<String> websitesList;
	private List<String> categoriesList;
	
	
	
	
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

	public List<String> getWebsitesList() {
		return websitesList;
	}

	public void setWebsitesList(List<String> websitesList) {
		this.websitesList = websitesList;
	}

	public List<String> getCategoriesList() {
		return categoriesList;
	}

	public void setCategoriesList(List<String> categoriesList) {
		this.categoriesList = categoriesList;
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

	public List<String> getExternalLinkList() {
		return externalLinkList;
	}

	public void setExternalLinkList(List<String> externalLinkList) {
		this.externalLinkList = externalLinkList;
	}


	public List<String> getLocationList() {
		return locationList;
	}

	public void setLocationList(List<String> locationList) {
		this.locationList = locationList;
	}

	public GeoLocation getGeoLocation() {
		return geoLocation;
	}

	public void setGeoLocation(GeoLocation geoLocation) {
		this.geoLocation = geoLocation;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
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
