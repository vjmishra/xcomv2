package com.sterlingcommerce.xpedx.marketinggroupindex;

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

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.FSDirectory;

/**
 * Processes the pun_hierarchy table (raw data dump from external source) to generate a Lucene index and updates the yfs_item.marketing_group_id values. Note that marketing_group
 * and marketing_group_item tables are temporary tables so the data is not persisted past the session.
 *
 * @author Trey Howard
 */
public class CreateMarketingGroupIndex {

	private static final String[] luceneEscapeWords = { "a", "an", "and", "are", "as", "at", "be", "but", "by", "for", "if", "in", "into", "is", "it", "no", "not", "of", "on",
		"or", "such", "that", "the", "their", "then", "there", "these", "they", "this", "to", "was", "will", "with" };

	private static final String LUCENE_INDEX_ROOT_DIR = "lucene.indexRootDir";
	private static final String JDBC_DRIVER = "jdbc.driver";
	private static final String JDBC_URL = "jdbc.url";
	private static final String JDBC_USER = "jdbc.user";
	private static final String JDBC_PASS = "jdbc.pass";

	private static final Logger log = Logger.getLogger(CreateMarketingGroupIndex.class);

	private static Properties config;

	/**
	 * Creates a Lucene index from the database from the pun_hierarchy table and updates the yfs_item.marketing_group_id values.
	 *
	 * @return Returns the number of Lucene documents created.
	 * @throws Exception
	 *             Any exceptions caught are re-thrown as an Exception (sql errors and lucene errors)
	 */
	private static long createIndex() throws Exception {
		Connection conn = null;
		IndexWriter writer = null;
		try {
			conn = createConnection();
			conn.setAutoCommit(false);

			// normalize pun_hierarchy into temp tables
			insertMarketingGroupRecords(conn);
			insertMarketingGroupItemRecords(conn);

			// update yfs_item table from the temp tables
			updateYfsItemRecords(conn);

			// create index from the temp tables
			File marketingGroupIndexRootDir = new File(config.getProperty(LUCENE_INDEX_ROOT_DIR));
			log.info("Creating marketing group index: " + marketingGroupIndexRootDir);

			marketingGroupIndexRootDir.mkdirs();

			writer = new IndexWriter(FSDirectory.getDirectory(marketingGroupIndexRootDir), new StandardAnalyzer(luceneEscapeWords), true, IndexWriter.MaxFieldLength.UNLIMITED);

			long numDocuments = writeIndex(writer, conn);

			// failure point: it's important to commit database transaction before creating IndexWriter, which overwrites the existing Lucene index
			conn.commit();

			writer.commit();
			writer.optimize();

			return numDocuments;

		} catch (Exception e) {
			// rollback and re-throw exception
			if (conn != null) {
				try {
					conn.rollback();
				} catch (Exception ignore) {
				}
			}
			if (writer != null) {
				try {
					writer.rollback();
				} catch (Exception ignore) {
				}
			}

			throw e;

		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception ignore) {
				}
			}
			if (writer != null) {
				try {
					writer.close();
				} catch (Exception ignore) {
				}
			}
		}
	}

	/**
	 * Encapsulates the creation of the marketing_group records (temp table).
	 * @param conn
	 * @throws SQLException
	 */
	private static void insertMarketingGroupRecords(Connection conn) throws SQLException {
		// note: marketing_group is a temp table
		String sql = ""
				+ " insert into marketing_group (marketing_group_name, cat1, cat2, cat3, cat4)"
				+ " select distinct \"publishing_unit_name_automated\", \"category1\", \"category2\", \"category3\", \"category4\""
				+ " from pun_hierarchy"
				+ " where \"publishing_unit_name_automated\" is not null";

		if (log.isDebugEnabled()) {
			log.debug(String.format("Normalize pun hierarchy:\t%s", sql));
		}

		PreparedStatement stmt = conn.prepareStatement(sql);

		int numAffected = stmt.executeUpdate();

		if (log.isDebugEnabled()) {
			log.debug(String.format("Inserted %s marketing_group records", numAffected));
		}

		stmt.close();
	}

	/**
	 * Encapsulates the creation of the marketing_group_item records (temp table).
	 * @param conn
	 * @throws SQLException
	 */
	private static void insertMarketingGroupItemRecords(Connection conn) throws SQLException {
		// note: marketing_group_item is a temp table
		String sql = ""
				+ " insert into marketing_group_item (marketing_group_id, item_id)"
				+ " select mg.marketing_group_id, yi.item_id"
				+ " from  pun_hierarchy ph"
				+ "   join marketing_group mg on ph.\"category1\" = mg.cat1 and ph.\"category2\" = mg.cat2 and ph.\"category3\" = mg.cat3 and ph.\"category4\" = mg.cat4 and ph.\"publishing_unit_name_automated\" = mg.marketing_group_name"
				+ "   join yfs_item yi on trim(ph.\"item_id\") = trim(yi.item_id)"
				+ " where ph.\"publishing_unit_name_automated\" is not null";

		if (log.isDebugEnabled()) {
			log.debug(String.format("Normalize pun hierarchy:\t%s", sql));
		}

		PreparedStatement stmt = conn.prepareStatement(sql);

		int numAffected = stmt.executeUpdate();

		if (log.isDebugEnabled()) {
			log.debug(String.format("Inserted %s marketing_group_item records", numAffected));
		}

		stmt.close();
	}

	/**
	 * Encapsulates the updating of the yfs_item records (extn_marketing_group_id column).
	 * @param conn
	 * @throws SQLException
	 */
	private static void updateYfsItemRecords(Connection conn) throws SQLException {
		String sql = ""
				+ " update yfs_item yi set extn_marketing_group_id ="
				+ " 	(select marketing_group_id from marketing_group_item mgi where yi.item_id = mgi.item_id)";

		if (log.isDebugEnabled()) {
			log.debug(String.format("Update items:\t%s", sql));
		}

		PreparedStatement stmt = conn.prepareStatement(sql);

		int numAffected = stmt.executeUpdate();

		if (log.isDebugEnabled()) {
			log.debug(String.format("Updated %s yfs_item records", numAffected));
		}

		stmt.close();
	}

	/**
	 * Encapsulates the creation of the Lucene index.
	 * @param writer
	 * @param conn
	 * @return Returns the number of Lucene documents created.
	 * @throws SQLException
	 * @throws IOException
	 * @throws CorruptIndexException
	 */
	@SuppressWarnings("finally")
	private static long writeIndex(IndexWriter writer, Connection conn) throws SQLException, CorruptIndexException, IOException {
		String sql = "select * from marketing_group where marketing_group_id in"
				+ " (select marketing_group_id from marketing_group_item)";

		if (log.isDebugEnabled()) {
			log.debug(String.format("Querying database lucene contents:\t%s", sql));
		}

		long count = 0;

		PreparedStatement stmtSelect = null;
		ResultSet resSelect = null;
		try {
			stmtSelect = conn.prepareStatement(sql);
			resSelect = stmtSelect.executeQuery();

			final String EMPTY_CAT = "All";
			while (resSelect.next()) {
				String id = resSelect.getString("marketing_group_id");
				String cat1 = resSelect.getString("cat1");
				String cat2 = resSelect.getString("cat2");
				String cat3 = resSelect.getString("cat3");
				String cat4 = resSelect.getString("cat4");
				String name = resSelect.getString("marketing_group_name");

				StringBuilder path = new StringBuilder(1024);
				path.append(cat2);
				path.append(EMPTY_CAT.equals(cat3) ? "" : " > " + cat3);
				path.append(EMPTY_CAT.equals(cat4) ? "" : " > " + cat4);
				path.append(" > " + name);

				String pathStr = path.toString();

				Document doc = new Document();
				doc.add(new Field("marketing_group_id", String.valueOf(id), Field.Store.YES, Field.Index.NOT_ANALYZED));
				doc.add(new Field("marketing_group_name", name, Field.Store.YES, Field.Index.NOT_ANALYZED));
				doc.add(new Field("marketing_group_path", pathStr, Field.Store.YES, Field.Index.NOT_ANALYZED));
				doc.add(new Field("cat1", cat1, Field.Store.YES, Field.Index.NOT_ANALYZED)); // required for sorting

				doc.add(new Field("marketing_group_path_parsed", pathStr, Field.Store.NO, Field.Index.ANALYZED));

				log.debug(id + ":\t" + path);
				writer.addDocument(doc);

				count++;
			}

		} finally {
			if (resSelect != null) {
				try {
					resSelect.close();
				} catch (Exception ignore) {
				}
			}
			if (stmtSelect != null) {
				try {
					stmtSelect.close();
				} catch (Exception ignore) {
				}
			}

			return count;
		}
	}

	private static Connection createConnection() throws ClassNotFoundException, SQLException, FileNotFoundException, IOException {
		log.info(String.format("Creating sql connection: url=%s\tuser=%s\tpass=%s", config.getProperty(JDBC_URL), config.getProperty(JDBC_USER), config.getProperty(JDBC_PASS)));

		Class.forName(config.getProperty(JDBC_DRIVER));
		Connection conn = DriverManager.getConnection(config.getProperty(JDBC_URL), config.getProperty(JDBC_USER), config.getProperty(JDBC_PASS));
		return conn;
	}

	public static void doMain(Properties configSettings) {
		config = configSettings;

		if (log.isInfoEnabled()) {
			log.info(String.format("%s:\t%s", JDBC_DRIVER, config.getProperty(JDBC_DRIVER)));
			log.info(String.format("%s:\t%s", JDBC_URL, config.getProperty(JDBC_URL)));
			log.info(String.format("%s:\t%s", JDBC_USER, config.getProperty(JDBC_USER)));
			log.info(String.format("%s:\t%s", JDBC_PASS, config.getProperty(JDBC_PASS)));
			log.info(String.format("%s:\t%s", LUCENE_INDEX_ROOT_DIR, config.getProperty(LUCENE_INDEX_ROOT_DIR)));
		}

		long start = System.currentTimeMillis();
		long count = 0;
		try {
			count = createIndex();
		} catch (Exception e) {
			log.error("Error creating index (see debug log for details): " + e.getMessage());
			log.debug("", e);
		}
		long stop = System.currentTimeMillis();

		log.info(String.format("Indexed %s marketing groups. Total time (ms): %s", count, stop - start));
	}

	public static void main(String[] args) throws Exception {
		if (args.length < 1) {
			System.err.println("Usage: " + CreateMarketingGroupIndex.class.getSimpleName() + " <sterling foundation directory>");
			System.exit(-1);
			return;
		}

		File sterlingFoundationDir = new File(args[0]);

		Properties jdbcProps = new Properties();
		jdbcProps.load(new FileReader(new File(sterlingFoundationDir, "properties/jdbc.properties")));

		Properties custOverrideProps = new Properties();
		custOverrideProps.load(new FileReader(new File(sterlingFoundationDir, "properties/customer_overrides.properties")));

		Properties configSettings = new Properties();
		configSettings.setProperty(JDBC_DRIVER, jdbcProps.getProperty("oraclePool.driver"));
		configSettings.setProperty(JDBC_URL, jdbcProps.getProperty("oraclePool.url"));
		configSettings.setProperty(JDBC_USER, jdbcProps.getProperty("oraclePool.user"));
		configSettings.setProperty(JDBC_PASS, jdbcProps.getProperty("oraclePool.password"));
		configSettings.setProperty(LUCENE_INDEX_ROOT_DIR, custOverrideProps.getProperty("yfs.marketingGroupIndex.rootDirectory"));

		doMain(configSettings);
	}

}
