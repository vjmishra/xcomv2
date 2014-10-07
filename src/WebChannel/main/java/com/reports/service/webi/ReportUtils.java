package com.reports.service.webi;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

//import com.linar.jintegra.Log;
import com.reports.service.Report;
import com.sterlingcommerce.xpedx.webchannel.services.XPEDXGetAllReportsAction;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;
import com.yantra.yfs.core.YFSSystem;

//ML: BI4 changes.
import javax.servlet.http.HttpSession;

/*
 * This utility class is used to connect to the BI4 server, query the CMS server and retrieve report documents.
 * 
 * 
 */
public class ReportUtils {

	String msgStr = "";
	private static final Logger log = Logger.getLogger(XPEDXGetAllReportsAction.class);

	/*
	 * getCMSLogonDetails returns the logon settings for the BI server specified in xpedx_reporting.properties
	 */
	public static Map<String, String> getCMSLogonDetails() {
		Map<String, String> logonMap = new HashMap<String, String>();

		String wcPropertiesFile = "xpedx_reporting.properties";
		XPEDXWCUtils.loadXPEDXSpecficPropertiesIntoYFS(wcPropertiesFile);
		// Retrieve the logon information
		logonMap.put("username", YFSSystem.getProperty("username"));
		logonMap.put("password", YFSSystem.getProperty("password"));
		logonMap.put("CMS", YFSSystem.getProperty("CMS"));
		logonMap.put("authentication", YFSSystem.getProperty("authentication"));
		logonMap.put("standard_folder_id",
				YFSSystem.getProperty("standard_folder_id"));
		logonMap.put("custom_folder_id",
				YFSSystem.getProperty("custom_folder_id"));


		return logonMap;

	}

	/*
	 * loadInputStream reads an input stream and returns it as a string. 
	 * 
	 */
	public String loadInputStream(InputStream inputStream) throws Exception {
		byte[] buffer = new byte[1024];
		String sFinal = "";
		int bytesRead = 0;
		BufferedInputStream bis = new BufferedInputStream(inputStream);
		try {
			while ((bytesRead = bis.read(buffer)) != -1) {
				sFinal = sFinal + new String(buffer, 0, bytesRead);
			}
		} finally {
			bis.close();
			msgStr = sFinal;
		}
		return sFinal;
	}

	/*
	 * getOpenDocURL returns the URI of the open document specified by the JSONObject obj.
	 */
	public String getOpenDocURL(JSONObject obj) {
		String sReturn = "";
		JSONObject subobj = (JSONObject) obj.get("openDocument");
		subobj = (JSONObject) subobj.get("__deferred");
		sReturn = subobj.get("uri").toString();
		return sReturn;
	}

	/*
	 * getAllReports returns an arraylist of report objects from/after parsing the JSONObject obj.
	 * 
	 */
	public List<Report> getAllReports(JSONObject obj) throws Exception {
		List<Report> reportList = new ArrayList<Report>();
		JSONArray document = (JSONArray) obj.get("entries");
		Iterator<JSONObject> documentIterator = document.iterator();
		while (documentIterator.hasNext()) {
			Report reportObj = new Report();
			JSONObject jsonDoc = (JSONObject) documentIterator.next();
			Long id = (Long) jsonDoc.get("id");
			reportObj.setId(id.intValue());
			reportObj.setCuid((String) jsonDoc.get("cuid"));
			reportObj.setDescription((String) jsonDoc.get("description"));
			reportObj.setKind("Webi");
			reportObj.setName((String) jsonDoc.get("name"));
			reportList.add(reportObj);
		}

		return reportList;
	}

