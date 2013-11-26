package org.herodotus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * Hello world!
 * 
 */
public class App {

	public static void main(String[] args) throws IOException {
		
		String url = "http://en.wikipedia.org/w/api.php?action=query&titles=List_of_museums_in_Greece&prop=links&pllimit=500&format=json";
		pageSemantics(url);
	}
	
	
	
	
	public static void pageSemantics(String url) throws IOException,JsonParseException, JsonMappingException {
		byte[] jsonBytes = getUrl(url).getBytes();
		List<String> museumsTitleList = getLinksAttr(jsonBytes,"links");
				
		//global statistics
		Map<String,Integer> linksDfMap = new HashMap<String,Integer>();
		Map<String,Integer> categoriesDfMap = new HashMap<String,Integer>(); 
		
		int c=0;
		for(String museumTitle:museumsTitleList){
			if(c++ ==20)break;
			//System.out.println(museumTitle);

			//get page outlinks
			String pageUrl = "http://en.wikipedia.org/w/api.php?action=query&titles=" + museumTitle.replaceAll("\\s", "_") + "&prop=links&pllimit=500&format=json";
			byte[] pageJsonBytes = getUrl(pageUrl).getBytes();
			List<String> outLinksList = getLinksAttr(pageJsonBytes,"links");
			
			//get page categories
			String categoryUrl = "http://en.wikipedia.org/w/api.php?action=query&titles=" + museumTitle.replaceAll("\\s", "_") + "&prop=categories&cllimit=500&format=json";
			byte[] categoryJsonBytes = getUrl(categoryUrl).getBytes();
			List<String> categoriesList = getLinksAttr(categoryJsonBytes,"categories");
			
			//filter invalid pages
			if(!isValidPage(museumTitle, categoriesList)){
				System.out.println("INVALIDE:"+museumTitle);
				System.out.println("\tcategoriesList:"+categoriesList.toString());
				continue;
			}
			
			//count frequencies
			countDf(outLinksList,linksDfMap);
			countDf(categoriesList,categoriesDfMap);
		}
		linksDfMap = sortByComparator(linksDfMap);
		categoriesDfMap = sortByComparator(categoriesDfMap);


		System.out.println("\n\n##########################\n\n");
		for(Map.Entry<String, Integer> entry:linksDfMap.entrySet()){
			System.out.println(entry.getKey()+"\t"+entry.getValue());
		}
		System.out.println("\n\n##########################\n\n");
		for(Map.Entry<String, Integer> entry:categoriesDfMap.entrySet()){
			System.out.println(entry.getKey()+"\t"+entry.getValue());
		}
	}
	
	public static boolean isValidPage(String title,List<String> categoriesList){
		if(title.startsWith("List of"))
			return false;

		if(title.toLowerCase().contains("museum"))
			return true;

		if(categoriesList.isEmpty())
			return true;
		
		for(String cat: categoriesList){
			if(cat.toLowerCase().contains("museum"))
				return true;
		}
		return false;
	}
	public static Map<String,Integer> countDf(List<String> semanticslist,Map<String,Integer> dfMap){
		for(String semantic : semanticslist){
			Integer sem = dfMap.get(semantic);
			if(sem != null)
				dfMap.put(semantic, sem+1);
			else
				dfMap.put(semantic, 1);
		}
		return dfMap;
	}
	
	public static List<String> getLinksAttr(byte[] jsonBytes,String attr) throws JsonParseException, JsonMappingException, IOException{
		// String input = The JSON data from your question
		List<String> linksList = new ArrayList<String>();
				ObjectMapper mapper = new ObjectMapper();

				JsonNode rootNode = mapper.readValue(jsonBytes, JsonNode.class);

				// can also use ArrayNode here, but JsonNode allows us to get(index)
				// line an array:
				JsonNode query = rootNode.get("query");
				JsonNode pages = query.get("pages");
//				JsonNode page = pages.get("10944017");
				
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
	
	
	public static String getUrl(String url) throws IOException {
		URL oracle = new URL(url);
		BufferedReader in = new BufferedReader(new InputStreamReader(
				oracle.openStream()));
		String inputLine;
		StringBuilder buf = new StringBuilder();
		while ((inputLine = in.readLine()) != null)
			buf.append(inputLine);
		in.close();
		return buf.toString();
	}

	private static Map sortByComparator(Map unsortMap) {
		 
		List list = new LinkedList(unsortMap.entrySet());
 
		// sort list based on comparator
		Collections.sort(list, new Comparator() {
			public int compare(Object o1, Object o2) {
				return ((Comparable) ((Map.Entry) (o1)).getValue())
                                       .compareTo(((Map.Entry) (o2)).getValue());
			}
		});
 
		// put sorted list into map again
                //LinkedHashMap make sure order in which keys were inserted
		Map sortedMap = new LinkedHashMap();
		for (Iterator it = list.iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}
	
}
