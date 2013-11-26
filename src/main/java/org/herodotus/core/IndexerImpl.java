package org.herodotus.core;

import static org.elasticsearch.node.NodeBuilder.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.node.Node;
import org.herodotus.domain.Page;

public class IndexerImpl implements Indexer {

	@Override
	public void index(List<Page> pages) {
		
		Node node = nodeBuilder().node();
		Client client = node.client();

		ObjectMapper mapper = new ObjectMapper(); // Spring injection...
		
		for(Page page : pages) {
		
			String jsonPage = null;
			
			try {
				jsonPage = mapper.writeValueAsString(page);
			} catch (JsonGenerationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(jsonPage != null) {
				IndexResponse response = client.prepareIndex("herodotus", "page")
				        .setSource(jsonPage)
				        .execute()
				        .actionGet();
			}
		}
		
		node.close();

	}
	
	
	public static void main(String[] args) {
		Indexer indexer = new IndexerImpl();
		
		List<Page> pages = new ArrayList<Page>();
		
		Page page1 = new Page();
		page1.setId(1);
		page1.setTitle("Veria Archealogical Museum");
		page1.setContent("Veria Archealogical Museum");
		
		Page page2 = new Page();
		page2.setId(2);
		page2.setTitle("Veria Archealogical Museum");
		page2.setContent("Veria Archealogical Museum");
		
		pages.add(page1);
		pages.add(page2);
		
		indexer.index(pages);
	}

}
