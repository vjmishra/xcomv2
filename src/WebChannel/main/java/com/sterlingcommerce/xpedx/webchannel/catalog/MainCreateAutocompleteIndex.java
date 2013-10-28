package com.sterlingcommerce.xpedx.webchannel.catalog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.FSDirectory;

/**
 * Main class that builds the autocomplete index.
 * 
 * @author Trey Howard
 */
public class MainCreateAutocompleteIndex {

	private static final String[] luceneEscapeWords = { "a", "an", "and", "are", "as", "at", "be", "but", "by", "for", "if", "in", "into", "is", "it", "no", "not", "of", "on",
			"or", "such", "that", "the", "their", "then", "there", "these", "they", "this", "to", "was", "will", "with" };

	public static long createIndex(File punIndexRoot) throws CorruptIndexException, IOException, ClassNotFoundException, SQLException {
		Connection conn = null;
		try {
			conn = createConnection();

			IndexWriter writer = new IndexWriter(FSDirectory.getDirectory(punIndexRoot), new StandardAnalyzer(luceneEscapeWords), true, IndexWriter.MaxFieldLength.UNLIMITED);

			// TODO exclude puns that have no items (not in trey_item_pun table)
			PreparedStatement stmt = conn.prepareStatement("select * from pun where pun_key in (select pun_key from pun_item)"); // order by pun_key
			ResultSet res = stmt.executeQuery();

			long count = 0;

			final String EMPTY_CAT = "All";
			while (res.next()) {
				String id = res.getString("pun_key");
				String cat1 = res.getString("cat1");
				String cat2 = res.getString("cat2");
				String cat3 = res.getString("cat3");
				String cat4 = res.getString("cat4");
				String name = res.getString("pun_name");

				StringBuilder path = new StringBuilder(1024);
				path.append(cat2);
				path.append(EMPTY_CAT.equals(cat3) ? "" : " > " + cat3);
				path.append(EMPTY_CAT.equals(cat4) ? "" : " > " + cat4);
				path.append(" > " + name);

				String pathStr = path.toString();

				Document doc = new Document();
				doc.add(new Field("pun_key", String.valueOf(id), Field.Store.YES, Field.Index.NOT_ANALYZED));
				doc.add(new Field("pun_name", name, Field.Store.YES, Field.Index.NOT_ANALYZED));
				doc.add(new Field("pun_path", pathStr, Field.Store.YES, Field.Index.NOT_ANALYZED));
				doc.add(new Field("cat1", cat1, Field.Store.YES, Field.Index.NOT_ANALYZED)); // required for sorting

				doc.add(new Field("pun_path_parsed", pathStr, Field.Store.NO, Field.Index.ANALYZED));

				// System.out.println(id + ":\t" + path);
				writer.addDocument(doc);

				count++;
			}

			writer.commit();
			writer.optimize();
			writer.close();

			res.close();
			stmt.close();

			return count;

		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception ignore) {
				}
			}
		}
	}

	private static Connection createConnection() throws ClassNotFoundException, SQLException, FileNotFoundException, IOException {
		Properties props = new Properties();
		props.load(new FileReader("C:/Sterling/Foundation/properties/jdbc.properties"));

		Class.forName(props.getProperty("oraclePool.driver"));
		Connection conn = DriverManager.getConnection(props.getProperty("oraclePool.url"), props.getProperty("oraclePool.user"), props.getProperty("oraclePool.password"));
		return conn;
	}

	public static void main(String[] args) throws Exception {
		File sterlingFoundationDir = new File("C:/Sterling/Foundation");
		
		Properties props = new Properties();
		props.load(new FileReader(sterlingFoundationDir.getAbsolutePath() + "/properties/customer_overrides.properties"));
		
		File punIndex = new File(props.getProperty("yfs.punIndex.rootDirectory"));
		System.out.println("Creating pun index: " + punIndex);
		
		punIndex.mkdirs();
		
		long start = System.currentTimeMillis();
		// long count = createIndex("C:/search/index/autocomplete-analyzed");
		long count = createIndex(punIndex);
		long stop = System.currentTimeMillis();
		System.out.println(String.format("Indexed %s puns. Total time (ms): %s", count, stop - start));
	}

}
