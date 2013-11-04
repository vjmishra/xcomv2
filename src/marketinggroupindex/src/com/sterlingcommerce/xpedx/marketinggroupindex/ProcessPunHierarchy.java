package com.sterlingcommerce.xpedx.marketinggroupindex;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;

public class ProcessPunHierarchy {

	public static final String JDBC_DRIVER = "jdbc.driver";
	public static final String JDBC_URL = "jdbc.url";
	public static final String JDBC_USER = "jdbc.user";
	public static final String JDBC_PASS = "jdbc.pass";

	private static final Logger log = Logger.getLogger(ProcessPunHierarchy.class);

	private static Properties config;

	private static long processPunData() throws Exception {
		Connection conn = null;
		try {
			conn = createConnection();
			conn.setAutoCommit(false);

			deleteMarketingGroupRecords(conn);
			deleteMarketingGroupItemRecords(conn);

			insertMarketingGroupRecords(conn);
			insertMarketingGroupItemRecords(conn);

			long numYfsItemsUpdated = updateYfsItemRecords(conn);

			conn.commit();

			return numYfsItemsUpdated;

		} catch (Exception e) {
			if (conn != null) {
				try {
					log.debug("Rolling back database transaction");
					conn.rollback();
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
		}
	}

	private static void deleteMarketingGroupRecords(Connection conn) throws SQLException {
		String sql = ""
				+ " delete from marketing_group";

		if (log.isDebugEnabled()) {
			log.debug(String.format("Delete old data:\t%s", sql));
		}

		PreparedStatement stmt = conn.prepareStatement(sql);

		int numAffected = stmt.executeUpdate();

		if (log.isDebugEnabled()) {
			log.debug(String.format("Deleted %s marketing_group records", numAffected));
		}

		stmt.close();
	}

	private static void deleteMarketingGroupItemRecords(Connection conn) throws SQLException {
		String sql = ""
				+ " delete from marketing_group_item";

		if (log.isDebugEnabled()) {
			log.debug(String.format("Delete old data:\t%s", sql));
		}

		PreparedStatement stmt = conn.prepareStatement(sql);

		int numAffected = stmt.executeUpdate();

		if (log.isDebugEnabled()) {
			log.debug(String.format("Deleted %s marketing_group records", numAffected));
		}

		stmt.close();
	}

	/**
	 * Encapsulates the creation of the marketing_group records (temp table).
	 * @param conn
	 * @throws SQLException
	 */
	private static void insertMarketingGroupRecords(Connection conn) throws SQLException {
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
	private static long updateYfsItemRecords(Connection conn) throws SQLException {
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

		return numAffected;
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
		}

		long start = System.currentTimeMillis();
		long numYfsItemsUpdated = 0;
		try {
			numYfsItemsUpdated = processPunData();
		} catch (Exception e) {
			log.error("Error processing pun data (see debug log for details): " + e.getMessage());
			log.debug("", e);
		}
		long stop = System.currentTimeMillis();

		log.info(String.format("Updated %s yfs_item records. Total time (ms): %s", numYfsItemsUpdated, stop - start));
	}

}
