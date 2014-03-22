package org.herodotus.core;

import java.util.List;

import org.herodotus.domain.Page;

public interface Indexer {
	
	public void index(List<Page> pages, String CLUSTER_NAME, String INDEX_NAME, String DOCUMENT_TYPE);

}
