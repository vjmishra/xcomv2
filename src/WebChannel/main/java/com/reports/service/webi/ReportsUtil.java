package com.reports.service.webi;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ReportsUtil {
	String msgStr = "";

	// Retrieve the logon information
	String username = "xp_nextgen"; // request.getParameter("username");
	String password = "NextGen!"; // request.getParameter("password");
	String cmsName = "S02ABI4MGMTD1.na.ipaper.com:6400"; // request.getParameter("cmsName");
	String authType = "secEnterprise"; // request.getParameter("authType");
	String standardFolder = "AfTWhiyy0N1Jgy2TPaYwguk";
	
	
	String msg = "";

	public String logon(String rptId, HashMap<String, String> paramVals) throws Exception {
		String finalURL = "";
		String[] cms = cmsName.split("\\:");
		String port = "6405";
		if (cms.length > 1) {
			cmsName = cms[0];
		}
		HttpHost target = getHttpHost(cmsName, port);

		ArrayList<String> logonTokens = logonCMS(username, password, authType,
				target);
		Boolean isOK = true;
		if (logonTokens.size() < 2) {
			msg = "No Tokens Found";
			isOK = false;
		}

		if (isOK) {
			String openDocURL = getOpenDoc(rptId, target, logonTokens.get(0));
			msg += "<p>OpenDocURL: " + openDocURL;
			if (!openDocURL.isEmpty()) {

				// The sample report I'm using has two params - one for Year and
				// one for State
				// This is hardcoded for those two params but it shows how to
				// get and set the values.
				
				// This is how we pass paramvals
				
				//HashMap<String, String> paramVals = new HashMap<String, String>();
				//paramVals.put("Year", "2006");
				//paramVals.put("State", "California,Colorado,DC,Florida");
				
				
				String params = getParamString(rptId, target, logonTokens.get(0), paramVals);
				msg += "<p>" + params;

				String encodedToken = logonTokens.get(1);
				finalURL = openDocURL + params + "&X-SAP-LogonToken=" + encodedToken;

			} else {
				msg += "<p>No OpenDocument URL Found.";
			}
		}

		return finalURL;
	}

	String loadInputStream(InputStream inputStream) throws Exception {
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

	String getOpenDocURL(JSONObject obj) throws JSONException {
		String sReturn = "";
		JSONObject subobj = (JSONObject) obj.get("openDocument");
		subobj = (JSONObject) subobj.get("__deferred");
		sReturn = subobj.get("uri").toString();
		return sReturn;
	}

	String getParams(JSONObject obj, HashMap<String, String> paramVals)
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

	HttpHost getHttpHost(String sServer, String sPort) {
		HttpHost target = new HttpHost(sServer, Integer.parseInt(sPort), "http");
		return target;
	}

	ArrayList<String> logonCMS(String sUserName, String sPassword,
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

	String getOpenDoc(String docId, HttpHost target, String token)
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

	String getParamString(String docID, HttpHost target, String token,
			HashMap<String, String> paramVals) throws Exception {
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
}
