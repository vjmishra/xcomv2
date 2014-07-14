package com.xpedx.api.searchindex;



import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;

import com.lowagie.text.pdf.codec.Base64;
import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.simplifieddataaccess.SimpleXML;
import com.xpedx.api.common.XpedxYIFCustomApi;
import com.xpedx.common.XpedxUtil;
import com.xpedx.constants.XpedxConstants;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.dom.YFCNodeList;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.japi.YFSEnvironment;

public class CopySearchIndexAPI extends XpedxYIFCustomApi implements XpedxConstants {
//	private static final YFCLogCategory LOG = YFCLogCategory.instance(CopySearchIndexAPI.class.getName());
//	private static final Logger LOG = new Logger(CopySearchIndexAPI.class.getName());
	private static final YFCLogCategory LOG =(YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");

	/**
	 * ServiceName = copysearchindex
	 * 
	 * @param env
	 * @param inXML Looks like so <CopySearchIndex SearchIndexTriggerKey="" IndexPath="" BaseName=""></CopySearchIndex>
	 * @return
	 * @throws Exception
	 */
	public Document invoke(YFSEnvironment env, Document inXML) throws Exception {
		LOG.debug("Input recieved for CopySearchIndexAPI  :"+ SCXmlUtil.getString(inXML));
		//String activePath = SearchIndexCleanUp.getActiveSearchIndexPath(null,env);
		
		SimpleXML output = SimpleXML.getInstance("(ApiSuccess)");
		YFCDocument inXMLDoc = YFCDocument.getDocumentFor(inXML);
		YFCElement inXMLDocElement = inXMLDoc.getDocumentElement();

		String searchIndexTriggerKey = inXMLDocElement.getAttribute("SearchIndexTriggerKey");
		LOG.debug("Search Index Trigger key: "+searchIndexTriggerKey);
		String indexPath = inXMLDocElement.getAttribute("IndexPath");
		LOG.debug("Index Path: "+indexPath);
		String baseName = inXMLDocElement.getAttribute("BaseName");
		LOG.debug("base Path: "+baseName);

		LOG.debug("Getting search index file from the database");
		String searchIndexFilesContent = getFileContentsFromDB(env, searchIndexTriggerKey);
		if (searchIndexFilesContent!=null) {
			createSearchIndexDiectory(indexPath+"/"+"en_US");
			copySearchIndexFiles(searchIndexFilesContent,baseName);			
		} else {
			throw new Exception("Unable to retrieve search index file from the database");
		}
		
		//If we reach this point, the call is successful, otherwise Exception must have been thrown
		return output.getDOMDocument();
	}

	private String getFileContentsFromDB(YFSEnvironment env,String searchIndexTriggerKey) throws Exception{
//		YFCDocument inputDoc = YFCDocument.createDocument("LCSearchIndex");
		YFCDocument inputDoc = YFCDocument.createDocument("XPEDXSearchIndex");
		YFCElement inputDocElement = inputDoc.getDocumentElement();
		inputDocElement.setAttribute("SearchIndexTriggerKey", searchIndexTriggerKey);
		Document output = XpedxUtil.invokeExtendedDbAPI(env, "getXPEDXSearchIndexList", inputDoc.getDocument());
		YFCDocument yfsOutputDoc = YFCDocument.getDocumentFor(output); 
		String searchIndexFilesContent=null;
		if(yfsOutputDoc!=null && yfsOutputDoc.getDocumentElement()!=null){
			YFCNodeList<YFCElement> list = yfsOutputDoc.getDocumentElement().getElementsByTagName("XPEDXSearchIndex");
			if(list!=null && list.getLength()>0){
				YFCElement lcSearchIndexElement  = (YFCElement)list.item(0);
				searchIndexFilesContent = lcSearchIndexElement.getAttribute("ExtnSIFilesByteArray");
			}
		}
		return searchIndexFilesContent;
	}

	private static void copySearchIndexFiles(String searchIndexFilesContent,String baseName) throws Exception{
		InputStream is1 = new ByteArrayInputStream(Base64.decode(searchIndexFilesContent));

		ZipInputStream zis = new ZipInputStream(is1);
		ZipEntry entry;
		int BUFFER=1024;
		BufferedOutputStream out = null;

		while ((entry = zis.getNextEntry()) != null){
			if(	entry.isDirectory()) {
				// Assume directories are stored parents first then children.
				LOG.error("Extracting directory: " + entry.getName());
				(new File(entry.getName())).mkdir();
				continue;

			}
			int count;
			byte data[] = new byte[BUFFER];
			// write the files to the disk
			out = new BufferedOutputStream(
					new FileOutputStream(baseName+entry.getName()),BUFFER);
			while ((count = zis.read(data,0,BUFFER)) != -1) {
				out.write(data,0,count);
			}
			out.close();
			LOG.debug("entry: " + entry.getName() + ", " + entry.getSize());
		}
	}

	private static void createSearchIndexDiectory(String directoryStructure) throws Exception {
		if(!(new File(directoryStructure).mkdirs())){
			LOG.debug("Directory already exists");
		}
	}
}
