package com.xpedx.nextgen.customermanagement.api;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.xpedx.nextgen.common.cent.ErrorLogger;
import com.xpedx.nextgen.common.util.XPXLiterals;
import com.xpedx.nextgen.dashboard.CallDBSequence;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.japi.*;

import java.sql.*;


public class UploadUom implements YIFCustomApi {
	private static YIFApi api = null;

	/** Added by Arun Sekhar on 01-Feb-2011 for logging **/
	private static YFCLogCategory log;
	
	public static final String getUomListTemplate = "global/template/api/getUomList.XPXMasterUomLoad.xml";
	public static final String getItemUomMasterListTemplate = "global/template/api/getItemUomMasterList.XPXMasterUomLoad.xml";
	
	static
	{
		log = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");		
	}
	public void setProperties(Properties arg0) throws Exception {
		// TODO Auto-generated method stub

	}	
	static
	{
		try {
			api = YIFClientFactory.getInstance().getApi();
		} catch (YIFClientCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public Document getUomUpload(YFSEnvironment env,Document inXML) throws Exception
    {
		/** try-catch block added by Arun Sekhar on 01-Feb-2011 for CENT tool logging **/
		Element UOMElement1 = null;
		try{
			int rSet = 0;
			String finalQuery = null; 
	        NodeList UOMList = inXML.getElementsByTagName("Uom");	        
	        for(int UOMNo = 0;UOMNo<UOMList.getLength();UOMNo++)
	        {  
	        	String uniqueSequenceNo=getUniqueSequenceNo(env); 
	        	
	        	UOMElement1 = (Element)UOMList.item(UOMNo);
	        	String Uom=UOMElement1.getAttribute("Uom");
	        	String UomDescription=UOMElement1.getAttribute("UomDescription");
	        	
	        	Document getUomListInputDoc = YFCDocument.createDocument("Uom").getDocument();
	        	getUomListInputDoc.getDocumentElement().setAttribute("Uom", Uom);
	        	getUomListInputDoc.getDocumentElement().setAttribute("OrganizationCode", "DEFAULT");
	        	env.setApiTemplate("getUomList", getUomListTemplate);
	        	Document getUomListOutputDoc = api.invoke(env, "getUomList", getUomListInputDoc);
	        	log.info("The output of getUomList is: "+SCXmlUtil.getString(getUomListInputDoc));
	        	env.clearApiTemplate("getUomList");
	        	if(getUomListOutputDoc.getDocumentElement().getElementsByTagName("Uom").getLength() > 0)
	        	{
	        		Element uomElement = (Element) getUomListOutputDoc.getDocumentElement().getElementsByTagName("Uom").item(0);
	        		String uomKey = "";
	        		uomKey = uomElement.getAttribute("UomKey");
	        		log.info("The uomkey retrieved is: "+uomKey);
	        		if(uomKey != null && uomKey.trim().length()>0)
	        		{
	        		  finalQuery = "UPDATE YFS_UOM SET UOM_DESCRIPTION = '"+UomDescription+"' WHERE UOM_KEY='"+uomKey+"'";
	        		  log.info("The query for uom is: "+finalQuery);
	        		}
	        	}
	        	else
	        	{
	        	
	        	  finalQuery = "INSERT INTO YFS_UOM(UOM_KEY,ORGANIZATION_CODE,UOM_TYPE,UOM,UOM_DESCRIPTION,CREATEUSERID,MODIFYUSERID,CREATEPROGID,MODIFYPROGID) VALUES('"+uniqueSequenceNo+"','DEFAULT','QUANTITY','"+Uom+"','"+UomDescription+"','xpxBatchLoadAgent','xpxBatchLoadAgent','xpxBatchLoadAgent','xpxBatchLoadAgent')";
	        	  log.info("The query for uom is: "+finalQuery);
	        	}
	        	rSet=fireQuery(env,finalQuery,inXML);
	        	/*if(rSet!=-1)
	        	{
	        		log.info(UomDescription+"YFS_UOM Record not Inserted in YFS_UOM table");
	        		
	        	}*/
	        	
	        	Document getItemUomMasterListInputDoc = YFCDocument.createDocument("ItemUOMMaster").getDocument();
	        	getItemUomMasterListInputDoc.getDocumentElement().setAttribute("UnitOfMeasure", Uom);
	        	getItemUomMasterListInputDoc.getDocumentElement().setAttribute("OrganizationCode", "xpedx");
	        	env.setApiTemplate("getItemUOMMasterList", getItemUomMasterListTemplate);
	        	Document getItemUomMasterListOutputDoc = api.invoke(env, "getItemUOMMasterList", getItemUomMasterListInputDoc);
	        	log.info("The output of getItemUOMMasterList is: "+SCXmlUtil.getString(getItemUomMasterListOutputDoc));
	        	env.clearApiTemplate("getItemUOMMasterList");
	        	if(getItemUomMasterListOutputDoc.getDocumentElement().getElementsByTagName("ItemUOMMaster").getLength() > 0)
	        	{
	        		Element itemUommasterElement = (Element) getItemUomMasterListOutputDoc.getDocumentElement().getElementsByTagName("ItemUOMMaster").item(0);
	        		String itemUommasterKey = "";
	        		itemUommasterKey = itemUommasterElement.getAttribute("ItemUOMMasterKey");
	        		log.info("The item uom master key is: "+itemUommasterKey);
	        		if(itemUommasterKey != null && itemUommasterKey.trim().length()>0)
	        		{
	        		  finalQuery = "UPDATE YFS_ITEM_UOM_MASTER SET DESCRIPTION = '"+UomDescription+"' WHERE ITEM_UOM_MASTER_KEY='"+itemUommasterKey+"'";
	        		  log.info("The query for item_uom_master is: "+finalQuery);
	        		}
	        	}
	        	else
	        	{	
	        	  finalQuery="INSERT INTO YFS_ITEM_UOM_MASTER(ITEM_UOM_MASTER_KEY,ORGANIZATION_CODE,UOM ,UOM_TYPE ,ITEM_GROUP_CODE,DESCRIPTION ,ALLOW_FRACTIONS_IN_CONVERSION,IS_INVENTORY_UOM ,IS_ORDERING_UOM,CREATEUSERID,MODIFYUSERID,CREATEPROGID,MODIFYPROGID)values('"+uniqueSequenceNo+"','xpedx','"+Uom+"','QUANTITY','PROD','"+UomDescription+"','N','Y','Y','xpxBatchLoadAgent','xpxBatchLoadAgent','xpxBatchLoadAgent','xpxBatchLoadAgent')";
	        	  log.info("The query for item_uom_master is: "+finalQuery);
	        	}  
	        	rSet=fireQuery(env,finalQuery,inXML);
	        	/*if(rSet!=-1)
	        	{
	        		//log.info(UomDescription+"YFS_ITEM_UOM_MASTER Record not Inserted");
	        	}*/
	        }	       
		}catch (NullPointerException ne) {
			log.error("------------Failed XML Needs to Catch for Re-Processing XML START ----------");
			log.error(SCXmlUtil.getString(UOMElement1));
			log.error("------------Failed XML Needs to Catch for Re-Processing XML END ----------");
			log.error("NullPointerException: " + ne.getStackTrace());	
			prepareErrorObject(ne, XPXLiterals.UOM_B_TRANS_TYPE, XPXLiterals.NE_ERROR_CLASS, env, inXML);	
			throw ne;
		} catch (YFSException yfe) {
			log.error("------------Failed XML Needs to Catch for Re-Processing XML START ----------");
			log.error(SCXmlUtil.getString(UOMElement1));
			log.error("------------Failed XML Needs to Catch for Re-Processing XML END ----------");
			log.error("YFSException: " + yfe.getStackTrace());
			prepareErrorObject(yfe, XPXLiterals.UOM_B_TRANS_TYPE, XPXLiterals.YFE_ERROR_CLASS, env, inXML);	
			throw yfe;
		} catch (Exception e) {
			log.error("------------Failed XML Needs to Catch for Re-Processing XML START ----------");
			log.error(SCXmlUtil.getString(UOMElement1));
			log.error("------------Failed XML Needs to Catch for Re-Processing XML END ----------");
			log.error("Exception: " + e.getStackTrace());
			prepareErrorObject(e, XPXLiterals.UOM_B_TRANS_TYPE, XPXLiterals.E_ERROR_CLASS, env, inXML);
			throw e;
		}
		return inXML;	
    }
	
	
	/**@author asekhar-tw on 01-Feb-2011
	 * This method prepares the error object with the exception details which in turn will be used to log into CENT
	 */
	private void prepareErrorObject(Exception e, String transType, String errorClass, YFSEnvironment env, Document inXML){
		com.xpedx.nextgen.common.cent.Error errorObject = new com.xpedx.nextgen.common.cent.Error();	
		errorObject.setTransType(transType);
		errorObject.setErrorClass(errorClass);
		errorObject.setInputDoc(inXML);
		errorObject.setException(e);
		ErrorLogger.log(errorObject, env);
	}
	
	private Connection getDBConnection(YFSEnvironment env)
    {
		YFSConnectionHolder connHolder     = (YFSConnectionHolder)env;
        Connection m_Conn= connHolder.getDBConnection();
        return m_Conn;
    }
	private int fireQuery(YFSEnvironment env,String str, Document inXML) throws Exception
	{
		    Connection m_Conn   = getDBConnection(env);
	        Statement stmt = null;
	        int rSet = 0;
	        try
	        {
	       stmt =m_Conn.createStatement();
	        rSet = stmt.executeUpdate(str);	
	        	
	        	        
	        }
	        catch(Exception e)
	        {
	        	log.error("Exception: " + e.getStackTrace());
				prepareErrorObject(e, XPXLiterals.UOM_B_TRANS_TYPE, XPXLiterals.E_ERROR_CLASS, env, inXML);	
				rSet=0;
	        	return rSet;
	        }
	        	        
	     return rSet; 
	        	
	}
	private String getUniqueSequenceNo(YFSEnvironment env)
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        java.util.Date date = new java.util.Date();
        String datetime = dateFormat.format(date);
        long uniqueSequenceNo = CallDBSequence.getNextDBSequenceNo(env, XPXLiterals.SEQ_XPEDX_UOMSEQ);
        String Data=new Long(uniqueSequenceNo).toString();
       
        int l=(new Long(uniqueSequenceNo)).toString().length();
        for(int i=6;i!=l;i--)
        {
        	Data="0"+Data;
        }
        
        
        return (datetime+Data);
	}	
}

