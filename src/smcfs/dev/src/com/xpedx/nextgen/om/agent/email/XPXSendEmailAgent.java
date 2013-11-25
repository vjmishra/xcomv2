package com.xpedx.nextgen.om.agent.email;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.ycp.japi.util.YCPBaseAgent;
import com.yantra.yfc.core.YFCIterable;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfc.log.YFCLogUtil;
import com.yantra.yfc.util.YFCCommon;
import com.yantra.yfs.core.YFSSystem;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSException;

public class XPXSendEmailAgent extends YCPBaseAgent {
	private static YFCLogCategory log = YFCLogCategory.instance(XPXSendEmailAgent.class);
	private static Map<String, YFCElement> emailDefnMap=new HashMap<String, YFCElement>();

	public List getJobs(YFSEnvironment env, Document criteria, Document lastMessageCreated) {
		Document emailDetailsInQry=null;
		Document emailDetailsListOutDoc = null;
		List emailDetailsList=null;
		log.beginTimer("getJobs() in XPXSendEmailAgent class");
		try{
			YIFApi api = YIFClientFactory.getInstance().getLocalApi();
			if(emailDefnMap.size()==0)
			{
				populateEmailDefnMap(env, api);	
			}
			if (log.isDebugEnabled()) {
				log.debug("The input to getXPXEmailDetailsList is: " + SCXmlUtil.getString(emailDetailsInQry));
			}
			emailDetailsInQry=SCXmlUtil.createFromString("<XPXEmailDetails EmailRetryCount='-1' EmailRetryCountQryType='NE'><OrderBy><Attribute Desc='Y' Name='Createts'/></OrderBy></XPXEmailDetails>");
			emailDetailsListOutDoc = api.executeFlow(env, "getXPXEmailDetailsList", emailDetailsInQry);
			
			if (log.isDebugEnabled()) {
				log.debug(new StringBuilder("Inside XPXSendEmailAgent.getJobs() - Output document for Email Details list is ").append(YFCLogUtil.toString(emailDetailsListOutDoc)).toString());
			}
			
			emailDetailsList = getEmailDetailsList(env, emailDetailsListOutDoc);
		
		} 
		catch(Exception e){
			StringBuffer centException=new StringBuffer();
			if(e instanceof com.yantra.yfs.japi.YFSException) {
            	YFSException yfe = (YFSException)e;            	
            	centException.append("YFSException caught inside XPXSendEmailAgent.getJobs(). "+yfe.getErrorCode()).append(":").append(yfe.getErrorDescription()).append(":").append(yfe.getErrorUniqueId());
            	
            } else {
            	centException.append("Exception caught inside XPXSendEmailAgent.getJobs(). "+e);
            	 	
            }
			log.error(centException.toString());
			
		}
		
		if (log.isDebugEnabled()) {
			log.debug("Inside XPXSendEmailAgent.getJobs method - The length of emailDetailsList is : " + emailDetailsList.size());
		}
		log.endTimer("getJobs() in XPXSendEmailAgent class");
		return emailDetailsList;

	}
	
