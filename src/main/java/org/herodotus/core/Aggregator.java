package org.herodotus.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.herodotus.domain.Link;
import org.herodotus.domain.Page;
import org.herodotus.domain.PageInfo;
import org.herodotus.util.Helper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Hello world!
 * 
 */
public class Aggregator {

	public static void main(String[] args) throws IOException {
		
		String url = "http://en.wikipedia.org/w/api.php?action=query&titles=List_of_museums_in_Greece&prop=links&pllimit=5&format=json";
		Aggregator aggregator = new Aggregator();
		List<Page> pageList = aggregator.pageSemantics(url);
		IndexerImpl indexer = new IndexerImpl();
		indexer.index(pageList);
		
		
//		aggregator.getDBPedia("Arta Folklore Museum of \"Skoufas\" Association");
//		System.out.println("aaaaaaa");

	}
	
	
	private boolean getDBPedia(String title) throws IOException{
		System.out.println(title);

		title = title.replaceAll("\\s", "_");
		title = title.replaceAll("–", "%E2%80%93");

		String ontology = "point";
//		String ontology_url = "http://www.w3.org/1999/02/22-rdf-syntax-ns/";
//		String ontology_url = "http://dbpedia.org/ontology/";
//		String ontology_url = "http://purl.org/dc/terms/";
		String ontology_url = "http://www.georss.org/georss/";
		
		String pageUrl = "http://dbpedia.org/data/" + title + ".json";
		System.out.println(pageUrl);

		String content = Helper.getUrl(pageUrl);
		if(content==null) return false;

		byte[] pageJsonBytes = content.getBytes();
		ObjectMapper mapper = new ObjectMapper();
		JsonNode rootNode = mapper.readValue(pageJsonBytes, JsonNode.class);
		JsonNode query = rootNode.get("http://dbpedia.org/resource/"+title);		
		JsonNode pages = query.get(ontology_url+""+ontology);
		if(pages==null)return false;

		Iterator<JsonNode> pagesNode = pages.getElements();
		while(pagesNode.hasNext()) {
			JsonNode pageNode = pagesNode.next();
			JsonNode valueNode = pageNode.get("value");
			String value =  valueNode.asText();	
			System.out.println(value);
			return true;
		}
		return false;
	}

	
	public List<Page> pageSemantics(String url) throws IOException,JsonParseException, JsonMappingException {
		List<Page> pageList = new ArrayList<Page>();
		
		byte[] jsonBytes = Helper.getUrl(url).getBytes();
		List<Link> museumsTitleList = getLinksAttr(jsonBytes,"links");
				
		int c=0;
		int counter = 0;
		for(Link museumLink:museumsTitleList){
			String  museumTitle = museumLink.getTitle();
			System.out.println(c++ + " museumTitle:"+museumTitle);
			
			//get DBpedia data
			//if(getDBPedia(museumTitle))
			//	counter++;

			//get page info
			PageInfo pageInfo = getPageInfo(museumTitle);

			//get page first paragraph
			String firstParagraph = getPageFirstParagraph(museumTitle);
			
			//get page outlinks
			String pageLinksUrl = "http://en.wikipedia.org/w/api.php?action=query&titles=" + museumTitle.replaceAll("\\s", "_") + "&prop=links&pllimit=500&format=json";
			byte[] pageJsonBytes = Helper.getUrl(pageLinksUrl).getBytes();
			List<Link> outLinksList = getLinksAttr(pageJsonBytes,"links");
			
			//get page categories
			String categoryUrl = "http://en.wikipedia.org/w/api.php?action=query&titles=" + museumTitle.replaceAll("\\s", "_") + "&prop=categories&cllimit=500&format=json";
			byte[] categoryJsonBytes = Helper.getUrl(categoryUrl).getBytes();
			List<Link> categoriesList = getLinksAttr(categoryJsonBytes,"categories");
			
			/*
			 * filter invalid pages
			 */
			if(!isValidPage(museumTitle, categoriesList) || pageInfo == null){
				System.out.print("\tINVALID:"+museumTitle);
				System.out.println("\tcategoriesList:"+categoriesList.toString());
				continue;
			}
			
			/*
			 * original page url
			 */
			String pageUrl = "http://en.wikipedia.org/wiki/"+museumTitle.replaceAll("\\s", "_");
			
			
			//save page to list
			Page page = new Page();
			page.setId(pageInfo.getId());
			page.setTitle(museumTitle);
			page.setUrl(pageUrl);
			page.setContent(firstParagraph);
			page.setOutlinks(outLinksList);
			page.setCategories(categoriesList);
			pageList.add(page);
		}
		System.out.println("##Nr of pages with coordinates from DBpedia::"+counter);
		return pageList;
	}

	private String getPageFirstParagraph(String title) throws IOException{
		String pageUrl = "http://en.wikipedia.org/w/api.php?action=parse&format=json&prop=text&section=0&page=" + title.replaceAll("\\s", "_");
		byte[] pageJsonBytes = Helper.getUrl(pageUrl).getBytes();
		String firstParagraph = getPageFirstParagraphMediaWiki(pageJsonBytes,"pageid");
		if(firstParagraph!=null){
			Document doc = Jsoup.parse(firstParagraph);
			return doc.text();
		}
		else
			return null;
		
	}
	
	private String getPageFirstParagraphMediaWiki(byte[] pageJsonBytes,String attr) throws JsonParseException, JsonMappingException, IOException{
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

	
	private PageInfo getPageInfo(String title) throws IOException{
		String pageUrl = "http://en.wikipedia.org/w/api.php?action=query&titles=" + title.replaceAll("\\s", "_") + "&prop=info&format=json";
		byte[] pageJsonBytes = Helper.getUrl(pageUrl).getBytes();
		PageInfo pageInfo = readPageInfoMediaWiki(pageJsonBytes,"pageid");
		return pageInfo;
	}
	
	private PageInfo readPageInfoMediaWiki(byte[] pageJsonBytes,String attr) throws JsonParseException, JsonMappingException, IOException{
		ObjectMapper mapper = new ObjectMapper();
		JsonNode rootNode = mapper.readValue(pageJsonBytes, JsonNode.class);
		JsonNode query = rootNode.get("query");
		JsonNode pages = query.get("pages");
		Iterator<JsonNode> pagesNode = pages.getElements();
		while(pagesNode.hasNext()) {
			JsonNode pageNode = pagesNode.next();
			JsonNode links =  pageNode.get(attr);
			
			if(links != null){
				String id = links.asText();
				return new PageInfo(Integer.parseInt(id));
			}
		}
		return null;
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
	
	private Map<String,Integer> countDf(List<String> semanticslist,Map<String,Integer> dfMap){
		for(String semantic : semanticslist){
			Integer sem = dfMap.get(semantic);
			if(sem != null)
				dfMap.put(semantic, sem+1);
			else
				dfMap.put(semantic, 1);
		}
		return dfMap;
	}
	
	private List<Link> getLinksAttr(byte[] jsonBytes,String attr) throws JsonParseException, JsonMappingException, IOException{
		// String input = The JSON data from your question
		List<Link> linksList = new ArrayList<Link>();
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
					linksList.add(new Link(text));
				}
			}
		}
		return linksList;
	}
	
	
}
