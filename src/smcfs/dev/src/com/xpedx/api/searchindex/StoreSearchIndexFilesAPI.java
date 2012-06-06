package com.xpedx.api.searchindex;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.axis.encoding.Base64;
import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

//import com.labcorp.api.common.LCPropertyValueHelper;
//import com.labcorp.api.common.LCYIFCustomApi;
//import com.labcorp.axis.YIFWebServiceLocator;
//import com.labcorp.axis.Yantrawebservice_PortType;
//import com.labcorp.common.LabCorpUtil;
//import com.labcorp.constants.LabCorpConstants;
//import com.labcorp.logger.Logger;
import com.sterlingcommerce.simplifieddataaccess.QuickQuery;
import com.sterlingcommerce.simplifieddataaccess.SimpleXML;
import com.sterlingcommerce.util.searchindex.SearchIndexCleanUp;
import com.xpedx.api.common.XpedxPropertyValueHelper;
import com.xpedx.api.common.XpedxYIFCustomApi;
import com.xpedx.axis.Yantrawebservice_PortType;
import com.xpedx.common.XpedxUtil;
import com.xpedx.constants.XpedxConstants;

import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.dom.YFCNodeList;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfc.util.YFCCommon;
import com.yantra.yfs.core.YFSSystem;
import com.yantra.yfs.japi.YFSConnectionHolder;
import com.yantra.yfs.japi.YFSEnvironment;

/*
 * This is the API to store zipped latest complete search index folder (recursively) 
 * into the database.
 * 
 * Input Document is not needed or used
 * Output Document is null
 */

public class StoreSearchIndexFilesAPI extends XpedxYIFCustomApi implements XpedxConstants {

	private static final String STATUS_COMPLETE = "03";
	private static final String STATUS_ACTIVE = "04";
	//private static final Logger LOG = new Logger(StoreSearchIndexFilesAPI.class.getName());
	private static final YFCLogCategory LOG = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");
	private static YIFApi api = null;
	
	static {
		try 
		{
			api = YIFClientFactory.getInstance().getApi();
		}
		catch (YIFClientCreationException e1) {

			e1.printStackTrace();
		}
	}

	public Document invoke(YFSEnvironment env, Document inXML) throws Exception {

		LOG.debug("Start storing search index to database.");


		LOG.debug("Evaluating latest search index trigger.");
		//Here we check if the most recent search index trigger has a completed status
		YFCElement searchIndexTriggerElement = getLatestSearchIndex(env);
		if(searchIndexTriggerElement == null) {
			LOG.debug("Latest search index trigger is not completed yet.");
			return null;
		}
		LOG.debug("Completed search index trigger found.");
		LOG.debug("Latest search index element :\n"+searchIndexTriggerElement);
		String indexPath="";
		String searchIndexTriggerKey = "";

		//Getting the Search Index Root from yfs.properties_ysc_ext.in file
		String searchIndexRootDirectory = YFSSystem.getProperty("yfs.searchIndex.rootDirectory");
		LOG.debug("Search Index Root Directory :"+searchIndexRootDirectory);


		String searchIndexCompletePath="";
		if(searchIndexTriggerElement!=null){
			
			//Constructing the Complete Path
			searchIndexTriggerKey = searchIndexTriggerElement.getAttribute("SearchIndexTriggerKey");
			LOG.debug("searchIndexTriggerKey :"+searchIndexTriggerKey);
			indexPath = searchIndexTriggerElement.getAttribute("IndexPath");
			LOG.debug("indexPath :"+indexPath);
			searchIndexCompletePath=searchIndexRootDirectory+indexPath;
			LOG.debug("searchIndexCompletePath :"+searchIndexCompletePath);
			
			/*BEGIN: Make the search index copy, property driven.Return if there are no app servers configured in the property file*/
			String endPointURLs= XpedxPropertyValueHelper.getPropertyValue("xpedx.searhindex.serverURLs");
			if(YFCCommon.isVoid(endPointURLs)){
				LOG.debug("There are no app servers configured for for search index copy. The system will return gracefully.");
				//activateSearchIndex(env, searchIndexTriggerKey);
				return null;
			}
			/*END: Make the search index copy, property driven.Return if there are no app servers configured in the property file*/

			//Now we Zip & Store
			process(env, searchIndexCompletePath, searchIndexTriggerKey);

			//The next three lines seems confusing but it is only done to get the Path to the Folder that was recursively
			File zipFolder = new File(searchIndexCompletePath);
			int len = zipFolder.getAbsolutePath().lastIndexOf(File.separator);
			String baseName = zipFolder.getAbsolutePath().substring(0,len+1);

			LOG.debug("base name :"+baseName);

			//Now that the search index is stored in database, we call the app servers to pick it up
			LOG.debug("Calling App Servers to pick up search index.");
			if(copySearchIndexFiles(searchIndexTriggerKey,searchIndexCompletePath, baseName)){
				//LOG.debug("Activate search index it is after successfully picked up by all app servers");
				//activateSearchIndex(env, searchIndexTriggerKey);
				LOG.debug("Start cleaning up the temporary files and db tables");
				SearchIndexCleanUp.cleanUp(null,env);
				LOG.debug("Finished cleaning up the temporary files and db tables");
			}else{
				LOG.error("Error!! App Server(s) failed to pcik up the search index. Not Activating!!");
				//More Error Handling & Notification could be added here if desired.
			}

		}

		return null;
	}


