package com.xpedx.nextgen.common.util;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
/**
 * Currently special character processing of item data happens during ItemLoad service. Every time we make 
 * updates to the special character processing there is a need to run the full item load which spans 3-4 days.
 * Purpose of this class is to retrieve item data from database and perform the special character processing
 * so that the full item load can be by-passed. 
 * 
 * It retrieves item data from the database, builds the item array and then passes it to the Oracle stored 
 * procedure. The Oracle stored procedure returns the number of items processed. Item details are passed to
 * stored procedure in batches of 50,000.
 * 
 * This class can be run as a stand alone java class by passing the environment as command line parameter.
 * Item Load process should be turned off.
 * 
 * Following are the instructions to run the code in any environment:
 * 1.Stop the Item Load agent
 * 
 * 2.Retrieve following java files from StarTeam:	
 * 	src/smcfs/dev/src/com/nextgen/common/util/XPXCatalogDataProcessor.java
 *  src/smcfs/dev/src/com/nextgen/common/util/XPXItemDetail.java
 *  src/smcfs/dev/src/com/nextgen/common/util/XPXItemLoadProcessor.java
 *  scripts/utilities/processItems.sh
 *  
 * 3.create the following directory structure in the server environment:
 * 	itemload/com/nextgen/common/util
 * 	
 * 4.Copy the above java files to the util folder
 * 
 * 5.Copy the processItems.sh to the itemload folder
 * 
 * 6.Run the processItems.sh as below:
 * 	./processItems.sh dev
 * 
 * 7.Trigger a search index and activate it once complete.
 * 
 * 8.Start the Item Load agent
 * @author Muthukumar SM
 *
 */
public class XPXItemLoadProcessor {
	private static Logger logger = Logger.getLogger("com.xpedx.nextgen.log");
	/**
	 * This method returns database connection from local database
	 * @return Connection
	 */
	private static Connection getLocalConnection(){
		Connection dbConn = null;
		try{
		Class.forName("oracle.jdbc.driver.OracleDriver");
        String url = "jdbc:oracle:thin:@127.0.0.1:1521:ORCL";
        dbConn =  DriverManager.getConnection(url,"NGMC_NEW", "NGMC_NEW");
        logger.info("Database connection established");
		}catch(SQLException exSQL){
			logger.severe("Error getting Database Connection: "+exSQL.getMessage());
		}catch(ClassNotFoundException exClass){
			logger.severe("Error getting Database Connection: "+exClass.getMessage());
		}
        return dbConn;
	}
	
	/**
	 * This method returns database connection from sand box database
	 * @return Connection
	 */
	private static Connection getSandBoxConnection(){
		Connection dbConn = null;
		try{
		Class.forName("oracle.jdbc.driver.OracleDriver");
        String url = "jdbc:oracle:thin:@oratst08.ipaper.com:1521:ngD1";
        dbConn =  DriverManager.getConnection(url,"ngmc", "ngmc1");
        logger.info("Database connection established");
		}catch(SQLException exSQL){
			logger.severe("Error getting Database Connection: "+exSQL.getMessage());
		}catch(ClassNotFoundException exClass){
			logger.severe("Error getting Database Connection: "+exClass.getMessage());
		}
        return dbConn;

	}
	
	/**
	 * This method returns database connection from development database
	 * @return Connection
	 */
	private static Connection getDevConnection(){
		Connection dbConn = null;
		try{
		Class.forName("oracle.jdbc.driver.OracleDriver");
        String url = "jdbc:oracle:thin:@172.20.137.35:1521:ngd1";
        dbConn =  DriverManager.getConnection(url,"ng", "ng1");
        logger.info("Database connection established");
        
		}catch(SQLException exSQL){
			logger.severe("Error getting Database Connection: "+exSQL.getMessage());
		}catch(ClassNotFoundException exClass){
			logger.severe("Error getting Database Connection: "+exClass.getMessage());
		}
		return dbConn;

	}
	
	/**
	 * This method returns database connection from staging database
	 * @return Connection
	 */
	private static Connection getStgConnection(){
		Connection dbConn = null;
		try{
		Class.forName("oracle.jdbc.driver.OracleDriver");
        String url = "jdbc:oracle:thin:@172.20.137.35:1521:ngt1";
        dbConn =  DriverManager.getConnection(url,"ng", "ng1");
        logger.info("Database connection established");
        
		}catch(SQLException exSQL){
			logger.severe("Error getting Database Connection: "+exSQL.getMessage());
		}catch(ClassNotFoundException exClass){
			logger.severe("Error getting Database Connection: "+exClass.getMessage());
		}
		return dbConn;

	}
	
