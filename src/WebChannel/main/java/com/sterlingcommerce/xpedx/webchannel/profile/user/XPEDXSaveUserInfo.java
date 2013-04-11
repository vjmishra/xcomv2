/**
 * This class provides the funtionality to manage Buyer User General
 * Information.
 */
package com.sterlingcommerce.xpedx.webchannel.profile.user;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.sterlingcommerce.framework.utils.SCXmlUtils;
import com.sterlingcommerce.ui.web.framework.extensions.ISCUITransactionContext;
import com.sterlingcommerce.ui.web.platform.transaction.SCUITransactionContextFactory;
import com.sterlingcommerce.webchannel.core.WCAttributeScope;
import com.sterlingcommerce.webchannel.core.WCMashupAction;
import com.sterlingcommerce.webchannel.profile.user.UserProfileHelper;
import com.sterlingcommerce.webchannel.utilities.WCConstants;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper.CannotBuildInputException;
import com.sterlingcommerce.xpedx.webchannel.common.XPEDXConstants;
import com.sterlingcommerce.xpedx.webchannel.common.XPEDXCustomerContactInfoBean;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXClientPasswordValidator;
import com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.ycp.passwordpolicy.result.PasswordPolicyResult;
import com.yantra.ycp.passwordpolicy.result.PasswordPolicyResult.ResultType;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.dom.YFCNodeList;
import com.yantra.yfc.ui.backend.util.APIManager.XMLExceptionWrapper;
import com.yantra.yfc.util.YFCCommon;
import com.yantra.yfc.util.YFCException;
import com.yantra.yfs.core.YFSSystem;
import com.yantra.yfs.japi.YFSEnvironment;

/**
 * @author vsriram
 * 
 */
public class XPEDXSaveUserInfo extends WCMashupAction

{
	/**
     * 
     */
	private static final long serialVersionUID = -7194085148560207953L;
	private Element customer;
	private Element contact;
	private Element user;
	private Element customerAdmin;
	private String userPassword = "*****";
	private String organizationCode;
	private Element loc;
	private String userId;
	private String selectedTab;

	// Logger
	private static final Logger log = Logger.getLogger(XPEDXSaveUserInfo.class);

	// Strings
	private static final String CUSTOMER_STATUS_ACTIVE = "10";
	private static final String CUSTOMER_STATUS_ON_HOLD = "20";
	private static final String YES = "Y";
	private static final String BUYER_USER_GROUP_KEY = "BUYER-USER";
	private static final String BUYER_ADMIN_GROUP_KEY = "BUYER-ADMIN";
	private static final String BUYER_APPROVER_GROUP_KEY = "BUYER-APPROVER";

	// mashups
	private static final String MANAGE_CONTACT_MASHUP = "ManageXpedxContact";
	private static final String MANAGE_USER_MASHUP = "ManageXpedxUser";
	// Form Fields
	private String userName;
	private String password;
	private String confirmPassword;
	private String secretQuestion;
	private String secretAnswer;
	private String confirmAnswer;
	private String preferredLocale;
	private String title;
	private String firstName;
	private String lastName;
	private String jobTitle;
	// private String comparnyName;
	private String deptName;
	private String buyerUser;
	private String buyerAdmin;
	private String approver;
	private String status;
	private String contactStatus;
	private String userStatus;
	private String emailId;
	private String viewPrices;
	private String viewReports;
	private String invoiceEmailID;
	private String orderConfirmationEmailFlag;
	private String orderCancellationEmailFlag;
	private String orderUpdateEmailFlag;
	private String orderShipmentEmailFlag;
	private String backorderEmailFlag;
	private String bodyData;
	private List<String> oldAssignedCustomers = new ArrayList<String>();
	List<String> alNewSelectedCustomers = new ArrayList<String>();
	private String buyAdmin;
	private String spendingLtCurrency;
	private String currentSelTab;
	private String userPwdToValidate;
	public String defaultShipTo;
	private ArrayList<String> oldAssignCusts = new ArrayList<String>();
	private Document manageCustomerAssigmentInputDoc = null;
	private String orderApprovalFlag;// added for XB 226
	private String newAssignedCustomers;
	private String userNotAdmin;
	List<String> alFinalSelectedCustomers = new ArrayList<String>();	
	public String getUserNotAdmin() {
		return userNotAdmin;
	}

	public void setUserNotAdmin(String userNotAdmin) {
		this.userNotAdmin = userNotAdmin;
	}

	public String getNewAssignedCustomers() {
		return newAssignedCustomers;
	}

	public void setNewAssignedCustomers(String newAssignedCustomers) {
		this.newAssignedCustomers = newAssignedCustomers;
	}

	public String getDefaultShipTo() {
		return defaultShipTo;
	}

	public void setDefaultShipTo(String defaultShipTo) {
		this.defaultShipTo = defaultShipTo;
	}

	/* Added for Jira 3902 */
	private String loginId;

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	private Map<String, String> pwdValidationResultMap;
	private boolean success;
	private boolean saveAddUser;

	public boolean isSaveAddUser() {
		return saveAddUser;
	}

