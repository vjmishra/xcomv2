package com.sterlingcommerce.xpedx.webchannel.MyItems;

import java.util.Map;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sterlingcommerce.webchannel.core.WCMashupAction;
import com.sterlingcommerce.webchannel.utilities.XMLUtilities;

@SuppressWarnings("serial")
public class XPEDXMyItemsListCreateAction extends WCMashupAction {
	
	private static final Logger LOG = Logger.getLogger(XPEDXMyItemsListCreateAction.class);
	private Document outDoc;
	
	private String name 		= "";
	private String desc 		= "";
	private String customerId	= "";
	private String createdBy 	= "";
	private String lastModified = "";
	
	@Override
	public String execute() {
		try {
			Map<String, Element> out;
			
			out = prepareAndInvokeMashups();
			outDoc = (Document)out.values().iterator().next().getOwnerDocument();
			
			LOG.debug("RESULTS: " + XMLUtilities.getXMLDocString(outDoc));
			
		} catch (Exception e) {
			LOG.error(e.getStackTrace());
			return ERROR;
		}
		return SUCCESS;
	}

	public Document getOutDoc() {
		return outDoc;
	}

	public void setOutDoc(Document outDoc) {
		this.outDoc = outDoc;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getLastModified() {
		return lastModified;
	}

	public void setLastModified(String lastModified) {
		this.lastModified = lastModified;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

}
