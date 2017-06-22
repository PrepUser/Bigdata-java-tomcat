package main.java.org.grouplens.mooc.cbf;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.grouplens.lenskit.ItemRecommender;
import org.grouplens.lenskit.ItemScorer;
import org.grouplens.lenskit.Recommender;
import org.grouplens.lenskit.RecommenderBuildException;
import org.grouplens.lenskit.core.LenskitConfiguration;
import org.grouplens.lenskit.core.LenskitRecommender;
import org.grouplens.lenskit.data.dao.EventDAO;
import org.grouplens.lenskit.data.dao.ItemDAO;
import org.grouplens.lenskit.data.dao.UserDAO;
import org.grouplens.lenskit.scored.ScoredId;
import main.java.org.grouplens.mooc.cbf.dao.*;
import org.grouplens.lenskit.vectors.SparseVector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.util.StringUtils;

public class CBFMain {

	String indexDir = "C:\\Lucene\\Index";
	String dataDir = "C:\\Lucene\\Data";
	Indexer indexer;
	Searcher searcher;
	private static final String SUFFIX = "/";

	//private static final Logger logger = LoggerFactory.getLogger(CBFMain.class);

	// debug print
	// Comment this section to test your code before deployment to Elastic Bean
	// Stalk

	public static void main(String[] args) {

		ArrayList<String> response = new ArrayList<String>();

		response = getRec(args);

		for (int i = 0; i < response.size(); i++) {
			System.out.println(response.get(i));
			//System.out.println("________________________MAIN");
		}
	}
	

