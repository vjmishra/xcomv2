package com.xpedx.nextgen.dataload.api;

import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.Properties;

import org.w3c.dom.Document;

import com.xpedx.nextgen.common.cent.ErrorLogger;
import com.xpedx.nextgen.common.util.XPXLiterals;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.japi.YFSConnectionHolder;
import com.yantra.yfs.japi.YFSEnvironment;



public class XPXInventoryIndicatorUpdate {

	private static YIFApi api = null;
	private static YFCLogCategory log;

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

	private void prepareErrorObject(Exception e, String transType, String errorClass, YFSEnvironment env,Document inXML){
		com.xpedx.nextgen.common.cent.Error errorObject = new com.xpedx.nextgen.common.cent.Error();	
		errorObject.setTransType(transType);
		errorObject.setErrorClass(errorClass);
		errorObject.setInputDoc(inXML);
		errorObject.setException(e);
		ErrorLogger.log(errorObject, env);
	}

	private Connection getDBConnection(YFSEnvironment env,Document inputXML)
	{
		Connection m_Conn=null;
		try{
			YFSConnectionHolder connHolder     = (YFSConnectionHolder)env;
			m_Conn= connHolder.getDBConnection();
		}
		catch(Exception e){

			log.error("Exception: " + e.getStackTrace());
//			prepareErrorObject(e, "Item_Branch", XPXLiterals.E_ERROR_CLASS, env,inputXML);	

		}
		finally
		{
			try {
				m_Conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return m_Conn;
	}

	public  Document updateInventoryIndicator(YFSEnvironment env,Document inputXML) throws Exception
	{
		Connection m_Conn   = getDBConnection(env,inputXML);
		CallableStatement cstmt = null;
		int rSet = 0;

		try
		{   log.info("Start of Method updateInventoryIndicator in class XPXInventoryIndicatorUpdate");
			long startTime = System.currentTimeMillis();
			String query="{call INVENTORY_UPDATE()}";

			cstmt =m_Conn.prepareCall(query);
			rSet  = cstmt.executeUpdate();
			if(log.isDebugEnabled()){
				log.debug("Update Value after inventory update is :"+rSet);
			}
			m_Conn.commit();
			long endTime = System.currentTimeMillis();
			log.info("Completed Update InventoryIndicator in  " + (endTime - startTime) + " milliseconds");
			  log.info("End of Method updateInventoryIndicator in class XPXInventoryIndicatorUpdate");

		}
		catch(Exception e)
		{
			e.printStackTrace();
			log.error("Exception: " + e.getStackTrace());

			//prepareErrorObject(e, "Item_Branch", XPXLiterals.E_ERROR_CLASS, env,inputXML);	


		}finally{
		//	cstmt.close();
		//	m_Conn.close();
		}
		return inputXML;
	}
}