	/*
	 * getParams returns a list of parameters as a string from the report specified in the JSONObject.
	 * 
	 */
	public String getParams(JSONObject obj, Map<String, String> paramVals)
			throws Exception {
		String sResult = "";
		JSONObject paramObj = (JSONObject) obj.get("parameters");
		if (paramObj != null) {
			JSONArray param = (JSONArray) paramObj.get("parameter");
			Iterator<JSONObject> paramIterator = param.iterator();
			JSONObject answerObj;
			JSONObject infoObj;
			String cardinality;
			String paramName;
			String paramNameModified = "";
			String optionalParam;
			String tmp = "";
			while (paramIterator.hasNext()) {
				paramObj = (JSONObject) paramIterator.next();
				paramName = (String) paramObj.get("name");
				optionalParam = (String) paramObj.get("@optional");
				answerObj = (JSONObject) paramObj.get("answer");
				infoObj = (JSONObject) answerObj.get("info");
				cardinality = (String) infoObj.get("@cardinality");

				if (paramVals.containsKey(paramName)) {
					//
					// set up the parameters that have a value
					//
					// TODO: if answerObj.get("@constrained") = "true", validate
					// parameter values against
					// the values in
					// answerObj.get("lov").get("values").get("value") array to
					// verify
					// that the value is allowed.
					//
					if ("single".equalsIgnoreCase(cardinality)) {
						if (paramVals.get(paramName) != null && !("").equals(paramVals.get(paramName))) {
							if (paramName.contains(" ")) {
								paramNameModified = paramName.replace(" ", "+");
							} else {
								paramNameModified = paramName;
							}
							tmp = "&lsS" + paramNameModified + "="
									+ paramVals.get(paramName);
							sResult += tmp;
						} 												
					} else if ("multiple".equalsIgnoreCase(cardinality)) {
						if (paramVals.get(paramName) != null && !("").equals(paramVals.get(paramName))) {
							if (paramName.contains(" ")) {
								paramNameModified = paramName.replace(" ", "+");
							}else {
								paramNameModified = paramName;
							}
							tmp = "&lsM" + paramNameModified + "="
									+ paramVals.get(paramName);
							sResult += tmp;
						}												
					}
				} 				
			}

		}
		return sResult;
	}

	/*
	 * makes an http post to a server.
	 * 
	 */
	public HttpHost getHttpHost(String sServer) {

		String[] cms = sServer.split("\\:");
		String sPort = "6405";
		if (cms.length > 1) {
			sServer = cms[0];
		}

		HttpHost target = new HttpHost(sServer, Integer.parseInt(sPort), "http");
		return target;
	}

	/*
	 * logonCMS logs onto the CMS server using the authentication model specified and returns a logonToken. 
	 * NOTE/WARNING: Encoding the token might cause the new sessions to require new logons again. 
	 * TO DO: FIND OUT IF TOKEN IS GETTING PASSED CORRECTLY ON SUBSEQUENT CALLS.
	 */
	public ArrayList<String> logonCMS(String sUserName, String sPassword,
			String sAuth, HttpHost target) throws Exception {
		final DefaultHttpClient httpClient = new DefaultHttpClient();
		ArrayList<String> result = new ArrayList<String>();
		try {
			// NOTE: We could get the JSON template for logon by sending a
			// request to the same URL that is
			// used for the POST below. However, this is a very simple JSON
			// object with just
			// three elements, so it's faster to just create it.

			//
			// post preparation
			//
			HttpPost httpPostRequest = new HttpPost("/biprws/logon/long");
			httpPostRequest.addHeader("Accept", "application/JSON");
			httpPostRequest.addHeader("Content-Type", "application/JSON");

			//
			// Set up the JSON object to send
			//
			JSONObject obj = new JSONObject();
			obj.put("userName", sUserName);
			obj.put("password", sPassword);
			obj.put("auth", sAuth);
			StringEntity sEntity = new StringEntity(obj.toJSONString());
			sEntity.setContentType("application/JSON");
			httpPostRequest.setEntity(sEntity);

			//
			// Send the Post.
			//
			HttpResponse httpResponse = httpClient.execute(target,
					httpPostRequest);

			String tmp = httpResponse.getStatusLine().toString();
			if (tmp.equals("HTTP/1.1 200 OK")) {
				//
				// Get X-SAP-LogonToken from header - we need this for future
				// calls to the web service
				//
				Header[] hdrs = httpResponse.getAllHeaders();
				String logonToken = "";
				for (Header hdr : hdrs) {
					if (hdr.getName().equals("X-SAP-LogonToken")) {
						logonToken = hdr.getValue();
					}
				}
				result.add(logonToken); // Header token

				//
				// Get the other token from the body - we'll encode this and use
				// it for the OpenDoc call
				//
				HttpEntity hEntity = httpResponse.getEntity();

				InputStream inputStream = hEntity.getContent();
				tmp = loadInputStream(inputStream);
				JSONParser parser = new JSONParser();
				obj = (JSONObject) parser.parse(tmp);
				String encodedToken = obj.get("logonToken").toString();
				encodedToken = URLEncoder.encode(encodedToken, "UTF-8");
				result.add(encodedToken); // encoded token for OpenDocument
											// call;
				return result;
			}
		}
		catch (Exception e) {
			log.debug("in logonCMS() function. Could not connect to target BI server due to:->" + e.getMessage());
			}
		finally {
			// When HttpClient instance is no longer needed,
			// shut down the connection manager to ensure
			// immediate deallocation of all system resources
			httpClient.getConnectionManager().shutdown();
		}
		return result;
	}