	@Override
	public void executeJob(YFSEnvironment env, Document emailDetailsInputDoc) throws Exception {

		log.beginTimer("executeJobs() in XPXSendEmailAgent class");
		Element emailDetailsElement = emailDetailsInputDoc.getDocumentElement();
		YIFApi api = YIFClientFactory.getInstance().getLocalApi();
		YFCElement emailDefnElement=null;
		String emailToAddresses="";
		StringBuffer errorMessage=null;
		if (log.isDebugEnabled()) {
			log.debug((new StringBuilder("executeJobs() XPXSendEmailAgent class:")).append(" Email Details input document is : ").append(YFCLogUtil.toString(emailDetailsInputDoc)).toString());
		}

		try {
			if(emailDefnMap.size()==0){
				populateEmailDefnMap(env, api);			
			}
			
			String emailType = emailDetailsElement.getAttribute("EmailType");			
			emailDefnElement = emailDefnMap.get(emailType);
			
			if(emailDefnElement==null){
				errorMessage=new StringBuffer();
				errorMessage.append("EmailType: ["+emailType+"] does not exist in the parent 'XPX_EMAIL_DEFN' table");
				throw new Exception(errorMessage.toString());
			}
						
			String emailXML = emailDetailsElement.getAttribute("EmailXML");
			if(YFCCommon.isVoid(emailXML)){
				errorMessage=new StringBuffer("Email XML not available to tranform and hence send an email.");
				throw new Exception(errorMessage.toString());
			}
			
			Element emailXMLElement=SCXmlUtil.createFromString(emailXML).getDocumentElement();
			
			String emailXSL=emailDefnElement.getAttribute("EmailXslPath");
			
			String emailFromAddress = emailDetailsElement.getAttribute("EmailFrom");		
			
			String emailToXPath = emailDefnElement.getAttribute("EmailToXPath");
			emailToAddresses = retrieveEmailIds(emailXMLElement, emailToXPath);
			
			String emailCCXPath = emailDefnElement.getAttribute("EmailCcXPath");
			String emailCCAddresses = retrieveEmailIds(emailXMLElement, emailCCXPath);
			
			String emailBCCXPath = emailDefnElement.getAttribute("EmailBccXPath");
			String emailBCCAddresses = retrieveEmailIds(emailXMLElement, emailBCCXPath);
			
			validateEmailAddresses(emailDetailsElement, emailFromAddress, emailToAddresses, emailCCAddresses, emailBCCAddresses);
			
			Address[] toAddresses = InternetAddress.parse(emailToAddresses);
			Address[] ccAddresses = InternetAddress.parse(emailCCAddresses);
		    Address[] bccAddresses = InternetAddress.parse(emailBCCAddresses);		
			
			String emailSubject = emailDetailsElement.getAttribute("EmailSubject");			
			String htmlMailContent = applyXsltTemplate(emailXSL, emailXMLElement);
			
			String smtpHost = YFSSystem.getProperty("EMailServer");
			if(YFCCommon.isVoid(smtpHost))
			{
				smtpHost = "smtp.ipaper.com";
			}
			
			sendEmail(env, api, emailFromAddress, toAddresses, ccAddresses, bccAddresses, emailSubject, htmlMailContent, smtpHost, emailDetailsElement);
			
			if (log.isDebugEnabled()) {
				log.debug(emailType+" sent successfully. Email Details Key is: ["+emailDetailsElement.getAttribute("EmailDetailsKey")+"].");
			}
			Document emailSuccessDoc = SCXmlUtil.createFromString("<XPXEmailDetails/>");
			Element emailSuccessEle = emailSuccessDoc.getDocumentElement();
			emailSuccessEle.setAttribute("EmailDetailsKey", emailDetailsElement.getAttribute("EmailDetailsKey"));
			emailSuccessEle.setAttribute("EmailRetryCount", "-1");
			emailSuccessEle.setAttribute("EmailErrorMessage", "EMAIL_SUCCESS");
			api.executeFlow(env, "changeXPXEmailDetails", emailSuccessDoc);
			
		} catch (Exception e) {
			errorMessage=new StringBuffer();
			Document emailExceptionDoc = SCXmlUtil.createFromString("<XPXEmailDetails/>");
			Element emailExceptionEle = emailExceptionDoc.getDocumentElement();
			String emailDetailsKey=emailDetailsElement.getAttribute("EmailDetailsKey");
			emailExceptionEle.setAttribute("EmailDetailsKey", emailDetailsKey);
			
			if (e instanceof AddressException)
			{
				AddressException adrEx=(AddressException)e;				
				errorMessage.append("AddressException message is : ").append(adrEx.getMessage());				
				emailExceptionEle.setAttribute("EmailRetryCount", "-1");				
				emailExceptionEle.setAttribute("EmailErrorMessage", errorMessage.toString());
				log.error("AddressException caught inside XPXSendEmailAgent.sendEmail(). "+errorMessage+". Email Details Key is :["+emailDetailsKey+"]."); 
			
			} else if (e instanceof MessagingException) 
			{
				if(e instanceof SendFailedException){
					SendFailedException sfe = (SendFailedException)e;
					emailExceptionEle.setAttribute("EmailRetryCount", "-1");
					
					errorMessage=new StringBuffer();
					errorMessage.append("SendFailedException message is : "+sfe.getMessage());
					errorMessage.append(" Valid Sent Addresses are : "+sfe.getValidSentAddresses());
					errorMessage.append(" Valid Unsent Addresses are : "+sfe.getValidUnsentAddresses());
					errorMessage.append(" Invalid Addresses are : "+sfe.getInvalidAddresses());
					emailExceptionEle.setAttribute("EmailErrorMessage", errorMessage.toString());
					log.error("SendFailedException caught, inside XPXSendEmailAgent.sendEmail(), while sending email. "+errorMessage.toString()+". Email Details Key is :["+emailDetailsKey+"].");
				
				}else 
				{
					String emailRetryCount=emailDetailsElement.getAttribute("EmailRetryCount");
					int intRetryCount=0;
					if(!YFCCommon.isVoid(emailRetryCount))
						intRetryCount = Integer.parseInt(emailRetryCount);
					
					MessagingException msgEx = (MessagingException)e;
					if(intRetryCount > -1 && intRetryCount < 9 )
					{
						emailExceptionEle.setAttribute("EmailRetryCount", String.valueOf(++intRetryCount));				
					
					} else {
						emailExceptionEle.setAttribute("EmailRetryCount", "-1");
					
					}
					errorMessage.append("MessagingException message is : "+msgEx.getMessage());
					emailExceptionEle.setAttribute("EmailErrorMessage", errorMessage.toString());
					log.error("MessagingException caught, inside XPXSendEmailAgent.sendEmail(), while sending email. "+errorMessage.toString()+". Email Details Key is :["+emailDetailsKey+"].");
				}				
				
			} else
			{
				errorMessage.append("Exception message is : "+e.getMessage());
				emailExceptionEle.setAttribute("EmailRetryCount", "-1");
				emailExceptionEle.setAttribute("EmailErrorMessage", errorMessage.toString());				
				log.error("Exception caught inside XPXSendEmailAgent.executeJob(). "+errorMessage.toString()+". Email Details Key is :["+emailDetailsKey+"].");
				
			}			
			
			api.executeFlow(env, "changeXPXEmailDetails", emailExceptionDoc);
			
		}

		log.endTimer("executeJobs()");
	}

