package com.sterlingcommerce.xpedx.webchannel.catalog;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

	public static void createIndex(String searchIndexRoot) throws CorruptIndexException, IOException, ClassNotFoundException, SQLException {
		Connection conn = null;
		try {
			conn = createConnection();

			IndexWriter writer = new IndexWriter(FSDirectory.getDirectory(searchIndexRoot), new StandardAnalyzer(luceneEscapeWords), true, IndexWriter.MaxFieldLength.UNLIMITED);

			PreparedStatement stmt = conn.prepareStatement("select * from trey_pun_data order by pun_id");
			ResultSet res = stmt.executeQuery();

			final String EMPTY_CAT = "All";
			while (res.next()) {
				Integer id = res.getInt("pun_id");
				String cat1 = res.getString("cat1");
				String cat2 = res.getString("cat2");
				String cat3 = res.getString("cat3");
				String cat4 = res.getString("cat4");
				String name = res.getString("pun_name");

				StringBuilder path = new StringBuilder(1024);
				// path.append(cat1);
				// path.append(EMPTY_CAT.equals(cat2) ? "" : " > " + cat2);
				path.append(cat2);
				path.append(EMPTY_CAT.equals(cat3) ? "" : " > " + cat3);
				path.append(EMPTY_CAT.equals(cat4) ? "" : " > " + cat4);
				path.append(" > " + name);

				String pathStr = path.toString();

				Document doc = new Document();
				doc.add(new Field("pun_id", String.valueOf(id), Field.Store.YES, Field.Index.NOT_ANALYZED));
				doc.add(new Field("pun_name", pathStr, Field.Store.YES, Field.Index.NOT_ANALYZED));
				doc.add(new Field("pun_path", pathStr, Field.Store.YES, Field.Index.NOT_ANALYZED));
				doc.add(new Field("cat1", cat1, Field.Store.YES, Field.Index.NOT_ANALYZED)); // required for sorting

				doc.add(new Field("pun_path_parsed", pathStr, Field.Store.NO, Field.Index.ANALYZED));

				System.out.println(id + ":\t" + path);
				writer.addDocument(doc);
			}

			writer.commit();
			writer.optimize();
			writer.close();

			res.close();
			stmt.close();

		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception ignore) {
				}
			}
		}

		System.out.println("Done!");
	}

	private static Connection createConnection() throws ClassNotFoundException, SQLException {
		Class.forName("oracle.jdbc.OracleDriver");
		Connection connection = null;
		connection = DriverManager.getConnection("jdbc:oracle:thin:@oratst08.ipaper.com:1521:ngd1", "NG", "NG1");
		return connection;
	}

	public static void main(String[] args) throws Exception {
		createIndex("C:/search/index/autocomplete-analyzed");
	}

}
