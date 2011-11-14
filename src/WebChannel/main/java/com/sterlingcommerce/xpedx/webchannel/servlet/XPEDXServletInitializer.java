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
	  
		String wcPropertiesFile = "xpedx_webchannel.properties";
		XPEDXWCUtils.loadXPEDXSpecficPropertiesIntoYFS(wcPropertiesFile);
		//String promoBeforeMassage = YFSSystem.getProperty(XPEDXConstants.DYN_PROMO_SHARED_PATH_PROP);
		String promoPath = YFSSystem.getProperty(XPEDXConstants.DYN_PROMO_SHARED_PATH_PROP);
		
		
	//	System.out.println(" ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
	//	try { dirListsting( promoPath ); } catch (RuntimeException re ) {re.printStackTrace(); } catch (Exception e) {e.printStackTrace();}

		
		try {
				
			
			System.out.println(" ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			//String promoPath = massageFileName(promoBeforeMassage );
			System.out.println("-DYN-PROMO- Marketing Promo Files Path : " + promoPath );
			System.out.println("-DYN-PROMO- (XPEDXServletInitializer) : config.getServletContext : "  + config.getServletContext() );
		
			String wlRunTimeFile = config.getServletContext().getRealPath("test.xml");
			String wlRunTimeDir = wlRunTimeFile.substring(0, wlRunTimeFile.lastIndexOf(SRVR_PATH_SEPARATOR));
			
			System.out.println("-DYN-PROMO- (XPEDXServletInitializer) : wlRunTimeFile : "  + wlRunTimeFile );
			System.out.println("-DYN-PROMO- (XPEDXServletInitializer) : wlRunTimeDir : "  + wlRunTimeDir );
			
			//Copy 
			XPEDXFileManager.copyDirectory(promoPath, config.getServletContext(), true);
			System.out.println(" ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
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
			System.out.println("-DYN-PROMO- Specified directory does not exist or is not a directory." + dName );
			System.exit(0);
		} else {
			for (int i = 0; i < childrens.length; i++) {
				String fileName = childrens[i];
				System.out.println("-DYN-PROMO-  " + fileName);
			}
		}
	}
  
  
}
