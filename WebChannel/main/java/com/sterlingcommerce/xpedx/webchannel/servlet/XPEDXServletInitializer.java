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

  public synchronized void init(ServletConfig config) throws ServletException
   {
	  
		String wcPropertiesFile = "xpedx_webchannel.properties";
		XPEDXWCUtils.loadXPEDXSpecficPropertiesIntoYFS(wcPropertiesFile);
		String promoPath = YFSSystem.getProperty(XPEDXConstants.DYN_PROMO_SHARED_PATH_PROP);
		
		try {
		System.out.println(" ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		System.out.println("-DYN-PROMO- Marketing Promo Files Path : " + promoPath );
		System.out.println("-DYN-PROMO- (XPEDXServletInitializer) : config.getServletContext : "  + config.getServletContext() );
		System.out.println(" ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		//Copy 
		XPEDXFileManager.copyDirectory(promoPath, config.getServletContext(), false);
		} catch (IOException e) {
			e.printStackTrace();
		}
  }
}
