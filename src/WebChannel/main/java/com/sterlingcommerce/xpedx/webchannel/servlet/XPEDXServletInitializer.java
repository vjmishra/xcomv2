package com.sterlingcommerce.xpedx.webchannel.servlet;

import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXFileManager;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;
import com.yantra.yfs.core.YFSSystem;

/**
 * XPEDXServletInitializer class handles the application related initialization
 *
 */
public class XPEDXServletInitializer extends HttpServlet {

	private static final long serialVersionUID = -919975026455500181L;
	
	//private static final String HTML_PAGE_FILE_PATH_PROP = "promotions_shared_path";
	public static String SRVR_PATH_SEPARATOR = System.getProperty("file.separator");
	

  public synchronized void init(ServletConfig config) throws ServletException
   {
	  	String custOverridePropertiesFile = "customer_overrides.properties";
		XPEDXWCUtils.loadXPEDXSpecficPropertiesIntoYFS(custOverridePropertiesFile);
		String adJugglerKeyworPrefix = YFSSystem.getProperty(XPEDXConstants.AD_JUGGLER_KEYWORD_PREFIX_PROP);
		String adJugglerSuffix = YFSSystem.getProperty(XPEDXConstants.AD_JUGGLER_SUFFIX_PROP);
		
		String wcPropertiesFile = "xpedx_webchannel.properties";
		XPEDXWCUtils.loadXPEDXSpecficPropertiesIntoYFS(wcPropertiesFile);
		String promoPath = YFSSystem.getProperty(XPEDXConstants.DYN_PROMO_SHARED_PATH_PROP);
		
		
	//	try { dirListsting( promoPath ); } catch (RuntimeException re ) {re.printStackTrace(); } catch (Exception e) {e.printStackTrace();}

		
		try {
				
			
			logMessage(" ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			//String promoPath = massageFileName(promoBeforeMassage );
			logMessage("-AD-JUGLLER-PREFIX- Advertizement Keyword Prefix : " + adJugglerKeyworPrefix );
			logMessage("-AD-JUGLLER-PREFIX- Advertizement Keyword Suffix : " + adJugglerSuffix );
			logMessage("-DYN-PROMO- Marketing Promo Files Path : " + promoPath );
			logMessage("-DYN-PROMO- (XPEDXServletInitializer) : config.getServletContext : "  + config.getServletContext() );
		
			String wlRunTimeFile = config.getServletContext().getRealPath("test.xml");
			String wlRunTimeDir = wlRunTimeFile.substring(0, wlRunTimeFile.lastIndexOf(SRVR_PATH_SEPARATOR));
			
			logMessage("-DYN-PROMO- (XPEDXServletInitializer) : wlRunTimeFile : "  + wlRunTimeFile );
			logMessage("-DYN-PROMO- (XPEDXServletInitializer) : wlRunTimeDir : "  + wlRunTimeDir );
			
			//Copy 
			XPEDXFileManager.copyDirectory(promoPath, config.getServletContext(), true);
			logMessage(" ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
  }
  
  
  /**
   * 
   * @param msg
   */
  public static void logMessage(String msg) {
	   if ( XPEDXConstants.DEBUG_TRACE == true )
		   System.out.print(msg);
  }
  

  /**
   * Gives the list of DynPromo files. 
   * 
   * @param fname
   */
	private static void dirListsting(String dName) {
		File dir = new File(dName);
		String[] childrens = dir.list();
		
		if (childrens == null) {
			logMessage("-DYN-PROMO- Specified directory does not exist or is not a directory." + dName );
			System.exit(0);
		} else {
			for (int i = 0; i < childrens.length; i++) {
				String fileName = childrens[i];
				logMessage("-DYN-PROMO-  " + fileName);
			}
		}
	}
  
  
}
