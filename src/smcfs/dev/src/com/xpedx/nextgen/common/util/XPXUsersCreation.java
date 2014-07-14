package com.xpedx.nextgen.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Properties;

import javax.xml.transform.Transformer;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.ui.web.framework.utils.SCUIUtils;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfc.util.YFCDate;
import com.yantra.yfs.core.YFSSystem;
import com.yantra.yfs.japi.YFSEnvironment;

public class XPXUsersCreation implements YIFCustomApi {

	private static YFCLogCategory log;
	private static YIFApi api = null;
	private String emailAddress = null;
	private StringBuilder emailAddressUpdateSql= null;
	
	static {
		log = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");
		try 
		{
			api = YIFClientFactory.getInstance().getApi();
			
		}
		catch (YIFClientCreationException e1) {
			log.error("API initialization error");
			e1.printStackTrace();
		}
	}
	
	@Override
	public void setProperties(Properties arg0) throws Exception {


	}
	
	public Document createUsers(YFSEnvironment env,Document inputXML) throws Exception
	{
		log = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");	
		log.info("XPXUsersCreation-InXML: "+ SCXmlUtil.getString(inputXML));
		if(inputXML != null)
		{
			Node node = inputXML.getElementsByTagName("EmailAddress").item(0);
			
			 if(!SCUIUtils.isVoid(node) && !SCUIUtils.isVoid(node.getTextContent())){
				 emailAddress = node.getTextContent();
				 File excelFile = getUsersListExcelFile();
				 return createXMLFromExcel(excelFile);
			 }else{
				 throw new Exception("input xml is not valid: valid input for ex:<EmailAddress>xpedx@gmail.com</EmailAddress>");	
			 }
		}
		else{
			throw new Exception("input xml is null: valid input for ex:<EmailAddress>xpedx@gmail.com</EmailAddress>");			
		}
		
	}
	private Document createXMLFromExcel(File excelFile) throws Exception {
		Workbook workbook = null;
		XSSFSheet userListSheet = null;
		XSSFSheet orderConfirmationEmailSheet = null;
		if(excelFile!=null){

				InputStream in = new FileInputStream(excelFile);
				workbook = WorkbookFactory.create(in);
				userListSheet = (XSSFSheet) workbook.getSheet("usersetup");	
				emailAddressUpdateSql = new StringBuilder("UPDATE YFS_CUSTOMER_CONTACT SET EMAILID=");
				
				emailAddressUpdateSql.append("'");
				emailAddressUpdateSql.append(emailAddress);
				emailAddressUpdateSql.append("'");// )
				emailAddressUpdateSql.append(" WHERE CUSTOMER_CONTACT_ID in \n( \n");
				
				orderConfirmationEmailSheet = (XSSFSheet) workbook.getSheet("OrderConfirmationEmailList");				
				String orderConfirmationEmailList = this.getOrderConfirmationEmails(orderConfirmationEmailSheet);				
                int noOfRows = ((org.apache.poi.ss.usermodel.Sheet) userListSheet).getPhysicalNumberOfRows();
                System.out.println("Number of excel rows are "+noOfRows);              
            	Document customerListInputDoc = YFCDocument.createDocument("CustomerList").getDocument();
    			Element customerListInputDocElement = customerListInputDoc.getDocumentElement();
    			
                for (int rownum = 1; rownum < noOfRows; rownum++) {
                	
                    Row row = ((org.apache.poi.ss.usermodel.Sheet) userListSheet).getRow(rownum);
                    String organizationCode = row.getCell(0)!=null?row.getCell(0).getStringCellValue():"";
                    String customerName = row.getCell(1)!=null?row.getCell(1).getStringCellValue():"";
                    String customerID = row.getCell(2)!=null?row.getCell(2).getStringCellValue():"";
                    String defaultShipTo = row.getCell(3)!=null?row.getCell(3).getStringCellValue():"";
                    String firstName = row.getCell(4)!=null?row.getCell(4).getStringCellValue():"";
                    String lastName = row.getCell(5)!=null?row.getCell(5).getStringCellValue():"";
                    String emailAddress = "xpedx@gmail.com"; //row.getCell(6)!=null?row.getCell(6).getStringCellValue():"";              
                    String currency = row.getCell(7)!=null?row.getCell(7).getStringCellValue():"USD";
                    String loginId = row.getCell(8)!=null?row.getCell(8).getStringCellValue().trim().toLowerCase():"";
                    if(rownum!=1){
                    	emailAddressUpdateSql.append(",\n");
                    }
                    emailAddressUpdateSql.append("\t'");
                    emailAddressUpdateSql.append(loginId);
                    emailAddressUpdateSql.append("'");
                    String isAdmin = row.getCell(9)!=null?row.getCell(9).getStringCellValue():"";
                    String isApprover = row.getCell(10)!=null?row.getCell(10).getStringCellValue():"";
                    String isBuyer = row.getCell(11)!=null?row.getCell(11).getStringCellValue():"";
                    String isProcurementUser = row.getCell(12)!=null?row.getCell(12).getStringCellValue():"";
                    String isViewReports = row.getCell(13)!=null?row.getCell(13).getStringCellValue():"";
                    String isStockCheck = row.getCell(14)!=null?row.getCell(14).getStringCellValue():"";
                    String isViewInvoices = row.getCell(15)!=null?row.getCell(15).getStringCellValue():"";
                    String isViewPrices = row.getCell(16)!=null?row.getCell(16).getStringCellValue():"";
                    String isEstimator = row.getCell(17)!=null?row.getCell(17).getStringCellValue():"";
                    String minOrderAmount = row.getCell(18)!=null?row.getCell(18).getNumericCellValue()+"":"";
                    String maxOrderAmount = row.getCell(19)!=null?row.getCell(19).getNumericCellValue()+"":"";
                    String primaryArrover = row.getCell(20)!=null?row.getCell(20).getStringCellValue():"";
                    String alternateApprover = row.getCell(21)!=null?row.getCell(21).getStringCellValue():"";
                    String spendingLimit = row.getCell(22)!=null?row.getCell(22).getNumericCellValue()+"":"";
                    String submitAllOrdersForApproval = row.getCell(23)!=null?row.getCell(23).getStringCellValue():"";
                    String receiveOrderConfirmationEmail = row.getCell(24)!=null?row.getCell(24).getStringCellValue():"";
                    String receiveOrderCancelationEmail = row.getCell(25)!=null?row.getCell(25).getStringCellValue():"";
                    String receiveOrderShipmentEmail = row.getCell(26)!=null?row.getCell(26).getStringCellValue():"";
                    String receiveBackOrderEmail = row.getCell(27)!=null?row.getCell(27).getStringCellValue():"";
                    String pos = row.getCell(28)!=null?row.getCell(28).getStringCellValue():"";                 
                           
                
                  
                    Element customer = SCXmlUtil.createChild(customerListInputDocElement,"Customer");
                    customer.setAttribute("CustomerID", customerID);
                    customer.setAttribute("OrganizationCode", organizationCode);
                    customer.setAttribute("Operation", "Manage");
                    Element customerContactList = SCXmlUtil.createChild(customer,"CustomerContactList");
                    Element customerContact = SCXmlUtil.createChild(customerContactList,"CustomerContact");
                    customerContact.setAttribute("FirstName", firstName);
                    customerContact.setAttribute("LastName", lastName);
                    customerContact.setAttribute("JobTitle", "Business Analyst");
                    customerContact.setAttribute("Department", "xpedx IT - eBusiness");
                    customerContact.setAttribute("EmailID", emailAddress);
                    customerContact.setAttribute("CustomerContactID", loginId);
                    customerContact.setAttribute("Status", "10");
                    customerContact.setAttribute("SpendingLimit", spendingLimit);
                    customerContact.setAttribute("SpendingLimitCurrency", currency);
                    
                    if(!SCUIUtils.isVoid(submitAllOrdersForApproval) && submitAllOrdersForApproval.equalsIgnoreCase("Y")){
                    		customerContact.setAttribute("SpendingLimit", "");
                	}
                    customerContact.setAttribute("ApproverUserId", primaryArrover);
                    customerContact.setAttribute("ApproverProxyUserId", alternateApprover);
                    
                    Element user = SCXmlUtil.createChild(customerContact,"User");
                    
                    user.setAttribute("Activateflag", "Y");
                    user.setAttribute("EnterpriseCode", organizationCode);
                    user.setAttribute("DisplayUserID", loginId);
                    user.setAttribute("Password", "Password1");
                    user.setAttribute("Loginid", loginId);
                    user.setAttribute("GeneratePassword", "N");
                    user.setAttribute("Localecode", "en_US_EST");
                    
                    Element contactPersonInfo = SCXmlUtil.createChild(user,"ContactPersonInfo");
                    
                    contactPersonInfo.setAttribute("FirstName", firstName);
                    contactPersonInfo.setAttribute("LastName", lastName);
                    contactPersonInfo.setAttribute("EMailID", emailAddress);
                 
                    
                    Element userGroupLists = SCXmlUtil.createChild(user,"UserGroupLists");
                    userGroupLists.setAttribute("Reset", "N");
                    
                    if(!SCUIUtils.isVoid(isAdmin) && isAdmin.equalsIgnoreCase("Y")){
	                    Element userGroupList = SCXmlUtil.createChild(userGroupLists,"UserGroupList");
	                    userGroupList.setAttribute("UsergroupId", "BUYER-ADMIN");	                   
                    }
                    
                    if(!SCUIUtils.isVoid(isBuyer) && isBuyer.equalsIgnoreCase("Y")){
                    	Element userGroupList1 = SCXmlUtil.createChild(userGroupLists,"UserGroupList");
                        userGroupList1.setAttribute("UsergroupId", "BUYER-USER");
                    }
                    
                    if(!SCUIUtils.isVoid(isProcurementUser) && isProcurementUser.equalsIgnoreCase("Y")){
                    	Element userGroupList2 = SCXmlUtil.createChild(userGroupLists,"UserGroupList");
                        userGroupList2.setAttribute("UsergroupId", "PROCUREMENT-USER");
                    }
                    
                    if(!SCUIUtils.isVoid(isApprover) && isApprover.equalsIgnoreCase("Y")){
                    	Element userGroupList3 = SCXmlUtil.createChild(userGroupLists,"UserGroupList");
                        userGroupList3.setAttribute("UsergroupId", "BUYER-APPROVER");
                    }
                    
                    Element extn = SCXmlUtil.createChild(customerContact,"Extn");
                    extn.setAttribute("ExtnEstimator", isEstimator);
                    extn.setAttribute("ExtnStockCheckWS", isStockCheck);
                    extn.setAttribute("ExtnViewInvoices", isViewInvoices);
                    extn.setAttribute("ExtnPunchOutUser", isProcurementUser);                   
                    extn.setAttribute("ExtnMaxOrderAmount", maxOrderAmount);
                    extn.setAttribute("ExtnMinOrderAmount", minOrderAmount);
                    extn.setAttribute("ExtnDefaultShipTo", defaultShipTo);
                    extn.setAttribute("ExtnUserType", "EXTERNAL");
                    extn.setAttribute("ExtnViewPricesFlag", isViewPrices);
                    extn.setAttribute("ExtnViewReportsFlag", isViewReports);
                    extn.setAttribute("ExtnOrderConfEmailFlag", receiveOrderConfirmationEmail);
                    extn.setAttribute("ExtnOrderCancelEmailFlag", receiveOrderCancelationEmail);
                    extn.setAttribute("ExtnOrderShipEmailFlag", receiveOrderShipmentEmail);
                    extn.setAttribute("ExtnBackOrderEmailFlag", receiveBackOrderEmail);
                    extn.setAttribute("ExtnAcceptTAndCFlag", "Y");
                    extn.setAttribute("ExtnTAndCAcceptedOn", new YFCDate().getString());
                    extn.setAttribute("ExtnLastLoginDate",  new YFCDate().getString());
                    extn.setAttribute("ExtnOrderApprovalFlag", submitAllOrdersForApproval);
                    if(!SCUIUtils.isVoid(pos)){
                    char lastChar =pos.trim().charAt(pos.trim().length()-1);
	                    if(lastChar!=','){
	                    	pos = pos.trim().concat(",");
	                    }
	                    extn.setAttribute("ExtnPOList",pos.trim());
                    }
                    if(!SCUIUtils.isVoid(orderConfirmationEmailList)){
                    	extn.setAttribute("ExtnAddnlEmailAddrs", orderConfirmationEmailList);
                    }
                    
                    Element customerAssignmentList = SCXmlUtil.createChild(customerContact,"CustomerAssignmentList");
                    Element customerAssignment = SCXmlUtil.createChild(customerAssignmentList,"CustomerAssignment");
                    customerAssignment.setAttribute("CustomerID", customerID);
                    customerAssignment.setAttribute("OrganizationCode", customerID);
                    customerAssignment.setAttribute("UserId", loginId);
                    customerAssignment.setAttribute("Operation", "Create");
                }
                	emailAddressUpdateSql.append("\n ); \n commit;");                	
					javax.xml.transform.TransformerFactory transFact = javax.xml.transform.TransformerFactory.newInstance();
					Transformer transformer = transFact.newTransformer();
					DOMSource source = new DOMSource(customerListInputDoc);
					File xmlFile=getUsersListXMLFile();			
					transformer.transform(source, new StreamResult(xmlFile));
		            
             in.close();
             createUpdateEmailAddressSqlFile();
             return customerListInputDoc;
			}
		return null;
		
		
		
  }
  private File getUsersListXMLFile(){
			String xmlFilePath = YFSSystem.getProperty("xpedx.datamigration.user_prof.input");
			log.info("usersListXML path "+xmlFilePath);
			if(xmlFilePath==null || xmlFilePath.isEmpty()){
				xmlFilePath ="/xpedx/sterling/Foundation/xpedxdataloads/migration/user/in";
			}
			String fileName = xmlFilePath+"/UserProfileCreateExternalUsers.xml";
			log.info("usersListXML.file "+fileName);
		  return new File(fileName);	      

  }
  
