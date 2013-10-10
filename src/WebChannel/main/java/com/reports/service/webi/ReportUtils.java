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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.reports.service.Report;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;
import com.yantra.yfs.core.YFSSystem;

public class ReportUtils {

	String msgStr = "";
	
	public static Map<String, String> getCMSLogonDetails() {
		Map<String, String> logonMap = new HashMap<String, String>();
		
		String wcPropertiesFile = "xpedx_reporting.properties";
		XPEDXWCUtils.loadXPEDXSpecficPropertiesIntoYFS(wcPropertiesFile);
		// Retrieve the logon information
		logonMap.put("username", YFSSystem.getProperty("username"));
		logonMap.put("password", YFSSystem.getProperty("password"));
		logonMap.put("CMS", YFSSystem.getProperty("CMS"));
		logonMap.put("authentication", YFSSystem.getProperty("authentication"));
		logonMap.put("standard_folder_id", YFSSystem.getProperty("standard_folder_id"));
		logonMap.put("custom_folder_id", YFSSystem.getProperty("custom_folder_id"));
		
		return logonMap;
		
	}

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

	public String getOpenDocURL(JSONObject obj) {
		String sReturn = "";
		JSONObject subobj = (JSONObject) obj.get("openDocument");
		subobj = (JSONObject) subobj.get("__deferred");
		sReturn = subobj.get("uri").toString();
		return sReturn;
	}

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
					if (cardinality.equals("Single")) {
						tmp = "&lsS" + paramName + "="
								+ paramVals.get(paramName);

					} else if (cardinality.equals("Multiple")) {
						tmp = "&lsM" + paramName + "="
								+ paramVals.get(paramName);
					}
				} else if (optionalParam.equals("true")) {
					//
					// handle optional params that have no value
					//
					if (cardinality.equals("Single")) {
						tmp = "&lsS" + paramName + "=no_value";

					} else if (cardinality.equals("Multiple")) {
						tmp = "&lsM" + paramName + "=no_value";
					}
				}
				sResult += tmp;
			}
		}
		return sResult;
	}

	public HttpHost getHttpHost(String sServer) {

		String[] cms = sServer.split("\\:");
		String sPort = "6405";
		if (cms.length > 1) {
			sServer = cms[0];
		}

		HttpHost target = new HttpHost(sServer, Integer.parseInt(sPort), "http");
		return target;
	}

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
		} finally {
			// When HttpClient instance is no longer needed,
			// shut down the connection manager to ensure
			// immediate deallocation of all system resources
			httpClient.getConnectionManager().shutdown();
		}
		return result;
	}

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
			httpClient.getConnectionManager().shutdown();
		}
		return promptList;
	}

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

}
