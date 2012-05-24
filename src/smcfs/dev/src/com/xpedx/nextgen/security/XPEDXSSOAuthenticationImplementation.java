package com.xpedx.nextgen.security;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.sterlingcommerce.ui.web.framework.context.SCUIContext;
import com.sterlingcommerce.ui.web.framework.extensions.ISCUISessionChangeAware;
import com.sterlingcommerce.ui.web.framework.listeners.SCUISessionChangeData;
import com.sterlingcommerce.ui.web.framework.listeners.SCUISessionChangeListener;
import com.sterlingcommerce.woodstock.util.frame.Manager;
import com.yantra.util.YFCUtils;
import com.yantra.ycp.japi.util.YCPSSOManager;
import com.yantra.yfc.util.YFCCommon;
import com.yantra.yfs.core.YFSSystem;
import com.yantra.yfs.ui.backend.IYFSAuthenticateType;

public class XPEDXSSOAuthenticationImplementation implements YCPSSOManager,
		IYFSAuthenticateType{
	
	public static final String DEFAULT_USERID_PARAM_NAME = "UserId";
	public static final String DEFAULT_DISP_USERID_PARAM_NAME = "DisplayUserID";
	public static final String DEFAULT_PASSWORD_PRAM_NAME = "Password";
	
	public static final String LDAP_FACTORY = "com.sun.jndi.ldap.LdapCtxFactory";
	
	public static final String LDAP_SERVER_URL = "xpedx.ldap.server.url";
	public static final String LDAP_SCHEMA = "xpedx.ldap.schema";
	public static final String LDAP_AUTH_ATTR_NAME = "xpedx.ldap.authentication.attribute.name";
	public static final String LDAP_AUTH_ATTR_SUFFIX = "xpedx.ldap.authentication.attribute.suffix";
	public static final String LDAP_AUTH_ATTR_DOMAIN = "xpedx.ldap.authentication.attribute.domain";
	private static final String LDAP_AUTH_IS_ACTIVE_DIR = "xpedx.ldap.authentication.isActiveDirectory";
	//added for jira 3393 - inactivate Ldap authentication
	private static final String LDAP_AUTH_IS_REQUIRED = "xpedx.ldap.authentication.IsRequired";
	
	private static final String USER_TYPE_INTERNAL = "INTERNAL";	
	
	private static String PLATFORM_AUTH_USERS = "platform_auth_enabled_usernames";
	
	private static final Logger LOG = Logger.getLogger(XPEDXSSOAuthenticationImplementation.class);
	private static ISCUISessionChangeAware sessionChangeAware = new ISCUISessionChangeAware() {
    	@Override
    	public SCUISessionChangeData handleSessionChange(SCUIContext uiContext,
    			boolean hasUserChanged) {
    		LOG.debug("XPEDXSSOAuthenticationImplementation::::::::Handle Session ::::::::::::::::::::");
    		SCUISessionChangeData changedData = new SCUISessionChangeData();
    		if (hasUserChanged){
    			HashMap sessionMap = new HashMap();
    			HttpSession session = uiContext.getSession(false);
    			LOG.debug("XPEDXSSOAuthenticationImplementation::::::::IS Sales Rep:::: " + session.getAttribute("IS_SALES_REP"));
    			sessionMap.put("IS_SALES_REP", session.getAttribute("IS_SALES_REP"));
    			sessionMap.put("loggedInUserName", session.getAttribute("loggedInUserName"));
    			//SRSalesRepEmailID added for jira 3438
    			sessionMap.put("SRSalesRepEmailID", session.getAttribute("SRSalesRepEmailID"));
       		 	changedData.setSessionMap(sessionMap);
    		}
    		return changedData;
    	}   
    };
	static {
        SCUISessionChangeListener.addListener(sessionChangeAware);
    }
	
	@Override
	public String getUserData(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String userId = getUserId(request);
		String actualUserId = userId;
		String password = getPassword(request);

		String ldapServerURL = YFSSystem.getProperty(LDAP_SERVER_URL);
		String ldapSchema = YFSSystem.getProperty(LDAP_SCHEMA);
		String ldapAuthAttrName = YFSSystem.getProperty(LDAP_AUTH_ATTR_NAME);
		String contextPath = request.getContextPath();
		String ldapAuthAttrsuffix = YFSSystem.getProperty(LDAP_AUTH_ATTR_SUFFIX);
		if (!YFCCommon.isVoid(ldapAuthAttrsuffix) && !userId.endsWith(ldapAuthAttrsuffix)){
			userId = userId + ldapAuthAttrsuffix.trim();
		}

		String ldapAuthAttrDomain = YFSSystem.getProperty(LDAP_AUTH_ATTR_DOMAIN);
		String ldapAuthIsActiveDir = YFSSystem.getProperty(LDAP_AUTH_IS_ACTIVE_DIR);
		String ldapAuthIsRequired = YFSSystem.getProperty(LDAP_AUTH_IS_REQUIRED);
		//start of jira 3393 condition
		if("/swc".equalsIgnoreCase(contextPath)){
		if(!YFCCommon.isVoid(ldapAuthIsRequired) && "Y".equalsIgnoreCase(ldapAuthIsRequired.trim())){
		if (!YFCCommon.isVoid(ldapAuthAttrDomain)){
			if (!YFCCommon.isVoid(ldapAuthIsActiveDir) && "Y".equalsIgnoreCase(ldapAuthIsActiveDir.trim())){
				if (!userId.startsWith(ldapAuthAttrDomain)){
					userId = ldapAuthAttrDomain.trim() + "\\" + userId;
				}
			}
			else {
				if (!userId.endsWith(ldapAuthAttrDomain)){
					userId = userId  + "@" + ldapAuthAttrDomain.trim();
				}
			}
		}
		
		String ldapDN=null;
		if (!YFCCommon.isVoid(ldapSchema)){
			ldapDN=(new StringBuilder()).append(userId).append(",").append(ldapSchema.trim()).toString();
		}
		else {
			ldapDN=userId;
		}
		if (!YFCCommon.isVoid(ldapAuthAttrName)){
			ldapDN=(new StringBuilder()).append(ldapAuthAttrName + "=").append(ldapDN).toString();
		}
		
		

		LOG.debug("XPEDXSSOAuthenticationImplementation:: LDAP server URL is " + ldapServerURL);
		LOG.debug("XPEDXSSOAuthenticationImplementation:: LDAP Schema is " + ldapSchema);
		LOG.debug("XPEDXSSOAuthenticationImplementation:: LDAP Attribute is " + ldapAuthAttrName);
		LOG.debug("XPEDXSSOAuthenticationImplementation:: LDAP userId is " + userId);
		LOG.debug("XPEDXSSOAuthenticationImplementation:: LDAP password is " + password);
		LOG.info("XPEDXSSOAuthenticationImplementation:: DN is " + ldapDN);

        Hashtable<String, String> env = new Hashtable<String, String>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, LDAP_FACTORY);
		env.put(Context.PROVIDER_URL, ldapServerURL);
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		env.put(Context.SECURITY_PRINCIPAL, ldapDN.trim());	
		env.put(Context.SECURITY_CREDENTIALS, password.trim());

        DirContext ctx = new InitialDirContext(env);
        ctx.close();
		LOG.debug("XPEDXSSOAuthenticationImplementation::"+ actualUserId + " Authenticated.");
		request.setAttribute("IS_LDAP_AUTHENTICATED", Boolean.TRUE);
		}
		} 
//JIRA 3852 starts
		else
		{
			System.out.println("COM Logging");
			
				if (!YFCCommon.isVoid(ldapAuthAttrDomain)){
					if (!YFCCommon.isVoid(ldapAuthIsActiveDir) && "Y".equalsIgnoreCase(ldapAuthIsActiveDir.trim())){
						if (!userId.startsWith(ldapAuthAttrDomain)){
							userId = ldapAuthAttrDomain.trim() + "\\" + userId;
						}
					}
					else {
						if (!userId.endsWith(ldapAuthAttrDomain)){
							userId = userId  + "@" + ldapAuthAttrDomain.trim();
						}
					}
				}
				
				String ldapDN=null;
				if (!YFCCommon.isVoid(ldapSchema)){
					ldapDN=(new StringBuilder()).append(userId).append(",").append(ldapSchema.trim()).toString();
				}
				else {
					ldapDN=userId;
				}
				if (!YFCCommon.isVoid(ldapAuthAttrName)){
					ldapDN=(new StringBuilder()).append(ldapAuthAttrName + "=").append(ldapDN).toString();
				}
				
				

				LOG.debug("XPEDXSSOAuthenticationImplementation:: LDAP server URL is " + ldapServerURL);
				LOG.debug("XPEDXSSOAuthenticationImplementation:: LDAP Schema is " + ldapSchema);
				LOG.debug("XPEDXSSOAuthenticationImplementation:: LDAP Attribute is " + ldapAuthAttrName);
				LOG.debug("XPEDXSSOAuthenticationImplementation:: LDAP userId is " + userId);
				LOG.debug("XPEDXSSOAuthenticationImplementation:: LDAP password is " + password);
				LOG.info("XPEDXSSOAuthenticationImplementation:: DN is " + ldapDN);

		        Hashtable<String, String> env = new Hashtable<String, String>();
		        env.put(Context.INITIAL_CONTEXT_FACTORY, LDAP_FACTORY);
				env.put(Context.PROVIDER_URL, ldapServerURL);
				env.put(Context.SECURITY_AUTHENTICATION, "simple");
				env.put(Context.SECURITY_PRINCIPAL, ldapDN.trim());	
				env.put(Context.SECURITY_CREDENTIALS, password.trim());

		        DirContext ctx = new InitialDirContext(env);
		        ctx.close();
				LOG.debug("XPEDXSSOAuthenticationImplementation::"+ actualUserId + " Authenticated.");
				request.setAttribute("IS_LDAP_AUTHENTICATED", Boolean.TRUE);
				
		}   //JIRA 3852 ends
//end of jira 3393 condition
		// need this attribute set in request to avoid customer contact lookup by post authentication
		
		String userName = (String)request.getAttribute("loggedInUserName");
		//SRSalesRepEmailID added for jira 3438
		String SRemailID = (String)request.getAttribute("SRSalesRepEmailID");
		if (request.getSession(false) != null){
			request.getSession(false).setAttribute("IS_SALES_REP","true");
			request.getSession(false).setAttribute("loggedInUserName",userName);
			request.getSession(false).setAttribute("loggedInUserId",actualUserId);
			request.getSession(false).setAttribute("SRSalesRepEmailID",SRemailID);
			
			request.setAttribute("IS_SALES_REP", "true");
			request.setAttribute("loggedInUserName", userName);
			request.setAttribute("loggedInUserId", actualUserId);
			request.setAttribute("SRSalesRepEmailID", SRemailID);
		}
    
		return actualUserId;
	}
	
	private String getPassword(HttpServletRequest request) {
		String password = request.getParameter(DEFAULT_PASSWORD_PRAM_NAME);
		if (!YFCCommon.isVoid(password)){
			password = password.trim();
		}
		return password;
	}


	private String getUserId(HttpServletRequest request) {
		String userId = request.getParameter(DEFAULT_DISP_USERID_PARAM_NAME);
		if (YFCCommon.isVoid(userId)){
			userId = (String)request.getAttribute(DEFAULT_DISP_USERID_PARAM_NAME);
		}
		if (YFCCommon.isVoid(userId)){
			userId = request.getParameter(DEFAULT_USERID_PARAM_NAME);
		}
		if (YFCCommon.isVoid(userId)){
			userId = (String)request.getAttribute(DEFAULT_USERID_PARAM_NAME);
		}
		if (!YFCCommon.isVoid(userId)){
			userId = userId.trim();
		}
		return userId;
	}

	
	@Override
	public boolean isPlatformLoginNeeded(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		LOG.debug("XPEDXSSOAuthenticationImplementation:: ::::::::::::::::::::::::: Authentication isPlatformLoginNeeded check :::::::::::::::::::::::::");
		
		// Check the YFS_USER table and see if the user is internal or external
		// If the user is internal return false so that SSO Authentication (this class's getUser(...)) 
		// method will be called.
		//return false;
		
		String loggedInUser = getUserId(request);
		boolean isPlatformLoginNeeded;
		boolean isInternalUser = false;
		
		if(!YFCUtils.isVoid(loggedInUser)){
			// set this to the guest_xpedx
			isInternalUser = isInternal(request, loggedInUser);
		}
		// If the user is external return true so that platform login (YFS_USER) will be called.
		// In xpedx all internal users including salesrep will go through ldap authentication
		// the salesrep's dummyuser and all SWC external users will go through platform authentication
		//return true;

		if(isInternalUser){
			isPlatformLoginNeeded =  false;
		}else{
			isPlatformLoginNeeded = true;
		}
		return isPlatformLoginNeeded;
	}

	/**
	 * This method checks if the logged in USER_TYPE is internal or external
	 * @param request
	 * @param loggedInUser
	 * @return
	 */
	private boolean isInternal(HttpServletRequest request, String loggedInUser) {
		boolean isInternal = false;
		
		if (userNeedsPlatformAuth(loggedInUser)){
			return false;
		}
		
		// user type
		String userType = null;
		//Added to fetch User name for Jira 2367
		String userName = null;
		String SRemailID = null;
		
		
		String jdbcURL = Manager.getProperty("jdbcService", "oraclePool.url");
		String jdbcDriver = Manager.getProperty("jdbcService", "oraclePool.driver");
		String jdbcUser = Manager.getProperty("jdbcService", "oraclePool.user");
		String jdbcPassword = Manager.getProperty("jdbcService", "oraclePool.password");
		//Added USERNAME in the query to fetch USERNAME column data for Jira 2367
		String queryString = "SELECT EXTN_USER_TYPE , USERNAME FROM yfs_user WHERE DISPLAY_USER_ID = ?";
				
		
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            Class.forName(jdbcDriver);
            con = DriverManager.getConnection(jdbcURL, jdbcUser, jdbcPassword);
            stmt = con.prepareStatement(queryString);
            stmt.setString(1, loggedInUser);
            
            rs =  stmt.executeQuery();
            while(rs!= null && rs.next()){
            	userType = rs.getString("EXTN_USER_TYPE");
            	}
            
            userName = (String)request.getSession().getAttribute("loggedInUserName");
			//SRSalesRepEmailID added for jira 3438
            SRemailID = (String)request.getSession().getAttribute("SRSalesRepEmailID");
            if(userName !=null && !userName.isEmpty()){
            request.getSession(false).setAttribute("loggedInUserName",userName);
			request.setAttribute("loggedInUserName", userName);
            }
            if(SRemailID !=null && !SRemailID.isEmpty()){
                request.getSession(false).setAttribute("SRSalesRepEmailID",SRemailID);
    			request.setAttribute("SRSalesRepEmailID", SRemailID);
                }
		}catch( Exception e ) {
		   LOG.error(" Error while fetching the USERTYPE for User: " + loggedInUser + e.getMessage(), e);    
		}
		finally{
			try{
				if (rs != null)
						rs.close();
				if (stmt != null)
						stmt.close();
				if (con != null)
						con.close();
			}
			catch( Exception e ) {
			   LOG.error(" Error while fetching the USERTYPE for User: " + loggedInUser + e.getMessage(), e);    
			}
		}
		
		LOG.debug("XPEDXSSOAuthenticationImplementation:: User " + loggedInUser + " is " +userType);
		
		  // if the user is internal then it goes through authentication
		if(userType != null && USER_TYPE_INTERNAL.equalsIgnoreCase(userType.trim())){
			if (request.getSession(false) != null){
				request.getSession(false).setAttribute("IS_SALES_REP","true");
				request.getSession(false).setAttribute("loggedInUserName",userName);
				request.getSession(false).setAttribute("loggedInUserId",loggedInUser);
    			//SRSalesRepEmailID added for jira 3438
				request.getSession(false).setAttribute("SRSalesRepEmailID",SRemailID);

				request.setAttribute("IS_SALES_REP", "true");
				request.setAttribute("loggedInUserName", userName);
				request.setAttribute("loggedInUserId", loggedInUser);
				request.setAttribute("SRSalesRepEmailID", SRemailID);
				LOG.debug("XPEDXSSOAuthenticationImplementation:isInternal: userName " + userName );
				}
			isInternal = true;
		}else 
			isInternal = false;
		
		return isInternal;
	}

	private boolean userNeedsPlatformAuth(String loggedInUser) {
		
		String usernames = YFSSystem.getProperty(PLATFORM_AUTH_USERS);
		if (YFCCommon.isVoid(usernames)){
			return false;
		}
		String [] users = usernames.split(",");
		for (int i=0; i < users.length; i++){
			if (loggedInUser.equalsIgnoreCase(users[i])){
				return true;
			}
		}
		return false;
	}
	
}