	private List getEmailDetailsList(YFSEnvironment env, Document emailDetailsListOutDoc) throws Exception {
		NodeList nodeListEmails = emailDetailsListOutDoc.getDocumentElement().getElementsByTagName("XPXEmailDetails");
		List listOfJobs = new ArrayList();
		int emailDtlsNodeList = nodeListEmails.getLength();		
		for (int email= 0; email < emailDtlsNodeList; email++) {
			Element emailDtlElement = (Element) nodeListEmails.item(email);
			Document emailDetailDoc = SCXmlUtil.createFromString(SCXmlUtil.getString(emailDtlElement));
			listOfJobs.add(emailDetailDoc);

		}
		return listOfJobs;
	}

	private String applyXsltTemplate(String emailXSL, Element emailXMLElement) throws Exception {
		StringBuffer errorMessage=null;
		try {			
			Document emailXMLDoc = SCXmlUtil.createFromString(SCXmlUtil.getString(emailXMLElement));
			File emailXSLFile = new File(emailXSL);
			StringWriter writer = new StringWriter();
			PrintWriter out = new PrintWriter(writer);
			javax.xml.transform.Source xmlSource = new DOMSource(emailXMLDoc);
			javax.xml.transform.Source xsltSource = new javax.xml.transform.stream.StreamSource(emailXSLFile);
			javax.xml.transform.Result result = new javax.xml.transform.stream.StreamResult(out);
			javax.xml.transform.TransformerFactory transFact = javax.xml.transform.TransformerFactory.newInstance();
			try {
				Transformer trans = transFact.newTransformer(xsltSource);			
				trans.transform(xmlSource, result);
			
			} catch (TransformerException trnsFrmrEx){
				errorMessage=new StringBuffer();
				errorMessage.append("TransformerException occured, in XPXSendEmailAgent.applyXsltTemplate method, while transforming email XML. ");
				errorMessage.append("TransformerException message is "+trnsFrmrEx.getMessage()+". ");
				errorMessage.append("Email Details Key is: "+emailXMLElement.getAttribute("EmailDetailsKey"));
				log.error(errorMessage);
				trnsFrmrEx.printStackTrace();
				throw trnsFrmrEx;
			}
			String htmlMailString = writer.toString();
			
			return htmlMailString;

		} catch (Exception e) {
			errorMessage=new StringBuffer();
			errorMessage.append("Exception occured in XPXSendEmailAgent.applyXsltTemplate method. Exception message is "+e.getMessage());
			log.error(errorMessage);
			e.printStackTrace();
			throw e;
		}

	}