	public void setSaveAddUser(boolean saveAddUser) {
		this.saveAddUser = saveAddUser;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getSelectedTab() {
		return selectedTab;
	}

	public void setSelectedTab(String selectedTab) {
		this.selectedTab = selectedTab;
	}

	public String getBuyAdmin() {
		return buyAdmin;
	}

	public void setBuyAdmin(String buyAdmin) {
		this.buyAdmin = buyAdmin;
	}


	public String getBackorderEmailFlag() {
		return backorderEmailFlag;
	}

	public void setBackorderEmailFlag(String backorderEmailFlag) {
		this.backorderEmailFlag = backorderEmailFlag;
	}

	public String getViewPrices() {
		return viewPrices;
	}

	public void setViewPrices(String viewPrices) {
		this.viewPrices = viewPrices;
	}

	public String getViewReports() {
		return viewReports;
	}

	public void setViewReports(String viewReports) {
		this.viewReports = viewReports;
	}

	public String getInvoiceEmailID() {
		return invoiceEmailID;
	}

	public void setInvoiceEmailID(String invoiceEmailID) {
		this.invoiceEmailID = invoiceEmailID;
	}

	public String getOrderConfirmationEmailFlag() {
		return orderConfirmationEmailFlag;
	}

	public void setOrderConfirmationEmailFlag(String orderConfirmationEmailFlag) {
		this.orderConfirmationEmailFlag = orderConfirmationEmailFlag;
	}

	public String getOrderCancellationEmailFlag() {
		return orderCancellationEmailFlag;
	}

	public void setOrderCancellationEmailFlag(String orderCancellationEmailFlag) {
		this.orderCancellationEmailFlag = orderCancellationEmailFlag;
	}

	public String getOrderUpdateEmailFlag() {
		return orderUpdateEmailFlag;
	}

	public void setOrderUpdateEmailFlag(String orderUpdateEmailFlag) {
		this.orderUpdateEmailFlag = orderUpdateEmailFlag;
	}

	public String getOrderShipmentEmailFlag() {
		return orderShipmentEmailFlag;
	}

	public void setOrderShipmentEmailFlag(String orderShipmentEmailFlag) {
		this.orderShipmentEmailFlag = orderShipmentEmailFlag;
	}

	public String getViewInvoices() {
		return viewInvoices;
	}

	public void setViewInvoices(String viewInvoices) {
		this.viewInvoices = viewInvoices;
	}

	public String getEstimator() {
		return estimator;
	}

	public void setEstimator(String estimator) {
		this.estimator = estimator;
	}

	public String getStockCheckWebservice() {
		return stockCheckWebservice;
	}

	public void setStockCheckWebservice(String stockCheckWebservice) {
		this.stockCheckWebservice = stockCheckWebservice;
	}

	public String getPunchoutUsers() {
		return punchoutUsers;
	}

	public void setPunchoutUsers(String punchoutUsers) {
		this.punchoutUsers = punchoutUsers;
	}

	/* STARTS - Customer-User Profile Changes - adsouza */

	public String getPaper101Grade() {
		return paper101Grade;
	}

	public void setPaper101Grade(String p101g) {
		if ("true".equals(p101g))
			paper101Grade = "Y";
	}

	/* ENDS - Customer-User Profile Changes - adsouza */

	private String estimator = "N";
	private String stockCheckWebservice = "F";
	private String punchoutUsers = "F";
	private String viewInvoices = "N";

	/* STARTS - Customer-User Profile Changes - adsouza */

	private String paper101Grade = "N";

	/* ENDS - Customer-User Profile Changes - adsouza */

	// Phone Book Fields
	private String dayFaxNo;
	private String eveningFaxNo;
	private String dayPhone;
	private String eveningPhone;
	private String mobilePhone;
	private String AddnlEmailAddrText = "";
	private String POListText = "";
	// Mode of operation
	private String operation = "";

	// String for Action Results
	private String REDIRECT = "redirect";

	// Request parameters
	private String customerId;
	private String customerContactId;
	private String buyerOrgCode;
	private List<String> states;
	protected String b2bCatalogView;

	public List<String> userRoleList = new ArrayList<String>();
	private String userNameAttribute;

	public String getB2bCatalogView() {
		return b2bCatalogView;
	}

	public void setB2bCatalogView(String b2bCatalogView) {
		this.b2bCatalogView = b2bCatalogView;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.opensymphony.xwork2.ActionSupport#execute()
	 */
	@Override
	public String execute() {
		return saveuserInfo();

	}

	/**
	 * Creates a new Buyer User
	 * 
	 * @return the action result
	 */
	public String createUser() {
		try {
			// Create User+Contact
			newUserOverrideContactAttributes();
			setUserNameAttribute(getFirstName() + " " + getLastName());
			prepareAndInvokeMashups();
			this.customerContactId = getUserName();
		} catch (XMLExceptionWrapper xmlEx) {
			log.error("Error in creating the User/Contact ", xmlEx.getCause());
			// For My Profile- Success on Save - Jira 3196
			setSuccess(false);
			setSaveAddUser(false);
			return ERROR;
		} catch (CannotBuildInputException e) {
			log.error("Error in creating the User/Contact ", e.getCause());
			// For My Profile- Success on Save - Jira 3196
			setSuccess(false);
			setSaveAddUser(false);
			return ERROR;
		}
		// For My Profile- Success on Save - Jira 3196
		setSuccess(true);
		setSaveAddUser(true);
		return REDIRECT;
	}

	/**
	 * Saves the Buyer User Information
	 * 
	 * @return the action result
	 */
	public String saveuserInfo() {
		// Password and Secret Answer not part of mashup. Should be overridden
		// in action class based on the values changed

		saveChanges();
		Boolean isSalesRep = null;
		String isSalesRepStr = (String) getWCContext().getSCUIContext()
				.getSession().getAttribute("IS_SALES_REP");
		if (isSalesRepStr != null) {
			isSalesRep = Boolean.valueOf(isSalesRepStr);
		}

		try {
			if (YFCCommon.isVoid(wcContext.getSCUIContext().getRequest()
					.getParameter("userName"))) {
				// Save contact only
				overrideContactAttributes();
				setUserNameAttribute(getFirstName() + " " + getLastName());
				prepareAndInvokeMashup(MANAGE_CONTACT_MASHUP);
			} else if (isSalesRep != null && isSalesRep) {
				overrideContactAttributes();
				prepareAndInvokeMashup("ManageXpedxUserForSalesRep");
			} else {
				// Save User+Contact
				overrideContactAttributes();
				setUserNameAttribute(getFirstName() + " " + getLastName());
				Element userContactInput = WCMashupHelper.getMashupInput(
						MANAGE_USER_MASHUP, wcContext);
				Map<String, String> valuemap = evaluateBindingsForMashup(
						MANAGE_USER_MASHUP, userContactInput);
				populateMashupInput(MANAGE_USER_MASHUP, valuemap,
						userContactInput);
				// added for 4284 - After Review
				/*
				 * String
				 * bodyDataRefCQL=(String)getWCContext().getSCUIContext().
				 * getSession().getAttribute("bodyDataforCQL");
				 * if(bodyDataRefCQL
				 * !=null&&!bodyDataRefCQL.equals(getBodyData())){
				 */
				Element Extn = (SCXmlUtils.getElements(userContactInput,
						"/CustomerContactList/CustomerContact/Extn")).get(0);
				Element XPXQuickLinkList = SCXmlUtil.createChild(Extn,
						"XPXQuickLinkList");
				AppendQuickLinkAttributes(XPXQuickLinkList);
				// }
				// End for 4284 - After Review
				if (UserProfileHelper.userIdExistsForCustomerContact(
						this.customerContactId, wcContext.getSCUIContext()
								.getRequest().getParameter("userName"),
						wcContext)) {

					if (checkIfAnswerChanged() || checkIfPasswordChanged()) {
						String newPassword = wcContext.getSCUIContext()
								.getRequest().getParameter("userpassword");
						String useLoginID = wcContext.getSCUIContext()
								.getRequest().getParameter("userName");
						if (checkIfPasswordChanged()) {
							// XB - 319
							List<String> strErrorMessage = new ArrayList<String>();
							// Added For Jira-3106
							if (newPassword.length() < 8) {

								// This exception is put here to handle the
								// password validation exceptions.
								// request.getSession().setAttribute("errorNote","The password must contain at least 8 characters. Please revise and try again.");
								strErrorMessage
										.add("The password must contain at least 8 characters.");
								setSuccess(false);
								setSaveAddUser(false);
								// return REDIRECT;
							}
							// else if(newPassword.length()>=8){
							char[] newPwdChar = newPassword.toCharArray();
							// int countSpclChar = 0;
							int pwdCharLength = newPwdChar.length;
							int iNoOfNumericChars = 0;
							int iNoOfUpperCaseChars = 0;
							int iNoOfAlphabeticalChars = 0;
							boolean exceededMaxRepeatedChars = this
									.checkIfExceedsMaxRepeatedChars(newPassword);
							for (int i = 0; i < pwdCharLength; i++) {
								char c = newPwdChar[i];
								if (newPwdChar[i] == 33) {
									// XB - 319
									strErrorMessage
											.add("The password must not contain the character '"
													+ newPwdChar[i] + "'");
									// countSpclChar++;
								}

								if (newPwdChar[i] == 36) {
									// XB - 319
									strErrorMessage
											.add("The password must not contain the character '"
													+ newPwdChar[i] + "'");
									// countSpclChar++;
								}

								if (newPwdChar[i] == 63) {
									// XB - 319
									strErrorMessage
											.add("The password must not contain the character '"
													+ newPwdChar[i] + "'");
									// countSpclChar++;
								}
								if (Character.isUpperCase(c)
										|| Character.isLowerCase(c)) {
									iNoOfAlphabeticalChars++;
								}
								if (Character.isDigit(c)) {
									iNoOfNumericChars++;
								}
								if (Character.isUpperCase(c)) {
									iNoOfUpperCaseChars++;
								}
							}
							if (newPassword.toUpperCase().contains(
									useLoginID.toUpperCase())) {
								// request.getSession().setAttribute("errorNote","The password cannot contain the user's login ID. Please revise and try again.");
								strErrorMessage
										.add("The password must not contain the user's login ID. ");
								setSuccess(false);
								setSaveAddUser(false);
								// return REDIRECT;
							}
							if (iNoOfAlphabeticalChars < 2) {
								// request.getSession().setAttribute("errorNote","The password must contain at least 2 alpha characters. Please revise and try again.");
								strErrorMessage
										.add("The password must contain at least 2 alpha characters.");
								setSuccess(false);
								setSaveAddUser(false);
								// return REDIRECT;
							}
							if (iNoOfNumericChars < 1) {
								// request.getSession().setAttribute("errorNote","The password must contain at least 1 numeric character. Please revise and try again.");
								strErrorMessage
										.add("The password must contain at least 1 numeric character. ");
								setSuccess(false);
								setSaveAddUser(false);
								// return REDIRECT;
							}
							if (iNoOfUpperCaseChars < 1) {
								// request.getSession().setAttribute("errorNote","The password must contain at least 1 uppercase character. Please revise and try again.");
								strErrorMessage
										.add("The password must contain at least 1 uppercase character.");
								setSuccess(false);
								setSaveAddUser(false);
								// return REDIRECT;
							}
							if (exceededMaxRepeatedChars) {
								// request.getSession().setAttribute("errorNote","The password cannot exceed more than 2 consecutive repeated characters. Please revise and try again.");
								strErrorMessage
										.add("The password must not exceed more than 2 consecutive repeated characters.");
								setSuccess(false);
								setSaveAddUser(false);
								// return REDIRECT;
							}
							// commented for XB - 319
							/*
							 * if(countSpclChar > 0){
							 * //request.getSession().setAttribute("errorNote",
							 * "The password cannot contain $, ? or ! characters. Please revise and try again."
							 * );
							 * 
							 * setSuccess(false); setSaveAddUser(false);
							 * //return REDIRECT; }
							 */
							// }
							// start XB - 319
							if (strErrorMessage.size() > 0) {
								strErrorMessage
										.add("Please revise and try again.");
								request.getSession().setAttribute("errorNote",
										strErrorMessage);
								return REDIRECT;
							}
							// end XB - 319

							// Fix End For Jira-3106
							valuemap.put(
									"/Customer/CustomerContactList/CustomerContact/User/@Password",
									newPassword);
						}
						if (checkIfAnswerChanged()) {
							String secretQuestionKey = wcContext
									.getSCUIContext().getRequest()
									.getParameter("secretQuestion");
							String secretAnswer = wcContext.getSCUIContext()
									.getRequest().getParameter("secretAnswer");
							Element userElem = (SCXmlUtils
									.getElements(userContactInput,
											"/CustomerContactList/CustomerContact/User"))
									.get(0);
							Element authAnswerListElem = SCXmlUtil.createChild(
									userElem, "AuthAnswerList");
							authAnswerListElem.setAttribute("Reset", "Y");
							Element authAnswerElem = SCXmlUtil.createChild(
									authAnswerListElem, "AuthAnswer");
							authAnswerElem.setAttribute("AuthQuestionKey",
									secretQuestionKey);
							authAnswerElem.setAttribute("Answer", secretAnswer);
						}
						populateMashupInput(MANAGE_USER_MASHUP, valuemap,
								userContactInput);
						invokeMashup(MANAGE_USER_MASHUP, userContactInput);
					} else {

						// prepareAndInvokeMashup(MANAGE_USER_MASHUP);

						invokeMashup(MANAGE_USER_MASHUP, userContactInput);
					}
				} else {
					// New user being created for Existing Contact
					setUserNameAttribute(getFirstName() + " " + getLastName());
					valuemap.put(
							"/Customer/CustomerContactList/CustomerContact/User/@GeneratePassword",
							YES);
					valuemap.put(
							"/Customer/CustomerContactList/CustomerContact/User/@UserName",
							getUserNameAttribute());
					populateMashupInput(MANAGE_USER_MASHUP, valuemap,
							userContactInput);
					invokeMashup(MANAGE_USER_MASHUP, userContactInput);
				}
				// set the attribute information in the session if the
				// loggedInUser=editedUser
				if (wcContext.getCustomerContactId().equals(
						getCustomerContactId())) {
					XPEDXCustomerContactInfoBean xpedxCustomerContactInfoBean = (XPEDXCustomerContactInfoBean) XPEDXWCUtils
							.getObjectFromCache(XPEDXConstants.XPEDX_Customer_Contact_Info_Bean);
					xpedxCustomerContactInfoBean
							.setExtnViewPricesFlag(getViewPrices());
					xpedxCustomerContactInfoBean
							.setExtnViewReportsFlag(getViewReports());
					// XB - 537 - start
					String prefCatalog = request.getParameter("PrefCatalog");
					xpedxCustomerContactInfoBean.setFirstName(getFirstName());
					xpedxCustomerContactInfoBean.setLastName(getLastName());
					xpedxCustomerContactInfoBean
							.setExtnViewInvoices(getViewInvoices());

					boolean isEstimator = getEstimator().equals("Y") ? true
							: false;
					xpedxCustomerContactInfoBean.setIsEstimator(isEstimator);

					xpedxCustomerContactInfoBean
							.setExtnPrefCatalog(prefCatalog);

					String selectView = null;
					switch (Integer.parseInt(getB2bCatalogView())) {
					case 0:
						selectView = "normal-view";
						break;
					case 1:
						selectView = "condensed-view";
						break;
					case 2:
						selectView = "mini-view";
						break;
					case 3:
						selectView = "papergrid-view";
						break;
					default:
						selectView = "normal-view";
						break;

					}
					request.getSession().setAttribute("selectedView",
							selectView);
					// XB - 537 - end

					// wcContext.getSCUIContext().getSession().setAttribute("viewPricesFlag",
					// getViewPrices());
					// wcContext.getSCUIContext().getSession().setAttribute("viewReportsFlag",
					// getViewReports());
					xpedxCustomerContactInfoBean
							.setOrderApproveFlag(getOrderApprovalFlag());// added
					// for
					// XB
					// 226
					// added for jira 3780
					xpedxCustomerContactInfoBean.setEmailID(getEmailId());
					XPEDXWCUtils.setObectInCache(
							XPEDXConstants.XPEDX_Customer_Contact_Info_Bean,
							xpedxCustomerContactInfoBean);
				}
			}
			saveXPXCustomerContactFields();
			// For Jira 1998 - checking if emaill address is updated, if yes,
			// then calling service to send the email
			String newEmailId = wcContext.getSCUIContext().getRequest()
					.getParameter("emailId");
			String oldEmailId = (String) wcContext.getSCUIContext()
					.getSession().getAttribute("emailId");
			String useLoginID = wcContext.getSCUIContext()
            .getRequest().getParameter("userName");

			
			if (oldEmailId != null && newEmailId != null
					&& ((checkIfEmailChanged(oldEmailId, newEmailId)) || (checkIfAnswerChanged()) || (checkIfPasswordChanged()))) {
				updateEMailAddress(oldEmailId, newEmailId,useLoginID);
			}
		} catch (YFCException passexp) {
			// This exception is put here to handle the password validation
			// exceptions.
			if (log.isDebugEnabled()) {
				log.debug("GOT EXCEPTION");
			}
			pwdValidationResultMap = new HashMap<String, String>();
			pwdValidationResultMap.put(passexp.getAttribute("ErrorCode"),
					passexp.getAttribute("ErrorDescription"));
			setPwdValidationResultMap(pwdValidationResultMap);
			setSuccess(false);
			setSaveAddUser(false);
			return ERROR;
		} catch (XMLExceptionWrapper xmlEx) {
			log.error("Error in saving the User/Contact Information",
					xmlEx.getCause());
			// Added For Jira-3106
			YFCElement errorMsgElement = xmlEx.getXML();

			boolean isPPMesage = false;
			if (errorMsgElement != null) {
				YFCNodeList<YFCElement> errors = errorMsgElement
						.getElementsByTagName("Error");
				if (errors != null) {
					YFCElement errorMessages = errors.item(0);
					String errorMsg = "";
					if (errorMessages != null) {
						errorMsg = errorMessages
								.getAttribute("ErrorDescription");
						String errorCode = errorMessages
								.getAttribute("ErrorCode");
						if (errorMsg != null && errorMsg.trim().length() != 0) {
							// Added for JIRA 3553
							if (errorCode.equalsIgnoreCase("YCP0468")
									|| errorMsg
											.equalsIgnoreCase("The new password matches one of the old password.")) {
								setSuccess(true);
								setSaveAddUser(true);
								return REDIRECT;
							} else {
								isPPMesage = true;
								request.getSession().setAttribute("errorNote",
										errorMsg);
							}
						}
					}

				}
			}
			if (!isPPMesage)
				request.getSession()
						.setAttribute("errorNote",
								"The password is invalid. Please revise and try again.");
			// Fix End For Jira-3106
			setSuccess(false);
			setSaveAddUser(false);
			return REDIRECT;
		} catch (CannotBuildInputException e) {
			log.error("Error in creating the User/Contact Information",
					e.getCause());
			setSuccess(false);
			setSaveAddUser(false);
			return ERROR;
		}
		setSuccess(true);
		setSaveAddUser(true);
		return REDIRECT;
	}

	// Added for 3106
	private boolean checkIfExceedsMaxRepeatedChars(String newPassword) {

		ArrayList<String> setOfRepeatedChars = new ArrayList<String>();
		// Commented for JIRA 1454
		// newPassword = newPassword.toUpperCase();
		char[] cArray = newPassword.toCharArray();
		char c1, c2;
		boolean exceededMaxRepeatedChars = false;
		for (int i = 0; i < cArray.length; i++) {
			c1 = cArray[i];

			if ((i + 1) < cArray.length) {
				for (int j = i + 1; j < cArray.length; j++) {
					c2 = cArray[j];

					if (c1 == c2) {

						setOfRepeatedChars.add(String.valueOf(c1));
						setOfRepeatedChars.add(String.valueOf(c2));

						if (setOfRepeatedChars.size() > 2) {
							exceededMaxRepeatedChars = true;
							break;
						}

					} else {
						setOfRepeatedChars.clear();
						break;
					}
				}
			}
			if (setOfRepeatedChars.size() > 2) {
				exceededMaxRepeatedChars = true;
				break;
			}
		}

		return exceededMaxRepeatedChars;
	}

	private void saveXPXCustomerContactFields() {
		String addnlEmailAddrs = null;
		String addnlPoList = null;
		String custContRefKey = null;
		boolean createCCExtn = false;
		Map<String, String> attributeMap = new HashMap<String, String>();

		Element xpxCustContExtnEle = XPEDXWCUtils.getXPXCustomerContactExtn(
				wcContext, this.customerContactId);
		if (xpxCustContExtnEle == null)
			createCCExtn = true;
		else {
			custContRefKey = xpxCustContExtnEle.getAttribute("CustContRefKey");
		}

		StringBuffer extnAddnlEmailAddrs = new StringBuffer();
		StringBuffer extnPoList = new StringBuffer();

		if (!YFCCommon.isStringVoid(getAddnlEmailAddrText())
				&& !"null".equals(getAddnlEmailAddrText().trim())) {
			addnlEmailAddrs = getAddnlEmailAddrText();
			String[] splitAddrs = addnlEmailAddrs.split(",");
			Set<String> addrs = new HashSet<String>();
			for (int i = 0; i < splitAddrs.length; i++) {
				addrs.add(splitAddrs[i].trim());
			}

			for (String addr : addrs) {
				extnAddnlEmailAddrs.append(addr.trim() + ",");
			}

			// valueMap.put("/Customer/CustomerContactList/CustomerContact/Extn/@ExtnAddnlEmailAddrs",extnAddnlEmailAddrs.toString());
		}
		attributeMap.put(XPEDXConstants.XPX_CUSTCONTACT_EXTN_ADDLN_EMAIL_ATTR,
				extnAddnlEmailAddrs.toString());

		if (custContRefKey != null && custContRefKey.length() > 0)
			attributeMap.put(XPEDXConstants.XPX_CUSTCONTACT_EXTN_REF_ATTR,
					custContRefKey);

		if (!YFCCommon.isVoid(getPOListText())
				&& !"null".equals(getPOListText())) {
			addnlPoList = getPOListText();
			String[] splitPOs = addnlPoList.split(",");
			Set<String> pos = new HashSet<String>();
			for (int i = 0; i < splitPOs.length; i++) {
				pos.add(splitPOs[i].trim());
			}

			for (String po : pos) {
				extnPoList.append(po.trim() + ",");
			}
		}
		attributeMap.put(XPEDXConstants.XPX_CUSTCONTACT_EXTN_PO_LIST_ATTR,
				extnPoList.toString());
		Element outDoc = XPEDXWCUtils.updateXPXCustomerContactExtn(wcContext,
				this.customerContactId, createCCExtn, attributeMap);
		// Code Added For Fix XNGTP-3088
		if (customerContactId.equals(getWCContext().getCustomerContactId())) {

			getWCContext().setWCAttribute("addnlPOList",
					outDoc.getAttribute("POList"),
					WCAttributeScope.LOCAL_SESSION);
			getWCContext().setWCAttribute("lastLoginDate",
					outDoc.getAttribute("LastLoginDate"),
					WCAttributeScope.LOCAL_SESSION);
			getWCContext().setWCAttribute("addnlEmailAddrs",
					outDoc.getAttribute("AddnlEmailAddrs"),
					WCAttributeScope.LOCAL_SESSION);

		}
		// End Fix For XNGTP-3088
	}

	/**
	 * This method is invoked while changing the password for a user on the UI
	 * 
	 * @return
	 */
	public String validatePassword() {
		HashMap<String, String> userInfoMap = new HashMap<String, String>();
		userInfoMap.put("firstName", getFirstName());
		userInfoMap.put("lastName", getLastName());
		userInfoMap.put("newPassword", getUserPwdToValidate());
		userInfoMap.put("loginId", wcContext.getLoggedInUserId());

		String returnStr = SUCCESS;
		PasswordPolicyResult pwdValidationResult;
		Map<String, String> resultMap = null;
		try {
			// validate client password
			pwdValidationResult = XPEDXClientPasswordValidator
					.validateClientPassword(userInfoMap);
			if (ResultType.FAILURE.equals(pwdValidationResult.getResultType())) {
				String errorMsg = (String) pwdValidationResult.getResultMap()
						.get("ErrorMsg");
				if (errorMsg != null) {
					resultMap = new HashMap<String, String>();
					resultMap.put("ErrorMsg", errorMsg);
					setPwdValidationResultMap(resultMap);
				}
			}
		} catch (Exception e) {
			if (e instanceof YFCException) {
				pwdValidationResultMap = new HashMap<String, String>();
				pwdValidationResultMap.put(
						((YFCException) e).getAttribute("ErrorCode"),
						((YFCException) e).getAttribute("ErrorDescription"));
			}
		}
		return returnStr;
	}

	// Adding a new method for jira 3992 with return type as HashMap
	public String validateResetPassword() {
		HashMap<String, String> userInfoMap = new HashMap<String, String>();
		HashMap errorMap = new HashMap();
		// userInfoMap.put("firstName", getFirstName());
		// userInfoMap.put("lastName", getLastName());
		userInfoMap.put("newPassword", getUserPwdToValidate());
		// userInfoMap.put("loginId", wcContext.getLoggedInUserId());
		if (getLoginId() != null && getLoginId().equalsIgnoreCase("")) {
			userInfoMap.put("loginId", wcContext.getLoggedInUserId());
		} else
			userInfoMap.put("loginId", getLoginId());

		String returnStr = SUCCESS;

		try {
			// validate Reset password
			errorMap = XPEDXClientPasswordValidator
					.validateClientAllPassword(userInfoMap);
			if (null != errorMap && errorMap.size() > 0) {
				setPwdValidationResultMap(errorMap);
			}
		} catch (Exception e) {
			if (e instanceof YFCException) {
				pwdValidationResultMap = new HashMap<String, String>();
				pwdValidationResultMap.put(
						((YFCException) e).getAttribute("ErrorCode"),
						((YFCException) e).getAttribute("ErrorDescription"));
			}
			log.error("Error in ValidateResetPassword method ", e.getCause());
		}
		return returnStr;
	}

	/**
	 * <CustomerAssignment CustomerID="" OrganizationCode="" UserId=""
	 * Operation="" />
	 * 
	 * @return
	 */
	private void saveChanges() {
		/* Begin - Changes made for XB-287 : Save functionality */
		
		List<String> alOldAssignedCustomers = new ArrayList<String>();
		//List<String> alFinalSelectedCustomers = new ArrayList<String>();
		if (getNewAssignedCustomers().indexOf(",") != -1) {
			alNewSelectedCustomers = Arrays.asList(getNewAssignedCustomers()
					.split(","));
		} else {
			alNewSelectedCustomers = Arrays.asList(getNewAssignedCustomers());
		}
		alOldAssignedCustomers = (List) XPEDXWCUtils
				.getObjectFromCache("AUTHORIZED_LOCATIONS");

		if(("false").equalsIgnoreCase(getUserNotAdmin())){
		//if (("true").equalsIgnoreCase(getBuyAdmin())) {
		/* End - Changes made for XB-287 : Save functionality */
		String tmpCustomerId = "";
		for (int i = 0; i < alNewSelectedCustomers.size(); i++) {
			tmpCustomerId = (String) alNewSelectedCustomers.get(i).trim();
			if (alOldAssignedCustomers.contains(tmpCustomerId)) {
				alOldAssignedCustomers.remove(tmpCustomerId);
			} else {
				alFinalSelectedCustomers.add(tmpCustomerId.trim());
			}

		}
		
		log.debug("Old Assigned Customers"+alOldAssignedCustomers.toString());
		log.debug("Final selected Customers"+alFinalSelectedCustomers.toString());
		manageCustomerAssigmentInputDoc = SCXmlUtil
				.createDocument("ManageCustomerAndAssignment");
		Element customerAssignmentListElem = SCXmlUtil.createChild(
				manageCustomerAssigmentInputDoc.getDocumentElement(),
				"CustomerAssignmentList");
		saveChanges(alFinalSelectedCustomers, "Create",
				customerAssignmentListElem);
		saveChanges(alOldAssignedCustomers, "Delete",
				customerAssignmentListElem);
		NodeList customerAssignementNodeList = customerAssignmentListElem
				.getElementsByTagName("CustomerAssignment");
		log.debug("Input for manage customer SERVICE is "
				+ SCXmlUtil.getString(manageCustomerAssigmentInputDoc));
		if (customerAssignementNodeList != null
				&& customerAssignementNodeList.getLength() > 0) {
			WCMashupHelper.invokeMashup("XPEDXManageCustomerAndAssignment",
					manageCustomerAssigmentInputDoc.getDocumentElement(),
					wcContext.getSCUIContext());
		}
		 }

		if (getSpendingLtCurrency() != null
				&& getSpendingLtCurrency().equals("-1")) {
			setSpendingLtCurrency("");
		}
	}

	private void saveChanges(List<String> wList, String operation,
			Element customerAssignmentListElem) {

		ArrayList<String> listOfShipTo = new ArrayList<String>();
		try {
			if (operation.equals("Delete"))
				//listOfShipTo = getAllShipTos(wList);
				listOfShipTo = getAllShipTos(alNewSelectedCustomers);
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (int index = 0; index < wList.size(); index++) {
			try {
				if (wList.get(index) != null
						&& wList.get(index).trim().length() == 0)
					continue;
				Element customerAssignmentElem = SCXmlUtil.createChild(
						customerAssignmentListElem, "CustomerAssignment");
				customerAssignmentElem.setAttribute("CustomerID",
						wList.get(index));
				customerAssignmentElem.setAttribute("OrganizationCode",
						getWCContext().getBuyerOrgCode());
				customerAssignmentElem
						.setAttribute("UserId", customerContactId);
				customerAssignmentElem.setAttribute("Operation", operation);
				if (operation.equals("Delete")) {
					if (listOfShipTo.size() == 0) {
						defaultShipTo = "";
						XPEDXWCUtils.setObectInCache(
								XPEDXConstants.DEFAULT_SHIP_TO_CHANGED, "true");
						XPEDXWCUtils.setObectInCache(
								XPEDXConstants.CHANGE_SHIP_TO_IN_TO_CONTEXT,
								"true");
					}

					if (!listOfShipTo.contains(defaultShipTo)) {
						defaultShipTo = "";
						if (customerContactId.equals(wcContext
								.getLoggedInUserId())) {
							XPEDXWCUtils.setObectInCache(
									XPEDXConstants.DEFAULT_SHIP_TO_CHANGED,
									"true");
							XPEDXWCUtils
									.setObectInCache(
											XPEDXConstants.CHANGE_SHIP_TO_IN_TO_CONTEXT,
											"true");
						}
					}
				}
				/*
				 * String inputXml = SCXmlUtil.getString(input);
				 * LOG.debug("Input XML: " + inputXml); Object obj =
				 * WCMashupHelper.invokeMashup( "xpedxSaveCustomerAssignments",
				 * input, wcContext .getSCUIContext()); Document outputDoc =
				 * null; if (obj != null) { outputDoc = ((Element)
				 * obj).getOwnerDocument(); if (null != outputDoc) { String
				 * outputXml = SCXmlUtil.getString((Element) obj);
				 * LOG.debug("Output XML: " + outputXml); } }
				 */
			} catch (Exception ex) {
				log.debug("Record already exists");
			}
		}
	}

	// Commented for JIRA 488 save performance issue.

	/*
	 * private void saveChanges(List<String> wList, String operation) { for (int
	 * index = 0; index < wList.size(); index++) { try { Map<String, String>
	 * valueMap = new HashMap<String, String>();
	 * valueMap.put("/CustomerAssignment/@CustomerID", wList .get(index));
	 * valueMap.put("/CustomerAssignment/@OrganizationCode",
	 * getWCContext().getBuyerOrgCode());
	 * valueMap.put("/CustomerAssignment/@UserId", customerContactId);
	 * valueMap.put("/CustomerAssignment/@Operation", operation); Element input
	 * = WCMashupHelper.getMashupInput( "xpedxSaveCustomerAssignments",
	 * valueMap, wcContext .getSCUIContext()); if(operation.equals("Delete")){
	 * try { ArrayList<String> listOfShipTo=getAllShipTos(wList);
	 * if(listOfShipTo.size() == 0 ){ defaultShipTo = "";
	 * XPEDXWCUtils.setObectInCache(XPEDXConstants.DEFAULT_SHIP_TO_CHANGED,
	 * "true");
	 * XPEDXWCUtils.setObectInCache(XPEDXConstants.CHANGE_SHIP_TO_IN_TO_CONTEXT
	 * ,"true" ); }
	 * 
	 * if(!listOfShipTo.contains(defaultShipTo)) { defaultShipTo = "";
	 * if(customerContactId.equals(wcContext.getLoggedInUserId())) {
	 * XPEDXWCUtils.setObectInCache(XPEDXConstants.DEFAULT_SHIP_TO_CHANGED,
	 * "true");
	 * XPEDXWCUtils.setObectInCache(XPEDXConstants.CHANGE_SHIP_TO_IN_TO_CONTEXT
	 * ,"true" ); } } } catch(Exception e) { e.printStackTrace(); } } String
	 * inputXml = SCXmlUtil.getString(input); LOG.debug("Input XML: " +
	 * inputXml); Object obj = WCMashupHelper.invokeMashup(
	 * "xpedxSaveCustomerAssignments", input, wcContext .getSCUIContext());
	 * Document outputDoc = null; if (obj != null) { outputDoc = ((Element)
	 * obj).getOwnerDocument(); if (null != outputDoc) { String outputXml =
	 * SCXmlUtil.getString((Element) obj); LOG.debug("Output XML: " +
	 * outputXml); } } } catch (Exception ex) {
	 * log.debug("Record already exists"); } } }
	 */

	private ArrayList<String> getAllShipTos(List<String> wList)
			throws Exception {
		ArrayList<String> shipToStr = new ArrayList<String>();
		Document inputDoc = SCXmlUtil.createDocument("XPXCustHierarchyView");
		Element complexQuery = inputDoc.createElement("ComplexQuery");
		Element or = inputDoc.createElement("Or");
		ISCUITransactionContext scuiTransactionContext = getWCContext()
				.getSCUIContext().getTransactionContext(true);
		YFSEnvironment env = (YFSEnvironment) scuiTransactionContext
				.getTransactionObject(SCUITransactionContextFactory.YFC_TRANSACTION_OBJECT);
		boolean isAPICall = false;
		for (int i = 0; i < oldAssignedCustomers.size(); i++) {
			if (wList.contains(oldAssignedCustomers.get(i).trim()))
				continue;
			createInput(inputDoc, oldAssignedCustomers.get(i), complexQuery, or);
			isAPICall = true;
		}
		for (int k = 0; k < alNewSelectedCustomers.size(); k++) {
			createInput(inputDoc, alNewSelectedCustomers.get(k), complexQuery, or);
			isAPICall = true;
		}
		if (!isAPICall)
			return shipToStr;

		YIFApi api = YIFClientFactory.getInstance().getApi();
		Document outputListDocument = api.executeFlow(env,
				"XPXCustomerHierarchyViewService", inputDoc);
		Element custView = outputListDocument.getDocumentElement();
		ArrayList<Element> xpxCustViewElems = SCXmlUtil.getElements(custView,
				"XPXCustHierarchyView");
		for (int j = 0; j < xpxCustViewElems.size(); j++) {
			shipToStr.add(xpxCustViewElems.get(j).getAttribute(
					"ShipToCustomerID"));
		}
		return shipToStr;
	}

	private void createInput(Document inputDoc, String customerID,
			Element complexQuery, Element or) {
		Element shipToExp = inputDoc.createElement("Exp");
		shipToExp.setAttribute("Name", "ShipToCustomerID");
		shipToExp.setAttribute("Value", customerID);
		or.appendChild(shipToExp);

		Element billToExp = inputDoc.createElement("Exp");
		billToExp.setAttribute("Name", "BillToCustomerID");
		billToExp.setAttribute("Value", customerID);
		or.appendChild(billToExp);

		Element sapExp = inputDoc.createElement("Exp");
		sapExp.setAttribute("Name", "SAPCustomerID");
		sapExp.setAttribute("Value", customerID);
		or.appendChild(sapExp);

		Element msapExp = inputDoc.createElement("Exp");
		msapExp.setAttribute("Name", "MSAPCustomerID");
		msapExp.setAttribute("Value", customerID);
		or.appendChild(msapExp);

		complexQuery.appendChild(or);
		inputDoc.getDocumentElement().appendChild(complexQuery);
	}

	/**
	 * Setting some input XML attributes like User roles, Customer/User status
	 * based on the request parameters
	 * 
	 * @return
	 */
	private void overrideContactAttributes() {
		setContactStatus(getStatus());
		if (!(YFCCommon.isVoid(wcContext.getSCUIContext().getRequest()
				.getParameter("userName")))) {
			if (getContactStatus() != null
					&& (getContactStatus().equals(CUSTOMER_STATUS_ACTIVE) || getContactStatus()
							.equals(CUSTOMER_STATUS_ON_HOLD))) {
				setUserStatus(YES);
			}
			this.userRoleList.add(BUYER_USER_GROUP_KEY);
			if ("true".equalsIgnoreCase(wcContext.getSCUIContext().getRequest()
					.getParameter("buyerAdmin"))) {
				this.userRoleList.add(BUYER_ADMIN_GROUP_KEY);
			}
			if ("true".equalsIgnoreCase(wcContext.getSCUIContext().getRequest()
					.getParameter("buyerApprover"))) {
				this.userRoleList.add(BUYER_APPROVER_GROUP_KEY);
			}
		}
		setViewInvoices(viewInvoices.equals("true") ? "Y" : "N");
		setPunchoutUsers(punchoutUsers.equals("true") ? "T" : "F");
		setStockCheckWebservice(stockCheckWebservice.equals("true") ? "T" : "F");
		setEstimator(estimator.equals("true") ? "Y" : "N");
		setViewPrices("true".equals(viewPrices) ? "Y" : "N");
		setViewReports("true".equals(viewReports) ? "Y" : "N");
		setOrderConfirmationEmailFlag("true".equals(orderConfirmationEmailFlag) ? "Y"
				: "N");
		setOrderCancellationEmailFlag("true".equals(orderCancellationEmailFlag) ? "Y"
				: "N");
		setOrderShipmentEmailFlag("true".equals(orderShipmentEmailFlag) ? "Y"
				: "N");
		// setOrderUpdateEmailFlag(orderUpdateEmailFlag.equals("true")?"Y":"N");
		setBackorderEmailFlag("true".equals(backorderEmailFlag) ? "Y" : "N");
		setOrderApprovalFlag("true".equals(orderApprovalFlag) ? "Y" : "N"); // added
		// for
		// XB
		// 226
	}

	private void newUserOverrideContactAttributes() {
		setContactStatus(getStatus());
		if (!(YFCCommon.isVoid(wcContext.getSCUIContext().getRequest()
				.getParameter("userName")))) {
			if (getContactStatus().equals(CUSTOMER_STATUS_ACTIVE)
					|| getContactStatus().equals(CUSTOMER_STATUS_ON_HOLD)) {
				setUserStatus(YES);
			}
			this.userRoleList.add(BUYER_USER_GROUP_KEY);
			if ("true".equalsIgnoreCase(wcContext.getSCUIContext().getRequest()
					.getParameter("buyerAdmin"))) {
				this.userRoleList.add(BUYER_ADMIN_GROUP_KEY);
			}
			if ("true".equalsIgnoreCase(wcContext.getSCUIContext().getRequest()
					.getParameter("buyerApprover"))) {
				this.userRoleList.add(BUYER_APPROVER_GROUP_KEY);
			}
		}
		setViewInvoices(viewInvoices.equals("true") ? "Y" : "N");
		setEstimator(estimator.equals("true") ? "Y" : "N");
		setViewPrices(viewPrices.equals("true") ? "Y" : "N");
		setViewReports(viewReports.equals("true") ? "Y" : "N");
		setOrderApprovalFlag("true".equals(orderApprovalFlag) ? "Y" : "N");
	}

	/**
	 * Checks whether user has changed password from the UI by comparing to the
	 * masked constant String to be shown in the UI
	 * 
	 * @return true if password field changed
	 */

	private boolean checkIfAnswerChanged() {
		String answerParam = wcContext.getSCUIContext().getRequest()
				.getParameter("secretAnswer");
		if (YFCCommon.isVoid(answerParam)
				|| answerParam.equals(WCConstants.MASKED_SECRET_ANSWER_STRING)) {
			return false;
		}
		return true;
	}

	/**
	 * Checks whether user has changed secret answer from the UI by comparing to
	 * the masked constant String to be shown in the UI
	 * 
	 * @return true if secret answer field changed
	 */
	private boolean checkIfPasswordChanged() {
		String passwordParam = wcContext.getSCUIContext().getRequest()
				.getParameter("userpassword");
		if (YFCCommon.isVoid(passwordParam)
				|| passwordParam.equals(WCConstants.MASKED_PASSWORD_STRING)) {
			return false;
		}
		return true;
	}

	/**
	 * Returns the Customer member variable
	 * 
	 * @return customer element
	 */
	public Element getCustomer() {
		return customer;
	}

	/**
	 * Sets the customer element
	 * 
	 * @param contact
	 */
	public void setCustomer(Element contact) {
		this.customer = contact;
	}

	/**
	 * @return the user element
	 */
	public Element getUser() {
		return user;
	}

	/**
	 * Sets the user element
	 * 
	 * @param user
	 */
	public void setUser(Element user) {
		this.user = user;
	}

	/**
	 * @return the password
	 */
	public String getUserPassword() {
		return userPassword;
	}

	/**
	 * Sets the user password to the member variable
	 * 
	 * @param userPassword
	 */
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	/**
	 * @return the confirm password variable
	 */
	public String getConfirmPassword() {
		return confirmPassword;
	}

	/**
	 * Sets the Confirm Password field to the member variable
	 * 
	 * @param confirmPassword
	 */
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	/**
	 * 
	 * @return the locale member variable
	 */
	public Element getLoc() {
		return this.loc;
	}

	/**
	 * Sets the locale value to the member variable
	 * 
	 * @param locale
	 */
	public void setLoc(Element locale) {
		this.loc = locale;
	}

	/**
	 * @return the UserID
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * Sets the UserID
	 * 
	 * @param userId
	 */
	public void setUserId(String Id) {
		userId = Id;
	}

	/**
	 * @return the Organization Code
	 */
	public String getOrganizationCode() {
		return organizationCode;
	}

	/**
	 * Sets the Organization code
	 * 
	 * @param organizationCode
	 */
	public void setOrganizationCode(String orgCode) {
		organizationCode = orgCode;
	}

	/**
	 * @return the Customer Admin List Element
	 */
	public Element getCustomerAdmin() {
		return customerAdmin;
	}

	/**
	 * Sets the Customer Admin Parameter
	 * 
	 * @param customerAdmin
	 */
	public void setCustomerAdmin(Element customerAdmin) {
		this.customerAdmin = customerAdmin;
	}

	/**
	 * @return the contact
	 */
	public Element getContact() {
		return contact;
	}

	/**
	 * @param contact
	 *            the contact to set
	 */
	public void setContact(Element contact) {
		this.contact = contact;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName
	 *            the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the secretQuestion
	 */
	public String getSecretQuestion() {
		return secretQuestion;
	}

	/**
	 * @param secretQuestion
	 *            the secretQuestion to set
	 */
	public void setSecretQuestion(String secretQuestion) {
		this.secretQuestion = secretQuestion;
	}

	/**
	 * @return the secretAnswer
	 */
	public String getSecretAnswer() {
		return secretAnswer;
	}

	/**
	 * @param secretAnswer
	 *            the secretAnswer to set
	 */
	public void setSecretAnswer(String secretAnswer) {
		this.secretAnswer = secretAnswer;
	}

	/**
	 * @return the confirmAnswer
	 */
	public String getConfirmAnswer() {
		return confirmAnswer;
	}

	/**
	 * @param confirmAnswer
	 *            the confirmAnswer to set
	 */
	public void setConfirmAnswer(String confirmAnswer) {
		this.confirmAnswer = confirmAnswer;
	}

	/**
	 * @return the locale
	 */
	public String getPreferredLocale() {
		return preferredLocale;
	}

	/**
	 * @param locale
	 *            the locale to set
	 */
	public void setPreferredLocale(String preffLocale) {
		this.preferredLocale = preffLocale;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName
	 *            the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName
	 *            the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the jobTitle
	 */
	public String getJobTitle() {
		return jobTitle;
	}

	/**
	 * @param jobTitle
	 *            the jobTitle to set
	 */
	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	// /**
	// * @return the comparnyName
	// */
	// public String getComparnyName() {
	// return comparnyName;
	// }
	//
	// /**
	// * @param comparnyName the comparnyName to set
	// */
	// public void setComparnyName(String comparnyName) {
	// this.comparnyName = comparnyName;
	// }

	/**
	 * @return the deptName
	 */
	public String getDeptName() {
		return deptName;
	}

	/**
	 * @param deptName
	 *            the deptName to set
	 */
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	/**
	 * @return the buyerUser
	 */
	public String getBuyerUser() {
		return buyerUser;
	}

	/**
	 * @param buyerUser
	 *            the buyerUser to set
	 */
	public void setBuyerUser(String buyerUser) {
		this.buyerUser = buyerUser;
	}

	/**
	 * @return the buyerAdmin
	 */
	public String getBuyerAdmin() {
		return buyerAdmin;
	}

	/**
	 * @param buyerAdmin
	 *            the buyerAdmin to set
	 */
	public void setBuyerAdmin(String buyerAdmin) {
		this.buyerAdmin = buyerAdmin;
	}

	/**
	 * @return the approver
	 */
	public String getApprover() {
		return approver;
	}

	/**
	 * @param approver
	 *            the approver to set
	 */
	public void setApprover(String approver) {
		this.approver = approver;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the emailId
	 */
	public String getEmailId() {
		return emailId;
	}

	/**
	 * @param emailId
	 *            the emailId to set
	 */
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	/**
	 * @return the dayFaxNo
	 */
	public String getDayFaxNo() {
		return dayFaxNo;
	}

	/**
	 * @param dayFaxNo
	 *            the dayFaxNo to set
	 */
	public void setDayFaxNo(String dayFaxNo) {
		this.dayFaxNo = dayFaxNo;
	}

	/**
	 * @return the eveningFaxNo
	 */
	public String getEveningFaxNo() {
		return eveningFaxNo;
	}

	/**
	 * @param eveningFaxNo
	 *            the eveningFaxNo to set
	 */
	public void setEveningFaxNo(String eveningFaxNo) {
		this.eveningFaxNo = eveningFaxNo;
	}

	/**
	 * @return the dayPhone
	 */
	public String getDayPhone() {
		return dayPhone;
	}

	/**
	 * @param dayPhone
	 *            the dayPhone to set
	 */
	public void setDayPhone(String dayPhone) {
		this.dayPhone = dayPhone;
	}

	/**
	 * @return the eveningPhone
	 */
	public String getEveningPhone() {
		return eveningPhone;
	}

	/**
	 * @param eveningPhone
	 *            the eveningPhone to set
	 */
	public void setEveningPhone(String eveningPhone) {
		this.eveningPhone = eveningPhone;
	}

	/**
	 * @return the mobilePhone
	 */
	public String getMobilePhone() {
		return mobilePhone;
	}

	/**
	 * @param mobilePhone
	 *            the mobilePhone to set
	 */
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	/**
	 * @return the customerId
	 */
	public String getCustomerId() {
		return customerId;
	}

	/**
	 * @param customerId
	 *            the customerId to set
	 */
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	/**
	 * @return the customerContactId
	 */
	public String getCustomerContactId() {
		return customerContactId;
	}

	/**
	 * @param customerContactId
	 *            the customerContactId to set
	 */
	public void setCustomerContactId(String customerContactId) {
		this.customerContactId = customerContactId;
	}

	/**
	 * @return the states
	 */
	public List<String> getStates() {
		return states;
	}

	/**
	 * @param states
	 *            the states to set
	 */
	public void setStates(List<String> states) {
		this.states = states;
	}

	/**
	 * @return the operation
	 */
	public String getOperation() {
		return operation;
	}

	/**
	 * @param operation
	 *            the operation to set
	 */
	public void setOperation(String operation) {
		this.operation = operation;
	}

	/**
	 * @return the buyerOrgCode
	 */
	public String getBuyerOrgCode() {
		return buyerOrgCode;
	}

	/**
	 * @param buyerOrgCode
	 *            the buyerOrgCode to set
	 */
	public void setBuyerOrgCode(String buyerOrgCode) {
		this.buyerOrgCode = buyerOrgCode;
	}

	/**
	 * @return the contactStatus
	 */
	public String getContactStatus() {
		return contactStatus;
	}

	/**
	 * @param contactStatus
	 *            the contactStatus to set
	 */
	public void setContactStatus(String contactStatus) {
		this.contactStatus = contactStatus;
	}

	/**
	 * @return the userStatus
	 */
	public String getUserStatus() {
		return userStatus;
	}

	/**
	 * @param userStatus
	 *            the userStatus to set
	 */
	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}

	/**
	 * @return the userRoleList
	 */
	public List<String> getUserRoleList() {
		return userRoleList;
	}

	/**
	 * @param userRoleList
	 *            the userRoleList to set
	 */
	public void setUserRoleList(List<String> userRoleList) {
		this.userRoleList = userRoleList;
	}

	/**
	 * @return the userNameAttribute
	 */
	public String getUserNameAttribute() {
		return userNameAttribute;
	}

	/**
	 * @param userNameAttribute
	 *            the userNameAttribute to set
	 */
	public void setUserNameAttribute(String userNameAttribute) {
		this.userNameAttribute = userNameAttribute;
	}

	public String getBodyData() {
		return bodyData;
	}

	public void setBodyData(String bodyData) {
		this.bodyData = bodyData;
	}

	public List<String> getOldAssignedCustomers() {
		return oldAssignedCustomers;
	}

	public void setOldAssignedCustomers(List<String> oldAssignedCustomers) {
		this.oldAssignedCustomers = oldAssignedCustomers;
	}

	/**
	 * @param currentSelTab
	 *            the currentSelTab to set
	 */
	public void setCurrentSelTab(String currentSelTab) {
		this.currentSelTab = currentSelTab;
	}

	/**
	 * @return the currentSelTab
	 */
	public String getCurrentSelTab() {
		return currentSelTab;
	}

	/**
	 * @param spendingLtCurrency
	 *            the spendingLtCurrency to set
	 */
	public void setSpendingLtCurrency(String spendingLtCurrency) {
		this.spendingLtCurrency = spendingLtCurrency;
	}

	/**
	 * @return the spendingLtCurrency
	 */
	public String getSpendingLtCurrency() {
		return spendingLtCurrency;
	}

	/**
	 * @param addnlEmailAddrText
	 *            the addnlEmailAddrText to set
	 */
	public void setAddnlEmailAddrText(String addnlEmailAddrText) {
		AddnlEmailAddrText = addnlEmailAddrText;
	}

	/**
	 * @return the addnlEmailAddrText
	 */
	public String getAddnlEmailAddrText() {
		return AddnlEmailAddrText;
	}

	/**
	 * @return the pOListText
	 */
	public String getPOListText() {
		return POListText;
	}

	/**
	 * @param pOListText
	 *            the pOListText to set
	 */
	public void setPOListText(String pOListText) {
		POListText = pOListText;
	}

	/**
	 * @param userPwdToValidate
	 *            the userPwdToValidate to set
	 */
	public void setUserPwdToValidate(String userPwdToValidate) {
		this.userPwdToValidate = userPwdToValidate;
	}

	/**
	 * @return the userPwdToValidate
	 */
	public String getUserPwdToValidate() {
		return userPwdToValidate;
	}

	/**
	 * @param pwdValidationResultMap
	 *            the pwdValidationResultMap to set
	 */
	public void setPwdValidationResultMap(Map pwdValidationResultMap) {
		this.pwdValidationResultMap = pwdValidationResultMap;
	}

	/**
	 * @return the pwdValidationResultMap
	 */
	public Map getPwdValidationResultMap() {
		return pwdValidationResultMap;
	}

	/** Start of code for XB 226 **/
	public String getOrderApprovalFlag() {
		return orderApprovalFlag;
	}

	public void setOrderApprovalFlag(String orderApprovalFlag) {
		this.orderApprovalFlag = orderApprovalFlag;
	}

	/** End of Code for XB 226 **/

	/** JIRA 1998--function to call API for mailId change-- **/
	private void updateEMailAddress(String oldMailId, String changedMailId, String useLoginID) {
		// String strEnterpriseCode = "";
		String strEnterpriseCode = ""; //
		StringBuffer sb = new StringBuffer();
		strEnterpriseCode = wcContext.getStorefrontId();
		String entryType = XPEDXConstants.ENTRY_TYPE_EMAIL_UPDATE;
		String emailFromAddresses = "";
		/* Start - JIRA 3262 */

		/**
		 * Value of username and suffix is retrieve by
		 * customer_overrides.properties
		 * 
		 * */
		if (strEnterpriseCode != null && !strEnterpriseCode.trim().isEmpty()) {
			String userName = YFSSystem.getProperty("fromAddress.username");
			String suffix = YFSSystem.getProperty("fromAddress.suffix");
			sb.append(userName).append("@").append(strEnterpriseCode)
					.append(suffix);
			emailFromAddresses = sb.toString();

		}

		/**
		 * Value of imagesRootFolder is retrieve by
		 * customer_overrides.properties
		 * 
		 **/

		String imageUrl = "";
		if (strEnterpriseCode != null && !strEnterpriseCode.trim().isEmpty()) {
			String imageName = getLogoName(strEnterpriseCode);
			String imagesRootFolder = YFSSystem.getProperty("ImagesRootFolder");
			if (imagesRootFolder != null && !imagesRootFolder.trim().isEmpty()
					&& imageName != null && !imageName.trim().isEmpty()) {
				imageUrl = imagesRootFolder + imageName;
			}
		}
		/* End - JIRA 3262 */

		// Creating input xml to send an email
		Document templateEmailDoc = YFCDocument.createDocument(
				"UserUpdateEmail").getDocument();
		Element templateElement = templateEmailDoc.getDocumentElement();
		if (log.isDebugEnabled()) {
			log.debug("" + SCXmlUtil.getString(templateElement));
		}
		Element emailElement = templateEmailDoc
				.createElement("UserUpdateEmail");
		emailElement.setAttribute("BrandName", strEnterpriseCode);
		emailElement.setAttribute("EntryType", entryType);
		emailElement.setAttribute("OldEmailID", oldMailId);
		emailElement.setAttribute("newEmailID", changedMailId);
		emailElement.setAttribute("SellerOrganizationCode", strEnterpriseCode);
		emailElement.setAttribute("EmailFromAddresses", emailFromAddresses); // Start
		emailElement.setAttribute("UserName", useLoginID);
		// -Jira
		// 3262
		emailElement.setAttribute("ImageUrl", imageUrl); // Start -Jira 3262

		String inputXml = SCXmlUtil.getString(emailElement);
		LOG.debug("Input XML: " + inputXml);
		Object obj = WCMashupHelper.invokeMashup("SendOnEmailChangeEmail",
				emailElement, wcContext.getSCUIContext());
		Document outputDoc = ((Element) obj).getOwnerDocument();
		if (null != outputDoc) {
			LOG.debug("Output XML: " + SCXmlUtil.getString((Element) obj));
		}
		if (LOG.isDebugEnabled()) {
			LOG.debug("SCXmlUtil.getString((Element) obj)"
					+ SCXmlUtil.getString((Element) obj));
		}

	}

	// Start - Jira 3262
	private String getLogoName(String sellerOrgCode) {
		String _imageName = "";
		if ("xpedx".equalsIgnoreCase(sellerOrgCode)) {
			_imageName = "/xpedx_r_rgb_lo.jpg";
		} else if ("BulkleyDunton".equalsIgnoreCase(sellerOrgCode)) {
			_imageName = "/BulkleyDunton_r_rgb_lo.jpg";
		} else if ("CentralLewmar".equalsIgnoreCase(sellerOrgCode)) {
			_imageName = "/CentralLewmar_r_rgb_lo.jpg";
		} else if ("CentralMarquardt".equalsIgnoreCase(sellerOrgCode)) {
			_imageName = "/CentralMarquardt_r_rgb_lo.jpg";
		} else if ("Saalfeld".equalsIgnoreCase(sellerOrgCode)) {
			_imageName = "/Saalfeld_r_rgb_lo.jpg";
		} else if ("StrategicPaper".equalsIgnoreCase(sellerOrgCode)) {
			_imageName = "/StrategicPaper_r_rgb_lo.jpg";
		} else if ("WesternPaper".equalsIgnoreCase(sellerOrgCode)) {
			_imageName = "/WesternPaper_r_rgb_lo.jpg";
		} else if ("WhitemanTower".equalsIgnoreCase(sellerOrgCode)) {
			_imageName = "/WhitemanTower_r_rgb_lo.jpg";
		} else if ("Zellerbach".equalsIgnoreCase(sellerOrgCode)) {
			_imageName = "/Zellerbach_r_rgb_lo.jpg";
		} else if ("xpedxCanada".equalsIgnoreCase(sellerOrgCode)) {
			_imageName = "/xpedx_r_rgb_lo.jpg";
		}
		return _imageName;
	}

	/**
	 * JIRA 1998 Checks whether user has changed email address from the
	 * UserProfile by comparing to the old email id from session
	 * 
	 * @return true if email address field changed
	 */

	private boolean checkIfEmailChanged(String oldEmailId, String newEmailId) {
		if (oldEmailId == null) {
			oldEmailId = "";
		} else if (newEmailId != null
				&& newEmailId.equalsIgnoreCase(oldEmailId)) {
			return false;
		}
		return true;
	}

	// AppendQuickLinkAttributes method added for 4284 - After Review
	public void AppendQuickLinkAttributes(Element eleXPXQuickLinkList) {
		eleXPXQuickLinkList.setAttribute("Reset", "true");

		Element eleXPXQuickLink = null;
		StringTokenizer st = new StringTokenizer(getBodyData(), "||");
		int i = 1;
		while (st.hasMoreTokens()) {
			// From UserProfile1.jsp
			String token = st.nextToken();
			if (token.equals("*#?")) {
				token = "";
			}

			if (i == 1) {
				eleXPXQuickLink = SCXmlUtil.createChild(eleXPXQuickLinkList,
						"XPXQuickLink");
				eleXPXQuickLink.setAttribute("QuickLinkName", token);
			}

			if (i == 2)
				eleXPXQuickLink.setAttribute("QuickLinkUrl", token);

			if (i == 3) {
				eleXPXQuickLink.setAttribute("ShowQuickLink", token);
			}
			if (i == 4) {
				eleXPXQuickLink.setAttribute("URLOrder", token);
				eleXPXQuickLinkList.appendChild(eleXPXQuickLink);
			}

			i++;

			if (i == 5) {
				i = 1;

			}

		}

		// return templateElement;

	}

}
