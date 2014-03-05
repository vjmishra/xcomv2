package com.sterlingcommerce.xpedx.webchannel.catalog;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Date;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.sterlingcommerce.woodstock.util.frame.Manager;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfs.core.YFSSystem;

/*
 * Created on Mar 4, 2014
 */

/**
 * We're having trouble identifying the root cause of eb-4748 so we're going to add a lot of logging to isolate the slowness.
 * This class and all calls to it will be deleted in the near future when the performance issue is fixed.
 *
 * @author Trey Howard
 */
public class Trey4748Logging {

	private static final Logger log = Logger.getLogger(Trey4748Logging.class);

	private static Trey4748Logging instance = new Trey4748Logging();

	public static Trey4748Logging getInstance() {
		return instance;
	}

	public boolean isEnabled() {
		String enabled = YFSSystem.getProperty("Trey4748Logging.enabled");
		return "true".equals(enabled);
	}

	/**
	 * @param session
	 * @param elapsed In milliseconds
	 * @param description Used as {@code String.format(description, params)}
	 * @param params Used as {@code String.format(description, params)}
	 */
	public void snapshot(HttpSession session, long elapsed, String description, Object... params) {
		if (!isEnabled()) {
			return;
		}

		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			Runtime rt = Runtime.getRuntime();

			long total = rt.totalMemory();
			long free = rt.freeMemory();
			long max = rt.maxMemory();
			Date now = new Date();

			YFCElement currentUser = (YFCElement) session.getAttribute("CurrentUser");
			String loginid = currentUser == null ? null : currentUser.getAttribute("Loginid");

			String message = String.format(description, params);

			String driver = Manager.getProperty("jdbcService", "oraclePool.driver");
			String url = Manager.getProperty("jdbcService", "oraclePool.url");
			String user = Manager.getProperty("jdbcService", "oraclePool.user");
			String password = Manager.getProperty("jdbcService", "oraclePool.password");
			Class.forName(driver);
			conn = DriverManager.getConnection(url, user, password);

			conn.setAutoCommit(false);

			stmt = conn.prepareStatement("insert into trey_4748_logging (session_id, username, description, elapsed_millis, mem_total, mem_free, mem_used, mem_max, created) values (?, ?, ?, ?, ?, ?, ?, ?, ?)");
			int idx = 1;
			stmt.setString(idx++, session.getId());
			stmt.setString(idx++, loginid);
			stmt.setString(idx++, truncate(message));
			stmt.setLong(idx++, elapsed);
			stmt.setLong(idx++, total);
			stmt.setLong(idx++, free);
			stmt.setLong(idx++, total - free);
			stmt.setLong(idx++, max);
			stmt.setTimestamp(idx++, new java.sql.Timestamp(now.getTime()));

			stmt.executeUpdate();

			conn.commit();

			log.info(String.format("loginid=%s | %s", loginid, message));

		} catch (Exception ignore) {
			// do not let logging stop the train
			log.error("Failed to write log to database", ignore);

		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception ignore) {
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception ignore) {
				}
			}
		}
	}

	/**
	 * Truncate string at 4000 characters to avoid sql error.
	 * @param str
	 * @return
	 */
	private static String truncate(String str) {
		return (str != null && str.length() > 4000) ? (str.substring(0, 3997) + "...") : str;
	}

}
