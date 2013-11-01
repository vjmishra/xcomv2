package com.sterlingcommerce.xpedx.marketinggroupindex;

import java.io.File;
import java.io.FileReader;
import java.util.Properties;

/**
 * Main class for the jar. Command line argument indicates which operation is to be performed.
 *
 * @author Trey Howard
 * @see CreateMarketingGroupIndex
 * @see ProcessPunHierarchy
 */
public class Main {

	/**
	 * @param args 1st param is operation. 2nd param is the file path to the Sterling Foundation directory.
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		if (args.length < 2) {
			System.err.print("Usage: Two forms, depending on desired operation:" + "\n"
					+ "In both cases the path to the Sterling Foundation folder is provided, using the settings provided in the jdbc.properties and/or customer_overrides.properties as needed." + "\n"
					+ "\n"
					+ "To process the pun_hierarchy data (populates marketing_group and marketing_group_index tables):" + "\n"
					+ "  java -jar marketinggroupindex.jar database /path/to/sterling/Foundation" + "\n"
					+ "  For example: ava -jar marketinggroupindex.jar database /xpedx/sterling/Foundation" + "\n"
					+ "\n"
					+ "To create the Lucene index (from marketing_group and marketing_group_index tables) as a sub-directory of the Foundation folder:" + "\n"
					+ "  java -jar marketinggroupindex.jar index /path/to/sterling/Foundation" + "\n"
					+ "  For example: ava -jar marketinggroupindex.jar index /xpedx/sterling/Foundation" + "\n"
					);
			System.exit(1);
			return;
		}

		String operation = args[0];

		File sterlingFoundationDir = new File(args[1]);

		Properties jdbcProps = new Properties();
		jdbcProps.load(new FileReader(new File(sterlingFoundationDir, "properties/jdbc.properties")));

		Properties custOverrideProps = new Properties();
		custOverrideProps.load(new FileReader(new File(sterlingFoundationDir, "properties/customer_overrides.properties")));

		if ("database".equals(operation)) {
			validateProperties(jdbcProps, "oraclePool.driver", "oraclePool.url", "oraclePool.user", "oraclePool.password");

			Properties configSettings = new Properties();
			configSettings.setProperty(ProcessPunHierarchy.JDBC_DRIVER, jdbcProps.getProperty("oraclePool.driver"));
			configSettings.setProperty(ProcessPunHierarchy.JDBC_URL, jdbcProps.getProperty("oraclePool.url"));
			configSettings.setProperty(ProcessPunHierarchy.JDBC_USER, jdbcProps.getProperty("oraclePool.user"));
			configSettings.setProperty(ProcessPunHierarchy.JDBC_PASS, jdbcProps.getProperty("oraclePool.password"));

			ProcessPunHierarchy.doMain(configSettings);

		} else if ("index".equals(operation)) {
			validateProperties(jdbcProps, "oraclePool.driver", "oraclePool.url", "oraclePool.user", "oraclePool.password");
			validateProperties(custOverrideProps, "yfs.marketingGroupIndex.rootDirectory");

			Properties configSettings = new Properties();
			configSettings.setProperty(CreateMarketingGroupIndex.JDBC_DRIVER, jdbcProps.getProperty("oraclePool.driver"));
			configSettings.setProperty(CreateMarketingGroupIndex.JDBC_URL, jdbcProps.getProperty("oraclePool.url"));
			configSettings.setProperty(CreateMarketingGroupIndex.JDBC_USER, jdbcProps.getProperty("oraclePool.user"));
			configSettings.setProperty(CreateMarketingGroupIndex.JDBC_PASS, jdbcProps.getProperty("oraclePool.password"));
			configSettings.setProperty(CreateMarketingGroupIndex.LUCENE_INDEX_ROOT_DIR, custOverrideProps.getProperty("yfs.marketingGroupIndex.rootDirectory"));
			CreateMarketingGroupIndex.doMain(configSettings);

		} else {
			throw new IllegalArgumentException("Unexpected operation");
		}
	}

	private static void validateProperties(Properties props, String... keys) {
		for (String key : keys) {
			if (!props.containsKey(key)) {
				throw new IllegalStateException("Missing required configuration: " + key);
			}
		}
	}

}