	/**
	 * This method returns database connection from prod_support database
	 * @return Connection
	 */
	private static Connection getProdSupportConnection(){
		Connection dbConn = null;
		try{
		Class.forName("oracle.jdbc.driver.OracleDriver");
        String url = "jdbc:oracle:thin:@oratst08.ipaper.com:1521:ngpst1";
        dbConn =  DriverManager.getConnection(url,"ng", "ng1");
        logger.info("Database connection established");
       	}catch(SQLException exSQL){
			logger.severe("Error getting Database Connection: "+exSQL.getMessage());
		}catch(ClassNotFoundException exClass){
			logger.severe("Error getting Database Connection: "+exClass.getMessage());
		}
		return dbConn;

	}
	
	/**
	 * This method returns database connection from prod database
	 * @return Connection
	 */
	private static Connection getProdConnection(){
		Connection dbConn = null;
		try{
		Class.forName("oracle.jdbc.driver.OracleDriver");
        String url = "jdbc:oracle:thin:@164.103.185.68:1521:NGP1";
        dbConn =  DriverManager.getConnection(url,"ng", "ng1");
        logger.info("Database connection established");
        
		}catch(SQLException exSQL){
			logger.severe("Error getting Database Connection: "+exSQL.getMessage());
		}catch(ClassNotFoundException exClass){
			logger.severe("Error getting Database Connection: "+exClass.getMessage());
		}
		return dbConn;

	}
	
	/**
	 * This method closes database connection
	 * @param Connection
	 */
	private static void closeConnection(Connection dbConn){
		try{
			if(!dbConn.isClosed()){
				dbConn.close();
       	 	}
       	}catch(SQLException exSQL){
 			logger.severe("Error closing Database Connection: "+exSQL.getMessage());
       	}
	}
	
	/**
	 * This method returns the total number of rows in the YFS_ITEM table
	 * @param Connection
	 * @return int
	 * @throws SQLException
	 */
	private static int retrieveItemCount(Connection dbConn){
        PreparedStatement itemStatement = null;
        ResultSet itemResult = null;
        int itemCount = 0;
        try{
        itemStatement = dbConn.prepareStatement("SELECT COUNT(*) FROM YFS_ITEM");
        itemResult = itemStatement.executeQuery();
        if(itemResult.next()){
        	itemCount = itemResult.getInt(1);
        }
        logger.info("Total item count in the database: "+itemCount);
        }catch(SQLException exSQL){
        	logger.severe("Error in retrieving item count: "+exSQL.getMessage());
        }finally{
        	try{
        	itemResult.close();
        	itemStatement.close();
        	}catch(SQLException exSQL){
        		logger.severe("Error closing database results and statements: "+exSQL.getMessage());
        	}
        }
        return itemCount;
	}
	
	/**
	 * This method retrieves item details from YFS_ITEM table, populates them in the XPXItemDetail object 
	 * and returns the collection of XPXItemDetail objects
	 * @param Connection
	 * @return List<XPXItemDetail[]>
	 * @throws SQLException
	 */
	private static List<XPXItemDetail[]> createItemObjects(Connection dbConn){
		int itemCount = retrieveItemCount(dbConn);
		if(itemCount == 0){
			logger.info("Item objects not created since the item count is 0");
			return null;
		}
		int batchCount = 50000;
		int remainingCount = itemCount;
		int initialArrayCount = 0;
		if(itemCount > batchCount){
			initialArrayCount = batchCount;
		}
		List<XPXItemDetail[]> itemArrayList = new ArrayList<XPXItemDetail[]>();
		XPXItemDetail[] itemDetailArray = new XPXItemDetail[initialArrayCount];
		PreparedStatement itemStatement = null;
	    ResultSet itemResult = null;
	    int itemIndex = 0;
	    int arrayCount = 0;
	    try{
	    	itemStatement = dbConn.prepareStatement("SELECT ITEM_KEY, ITEM_ID, KEYWORDS, DESCRIPTION, SHORT_DESCRIPTION, EXTENDED_DESCRIPTION FROM YFS_ITEM");
	        itemResult = itemStatement.executeQuery();
	        while(itemResult.next()){
	        	XPXItemDetail itemDetail = new XPXItemDetail();
	        	itemDetail.setItemKey(itemResult.getString("ITEM_KEY"));
	        	itemDetail.setItemId(itemResult.getString("ITEM_ID"));
	        	itemDetail.setKeyWords(XPXCatalogDataProcessor.preprocessCatalogData(itemResult.getString("KEYWORDS")));
	        	itemDetail.setDescription(XPXCatalogDataProcessor.preprocessCatalogData(itemResult.getString("DESCRIPTION")));
	        	itemDetail.setShortDescription(XPXCatalogDataProcessor.preprocessCatalogData(itemResult.getString("SHORT_DESCRIPTION")));
	        	itemDetail.setExtendedDescription(XPXCatalogDataProcessor.preprocessCatalogData(itemResult.getString("EXTENDED_DESCRIPTION")));
	        	logger.info("Item Detail retrieved from Database: "+itemDetail.toString());
	        	//Add item to the item array
	        	itemDetailArray[itemIndex++] = itemDetail;
	        	//if the batch count limit has reached then re-initiate the item array. Add the old item array to List.
	        	if(itemIndex == batchCount){
	        		itemIndex = 0;
	        		itemArrayList.add(itemDetailArray);
	        		remainingCount = remainingCount-batchCount;
	        		if(remainingCount > batchCount){
	        			arrayCount = batchCount;
	        		}else{
	        			arrayCount = remainingCount;
	        		}
	        		itemDetailArray = new XPXItemDetail[arrayCount];
	        	}
	        }
	        if(itemIndex < batchCount){
	        	itemArrayList.add(itemDetailArray);
	        }
	    }catch(SQLException exSQL){
	    	logger.severe("Error in retrieving item details: "+exSQL.getMessage());
	    }finally{
	    	try{
	        	itemResult.close();
	        	itemStatement.close();
	    	}catch(SQLException exSQL){
	    		logger.severe("Error in closing database results and statements: "+exSQL.getMessage());
	    	}
	        	
	    }
	    return itemArrayList;
		
	}
	