	private static void sendEmail(YFSEnvironment env, YIFApi api, String from, Address[] toAddresses, Address[] ccAddresses, 
								  Address[] bccAddresses, String subject, String content, String smtpHost, Element emailDetailsElement) throws Exception
	{		
		Properties props = new Properties();

        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.class", "com.sun.mail.smtp.SMTPTransport");
        props.put("mail.smtp.host", smtpHost);        
        props.put("mail.smtp.sendpartial", "true");
        //email logging issue
        if(YFSSystem.getProperty("mail.debug")!=null)
        {
        	props.put("mail.debug", YFSSystem.getProperty("mail.debug"));
        
        } else 
        {
        	props.put("mail.debug", "false");
        }
		
        Session emailSession = Session.getDefaultInstance(props);
		
		Message emailMessage = new MimeMessage(emailSession);
		
		if(toAddresses!=null && toAddresses.length>0)			
			emailMessage.setRecipients(Message.RecipientType.TO, toAddresses);
		
		if(ccAddresses!=null && ccAddresses.length>0)
			emailMessage.setRecipients(Message.RecipientType.CC, ccAddresses);
		
		if(bccAddresses!=null && bccAddresses.length>0)
			emailMessage.setRecipients(Message.RecipientType.BCC, bccAddresses);
		
		String emltencoding = null;
        emltencoding = YFSSystem.getProperty("yfs.email.template.encoding");
        if (YFCCommon.isVoid(emltencoding)) {
          emltencoding = "UTF-8";
        }
        
		emailMessage.setFrom(new InternetAddress(from));
		emailMessage.setSubject(subject);
		emailMessage.setContent(content, "text/plain");
		//emailMessage.setContent(content,"text/html;charset=" + emltencoding);
		emailMessage.setSentDate(new java.util.Date());
		//email logging issue
		//emailSession.setDebug(true);
		Transport.send(emailMessage);		
	}
	
	private String retrieveEmailIds(Element emailXMLElement, String emailXPath) throws Exception {
		StringBuffer emailIdBuf=new StringBuffer("");
		if(emailXPath==null || 	"".equals(emailXPath)){
			return emailIdBuf.toString();
		}
			
		String [] emailIdArr = emailXPath.split(",");
		
		if(emailIdArr.length==0)
		{
			emailIdBuf.append(SCXmlUtil.getXpathAttribute(emailXMLElement, emailXPath));
		
		} else {
			for(int i=0;i<emailIdArr.length;i++)
			{
				if(i==(emailIdArr.length-1))		
				{
					emailIdBuf.append(SCXmlUtil.getXpathAttribute(emailXMLElement, emailIdArr[i]));
				
				} else {
					String emailId = SCXmlUtil.getXpathAttribute(emailXMLElement, emailIdArr[i]);
					if(!"".equals(emailId))
						emailIdBuf.append(emailId).append(",");
				
				}
			}
		}
		
		String emailIds=emailIdBuf.toString();
		if (emailIds.indexOf(";") > -1) {
			emailIds = emailIds.replace(";", ",");
		}
		
		return emailIds;
	}
	
	private void populateEmailDefnMap(YFSEnvironment env, YIFApi api) throws YFSException, RemoteException
	{
		env.setApiTemplate("getXPXEmailDefnList", "<XPXEmailDefnList><XPXEmailDefn/></XPXEmailDefnList>");			
		Document emailDefnInQry = SCXmlUtil.createFromString("<XPXEmailDefn/>");
		if (log.isDebugEnabled()) {
			log.debug("Inside XPXSendEmailAgent.populateEmailDefnMap(). The input to getXPXEmailDefnList is: " + SCXmlUtil.getString(emailDefnInQry));
		}
		
		Document emailDefnListOutDoc = api.executeFlow(env, "getXPXEmailDefnList", emailDefnInQry);
		env.clearApiTemplate("getXPXEmailDefnList");
		YFCDocument yfcEmailDefnListDoc = YFCDocument.getDocumentFor(SCXmlUtil.getString(emailDefnListOutDoc));
		YFCIterable<YFCElement> emailDefnElements = yfcEmailDefnListDoc.getDocumentElement().getChildren("XPXEmailDefn");
		while (emailDefnElements.hasNext()) {
		   YFCElement emailDefnEle = emailDefnElements.next();
		   if (emailDefnEle != null) {
			   String emailType = emailDefnEle.getAttribute("EmailType");
			   if(!emailDefnMap.containsKey(emailType))
				   emailDefnMap.put(emailType, emailDefnEle);
		   }
		}
	}
	
	private void validateEmailAddresses(Element emailDetailsElement, String emailFromAddress, String emailToAddresses, String emailCCAddresses, String emailBCCAddresses) throws AddressException
	{
		if(YFCCommon.isVoid(emailFromAddress))
		{
			throw new AddressException("'From' Email address is blank and so there's no need to hit SMTP email server.");				
			
		} else if(YFCCommon.isVoid(emailToAddresses) && YFCCommon.isVoid(emailCCAddresses) && YFCCommon.isVoid(emailBCCAddresses))
		{
			throw new AddressException("Email addresses : 'To', 'CC' and 'BCC' are blank and so there's no need to hit SMTP email server.");
		
		}		
	}
	
}