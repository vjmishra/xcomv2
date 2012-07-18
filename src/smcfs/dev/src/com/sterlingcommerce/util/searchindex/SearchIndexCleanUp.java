package com.sterlingcommerce.util.searchindex;

import java.io.File;
import java.util.ArrayList;

import com.sterlingcommerce.simplifieddataaccess.QuickQuery;
import com.sterlingcommerce.simplifieddataaccess.SimpleXML;
import com.sterlingcommerce.ui.web.framework.context.SCUIContext;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.core.YFSSystem;
import com.yantra.yfs.japi.YFSEnvironment;

public class SearchIndexCleanUp {
	public static void cleanUp(SCUIContext ctx, YFSEnvironment env) {
		cleanUpZipFiles(ctx,env);
		cleanUpDBTable(ctx,env);
	}
	
	public static void cleanUpZipFiles(SCUIContext ctx, YFSEnvironment env) {
		String searchIndexRoot = YFSSystem.getProperty("yfs.searchIndex.rootDirectory");
		String categoryDomain = YFSSystem.getProperty("xpedx.searhindex.categoryDomain");
		String orgCode = YFSSystem.getProperty("xpedx.searhindex.SellerOrganizationCode");
		if (searchIndexRoot.endsWith(File.separator)) {
			searchIndexRoot = searchIndexRoot.substring(0, searchIndexRoot.length()-1);
		}
		File deepSearchIndexFolder = new File(searchIndexRoot+File.separator+"SearchIndex"+File.separator+orgCode+File.separator+orgCode+File.separator+categoryDomain);
		if(deepSearchIndexFolder.exists()){
			deleteAllZipFiles(deepSearchIndexFolder.getAbsolutePath());
		}
	}

	public static void cleanUpDBTable(SCUIContext ctx, YFSEnvironment env) {
		try {
			String orgCode = YFSSystem.getProperty("xpedx.searhindex.SellerOrganizationCode");
			SimpleXML inputXML = SimpleXML.getInstance("(SearchIndexTrigger)");
			inputXML.setAttribute("CallingOrganizationCode", orgCode);

			SimpleXML output;
			if (ctx!=null)
				output = QuickQuery.executeDBApi(ctx, "getXPEDXSearchIndexList", SimpleXML.getInstance("(XPEDXSearchIndex)"), SimpleXML.getInstance("(XPEDXSearchIndexList (XPEDXSearchIndex SearchIndexTriggerKey= ))"));
			else
				output = QuickQuery.executeDBApi(env, "getXPEDXSearchIndexList", SimpleXML.getInstance("(XPEDXSearchIndex)"), SimpleXML.getInstance("(XPEDXSearchIndexList (XPEDXSearchIndex SearchIndexTriggerKey= ))"));

			ArrayList triggers = output.getChildren("XPEDXSearchIndex");
			for (int i=0; i<triggers.size(); i++) {
				SimpleXML trigger = (SimpleXML)triggers.get(i);
				if (ctx!=null)
						QuickQuery.executeDBApi(ctx, "deleteXPEDXSearchIndex", trigger, SimpleXML.getInstance("(XPEDXSearchIndex)"));
					else
						QuickQuery.executeDBApi(env, "deleteXPEDXSearchIndex", trigger, SimpleXML.getInstance("(XPEDXSearchIndex)"));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void deleteAllZipFiles(String directory) {
	    ExtensionFilter filter = new ExtensionFilter(".zip");
	    File dir = new File(directory);
	    String[] list = dir.list(filter);
	    File file;
	    if (list.length == 0) {
	    	if(logger.isDebugEnabled()){
	    	logger.debug("No zip files in the directory: "+directory);
	    	}
	    	return;
	    }
	    for (int i = 0; i < list.length; i++) {
	      //file = new File(directory + list[i]);
	      file = new File(directory, list[i]);
	      logger.debug("Deleting file: "+file.getName());
	      boolean isFileDeleted = file.delete();
	      if(!isFileDeleted){
	    	  logger.error("Error deleting file: "+file.getName());
	      }
	    }
	}
	private static final YFCLogCategory logger = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");
}
