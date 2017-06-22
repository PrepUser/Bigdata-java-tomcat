package main.java.org.grouplens.mooc.cbf;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;


import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;


import main.java.org.grouplens.mooc.cbf.Indexer;
import main.java.org.grouplens.mooc.cbf.TextFileFilter;


import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;




public class LuceneTester {
	


	 String indexDir = "C:\\Lucene\\Index";
	 String dataDir = "C:\\Lucene\\Data";
	 Indexer indexer;
	 Searcher searcher;
	 String filename;
   
   public void createIndex() throws IOException{
 	   indexer = new Indexer(indexDir);
 	   int numIndexed;
 	   long startTime = System.currentTimeMillis();
 	   numIndexed = indexer.createIndex(dataDir, new TextFileFilter());
 	   long endTime = System.currentTimeMillis();
 	   indexer.close();
 	   System.out.println(numIndexed+" Files indexed, time taken: "+(endTime-startTime)+" ms");
 	   }
    
    
    public String search(String searchQuery)
       throws IOException, ParseException{
       searcher = new Searcher(indexDir);
       long startTime = System.currentTimeMillis();
       //create a term to search file name
       //Term term = new Term(LuceneConstants.FILE_NAME, searchQuery);
       Term term = new Term(LuceneConstants.CONTENTS, searchQuery);
       //create the term query object
       Query query = new FuzzyQuery(term);
       //do the search
       
       TopDocs hits = searcher.search(query);
       long endTime = System.currentTimeMillis();


       System.out.println(hits.totalHits +
          " documents found. Time :" + (endTime - startTime) + "ms");
       
       for(ScoreDoc scoreDoc : hits.scoreDocs) {
          Document doc = searcher.getDocument(scoreDoc);
          System.out.print("Score: "+ scoreDoc.score + " ");
          System.out.println("File: "+ doc.get(LuceneConstants.FILE_PATH));
          filename = doc.get(LuceneConstants.FILE_PATH);
       }
       
       //reading searched file into an array
       String []data = new String[10];
       
       //check if the search found any files
       if(hits.totalHits != 0){
  	   	BufferedReader abc = new BufferedReader(new FileReader((filename)));
  	         
  	        {
  	        for (int i=0;i<10; i++) {
  	        //array to pass back with data   
  	        	data[i] = abc.readLine();
  	          //System.out.println(data[i]);
  	       
  	         
  	        }
  	      abc.close();

  	        }
       }
       searcher.close();
       return data[0];
    }
  
}