	private YFCElement getLatestSearchIndex(YFSEnvironment env) throws Exception{
		YFCDocument inputDoc = YFCDocument.createDocument("SearchIndexTrigger");
		YFCElement inputDocElement = inputDoc.getDocumentElement();
		YFCElement inputAttributeElement = inputDocElement.createChild("OrderBy").createChild("Attribute");
		inputAttributeElement.setAttribute("Name", "TriggerTimestamp");
		inputAttributeElement.setAttribute("Desc", "Y");
		Document output = XpedxUtil.invokeAPI(env, "getSearchIndexTriggerList", inputDoc.getDocument());
		YFCElement searchIndexTriggerElement =null;
		if(output!=null){
			YFCDocument yfcOutput = YFCDocument.getDocumentFor(output);
			if(yfcOutput.getDocumentElement()!=null){
				YFCNodeList<YFCElement> list = yfcOutput.getDocumentElement().getElementsByTagName("SearchIndexTrigger");
				if(list!=null && list.getLength()>0)
					searchIndexTriggerElement = (YFCElement)list.item(0);
			}
			if(searchIndexTriggerElement!=null){
				String status = searchIndexTriggerElement.getAttribute("Status");
				LOG.debug("Status :"+status);
				String searchIndexTriggerKey = searchIndexTriggerElement.getAttribute("SearchIndexTriggerKey");
				LOG.debug("SearchIndexTriggerKey :"+searchIndexTriggerKey);
				if(status ==STATUS_COMPLETE){
					inputDoc = YFCDocument.createDocument("XPEDXSearchIndex");
					inputDocElement = inputDoc.getDocumentElement();
					inputDocElement.setAttribute("SearchIndexTriggerKey", searchIndexTriggerKey);
					output = XpedxUtil.invokeExtendedDbAPI(env, "getXPEDXSearchIndexList", inputDoc.getDocument());
					if(output==null || output.getDocumentElement()==null){
						return searchIndexTriggerElement;
					}
					else {
						Element tempElement = output.getDocumentElement();
						if(tempElement.getElementsByTagName("XPEDXSearchIndex")==null || tempElement.getElementsByTagName("XPEDXSearchIndex").getLength()<1)
							return searchIndexTriggerElement;
						else
							return null;
					}

				}else return null;
			}

		}
		return searchIndexTriggerElement;

	}

	private  void process(YFSEnvironment env,String searchIndexCompletePath,String searchIndexTriggerKey) throws Exception{
		zipSearchIndexFiles(searchIndexCompletePath);
		storeSearchIndexFiles(env,searchIndexCompletePath,searchIndexTriggerKey);
	}

