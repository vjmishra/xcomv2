package com.xpedx.nextgen.dashboard;

import java.util.Properties;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.xpedx.nextgen.common.util.XPXLiterals;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.japi.YFSEnvironment;

public class XPXStampLinePocessCodes implements YIFCustomApi
{
	
	private static YFCLogCategory log;
	private static YIFApi api = null;

	static {
		log = YFCLogCategory.instance(XPXStampLinePocessCodes.class);
		try 
		{
			api = YIFClientFactory.getInstance().getApi();
		}
		catch (YIFClientCreationException e1) {

			e1.printStackTrace();
		}
	}
	
	public Document stampLineProcessCodes(YFSEnvironment env, Document inputXML)
	{
		
		if(inputXML != null){
			log.info("The input to stampLineProcessCodes in XPXStampLinePocessCodes is : " + SCXmlUtil.getString(inputXML));
		}
		Element inputDocRoot = inputXML.getDocumentElement();
		if(log.isDebugEnabled()){
			log.debug("StampLineProcessCodes_inputDocRoot :" + SCXmlUtil.getString(inputDocRoot));
		}
		
		
		Element orderLinesElement = (Element)inputDocRoot.getElementsByTagName(XPXLiterals.E_ORDER_LINES).item(0);
		
		NodeList orderLineList = orderLinesElement.getElementsByTagName(XPXLiterals.E_ORDER_LINE);
		
		for(int i=0; i<orderLineList.getLength(); i++)
		{
			Element orderLineElement = (Element)orderLineList.item(i);
			
			String lineProcessCode = orderLineElement.getAttribute(XPXLiterals.A_LINE_PROCESS_CODE);
			
			if(lineProcessCode == null || lineProcessCode.trim().length()== 0)
			{
				lineProcessCode = "A"; // Hard coding A for new line additions
				
				orderLineElement.setAttribute(XPXLiterals.A_LINE_PROCESS_CODE, lineProcessCode);
			}
			
		}
		
		//retrieve ExtnWebConfNum from inputDocRoot and set in txnobject
		Element extnElem = (Element) inputDocRoot.getElementsByTagName("Extn").item(0);
		String strExtnWebConfNum = extnElem.getAttribute("ExtnWebConfNum");
		env.setTxnObject("strExtnWebConfNum", strExtnWebConfNum);
		
		
		return inputXML;
	}

	public void setProperties(Properties arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