  private File getUsersListExcelFile(){

			String name = YFSSystem.getProperty("usersListExcel.file"); 
			log.info("usersListExcel.file "+name);
			if(name==null || name.isEmpty() || !new File(name).exists()){
				name ="/xpedx/testuserloads/in/usersListExcel.xls";
				if(name==null || name.isEmpty() ||!new File(name).exists()){
					name ="/xpedx/testuserloads/in/usersListExcel.xlsx";
				}
			}
			log.info("usersListExcel.file "+name);
		  return new File(name);
	    
  }
  
 private String getOrderConfirmationEmails(XSSFSheet orderConfirmationEmailSheet){
	 StringBuilder orderConfirmationEmails = new StringBuilder();
		 if(orderConfirmationEmailSheet!=null){				  
				 int noOfRows = ((org.apache.poi.ss.usermodel.Sheet) orderConfirmationEmailSheet).getPhysicalNumberOfRows(); 
				 for (int rownum = 0; rownum < noOfRows; rownum++) {
		             Row row = ((org.apache.poi.ss.usermodel.Sheet) orderConfirmationEmailSheet).getRow(rownum);
		             if(row.getCell(0)!=null){
		            	 orderConfirmationEmails.append(row.getCell(0).getStringCellValue()+",");
		             }
			 }			
		 }
	 return orderConfirmationEmails.toString();
 	} 
 private void createUpdateEmailAddressSqlFile() throws Exception {
	 String updateEmailAddressSqlFilePath = YFSSystem.getProperty("email_address_update_sql.file"); 
		log.info("email_address_update_sql.file "+updateEmailAddressSqlFilePath);
		if(updateEmailAddressSqlFilePath==null || updateEmailAddressSqlFilePath.isEmpty()){
			updateEmailAddressSqlFilePath ="/xpedx/testuserloads/out";
		}
		log.info("email_address_update_sql.file "+updateEmailAddressSqlFilePath.toString());
	 PrintWriter pw = new PrintWriter(updateEmailAddressSqlFilePath+"/"+emailAddress+"_emailAddress_update.sql");
	 pw.write(emailAddressUpdateSql.toString());
	 pw.close();

 }
}
