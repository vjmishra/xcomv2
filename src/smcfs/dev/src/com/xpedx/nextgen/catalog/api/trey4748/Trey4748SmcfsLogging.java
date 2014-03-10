package com.xpedx.nextgen.catalog.api.trey4748;

import java.net.InetAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Date;

import org.apache.log4j.Logger;

import com.sterlingcommerce.woodstock.util.frame.Manager;
import com.yantra.yfs.core.YFSSystem;
import com.yantra.yfs.japi.YFSEnvironment;


public class Trey4748SmcfsLogging {

	private static final Logger log = Logger.getLogger(Trey4748SmcfsLogging.class);

	private static Trey4748SmcfsLogging instance = new Trey4748SmcfsLogging();

	public static Trey4748SmcfsLogging getInstance() {
		return instance;
	}

	public boolean isEnabled() {
		String enabled = YFSSystem.getProperty("Trey4748Logging.enabled");
		return "true".equals(enabled);
	}

	/**
	 * @param env
	 * @param elapsed In milliseconds
	 * @param description Used as {@code String.format(description, params)}
	 * @param params Used as {@code String.format(description, params)}
	 */
	public void snapshot(YFSEnvironment env, long elapsed, String description, Object... params) {
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			if (!isEnabled()) {
				return;
			}

			String sessionId = env.getUserId(); // token is always empty so provide something that at least scopes it to the user

			Runtime rt = Runtime.getRuntime();

			long total = rt.totalMemory();
			long free = rt.freeMemory();
			long max = rt.maxMemory();
			Date now = new Date();

			String loginid = env.getUserId();

			String message = String.format(description, params);

			String serverName = InetAddress.getLocalHost().getHostName();

			String driver = Manager.getProperty("jdbcService", "oraclePool.driver");
			String url = Manager.getProperty("jdbcService", "oraclePool.url");
			String user = Manager.getProperty("jdbcService", "oraclePool.user");
			String password = Manager.getProperty("jdbcService", "oraclePool.password");
			Class.forName(driver);
			conn = DriverManager.getConnection(url, user, password);

			conn.setAutoCommit(false);

			stmt = conn.prepareStatement("insert into trey_4748_logging (session_id, username, description, elapsed_millis, mem_total, mem_free, mem_used, mem_max, created, server_name, application) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			int idx = 1;
			stmt.setString(idx++, sessionId);
			stmt.setString(idx++, loginid);
			stmt.setString(idx++, truncate(message));
			stmt.setLong(idx++, elapsed);
			stmt.setLong(idx++, total);
			stmt.setLong(idx++, free);
			stmt.setLong(idx++, total - free);
			stmt.setLong(idx++, max);
			stmt.setTimestamp(idx++, new java.sql.Timestamp(now.getTime()));
			stmt.setString(idx++, serverName);
			stmt.setString(idx++, "smcfs");

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
