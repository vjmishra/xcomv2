package com.xpedx.nextgen.security;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.xml.parsers.FactoryConfigurationError;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sterlingcommerce.baseutil.SCUtil;
import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.woodstock.util.frame.Manager;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfc.util.YFCCommon;
import com.yantra.yfc.util.YFCException;
import com.yantra.yfs.core.YFSSystem;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSException;

/*
 * Gets User details from LDAP server given an input xml as follows
 * In XML = <User LoginID="required"/>
 * Out XML = <User LoginID=".." EmployeeID=".." EmployeeName=".." EmailAddress=".." />
 */
public class XPXLdapSearchDetails implements YIFCustomApi {

	public static String AUTHENTICATION_ID_PARAM_NAME = "AuthIDParamName";
	public static String DEFAULT_USERID_PARAM_NAME = "DisplayUserID";
	public static String DEFAULT_PASSWORD_PRAM_NAME = "Password";

	public static final String LDAP_FACTORY = "com.sun.jndi.ldap.LdapCtxFactory";

	public static final String LDAP_SERVER_URL = "xpedx.ldap.server.url";
	public static final String LDAP_SCHEMA = "xpedx.ldap.schema";
	public static final String LDAP_AUTH_ATTR_NAME = "xpedx.ldap.authentication.attribute.name";
	public static final String LDAP_AUTH_ATTR_SUFFIX = "xpedx.ldap.authentication.attribute.suffix";
	public static final String LDAP_AUTH_ATTR_DOMAIN = "xpedx.ldap.authentication.attribute.domain";
	private static final String LDAP_AUTH_IS_ACTIVE_DIR = "xpedx.ldap.authentication.isActiveDirectory";
	private static final String LDAP_SEARCH_AUTH_USERID = "xpedx.ldap.search.authentication.userid";
	private static final String LDAP_SEARCH_AUTH_PASSWORD = "xpedx.ldap.search.authentication.pwd";
	private static final String LDAP_SEARCH_LDAP_SCHEMA = "xpedx.ldap.search.schema";
	private static final String LDAP_SEARCH_ATTR_NAME = "xpedx.ldap.search.attribute.name";

	private static final Logger LOG = Logger.getLogger(XPXLdapSearchDetails.class);
	private Properties _properties = null;
	private static DirContext _dirContext = null;

	public void setProperties(Properties properties) throws Exception {
		_properties = properties;
	}

	private void setInitialContext() throws NamingException {

		String ldapServerURL = YFSSystem.getProperty(LDAP_SERVER_URL);

		String ldapSchema = YFSSystem.getProperty(LDAP_SCHEMA);

		String ldapAuthAttrName = YFSSystem.getProperty(LDAP_AUTH_ATTR_NAME);

		String userId = YFSSystem.getProperty(LDAP_SEARCH_AUTH_USERID);

		String password = YFSSystem.getProperty(LDAP_SEARCH_AUTH_PASSWORD);

		String ldapAuthAttrsuffix = YFSSystem.getProperty(LDAP_AUTH_ATTR_SUFFIX);

		if (!YFCCommon.isVoid(ldapAuthAttrsuffix) && !userId.endsWith(ldapAuthAttrsuffix)) {
			userId = userId + ldapAuthAttrsuffix.trim();
		}

		String ldapAuthAttrDomain = YFSSystem.getProperty(LDAP_AUTH_ATTR_DOMAIN);

		String ldapAuthIsActiveDir = YFSSystem.getProperty(LDAP_AUTH_IS_ACTIVE_DIR);

		if (!YFCCommon.isVoid(ldapAuthAttrDomain)) {
			if (!YFCCommon.isVoid(ldapAuthIsActiveDir) && "Y".equalsIgnoreCase(ldapAuthIsActiveDir.trim())) {
				if (!userId.startsWith(ldapAuthAttrDomain)) {
					userId = ldapAuthAttrDomain.trim() + "\\" + userId;
				}
			} else {
				if (!userId.endsWith(ldapAuthAttrDomain)) {
					userId = userId + "@" + ldapAuthAttrDomain.trim();
				}
			}
		}

		String ldapDN = null;
		if (!YFCCommon.isVoid(ldapSchema)) {
			ldapDN = (new StringBuilder()).append(userId).append(",").append(ldapSchema.trim()).toString();
		} else {
			ldapDN = userId;
		}
		if (!YFCCommon.isVoid(ldapAuthAttrName)) {
			ldapDN = (new StringBuilder()).append(ldapAuthAttrName + "=").append(ldapDN).toString();
		}

		
		if(LOG.isDebugEnabled()){
			LOG.debug("XPEDXSSOAuthenticationImplementation:: LDAP server URL is " + ldapServerURL);
			LOG.debug("XPEDXSSOAuthenticationImplementation:: LDAP Schema is " + ldapSchema);
			LOG.debug("XPEDXSSOAuthenticationImplementation:: LDAP Attribute is " + ldapAuthAttrName);
			LOG.debug("XPEDXSSOAuthenticationImplementation:: LDAP userId is " + userId);
			// LOG.debug("XPEDXSSOAuthenticationImplementation:: LDAP password is " + password);
			LOG.debug("XPEDXSSOAuthenticationImplementation:: DN is " + ldapDN);
		}
		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, LDAP_FACTORY);
		env.put(Context.PROVIDER_URL, ldapServerURL);
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		env.put(Context.SECURITY_PRINCIPAL, ldapDN.trim());
		env.put(Context.SECURITY_CREDENTIALS, password.trim());
		