	public static ArrayList<String> getRec(String[] args) {

		/***************************
		 * DATABASE CONNECTION CODE
		 ********************/
		ArrayList<String> response = new ArrayList<String>();

		// response.add("A001");

		/*
		 * Arguments passed in from the post request (ident, model)
		 * 
		 * ident - used to determine whether this is a search function or a
		 * cache function Options: SEAR - indicates a search function CACH -
		 * indicates a cache function model - used to identify the product
		 */

		// Database connection information
		String host = "54.152.164.94";
		String username = "cassandra";
		String password = "oksvaOupBxd2";
		String keyspace = "products";

		// connect to the cluster and start a session using the information
		// defined above
		Cluster.Builder clusterBuilder = Cluster.builder().addContactPoints(host).withPort(9042)
				.withCredentials(username, password);

		// select the keyspace and connect to it
		Session session = clusterBuilder.build().connect(keyspace);

		// Get all the electronics from the cassandra database
		ResultSet results = session.execute("SELECT * FROM electronics;");

		// loop through the results and print the product

		for (Row row : results) {
			//String string_value = new String(row.getBytes("value"), "UTF-8");
			System.out.format("%s \n", row.getString("name"));
		}

		// System.out.println(results);
		// System.out.println("________________________");

		// ArrayList<String> response = new ArrayList<String>();

		/***************************
		 * DATABASE CONNECTION CODE END
		 ********************/

		// if this is a search to get the product
		if (args[0].equals("SEAR")) {
			/*********************** SEARCH CODE ******************************/
			// put search code here

			System.out.print("Beginning search... ");

			// Cluster cluster;
			// Session session;
			// Connect to the cluster and keyspace "ecommerce" 127.0.0.1:9042
			// cluster = Cluster.builder().addContactPoint("localhost").build();
			// session = cluster.connect("products");

			// Use select to get a product
			// ResultSet results = session.execute("SELECT * FROM
			// electronics;");

			// create files to hold search data
			String[] a = new String[10];

			a[0] = "C:\\Lucene\\Data\\file1.txt";
			a[1] = "C:\\Lucene\\Data\\file2.txt";
			a[2] = "C:\\Lucene\\Data\\file3.txt";
			a[3] = "C:\\Lucene\\Data\\file4.txt";
			a[4] = "C:\\Lucene\\Data\\file5.txt";
			a[5] = "C:\\Lucene\\Data\\file6.txt";
			a[6] = "C:\\Lucene\\Data\\file7.txt";
			a[7] = "C:\\Lucene\\Data\\file8.txt";
			a[8] = "C:\\Lucene\\Data\\file9.txt";
			a[9] = "C:\\Lucene\\Data\\file10.txt";

			int n = 0;

			// loop through each electronic that was gathered from the cassandra
			// database
			for (Row row : results) {

				// String string_value = new String(row.getBytes("value"),
				// "UTF-8");

				System.out.format("Electronic Name: %s \n", row.getString("name"));

				// write the product information to the text files
				try {

					String model = row.getString("model");
					String name = row.getString("name");
					String cost = row.getString("cost");
					String description = row.getString("description");
					String features = row.getString("features");

					File file = new File(a[n]);
					n++;

					// if file doesnt exists, then create it
					if (!file.exists()) {
						file.createNewFile();
					}

					FileWriter fw = new FileWriter(file.getAbsoluteFile());
					BufferedWriter bw = new BufferedWriter(fw);
					bw.write(model);
					bw.write(" ");
					bw.write(name);
					bw.write(" ");
					bw.write(cost);
					bw.write(" ");
					bw.write(description);
					bw.write(" ");
					bw.write(features);
					bw.write(" ");
					bw.close();

					System.out.println("Done indexing information for search..");

				} catch (IOException e) {
					e.printStackTrace();
				}

			}
			// run a search for the provided model number
			LuceneTester tester;

			try {
				// build lucene class
				tester = new LuceneTester();

				// creating index from files
				tester.createIndex();

				// run search
				System.out.println("Running search for: " + args[1]);
				
				response.add(tester.search(args[1].toLowerCase()));

			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}
			/***********************
			 * SEARCH CODE END
			 ******************************/

			/*********************** CACHE CODE *******************************/
			// give aws credentials
			AWSCredentials credentials = new BasicAWSCredentials("AKIAJ5N3CMZQ6ELKFFAA","wxXiiKNFylK8SZs16r3QtwKJOWNWZhh2dNHbuSqV");

			// create an S3 client
			AmazonS3 s3client = new AmazonS3Client(credentials);

			String bucketName = "usftrialbucket";
			// String key = "Images/teddybear.jpg";

			// to create a new bucket
			// s3client.createBucket(bucketName);

			// to list all the buckets
			// for (Bucket bucket : s3client.listBuckets()) {
			// System.out.println(" - " + bucket.getName());
			// }

			// create folder into bucket String folderName = "Images";
			String folderName = "Images";
			//createFolder(bucketName, folderName, s3client);

			// upload file to folder and set it to public
			String fileName = folderName + SUFFIX + "teddybear1.jpg";
			s3client.putObject(new PutObjectRequest(bucketName, fileName, new File("C:\\teddybear1.jpg"))
					.withCannedAcl(CannedAccessControlList.PublicRead));

			/*
			 * // to list a buckets content
			 * 
			 * Bucket bucket = new Bucket("usftrialbucket");
			 * 
			 * ObjectListing objects = s3client.listObjects(bucket.getName());
			 * do { for (S3ObjectSummary objectSummary :
			 * objects.getObjectSummaries()) {
			 * System.out.println(objectSummary.getKey() + "\t" +
			 * objectSummary.getSize() + "\t" +
			 * StringUtils.fromDate(objectSummary.getLastModified())); } objects
			 * = s3client.listNextBatchOfObjects(objects); } while
			 * (objects.isTruncated());
			 * 
			 * 
			 * System.out.println("Downloading an object"); S3Object s3object =
			 * s3client.getObject(new GetObjectRequest(bucketName, key));
			 * System.out.println("Content-Type: " +
			 * s3object.getObjectMetadata().getContentType());
			 * displayTextInputStream(s3object.getObjectContent());
			 */

			Bucket bucket = new Bucket(bucketName);
			ArrayList<String> objectName = new ArrayList<String>();

			ObjectListing objects = s3client.listObjects(bucket.getName());
			do {
				for (S3ObjectSummary objectSummary : objects.getObjectSummaries()) {

					objectName.add(objectSummary.getKey());
					//System.out.println(objectSummary.getKey());
				}
				objects = s3client.listNextBatchOfObjects(objects);
			} while (objects.isTruncated());

			/*********************
			 * CACHE CODE END
			 ***********************************/
			
			
			//build the url and add it to the response
			//https://s3.amazonaws.com/usftrialbucket/Images/teddybear.jpg
			
			String url = "https://s3.amazonaws.com/" + bucketName + "/"  + objectName.get(0) + response.get(0).substring(0,4) + ".png";
			
			response.add(url);
			
			System.out.println("URL: " + url);
			
			return response;
			// if this is an add or remove from the cache
		} else if (args[0].equals("CACH")) {

			// put cache code here

			response.add("hit the cach function");

			// return the result of the cache
			session.close();
			return response;
		} else {
			// Return to indicate this was a malformed identity request

			response.add("MALF");

			session.close();
			return response;

		}

	}

	// create a folder in the bucket
	public static void createFolder(String bucketName, String folderName, AmazonS3 client) {
		// create meta-data for your folder and set content-length to 0
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(0);
		// create empty content
		InputStream emptyContent = new ByteArrayInputStream(new byte[0]);
		// create a PutObjectRequest passing the folder name suffixed by /
		PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, folderName + SUFFIX, emptyContent,
				metadata);
		// send request to S3 to create folder
		client.putObject(putObjectRequest);
	}

	private static void displayTextInputStream(InputStream input) throws IOException {
		// Read one text line at a time and display.
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));
		while (true) {
			String line = reader.readLine();
			if (line == null)
				break;

			System.out.println("    " + line);
		}
		System.out.println();
	}

	
//	public static ArrayList<String> getRec(String[] args) {
//	ArrayList<String> response = new ArrayList<String>();
//	response.add("A001 LG G4 459.99 LGs Latest Smart Phone32GB RAM, Snap Dragon Processor https://s3.amazonaws.com/usftrialbucket/Images/A001.png");
//	return response;
//}
//
}