	/*
	 * getOpenDoc makes a call to the BI server and opens the document specified by docID using HttpPost and the specified token.
	 */
	public String getOpenDoc(String docId, HttpHost target, String token)
			throws Exception {
		final DefaultHttpClient httpClient = new DefaultHttpClient();
		String sReturn = "";
		try {
			HttpGet httpGetRequest = new HttpGet("/biprws/infostore/" + docId);
			httpGetRequest.addHeader("Accept", "application/JSON");
			httpGetRequest.addHeader("X-SAP-LogonToken", token);
			HttpResponse httpResponse = httpClient.execute(target,
					httpGetRequest);
			String tmp = httpResponse.getStatusLine().toString();
			if (tmp.equals("HTTP/1.1 200 OK")) {
				HttpEntity hEntity = httpResponse.getEntity();
				String sFinal = "";
				if (hEntity != null) {
					InputStream inputStream = hEntity.getContent();
					sFinal = loadInputStream(inputStream);
					JSONParser parser = new JSONParser();
					JSONObject obj = (JSONObject) parser.parse(sFinal);
					sReturn = getOpenDocURL(obj);
				}
			}
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
		return sReturn;
	}

	/*
	 *  getParamString returns a list of parameters for the specified report/document (docID) as a string. 
	 */
	public String getParamString(String docID, HttpHost target, String token,
			Map<String, String> paramVals) throws Exception {
		final DefaultHttpClient httpClient = new DefaultHttpClient();
		String sReturn = "";
		try {
			HttpGet httpGetRequest = new HttpGet(
					"/biprws/raylight/v1/documents/" + docID + "/parameters");
			httpGetRequest.addHeader("Accept", "application/JSON");
			httpGetRequest.addHeader("X-SAP-LogonToken", token);
			HttpResponse httpResponse = httpClient.execute(target,
					httpGetRequest);
			String tmp = httpResponse.getStatusLine().toString();
			if (tmp.equals("HTTP/1.1 200 OK")) {
				HttpEntity hEntity = httpResponse.getEntity();
				String sFinal = "";
				if (hEntity != null) {
					InputStream inputStream = hEntity.getContent();
					sFinal = loadInputStream(inputStream);
					JSONParser parser = new JSONParser();
					JSONObject obj = (JSONObject) parser.parse(sFinal);
					sReturn = getParams(obj, paramVals);
				}
			}
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
		return sReturn;
	}

	/*
	 * getPromptsAsString connects to BI using the specified token, and returns the report(docID) parameters as a plain string. 
	 * relies on getPrompts() to do the parsing of the JSONObject. 
	 */
	public List<String> getPromptsAsString(String docID, HttpHost target,
			String token) throws Exception {
		final DefaultHttpClient httpClient = new DefaultHttpClient();
		List<String> promptList = null;
		try {
 			HttpGet httpGetRequest = new HttpGet(
					"/biprws/raylight/v1/documents/" + docID + "/parameters");
			httpGetRequest.addHeader("Accept", "application/JSON");
			httpGetRequest.addHeader("X-SAP-LogonToken", token);
			HttpResponse httpResponse = httpClient.execute(target,
					httpGetRequest);
			String tmp = httpResponse.getStatusLine().toString();
			if (tmp.equals("HTTP/1.1 200 OK")) {
				HttpEntity hEntity = httpResponse.getEntity();
				String sFinal = "";
				if (hEntity != null) {
					InputStream inputStream = hEntity.getContent();
					sFinal = loadInputStream(inputStream);
					JSONParser parser = new JSONParser();
					JSONObject obj = (JSONObject) parser.parse(sFinal);
					promptList = getPrompts(obj);
				}
			}
		} finally {
						
			//finally shutdown client.
			httpClient.getConnectionManager().shutdown();
		}
		return promptList;
	}

	/*
	 * getPrompts is a utility function that parses a JSONOjbect for "parameters" and retrieves their name property, 
	 * stores and returns them in an ArrayList of strings.
	 */
	public List<String> getPrompts(JSONObject obj) {
		List<String> promptList = new ArrayList<String>();

		JSONObject paramObj = (JSONObject) obj.get("parameters");
		if (paramObj != null) {
			JSONArray param = (JSONArray) paramObj.get("parameter");
			Iterator<JSONObject> paramIterator = param.iterator();
			while (paramIterator.hasNext()) {
				paramObj = (JSONObject) paramIterator.next();
				promptList.add((String) paramObj.get("name"));
			}
		}
		return promptList;
	}

	/*
	 * getAllDocuments logs onto the BI server using the previously retrieved token and retries all the reports in the specified folder (folderId) 
	 * and returns them as a List object. 
	 */
	public List<Report> getAllDocuments(HttpHost target, String token,
			String folderId) throws Exception {
		final DefaultHttpClient httpClient = new DefaultHttpClient();
		JSONObject obj = null;
		List<Report> reportList = null;
		try {
			HttpGet httpGetRequest = new HttpGet("/biprws/infostore/"
					+ folderId + "/children");
			httpGetRequest.addHeader("Accept", "application/JSON");
			httpGetRequest.addHeader("X-SAP-LogonToken", token);
			HttpResponse httpResponse = httpClient.execute(target,
					httpGetRequest);
			String tmp = httpResponse.getStatusLine().toString();
			if (tmp.equals("HTTP/1.1 200 OK")) {
				HttpEntity hEntity = httpResponse.getEntity();
				String sFinal = "";
				if (hEntity != null) {
					InputStream inputStream = hEntity.getContent();
					sFinal = loadInputStream(inputStream);
					JSONParser parser = new JSONParser();
					obj = (JSONObject) parser.parse(sFinal);
					reportList = getAllReports(obj);
				}
			}
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
		return reportList;
	}
	
	/*
	 *  EB-7221 - Logs off BI4 and returns the http status code of the logoff call. 
	 */
	public String logOffBI(String logonToken)
	{
		//BI4 reporting / logoff
		HttpHost target; 
		String username;
		String password;
		String authentication;
		String CMS;
		String tmp = "Error during logoff from BI.";
		//Logoff from BI session - Mahmoud		
		try{
			final DefaultHttpClient httpClient = new DefaultHttpClient();
			//log off from the BI session. Note: Only if we have a LogonToken can we do a logoff			
			if (logonToken != null) {			
				
				Map<String,String> CMSLogonDetails = ReportUtils.getCMSLogonDetails();
				username = CMSLogonDetails.get("username").toString();
				password = CMSLogonDetails.get("password").toString();
				authentication = CMSLogonDetails.get("authentication").toString();
				CMS = CMSLogonDetails.get("CMS").toString();
				//postpone setting _target until CMS is read to avoid calling getCMSLogonDetails() more than once. 
				
				target = getHttpHost(CMS);
				
				HttpPost httpPostRequest = new HttpPost("/biprws/logoff");
				httpPostRequest.addHeader("Accept", "application/JSON");
				httpPostRequest.addHeader("Content-Type", "application/JSON");
				httpPostRequest.addHeader("X-SAP-LogonToken", logonToken);				 
				//
				// Send the Post.
				//
				HttpResponse httpResponse = httpClient.execute(target,
						httpPostRequest);
				System.out.println("Executed call to logoff from BI.");
				tmp = httpResponse.getStatusLine().toString();
				System.out.println("Http Status code from BI Logoff = " + tmp);
			}		
			
		}
		catch(Exception ex1)
		{
			tmp = "Could not logoff from BI/CMS. due to " + ex1.getMessage();
			//System.out.println(tmp);
		}
		return tmp;

	}

}