		_dirContext = new InitialDirContext(env);
		if(LOG.isDebugEnabled()){
		LOG.debug("Created a new InitialDirContext");
		}
	}

	public String getPropertyValue(String propertyName) {
		return (YFSSystem.getProperty(propertyName) != null) ? YFSSystem.getProperty(propertyName) : Manager.getProperty("customer_overrides", propertyName);
	}

	/**
	 * Gets User details from LDAP server given an input xml as follows
	 * 
	 * <pre>
	 * Inputxml XML = &lt;User LoginID=&quot;required&quot;/&gt;
	 * Out XML = &lt;User LoginID=&quot;..&quot; EmployeeID=&quot;..&quot; EmployeeName=&quot;..&quot; EmailAddress=&quot;..&quot; ... /&gt;
	 * 
	 * Output XML varies based on newly added arguments in "XPXLDAPSearchDetails" Service. Argument Key will be the attribute name and Argument Value will be the LDAP server attribute name.
	 * 
	 */
	public Document invoke(YFSEnvironment yfsenv, Document inDoc) throws YFCException, NamingException {

		LOG.debug("Input XML for invoke method in XPXLdapSearchDetails:" + SCXmlUtil.getString(inDoc));

		Document resultDoc = SCXmlUtil.createDocument("User");
		Element resultEle = resultDoc.getDocumentElement();
		String userID = inDoc.getDocumentElement().getAttribute("LoginID");

		// Data Validation
		if (yfsenv == null) {
			// Throw new YFSException with the description
			throw new YFSException("YFSEnvironment cannot be null or invalid to XPXLdapSearchDetails.invoke()");
		}
		if (inDoc == null) {
			// Throw new YFSException with the description
			throw new YFSException("Input XML document cannot be null or invalid to XPXLdapSearchDetails.invoke()");
		}
		if (SCUtil.isVoid(userID)) {
			// throw new YFSException( "Invalid Input XML : 'LoginID' attribute is mandatory!");
			resultEle.setAttribute("LoginID", "");
			resultEle.setAttribute("Exception", "Invalid NetworkID/LoginID should be provided.");
			return resultDoc;
		} else {
			resultEle.setAttribute("LoginID", userID);
		}

		try {

			// set the context only if it is not set before
			if (YFCCommon.isVoid(_dirContext)) {
				setInitialContext();
			}

			String ldapSearchAttrName = YFSSystem.getProperty(LDAP_SEARCH_ATTR_NAME);

			String ldapSearchBaseSchema = YFSSystem.getProperty(LDAP_SEARCH_LDAP_SCHEMA);

			SearchControls constraints = new SearchControls();
			constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);

			String filter = (new StringBuilder(ldapSearchAttrName + "=")).append(userID.trim()).toString();
			NamingEnumeration searchResults = _dirContext.search(ldapSearchBaseSchema, filter, constraints);
			if (searchResults != null && searchResults.hasMore()) {
				// Found one user, stamp attributes to result Document
				final SearchResult searchResult = (SearchResult) searchResults.next();
				Attributes attributes = searchResult.getAttributes();
				if (attributes != null) {

					Enumeration attrIter = _properties.keys();
					while (attrIter.hasMoreElements()) {
						String currName = (String) attrIter.nextElement();
						String ldapKey = _properties.getProperty(currName, "");
						final Attribute attribute = (Attribute) attributes.get(ldapKey);
						if (attribute != null) {

							StringBuilder val = new StringBuilder();
							String attrid = attribute.getID().trim();
							for (NamingEnumeration namingEnu = attribute.getAll(); namingEnu.hasMore();)
								val.append(namingEnu.next());
							if (!currName.equalsIgnoreCase("LoginID"))
								resultEle.setAttribute(currName, val.toString().trim());
							else
								resultEle.getAttributeNode("LoginID").setValue(val.toString().trim());
						}
					}
				}
			}// End of if - Found user with given LoginID
			else {
				// User with given LoginID not found.
				resultEle.setAttribute("Exception", "Invalid NetworkID/LoginID.");
				return resultDoc;
			}

		} catch (FactoryConfigurationError e) {
			e.printStackTrace();
		} catch (NamingException e) {
			e.printStackTrace();
			resultEle.setAttribute("Exception", "Unable to find the provided Network/Login ID: " + userID);
		}
		finally{
			//Fix for JIRA Issue 2409 - LDAP Connection Timeout issue
			if(_dirContext!=null){
				_dirContext.close();
				//Making the context variable as null, so that setInitialContext is invoked for every search.
				_dirContext = null;
			}
		}
		LOG.debug("invoke() Output XML:" + SCXmlUtil.getString(resultDoc));
		return resultDoc;
	}

	// used for testing
	public static void main(String[] args) {
		XPXLdapSearchDetails ldapSearch = new XPXLdapSearchDetails();
		Document inDoc = SCXmlUtil.createDocument("User");
		inDoc.getDocumentElement().setAttribute("LoginID", "xpedxwebtest01");
		try {
			ldapSearch.invoke(null, inDoc);
		} catch (YFCException e) {
			e.printStackTrace();
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
}
