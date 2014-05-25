//package org.herodotus.core;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.logging.FileHandler;
//import java.util.logging.Logger;
//import java.util.logging.SimpleFormatter;
//
//import org.codehaus.jackson.JsonParseException;
//import org.codehaus.jackson.map.JsonMappingException;
//import org.herodotus.domain.Page;
//import org.herodotus.util.Helper;
//
//import com.cybozu.labs.langdetect.LangDetectException;
//import com.machine_learning.core.Dictionary;
//import com.machine_learning.core.DocumentParser;
//import com.machine_learning.core.analysis.SentenceDetector;
//import com.machine_learning.core.analysis.tokenizer.Tokenizer;
//import com.machine_learning.domain.Document;
//import com.machine_learning.domain.Feature;
//import com.machine_learning.exception.DictionaryDuplicationException;
//import com.machine_learning.exception.NoLanguageSupportException;
//
//public class Modeling {
//
//	private final static Logger LOGGER = Logger.getLogger(Modeling.class.getName());
//	
//	
//	private final Dictionary dictionary;
//	private final DocumentParser documentParser;
//	private final SentenceDetector sentenceDetector;
//	private final Aggregator aggregator;
//
//	 
//	public Modeling(String language, String CLUSTER_NAME, String dictionary_name, String document_type, String sentence_model_filepath , String lang_profiles_filepath, int max_nr_of_features_in_memory, int bulk_size) throws SecurityException, IOException, DictionaryDuplicationException, LangDetectException, NoLanguageSupportException{
//		this.dictionary = new Dictionary(CLUSTER_NAME,dictionary_name, document_type,max_nr_of_features_in_memory,bulk_size);
//		this.documentParser = new DocumentParser(lang_profiles_filepath);
//		this.sentenceDetector = new SentenceDetector(language, sentence_model_filepath);
//		this.aggregator = new Aggregator();
//	}
//	
//	
//	
//	/**
//	 * @param args
//	 * @throws InterruptedException 
//	 * @throws IOException 
//	 * @throws JsonMappingException 
//	 * @throws JsonParseException 
//	 * @throws DictionaryDuplicationException 
//	 * @throws SecurityException 
//	 * @throws LangDetectException 
//	 * @throws NoLanguageSupportException 
//	 * @throws FieldDoesntExistInDocumentException 
//	 */
//	public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException, InterruptedException, SecurityException, LangDetectException, NoLanguageSupportException, DictionaryDuplicationException {
//		FileHandler fileHandler = new FileHandler("./logs/herodotus.log");
//		LOGGER.addHandler(fileHandler);
//		fileHandler.setFormatter(new SimpleFormatter());
//		
//		
//		/**
//		 * ###########################################################################################################################################################################
//		 * Input
//		 * ###########################################################################################################################################################################
//		 */
//		//tab separated urls with list of museums and their corresponding country
//		List<String> list_museum_pages = new ArrayList<String>();
//		String list_of_museums_gr = "http://en.wikipedia.org/w/api.php?action=query&titles=List_of_museums_in_Greece&prop=links&pllimit=500&format=json\tGreece";
//		String list_of_museums_nl = "http://en.wikipedia.org/w/api.php?action=query&titles=List_of_museums_in_the_Netherlands&prop=links&pllimit=500&format=json\tNetherlands";
//		String list_of_most_visited_museums_it = "http://en.wikipedia.org/w/api.php?action=query&titles=List_of_museums_in_Italy&prop=links&pllimit=500&format=json\tItaly";
//		String list_of_most_visited_museums_es = "http://en.wikipedia.org/w/api.php?action=query&titles=List_of_museums_in_Spain&prop=links&pllimit=500&format=json\tSpain";
//		String list_of_museums_ge = "http://en.wikipedia.org/w/api.php?action=query&titles=List_of_museums_in_Germany&prop=links&pllimit=500&format=json\tGermany";
//		list_museum_pages.add(list_of_museums_gr);
////		list_museum_pages.add(list_of_museums_nl);
////		list_museum_pages.add(list_of_most_visited_museums_it);
////		list_museum_pages.add(list_of_most_visited_museums_es);
////		list_museum_pages.add(list_of_museums_ge);
//		
//		
//		
//		final boolean skip_stopwords = true;
//		final boolean lowerCase = true;
//		final boolean ignorePunctuation = true;
//		final boolean ignoreDigits = true;
//		final int min_ngram_length = 1;
//		final int max_ngram_length = 3;
//		final int min_token_length = 3;
//		final int max_token_length = 25;
//		final boolean erase_index_at_start = true;
//		final int max_nr_of_features_in_memory = 1000000; //how many features on memory to keep? before flush them into the index
//		final int bulk_size = 100;    //how many features to flush per time into the Es index via the bulk api (100 is the proposed value)
//		final boolean use_pos_tagger = false;		
//		
//		
//		final String CLUSTER_NAME = "dkarap";
//		final String language = "en";
//		final String dictionary_name = "museums_model_en";
//		final String document_type = "feature";
//		final List<String> fieldsToParseList = new ArrayList<String>(Arrays.asList("id","title","summary","categories","outlinks"));
//		
//		
//		//import stopwords
//		final List<String> stopwords;
//		if(language.equals("en"))
//			stopwords = Helper.getFileContentLineByLine("./data/stopwords/en/stopwords.txt");
//		else if(language.equals("nl"))
//			stopwords = Helper.getFileContentLineByLine("./data/stopwords/nl/stopwords.txt");
//		else
//			stopwords=null;
//
//		
//		String sentence_model_filepath = null;
//		if(language.equals("en"))
//			sentence_model_filepath = "./data/sentence_detector_openNlp/en-sent.bin";
//		else if(language.equals("nl"))
//			sentence_model_filepath = "./data/sentence_detector_openNlp/nl-sent.bin";
//		final String lang_profiles_filepath = "./data/language_profiles";		
//
//
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		/**
//		 * ###########################################################################################################################################################################
//		 * Main
//		 * ###########################################################################################################################################################################
//		 */
//		Modeling modeling = new Modeling(language, CLUSTER_NAME, dictionary_name, document_type, sentence_model_filepath, lang_profiles_filepath, max_nr_of_features_in_memory, bulk_size);
//		
//		
//		
//		/**
//		 * 1. Create model from a list of pages 
//		 * @param list_museum_pages is a list of tab separated strings with the first part to be the url with the list of pages and second the country from where exist the pages
//		 */
//		modeling.modelData(erase_index_at_start, list_museum_pages, fieldsToParseList, language, min_ngram_length, max_ngram_length, min_token_length, max_token_length, use_pos_tagger, lowerCase, ignorePunctuation, ignoreDigits, skip_stopwords, stopwords);
//
//		
//		
//		
//		
//		
//		
//		
//		
//	}
//	
//	
//	public void modelData(boolean erase_index_at_start, List<String> list_museum_pages, List<String> fieldsToParseList, String language, int min_ngram_length, int max_ngram_length, int min_token_length, int max_token_length, boolean use_pos_tagger, boolean lowerCase, boolean ignorePunctuation, boolean ignoreDigits, boolean skip_stopwords, List<String> stopwords) throws JsonParseException, JsonMappingException, IOException, InterruptedException, NoLanguageSupportException, DictionaryDuplicationException{
//		Tokenizer tokenizer = new Tokenizer(skip_stopwords,stopwords);
//
//		/*
//		 * erase dictionary initialy
//		 */
//		if(erase_index_at_start)
//			dictionary.eraseDictionary();
//
//		
//		
//		for(String list_museum_page:list_museum_pages){
//			String[] museumInfoArr = list_museum_page.split("\t");
//			String list_page_url = museumInfoArr[0];
//			String country_name = museumInfoArr[1];
//			
//			System.out.println("##Update model with museums from "+country_name);
//			List<Page> pageList = aggregator.pageSemantics(list_page_url, country_name);
//			for(Page page:pageList){
//				
//				Document document = Helper.convertPageToDocument(page, fieldsToParseList, language);				
//				
//			    List<Feature> document_feature_list = documentParser.getDocumentFeatures(document, fieldsToParseList, tokenizer, sentenceDetector, null, min_ngram_length, max_ngram_length, min_token_length, max_token_length, use_pos_tagger, lowerCase, ignorePunctuation, ignoreDigits);
//			    
//			    dictionary.increaseNumberOfParsedDocuments(1L);
//			 	for(Feature feature : document_feature_list){
//					dictionary.addFeature(feature, true,true);
//			 	}			 	
//			}			
//		}
//
//	    
//	    System.out.print("Compute features utility measures...");
//	    dictionary.computeUtilityMeasures();
//	    System.out.println("...[done..]");
//		documentParser.closeDocumentParser();
//		dictionary.closeDictionary();		
//	}
//	
//	
//
////	public void featureWeigth(Document document, List<String> fieldsToParseList, String language, int min_ngram_length, int max_ngram_length, int min_token_length, int max_token_length, boolean use_pos_tagger, boolean lowerCase, boolean ignorePunctuation, boolean ignoreDigits, boolean skip_stopwords, List<String> stopwords){
////		Tokenizer tokenizer = new Tokenizer(skip_stopwords,stopwords);
////
////		/**
////    	 * ####3. Get list of features from current document  
////    	 */
////		List<Feature> document_feature_list = documentParser.getDocumentFeatures(document, fieldsToParseList, tokenizer, sentenceDetector, null,min_ngram_length, max_ngram_length, min_token_length, max_token_length,use_pos_tagger, lowerCase, ignorePunctuation, ignoreDigits);
////		int nr_of_candidate_features = document_feature_list.size();
////		
////
////		
////		
////		
////		
////		/**
////		 * ####4. Feature Selection from current document features: from all the candidates keep only the most informative
////		 */
////		List<Feature> document_selected_feature_list = null;
////		try {
////			document_selected_feature_list = featureSelection.parser(document_feature_list, dictionary, top_N_features_to_return, fieldToWeigthMap);
////		} catch (SmoothingException e) {
////			LOGGER.info("FATAL ERROR:"+e.getMessage());
////			break;
////		}
////		
////		
////		/**
////		 * ####4. Debugging: display the features sorted by weight fro each document 
////		 */
////			System.out.println("\n\nTitle:"+document.getFieldValueByKey("title"));
////			Collections.sort(document_selected_feature_list, Feature.FeatureComparatorWeight);
////			for(Feature feature:document_selected_feature_list)
////				System.out.println(feature.getValue()+"\t"+feature.getWeight());
////
////	}
//
//
//	
//
//
//}
