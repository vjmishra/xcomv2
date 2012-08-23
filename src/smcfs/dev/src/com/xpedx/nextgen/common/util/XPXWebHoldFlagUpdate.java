package com.xpedx.nextgen.common.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.japi.YFSConnectionHolder;
import com.yantra.yfs.japi.YFSEnvironment;

/**
 * This Class will update Customer rule profile web hold flag in 
 * xpx_customer_rules_profile table by reading excel file received from
 * MAX and accordingly updating record in NG
 * 
 * @author lahotip
 *
 */

public class XPXWebHoldFlagUpdate implements YIFCustomApi {

	private static YFCLogCategory log;
	private static YIFApi api = null;
	
	static {
		log = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");
		try 
		{
			api = YIFClientFactory.getInstance().getApi();
			
			//TODO:createXPXCustomerRulesProfile
		}
		catch (YIFClientCreationException e1) {

			e1.printStackTrace();
		}
	}
	
	
	
	@Override
	public void setProperties(Properties arg0) throws Exception {
		// TODO Auto-generated method stub

	}
	
	
	/**
	 * This is the method which gets invoked on the hit of the service.
	 * This forms the complete input xml for manageCustomer api
	 * @param env
	 * @param inXML
	 * @return
	 * @throws Exception
	 */
	public Document invokeCustomerRuleProfile(YFSEnvironment env,Document inXML) throws Exception
	{
	 String fileName = "WebHold Settings as of 8-16-2012.xls";	
	 Element customerElement = inXML.getDocumentElement();
	 createCustomerRuleProfile(fileName,customerElement,env);	
	 return null;	
	}

	
	/**
	 * This Operation will read data from excel , create customer id and inser record in custome rule profile table
	 * createCustomerRuleProfile
	 * @param fileName
	 * @param customerElement
	 * @param env
	 */
	private void createCustomerRuleProfile(String fileName,Element customerElement,YFSEnvironment env) {
		
		 /*Rule ID should exist in XPX_RULE_DEFN ,If it does not we need to 
		  first execute service written for this populating rule id in XPX_RULE_DEFN table
		 */
         String ruleID = customerElement.getAttribute("RuleID");
         log.info("Rule ID "+ruleID);
 	     String workSheetName = customerElement.getAttribute("WorkSheetName");
 	     String suffixType = customerElement.getAttribute("suffixType"); 
      	 String orgnizationCode = "xpedx";
      	 String billTo = "-000-M-XX-B";
      	 String shipTo = "-M-XX-S";
      	 String ruleKey = null;
      	 String customerRuleKey = null;
      	Connection connection = null;
        
        System.out.println(fileName);
        Workbook workbook = null;
        HSSFSheet sheet = null;
      //  try {
            //Excel file has been stored in same package where java file exist as this is one time process
            //will not be called any time in future
            //TODO: Will Change location of file on basis on review comments. 
            
            InputStream in = XPXWebHoldFlagUpdate.class.getResourceAsStream(fileName);//new FileInputStream(fileName);
            log.info("File Stream"+in.toString());
            try {
                workbook = WorkbookFactory.create(in);
                sheet = (HSSFSheet) workbook.getSheet(workSheetName);
                int noOfRows = ((org.apache.poi.ss.usermodel.Sheet) sheet).getPhysicalNumberOfRows();
                log.info("Number of excel rows are "+noOfRows);
                /**
                 * Direct SQl query is used to make batch execution faster and as this is one time process so used direct SQl query   
                 */
                connection = getDBConnection(env);
	   	        String ruleQuery = "select rule_key from XPX_RULE_DEFN where rule_id = "+ "'" + ruleID + "'";
	   	        ruleKey = returnQueryValue(ruleQuery,connection);
	   	        log.info("Rule Key "+ruleKey);
                String customerId = null;
                String webHoldFlag  = null;

                for (int rownum = 0; rownum < noOfRows; rownum++) {
                    Row row = ((org.apache.poi.ss.usermodel.Sheet) sheet).getRow(rownum);
                    StringBuffer customerIdCreation = new StringBuffer();
                    int counter = 0;
                    int noOfColumns = row.getLastCellNum();
                    for (int colnum = 0; colnum < noOfColumns; colnum++) {
                    	counter++;
                    	if(noOfColumns == colnum){
                    		break;
                    	}
                        Cell cell = row.getCell(colnum);
                        switch (cell.getCellType()) {
                        case Cell.CELL_TYPE_STRING:
                            customerId = cell.getStringCellValue();
                            break;
                        case Cell.CELL_TYPE_NUMERIC:
                            if (DateUtil.isCellDateFormatted(cell)) {
                                cell.getDateCellValue();
                            } else {
                                customerId = String.valueOf(new java.text.DecimalFormat("0").format( cell.getNumericCellValue()));
                            }
                            break;
                        case Cell.CELL_TYPE_BOOLEAN:
                            customerId =String.valueOf(cell.getBooleanCellValue()) ;
                            break;
                        case Cell.CELL_TYPE_FORMULA:
                            customerId = cell.getCellFormula();
                            break;
                        default:
                            System.out.println("No Formated Data");
                        }
                        if(!customerId.equalsIgnoreCase("Y") && counter != 2){
                        customerIdCreation.append(customerId).append("-");
                        }
                        if(counter == 2){
                        	String zeroPrefix = getZeroPrefix(customerId);
                        	customerIdCreation.append(zeroPrefix).append(customerId);
                        }
                        else{
                        	webHoldFlag = customerId;	
                        }
                    }
       	        
                 /**
                  * Direct SQl query is used to make batch execution faster and as this is one time process so used direct SQl query   
                  */
                 if(suffixType.equalsIgnoreCase("B")){
                	 customerIdCreation.append(billTo);
                 }else{
                	 customerIdCreation.append(shipTo);
                 }
                 log.info("Customer ID "+customerIdCreation.toString());
    	         String customerKey = "	select * from yfs_customer where customer_id ="+ "'" + customerIdCreation.toString() + "'";
    	         customerRuleKey = returnQueryValue(customerKey,connection); 
    	         
                
    	         /**
    	          * Record will be inserted in xpx_customer_rules_profile table only on basis if web hold flag in 
    	          * excel received from MAX has value of "Y" and also customer key should exist in customer key table.
    	          */
    	        if(customerRuleKey != null && !customerRuleKey.isEmpty() && webHoldFlag.equalsIgnoreCase("Y")){
               	Document getCustomerProfileRuleInputDoc = YFCDocument.createDocument("XPXCustomerRulesProfile").getDocument();
               	getCustomerProfileRuleInputDoc.getDocumentElement().setAttribute("CustomerKey", customerRuleKey.trim());
               	getCustomerProfileRuleInputDoc.getDocumentElement().setAttribute("RuleKey",ruleKey.trim());
               	getCustomerProfileRuleInputDoc.getDocumentElement().setAttribute("CustomerID",customerIdCreation.toString().trim());
               	getCustomerProfileRuleInputDoc.getDocumentElement().setAttribute("OrganizationCode",orgnizationCode);
               	
               	/**
               	 * Check for Duplicate Customer_ID in XPX Create Customer Profile Rule table is not validated as in 
               	 * table xpx_customer_rules_profile there are no records in Development and staging and as adding 
               	 * check condition with impact Service performance so consideration as there is no duplicate record 
               	 * come in excel from MAX. 
               	 */
               	
              // 	log.debug("The input xml to XPXGetCustomerProfileRuleList is: "+SCXmlUtil.getString(getCustomerProfileRuleInputDoc));
               	Document getCustomerProfileRuleOutputDoc = api.executeFlow(env, "XPXCreateCustomerProfileRule", getCustomerProfileRuleInputDoc);
              // 	log.debug("The output xml to XPXGetCustomerProfileRuleList is: "+SCXmlUtil.getString(getCustomerProfileRuleOutputDoc));
    	        }else{
    	        
    	        	log.info("Customer ID is not in Database"+customerIdCreation.toString());
    	        	
    	        }  
                    
                }
                if(connection != null){
                connection.close();
                }
 
            } catch (InvalidFormatException e) {
            	e.printStackTrace();
            	log.error("InvalidFormatException: " + e.getStackTrace());	
              
            } catch (IOException e) {
            	e.printStackTrace();
            	log.error("IOException: " + e.getStackTrace());	
            }
            catch (SQLException e) {
            	e.printStackTrace();
            	log.error("SQLException: " + e.getStackTrace());	
            	try {
					connection.close();
				} catch (SQLException exception) {
					// TODO Auto-generated catch block
					exception.printStackTrace();
				}
			}
            catch (Exception e) {
            	e.printStackTrace();
            	log.error("Exception: " + e.getStackTrace());	
            }
 
       
    
}
	/**
	 * This operation will return 
	 * getZeroPrefix
	 * @param customerId
	 * @return
	 */
	private String getZeroPrefix(String customerId) {
		String appender = "";
		if (customerId.trim().length() < 10) {
			int numberZero = 10 - customerId.trim().length();
			//System.out.println(numberZero);
			if (numberZero == 1) {
				appender = "0";
			}
			else if (numberZero == 2) {
				appender = "00";
			} else if (numberZero == 3) {
				appender = "000";
			} else if (numberZero == 4) {
				appender = "0000";
			} else if (numberZero == 5) {
				appender = "00000";
			} else if (numberZero == 6) {
				appender = "000000";
			} else if (numberZero == 7) {
				appender = "0000000";
			}
			else if (numberZero == 8) {
				appender = "00000000";
			}
			return appender;
		}
		return appender;
	}


	/**
	 * This will return value based on query that has been fired
	 * returnQueryValue
	 * @param query
	 * @param connection
	 * @return
	 */
	private String returnQueryValue(String query, Connection connection) {
		Statement stmt = null;
		ResultSet resultset = null;
		try {
			stmt = connection.createStatement();
			resultset = stmt.executeQuery(query);
			while (resultset.next()) {
				return resultset.getString(1);
			}
			//connection.commit();
			resultset.close();
			stmt.close();
		} catch (Exception exception) {
			try {
				resultset.close();
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			exception.printStackTrace();
		}
		return null;
	}

	/**
	 * getDBConnection
	 * This operation will return connection object
	 * @param env
	 * @return
	 */
	private Connection getDBConnection(YFSEnvironment env)
	{
		Connection m_Conn=null;
		try{
			YFSConnectionHolder connHolder     = (YFSConnectionHolder)env;
			m_Conn= connHolder.getDBConnection();
		}
		catch(Exception e){
			log.error("Exception: " + e.getStackTrace());
			e.printStackTrace();
		}

		return m_Conn;
	}

}