	private  void zipSearchIndexFiles(String searchIndexCompletePath)throws Exception{  
		LOG.debug("Search index path :"+searchIndexCompletePath);
		String outFilename = searchIndexCompletePath +".zip";
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outFilename));
		File zipFolder = new File(searchIndexCompletePath);
		int len = zipFolder.getAbsolutePath().lastIndexOf(File.separator);
		String baseName = zipFolder.getAbsolutePath().substring(0,len+1);
		LOG.debug("Base Name :"+baseName);
		addFolderToZip(zipFolder, out, baseName);
		out.close();
	}

	private  void addFolderToZip(File folder, ZipOutputStream zip, String baseName) throws IOException {
		File[] files = folder.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				addFolderToZip(file, zip, baseName);
			} else {
				String name = file.getAbsolutePath().substring(baseName.length());
				ZipEntry zipEntry = new ZipEntry(name);
				zip.putNextEntry(zipEntry);
				IOUtils.copy(new FileInputStream(file), zip);
				zip.closeEntry();
			}
		}
	}

	private  void storeSearchIndexFiles(YFSEnvironment env,String indexPath,String searchIndexTriggerKey)throws Exception{

		String outFilename = indexPath +".zip";
		File file =new File(outFilename);
		InputStream is = new FileInputStream(file);
		long length = file.length();
		byte[] bytes = new byte[(int)length];

		LOG.debug("Length_storeSearchIndexFiles():"+length);
		int offset = 0;
		int numRead = 0;
		while ( (offset < bytes.length)
				&&
				( (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) ) {

			offset += numRead;
		}

		// Ensure all the bytes have been read in
		if (offset < bytes.length) {
			throw new IOException("Could not completely read file "+file.getName());
		}
		String searchIndexFilesContent = Base64.encode(bytes);
		storeFileContentsInDB(env, searchIndexFilesContent, searchIndexTriggerKey);
	}

	private void storeFileContentsInDB (YFSEnvironment env, String searchIndexFilesContent, String searchIndexTriggerKey) throws Exception{
		YFCDocument inputDoc = YFCDocument.createDocument("XPEDXSearchIndex");
		YFCElement inputDocElement = inputDoc.getDocumentElement();
		inputDocElement.setAttribute("SearchIndexTriggerKey", searchIndexTriggerKey);
		inputDocElement.setAttribute("ExtnSIFilesByteArray", searchIndexFilesContent);
		QuickQuery.executeDBApi(env, "createXPEDXSearchIndex", new SimpleXML(inputDoc), SimpleXML.getInstance("(XPEDXSearchIndex)"));

		/* We need to explicitly commit the transaction now into the database
		 * We cannot wait until the end of the service call (which is when it happens usually) because
		 * this service is about to call external app servers to pick up the data 
		 */
		getDBConnection(env).commit();
	}

	public  Connection getDBConnection(YFSEnvironment env){
		Connection conn = ((YFSConnectionHolder)env).getDBConnection();
		return conn;
	}


	private static boolean copySearchIndexFiles(String searchIndexTriggerKey,String indexPath,String baseName){
		boolean isFilesCopied = true;
		String userName="admin";
		String env = "<Environment userId=\"" + userName +"\" progId=\"SearchIndexCron\"><Templates><Template ApiName=\"copySearchIndexFiles\"><copySearchIndexFiles/></Template></Templates></Environment>";
		String input = "<CopySearchIndex SearchIndexTriggerKey=\""+searchIndexTriggerKey +"\" IndexPath=\""+indexPath +"\" BaseName=\""+baseName+"\" />";

		String endPointURLs= XpedxPropertyValueHelper.getPropertyValue("xpedx.searhindex.serverURLs");
		
		if(endPointURLs!=null && endPointURLs.trim().length()>0){
			String[] endPointURLsArr = endPointURLs.split(",");
			for(int i=0;i<endPointURLsArr.length;i++){
				if(endPointURLsArr[i]!=null && endPointURLsArr[i].trim().length()>0){
					String endPointURL = endPointURLsArr[i];
					LOG.debug("calling copySearchIndexFiles webservice to app server: "+endPointURL);
					try{
						com.xpedx.axis.YIFWebServiceLocator locator=new com.xpedx.axis.YIFWebServiceLocator();
						Yantrawebservice_PortType p= locator.getyantrawebservice(new URL(endPointURL.trim()));
						//Yantrawebservice p=                               locator.getyantrawebservice(new URL(endPointURL.trim()));
						
						SimpleXML response = SimpleXML.getInstance(p.copySearchIndexFiles(env, input));
						
						if("ApiSuccess".equals(response.getName())) {
							LOG.debug("Successfully copied the search index files to App Server at: "+endPointURL);
						} else {
							LOG.error("Error while copying search index files to App Server at: "+endPointURL);
							LOG.error("Response Received: "+response);
							isFilesCopied = false;
						}
					} catch (Exception e) {
						isFilesCopied =Boolean.FALSE;
						LOG.error("Error while copying search index files to App Server at: "+endPointURL);
						e.printStackTrace();
					}
				}
			}
		}
		return isFilesCopied;
	}

	private static void activateSearchIndex(YFSEnvironment env,String searchIndexTriggerKey) throws Exception{
		try{
			YFCDocument inputDoc = YFCDocument.createDocument("SearchIndexTrigger");
			YFCElement inputDocElement = inputDoc.getDocumentElement();
			inputDocElement.setAttribute("Operation", "Modify");
			inputDocElement.setAttribute("Status", STATUS_ACTIVE);
//			inputDocElement.setAttribute("OrganizationCode", "LABCORP");
//			inputDocElement.setAttribute("CategoryDomain", "LabcorpCatalog");
			inputDocElement.setAttribute("OrganizationCode", "xpedx");
			inputDocElement.setAttribute("CategoryDomain", YFSSystem.getProperty("xpedx.searhindex.categoryDomain"));
			
			inputDocElement.setAttribute("SearchIndexTriggerKey", searchIndexTriggerKey);
			//SimpleXML output = QuickQuery.executeDBApi(env, "manageSearchIndexTrigger", new SimpleXML(inputDoc), SimpleXML.getInstance("(SearchIndexTrigger)"));
			Document output = api.invoke(env, "manageSearchIndexTrigger", inputDoc.getDocument());
			
			LOG.debug("Sucessfully activated search index");
			LOG.debug("Output: "+output);
		} catch(Exception e){ 
			LOG.error("Error while activating the search index", e);
			e.printStackTrace();
			throw new Exception("Error while activating the search index: "+e.getMessage());
		}
	}
}
