package org.herodotus.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Helper {

//	public static Document convertPageToDocument(Page page,List<String> fieldsToParseList, String language) {
//		//Fields which are list of strings join them with a dot and space in order the sentence detector handle them as separate sentences..
//		Joiner joiner = Joiner.on(". ");
//		
//		Map<String,String> fieldToTextValueMap = new HashMap<String, String>();
//		for(String fieldName:fieldsToParseList){
//			
//			if(fieldName.equals("id")){
//				fieldToTextValueMap.put("id", String.valueOf(page.getId()));
//			}
//			
//			else if(fieldName.equals("title")){
//				fieldToTextValueMap.put("title", page.getTitle());
//			}
//			
//			else if(fieldName.equals("summary")){
//				fieldToTextValueMap.put("summary", page.getSummary());
//			}
//			
//			else if(fieldName.equals("categories")){
//				fieldToTextValueMap.put("categories", joiner.join(page.getCategories()));
//			}
//			
//			else if(fieldName.equals("outlinks")){
//				fieldToTextValueMap.put("outlinks", joiner.join(page.getOutlinks()));
//			}
//			
//			else{
//				System.out.println("UNKNOWN FIELD TO PARSE....:"+fieldName);
//			}		
//		}
//		
//		return new Document(page.getId(), fieldToTextValueMap, language);
//	}

	public static List<String> getFileContentLineByLine(String filePath) throws IOException{
		List<String> linesSet = new ArrayList<String>();
		File file = new File(filePath);
		if(file.exists() && !file.isDirectory()) { 
			BufferedReader br = new BufferedReader(new FileReader(filePath));
			String line;
			while ((line = br.readLine()) != null) {
				linesSet.add(line);
			}
			br.close();
		}
		return linesSet;
	}

	
	public static String getUrl(String url)  {
		try {
			URL oracle = new URL(url);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					oracle.openStream()));
			String inputLine;
			StringBuilder buf = new StringBuilder();
			while ((inputLine = in.readLine()) != null)
				buf.append(inputLine);
			in.close();
			return buf.toString();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	@SuppressWarnings({ "rawtypes", "unused", "unchecked" })
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
