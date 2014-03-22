package org.herodotus.core;

import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.node.Node;
import org.herodotus.domain.GeoLocation;
import org.herodotus.domain.Link;
import org.herodotus.domain.Page;
import org.herodotus.domain.PageInfo;
import org.herodotus.util.Helper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * Hello world!
 * 
 */
public class Aggregator {

	public static void main(String[] args) throws IOException {
		
		//##########################  INPUT  ########################## 
		//URL with a list of museums from a specific country
		String list_of_museums_from_specific_country_url = "http://en.wikipedia.org/w/api.php?action=query&titles=List_of_museums_in_Greece&prop=links&pllimit=50&format=json";
		//The country's name
		String country = "Greece";
		
		//ELASTIC SEARCH setting
		String CLUSTER_NAME = args[0];
		String INDEX_NAME = "herodotus";
		String DOCUMENT_TYPE = "page";		
		Boolean erase_index_at_start = true;
		//#############################################################
		
		
		
		
		
		
		
		
		
		//##########################  MAIN  ##########################
		Aggregator aggregator = new Aggregator();
		if(erase_index_at_start)
			aggregator.eraseindex(CLUSTER_NAME, INDEX_NAME, DOCUMENT_TYPE);
		List<Page> pageList = aggregator.pageSemantics(list_of_museums_from_specific_country_url, country);
		
		
		IndexerImpl indexer = new IndexerImpl();
		indexer.index(pageList, CLUSTER_NAME, INDEX_NAME, DOCUMENT_TYPE);
		
//		aggregator.getDBPedia("Arta Folklore Museum of \"Skoufas\" Association");
//		System.out.println("aaaaaaa");
		//#############################################################
		
		
		
		
		
	}
	
	public void eraseindex(String CLUSTER_NAME, String INDEX_NAME,String DOCUMENT_TYPE){
		Node node = nodeBuilder().clusterName(CLUSTER_NAME).client(true).node();
		Client client = node.client();
		client.prepareDeleteByQuery(INDEX_NAME).
        setQuery(QueryBuilders.matchAllQuery()).
        setTypes(DOCUMENT_TYPE).
        execute().actionGet();
	}

	
	
	public List<Page> pageSemantics(String list_of_museums_from_specific_country_url,String country) throws IOException,JsonParseException, JsonMappingException {
		List<Page> pageList = new ArrayList<Page>();
		
		byte[] jsonBytes = Helper.getUrl(list_of_museums_from_specific_country_url).getBytes();
		List<String> museumsTitleList = getLinksAttr(jsonBytes,"links");
				

		int counter = 0;
		int c=0;
		for(String museumTitle:museumsTitleList){
			System.out.println(c++ + " museumTitle:"+museumTitle);
			
			
			if(museumTitle.matches(".*[\"|(].*"))
				continue;
			
			/*
			 * get DBpedia data
			 */
			Page page = getDBPedia(museumTitle);

			
			
			
			/*
			 * get page info => ID , LANGUAGE and MODIFICATION DATE
			 * mediawiki api => {action=query}, {prop=info}
			 */
			PageInfo pageInfo1 = getPageInfo(museumTitle);

			if(pageInfo1==null || page==null)
				continue;
			
			
			
			/*
			 * get page info => first paragraph
			 * mediawiki api => {action=parse}, {prop=text}
			 */
//			PageInfo pageInfo2 = getPageInfoFromFirstParagraph(museumTitle);
			
			
			
			
			

			/*
			 * get page outlinks
			 * 			//TODO the following two functionalities can be done with the action=parse and prop=text 
			 */
			String pageLinksUrl = "http://en.wikipedia.org/w/api.php?action=query&titles=" + museumTitle.replaceAll("\\s", "_") + "&prop=links&pllimit=500&format=json";
			byte[] pageJsonBytes = Helper.getUrl(pageLinksUrl).getBytes();
			List<String> outLinksList = getLinksAttr(pageJsonBytes,"links");
//			/*
//			 * get page categories
//			 */
//			String categoryUrl = "http://en.wikipedia.org/w/api.php?action=query&titles=" + museumTitle.replaceAll("\\s", "_") + "&prop=categories&cllimit=500&format=json";
//			byte[] categoryJsonBytes = Helper.getUrl(categoryUrl).getBytes();
//			List<Link> categoriesList = getLinksAttr(categoryJsonBytes,"categories");
			
			
			
			
			/*
			 * filter invalid pages
			 */
//			if(!isValidPage(museumTitle, categoriesList) || pageInfo1 == null ){
//				System.out.print("\tINVALID:"+museumTitle);
//				System.out.println("\tcategoriesList:"+categoriesList.toString());
//				continue;
//			}
			
			
			/*
			 * original page url
			 */
			String pageUrl = "http://en.wikipedia.org/wiki/"+museumTitle.replaceAll("\\s", "_");
			

			
			
			/*
			 * COUNT HOW MANY GEOLOCATION WE FIND..
			 */
			if(page.getGeoLocation() != null){
				System.out.println(page.getGeoLocation().toString());
				counter++;
			}
			
			
			
			/*
			 * save page to list
			 */
//			Page page = new Page();
			
//			page.setId(pageInfo1.getId());
			page.setLanguage(pageInfo1.getLanguage());
			page.setTouched(pageInfo1.getTouched());
//			
////			page.setContent(pageInfo2.getSummary());
////			page.setGeoLocation(pageInfo2.getGeoLocation());
//			
			page.setTitle(museumTitle);
			page.setUrl(pageUrl);
			page.setOutlinks(outLinksList);
//			page.setCategories(categoriesList);
			page.setCountry(country);
			pageList.add(page);
		}
		
		
		
		
		System.out.println("##Nr of pages with coordinates from DBpedia:"+counter);
		return pageList;
	}

	
	
	
	