	/**
	 * This method passes the item array to the Oracle stored procedure. The stored procedure updates the 
	 * EXTN columns in the YFS_ITEM table. Items are passed to the stored procedure in batches.
	 * @param Connection
	 * @throws SQLException
	 */
	private static void processItems(Connection dbConn) {
		CallableStatement callStmt = null;
		try{
			List<XPXItemDetail[]> itemDetailArrayList = createItemObjects(dbConn);
			if(itemDetailArrayList == null || itemDetailArrayList.size() == 0){
				logger.info("Items not processed sine the item array was empty");
				return;
			}
			ArrayDescriptor descriptor = ArrayDescriptor.createDescriptor( "YFS_ITEM_TABLE", dbConn );
			ARRAY itemArray_Oracle = null;
			//Iterate through the collection of item detail array objects and pass them to the Stored Procedure
			for(XPXItemDetail[] itemDetailArray : itemDetailArrayList){
				itemArray_Oracle = new ARRAY( descriptor, dbConn, itemDetailArray);
				callStmt = dbConn.prepareCall("call processItems(?,?)");      
				callStmt.setArray( 1, itemArray_Oracle );
				callStmt.registerOutParameter(2, Types.VARCHAR);
				callStmt.executeUpdate();
				String itemCount = callStmt.getString(2);
				logger.info("Number of items successfully updated to database: "+itemCount);
			}
		}catch(SQLException exSQL){
			logger.severe("Error in calling oracle stored procedure: "+exSQL.getMessage());
		}finally{
			try{
				callStmt.close();
			}catch(SQLException exSQL){
				logger.severe("Error closing database statements: "+exSQL.getMessage());
			}
		}
	}
	
	/**
	 * Main method to execute the job
	 * @param String[]
	 */
	public static void main(String[] args) {
		Connection dbConn = null;
		if(args.length == 0){
			System.out.println("Please provide environment in the command line - local/sand_box/dev/stg/prod/prod_support");
			return;
		}
		String env = args[0];
		if(env == null || env.trim().equals("")){
			System.out.println("Please enter environment - local/sand_box/dev/stg/prod/prod_support");
			return;
		}
		
		if(!(env.equals("local") || env.equals("sand_box") || env.equals("dev") || env.equals("stg") || env.equals("prod") || env.equals("prod_support"))){
			System.out.println("Please enter valid environment - local/sand_box/dev/stg/prod/prod_support");
			return;
		}
		System.out.println("Is the Data load stopped?[y\\n]");
		Scanner in = new Scanner(System.in);
		String userInput = in.nextLine();
		if(!userInput.equalsIgnoreCase("y")){
			System.out.println("Please stop data load and then run the job.");
			return;
		}
		try{
			logger.info("current environment: "+env);
			if(env.equals("prod_support")){
				dbConn = getProdSupportConnection();
			}else if(env.equals("dev")){
				dbConn = getDevConnection();
			}else if(env.equals("prod")){
				dbConn = getProdConnection();
			}else if(env.equals("stg")){
				dbConn = getStgConnection();
			}else if(env.equals("sand_box")){
				dbConn = getSandBoxConnection();
			}
			
			if(dbConn == null){
				return;
			}
			processItems(dbConn);
		}catch(Exception exMain){
			logger.severe("Error running the batch job: "+exMain.getMessage());
		}finally{
			closeConnection(dbConn);
		}
	}

}