	private Page getDBPedia(String title) throws IOException{
		title = title.replaceAll("\\s", "_");
		title = title.replaceAll("�", "%E2%80%93");
		String pageUrl = "http://dbpedia.org/data/" + title + ".json";
		String content = Helper.getUrl(pageUrl);
		if(content==null) 
			return null;
		
		byte[] pageJsonBytes = content.getBytes();
		ObjectMapper mapper = new ObjectMapper();
		JsonNode rootNode = mapper.readValue(pageJsonBytes, JsonNode.class);
		JsonNode semanticsNode = rootNode.get("http://dbpedia.org/resource/"+title);		

		if(semanticsNode==null)
			return null;
		
		//GET COORDINATES
		List<String> longtitude = getSemantic(semanticsNode, "http://www.w3.org/2003/01/geo/wgs84_pos#long", "value",null,null);
		List<String> langtitude = getSemantic(semanticsNode, "http://www.w3.org/2003/01/geo/wgs84_pos#lat", "value",null,null);
		
		
		//GET SUMMARY
		List<String> summary = getSemantic(semanticsNode, "http://dbpedia.org/ontology/abstract", "value", "lang","en");
		
		
		//GET LOCATIONS (country,city,street)
		List<String> locations = getSemantic(semanticsNode, "http://dbpedia.org/ontology/location", "value", null, null);
		
		
		//GET Thubnails
		List<String> thubnails = getSemantic(semanticsNode, "http://dbpedia.org/ontology/thumbnail", "value", null, null);
		
		
		//GET TYPE 
		List<String> type = getSemantic(semanticsNode, "http://dbpedia.org/ontology/type", "value", null, null);
		
		//GET EXTERNAL LINKS 
		List<String> wikiPageExternalLink = getSemantic(semanticsNode, "http://dbpedia.org/ontology/wikiPageExternalLink", "value", null, null);
		
		//GET PAGE ID 
		List<String> wikiPageID = getSemantic(semanticsNode, "http://dbpedia.org/ontology/wikiPageID", "value", null, null);
		
		
		
		//GET PAGE IN LINKS COUNTER
		List<String> wikiPageInLinksCounter = getSemantic(semanticsNode, "http://dbpedia.org/ontology/wikiPageInLinkCount", "value", null, null);
		//GET PAGE OUT LINKS COUNTER
		List<String> wikiPageOutLinksCounter = getSemantic(semanticsNode, "http://dbpedia.org/ontology/wikiPageOutLinkCount", "value", null, null);
			
		
		//GET PAGE photcolection
		List<String> wikiPagePhotoCollection = getSemantic(semanticsNode, "http://dbpedia.org/property/hasPhotoCollection", "value", null, null);
		
		
		
		//GET PAGE website
		List<String> wikiPageWebSite = getSemantic(semanticsNode, "http://dbpedia.org/property/website", "value", null, null);
		wikiPageWebSite.addAll(getSemantic(semanticsNode, "http://xmlns.com/foaf/0.1/homepage", "value", null, null));
		
		
		
		//GET PAGE subjects/categories
		List<String> wikiPageCategories = getSemantic(semanticsNode, "http://purl.org/dc/terms/subject", "value", null, null);
			
		
		
		Page pageInfo = new Page();
		if(!longtitude.isEmpty())
			pageInfo.setGeoLocation(new GeoLocation(Float.parseFloat(longtitude.get(0)), Float.parseFloat(langtitude.get(0))));
		if(!summary.isEmpty())
			pageInfo.setSummary(summary.get(0));
		pageInfo.setLocationList(locations);
		pageInfo.setThubnailsList(thubnails);
		pageInfo.setTypesList(type);
		pageInfo.setExternalLinkList(wikiPageExternalLink);
		if(!wikiPageID.isEmpty())
			pageInfo.setId(Long.parseLong(wikiPageID.get(0)));
		if(!wikiPageInLinksCounter.isEmpty())
			pageInfo.setInLinkCounter(Long.parseLong(wikiPageInLinksCounter.get(0)));
		if(!wikiPageOutLinksCounter.isEmpty())
			pageInfo.setOutLinkCounter(Long.parseLong(wikiPageOutLinksCounter.get(0)));
		if(!wikiPagePhotoCollection.isEmpty())
			pageInfo.setPhotoCollectionUrl(wikiPagePhotoCollection.get(0));
		pageInfo.setWebsitesList(wikiPageWebSite);
		pageInfo.setCategories(wikiPageCategories);
		return pageInfo;
	}
	
	private List<String> getSemantic(JsonNode semanticsNode, String ontology_url, String attribute, String condition_attr, String condition_value){
		List<String> outputList = new ArrayList<String>();
		JsonNode pages = semanticsNode.get(ontology_url);
		if(pages==null)
			return outputList;

		Iterator<JsonNode> pagesNode = pages.getElements();
		while(pagesNode.hasNext()) {
			JsonNode pageNode = pagesNode.next();
			if(condition_attr != null && pageNode.get(condition_attr).asText().equalsIgnoreCase(condition_value)){
				JsonNode valueNode = pageNode.get(attribute);
				outputList.add(valueNode.asText());
				return outputList; 
			}
			else if(condition_attr == null){
				outputList.add(pageNode.get(attribute).asText());
			}
		}
		return outputList;
	}	
	
	
	private PageInfo getPageInfoFromFirstParagraph(String title) throws IOException{
		String pageUrl = "http://en.wikipedia.org/w/api.php?action=parse&format=json&prop=text&section=0&page=" + title.replaceAll("\\s", "_");
		byte[] pageJsonBytes = Helper.getUrl(pageUrl).getBytes();
		String firstParagraph = getFirstParagraphMediaWiki(pageJsonBytes);
		
		
		
		if(firstParagraph!=null){
			Document doc = Jsoup.parse(firstParagraph);
			GeoLocation geo_location = getLocationFromFirstParagraph(doc);
			
			
			PageInfo pageInfo = new PageInfo();
			pageInfo.setSummary(doc.text());
			pageInfo.setGeoLocation(geo_location);
			
			return pageInfo;
		}
		else
			return null;
		
	}
	
	private GeoLocation getLocationFromFirstParagraph(Document doc){
		Elements longitude_elements = doc.getElementsByClass("longitude");
		Elements latitude_elements = doc.getElementsByClass("latitude");
		if(longitude_elements.size()==1 && latitude_elements.size()==1){
			String longitude = longitude_elements.get(0).text();
			String latitude = latitude_elements.get(0).text();
			return new GeoLocation(Float.parseFloat(longitude),Float.parseFloat(latitude));
		}
		else
			return null;
	}
	
	private String getFirstParagraphMediaWiki(byte[] pageJsonBytes) throws JsonParseException, JsonMappingException, IOException{
		ObjectMapper mapper = new ObjectMapper();
		JsonNode rootNode = mapper.readValue(pageJsonBytes, JsonNode.class);
		JsonNode parse = rootNode.get("parse");
		String value = null;
		try{
			JsonNode text = parse.get("text");
			value = text.get("*").asText();
		}catch(NullPointerException e){
			return null;
		}
		return value;
	}

	
	
	
	private boolean isValidPage(String title,List<Link> categoriesList){
		if(title.startsWith("List of"))
			return false;

		if(title.toLowerCase().contains("museum"))
			return true;

		if(categoriesList.isEmpty())
			return true;
		
		for(Link cat: categoriesList){
			if(cat.getTitle().toLowerCase().contains("museum"))
				return true;
		}
		return false;
	}
	
	
	private List<String> getLinksAttr(byte[] jsonBytes,String attr) throws JsonParseException, JsonMappingException, IOException{
		// String input = The JSON data from your question
		List<String> linksList = new ArrayList<String>();
		ObjectMapper mapper = new ObjectMapper();

		JsonNode rootNode = mapper.readValue(jsonBytes, JsonNode.class);

		// can also use ArrayNode here, but JsonNode allows us to get(index)
		// line an array:
		JsonNode query = rootNode.get("query");
		JsonNode pages = query.get("pages");
		
		Iterator<JsonNode> pagesNode = pages.getElements();
		while(pagesNode.hasNext()) {
			JsonNode pageNode = pagesNode.next();
			JsonNode links =  pageNode.get(attr);
			
			if(links != null){
				Iterator<JsonNode> linkNode = links.getElements();
				while(linkNode.hasNext()) {
					JsonNode link = linkNode.next();
					String text = link.get("title").asText().replace("Category:", "");
					linksList.add(text);
				}
			}
		}
		return linksList;
	}

	
	
	

	

	/**
	 * get the Id , Language and touched date of a wiki page by its title
	 * @param title
	 * @return pageInfo object with the ID, LANGUGE and TOUCHED adte of the wiki page with the given title! 
	 * @throws IOException
	 */
	private PageInfo getPageInfo(String title) throws IOException{
		String pageUrl = "http://en.wikipedia.org/w/api.php?action=query&titles=" + title.replaceAll("\\s", "_") + "&prop=info&format=json";
		byte[] pageJsonBytes = Helper.getUrl(pageUrl).getBytes();
		PageInfo pageInfo = readPageInfoMediaWiki(pageJsonBytes,"pageid","pagelanguage","touched");
		return pageInfo;
	}
	private PageInfo readPageInfoMediaWiki(byte[] pageJsonBytes,String id_attr,String language_attr,String touched_attr) throws JsonParseException, JsonMappingException, IOException{
		ObjectMapper mapper = new ObjectMapper();
		JsonNode rootNode = mapper.readValue(pageJsonBytes, JsonNode.class);
		JsonNode query = rootNode.get("query");
		JsonNode pages = query.get("pages");
		Iterator<JsonNode> pagesNode = pages.getElements();
		while(pagesNode.hasNext()) {
			JsonNode pageNode = pagesNode.next();
			JsonNode id_links =  pageNode.get(id_attr);
			JsonNode language_links =  pageNode.get(language_attr);
			JsonNode touched_links =  pageNode.get(touched_attr);
			
			if(id_links != null){
				String id = id_links.asText();
				String language = language_links != null ? language_links.asText() : null;
				String touched = touched_links != null ? touched_links.asText() : null;
				
				PageInfo pageInfo = new PageInfo();
				pageInfo.setId(Long.parseLong(id));
				pageInfo.setLanguage(language);
				pageInfo.setTouched(touched);
				
				return pageInfo;
			}
		}
		return null;
	}
	
}
