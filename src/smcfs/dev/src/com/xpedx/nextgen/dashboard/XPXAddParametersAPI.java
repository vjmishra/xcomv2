package com.xpedx.nextgen.dashboard;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Properties;
import java.util.StringTokenizer;

import org.apache.xerces.dom.DocumentImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.xpedx.nextgen.common.cent.ErrorLogger;
import com.xpedx.nextgen.common.util.XPXLiterals;
import com.yantra.interop.client.ClientVersionSupport;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientCreationException;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfc.core.YFCIterable;
import com.yantra.yfc.core.YFCObject;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.log.YFCLogCategory;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSUserExitException;

/**
 * Description: This class is used to add the parameters like WebConfirmationNumber and WebLineNumber to the confirmed draft order
 *              just before the creation of the chained order(s). This also stamps the LineType of each orderline after checking the
 *              respective item details. If the ExtnItemType of the Item has no set value, then the default value used will be STOCK_ORDER.
 *                  
 * @author JStaney.
 *
 */

public class XPXAddParametersAPI implements YIFCustomApi {
	
	private static YFCLogCategory log;
	private static YIFApi api = null;

	String template = "global/template/api/getItemList.CreateCustomerOrderService.xml";
	String getCustomerListTemplate = "global/template/api/getCustomerList.XPXCreateChainedOrderService.xml";

	static {
		log = (YFCLogCategory) YFCLogCategory.getLogger("com.xpedx.nextgen.log");
		try  {
			api = YIFClientFactory.getInstance().getApi();
		} catch (YIFClientCreationException e1) {
			e1.printStackTrace();
		}
	}

	public Document beforeChangeOrder(YFSEnvironment env, Document inputXML) throws YFSUserExitException, RemoteException {
		
		boolean setWebHoldFlag = false;
		boolean WebOrder = false;
		String willCallFlag = null;
		String webHoldFlag = null;
		String billToId = null;
	    String orderUpdateFlag = null;
	    String legacyCustomerNo = null;
	    String entryType = null;
	    String sourceType = null;
	    String customer_branch = null;
	    String environment_id =  null;
	    String company_code = null;
	    String masterCustomerName  = null;
	    String buyerOrgCode = null;
	    String shipToName = null;
	    String billToName = null;
	    String billToSuffix = null;
	    String customerEnvtId = null;
	    String carrierServiceCode = null;
	    String customerOrderingDivision = null;
	    String customerDivision = null;
	    String customerContactID = null;
	    String orderedByName = "";
	    String envtCode = "";
	    String compCode = "";
	    String shipToSuffix = "";
		
		Document getCustomerListInputDoc = null;
		Document shipToCustProfileDoc = null;
		
		YFCDocument yfcDoc = YFCDocument.getDocumentFor(inputXML);
		if (log.isDebugEnabled()) {
			log.debug("XPXAddParametersAPI-InXML:" + yfcDoc.getString());
		}
        
        YFCElement orderElem = yfcDoc.getDocumentElement();
        Element rootElem = inputXML.getDocumentElement();

		entryType = orderElem.getAttribute(XPXLiterals.A_ENTRY_TYPE);
		customerContactID = orderElem.getAttribute("CustomerContactID");
		carrierServiceCode = orderElem.getAttribute("CarrierServiceCode");
		
		if (!YFCObject.isNull(entryType) && entryType.equalsIgnoreCase("Web")) {
			WebOrder = true;
		}
		
		if(YFCObject.isNull(carrierServiceCode) || YFCObject.isVoid(carrierServiceCode)) {
			carrierServiceCode = "Standard Shipping";
		}
		
		// Set Order Type.
		orderElem.setAttribute(XPXLiterals.A_ORDER_TYPE, XPXLiterals.CUSTOMER);
		
		YFCElement orderExtnElem = orderElem.getChildElement("Extn");
		if (orderExtnElem != null) {
			willCallFlag =  orderExtnElem.getAttribute(XPXLiterals.A_EXTN_WILL_CALL_FLAG);
		}
		
		// Get ShipTo Customer Details From The Transaction.
		shipToCustProfileDoc = (Document) env.getTxnObject("ShipToCustomerProfile");
	    
	    // To Set Customer Branch.
	    if (shipToCustProfileDoc != null) {
			if(!YFCObject.isNull(entryType) && entryType.equals(XPXLiterals.SOURCE_COM)) {
	            // From COM
				customer_branch = orderElem.getAttribute(XPXLiterals.A_SHIP_NODE);
				if(YFCObject.isNull(customer_branch) || YFCObject.isVoid(customer_branch)) {
					customer_branch = getShipNodeFromCustomer(shipToCustProfileDoc);
				}
			} else {
				customer_branch = getShipNodeFromCustomer(shipToCustProfileDoc);
			}
	    } else {
	    	customer_branch = getShipNodeFromCustomer(env, inputXML);
	    }
		
		// Set The Source Type.
		if(!YFCObject.isNull(entryType) && !YFCObject.isVoid(entryType)) {
				
	        if(entryType.equalsIgnoreCase(XPXLiterals.SOURCE_EDI) || entryType.equalsIgnoreCase(XPXLiterals.SOURCE_TYPE_B2B)) {
	        	sourceType = "1";
	        } else if(entryType.equalsIgnoreCase(XPXLiterals.SOURCE_WEB)) {
	        	sourceType = "3";      
	        } else if(entryType.equalsIgnoreCase(XPXLiterals.SOURCE_COM)) {
	        	sourceType = "4";
	        } else {
	        	// Do Nothing.
	        }
		}
		
		buyerOrgCode = orderElem.getAttribute(XPXLiterals.A_BUYER_ORGANIZATION_CODE);
		if (!YFCObject.isNull(buyerOrgCode) && !YFCObject.isVoid(buyerOrgCode)) {
			
			// To Get ShipToSuffix, Environment Code And Comp Code From Buyer Organization Code.
			String[] splitArrayOnBuyerOrgCode =  buyerOrgCode.split("-");
			if (splitArrayOnBuyerOrgCode.length == 5) {
				shipToSuffix = splitArrayOnBuyerOrgCode[2];
				envtCode = splitArrayOnBuyerOrgCode[3];
				compCode = splitArrayOnBuyerOrgCode[4];
			} else {
				log.info("Invalid Buyer Organization Code.");
			}
						
			if(shipToCustProfileDoc == null) {	
				try {
		              getCustomerListInputDoc = createGetCustomerListInput(env, buyerOrgCode);
		         	  shipToCustProfileDoc = api.invoke(env,XPXLiterals.GET_CUSTOMER_LIST_API, getCustomerListInputDoc);
		         	  env.clearApiTemplate(XPXLiterals.GET_CUSTOMER_LIST_API);
		           } catch (Exception e) {
			         log.error("Unable To Get ShipTo Customer Details: " + e.getMessage());
				   }
		    }
			
			YFCElement custListElem =  YFCDocument.getDocumentFor(shipToCustProfileDoc).getDocumentElement();  
			if(custListElem.getNodeName().equalsIgnoreCase("CustomerList")) {
				
				YFCElement custElement = custListElem.getChildElement("Customer");
				if (custElement != null) {
					YFCElement custExtnElem = custElement.getChildElement("Extn");
					if (custExtnElem != null) {
						orderUpdateFlag = custExtnElem.getAttribute(XPXLiterals.A_EXTN_ORDER_UPDATE_FLAG);
						legacyCustomerNo = custExtnElem.getAttribute(XPXLiterals.A_EXTN_LEGACY_CUST_NO);
						masterCustomerName  = custExtnElem.getAttribute(XPXLiterals.A_SAP_PARENT_NAME);
						customerEnvtId = custExtnElem.getAttribute("ExtnOrigEnvironmentCode");
						customerOrderingDivision = custExtnElem.getAttribute("ExtnCustOrderBranch")+"_" + envtCode;
						customerDivision = custExtnElem.getAttribute("ExtnCustomerDivision")+"_" + envtCode;
						
						YFCElement buyerOrgElem =  custElement.getChildElement(XPXLiterals.E_BUYER_ORGANIZATION);
						if (buyerOrgElem != null) {
							shipToName = buyerOrgElem.getAttribute(XPXLiterals.A_ORGANIZATION_NAME);
						}
						
						YFCElement parentCustElem = custElement.getChildElement(XPXLiterals.E_PARENT_CUSTOMER);
						if (parentCustElem != null) {
							billToId = parentCustElem.getAttribute(XPXLiterals.A_CUSTOMER_ID);
						}
						
						if(!YFCObject.isNull(billToId) && !YFCObject.isVoid(billToId)) {
							String[] splitArrayOnBillToId = billToId.split("-");
							billToSuffix = splitArrayOnBillToId[2];											         
						}
					}
				}							
			}
		
			// Get Bill To Name from Transaction Object.
			billToName = getBillToNameFromEnv(env);
			if (billToName == null) {
				// To Stamp Bill To Name.	
			    if (!YFCObject.isNull(billToId) && !YFCObject.isVoid(billToId)) {
			    	getCustomerListInputDoc = createGetCustomerListInput(env, billToId);			    	
			    	try  {						
						Document getParentCustomerListOutputDoc = api.invoke(env,XPXLiterals.GET_CUSTOMER_LIST_API, getCustomerListInputDoc);
						env.clearApiTemplate(XPXLiterals.GET_CUSTOMER_LIST_API);
						if(getParentCustomerListOutputDoc.getDocumentElement().getElementsByTagName(XPXLiterals.E_CUSTOMER).getLength() > 0) {
							Element customerElement  = (Element)getParentCustomerListOutputDoc.getDocumentElement().getElementsByTagName(XPXLiterals.E_CUSTOMER).item(0);
							Element buyerOrgElement = (Element)customerElement.getElementsByTagName(XPXLiterals.E_BUYER_ORGANIZATION).item(0);
							billToName = buyerOrgElement.getAttribute(XPXLiterals.A_ORGANIZATION_NAME);							
						}	
					} catch (Exception e) {
						log.error("The exception is: "+e.getMessage());
					} 
			    }
			}
		}
		
		// Check For Instructions To Set Web Hold Flag.
        NodeList instructionsList = rootElem.getElementsByTagName("Instruction");
        int instructionListSize = instructionsList.getLength();
        for (int i=0;i<instructionListSize;i++) {
              Element instructionElement = (Element) instructionsList.item(i);              
              if (instructionElement.hasAttribute("InstructionType")) {
                    String instructionType = instructionElement.getAttribute("InstructionType");           
                    if(!YFCObject.isNull(instructionType) && (instructionType.equalsIgnoreCase("HEADER") || instructionType.equalsIgnoreCase("LINE"))) {
                    	setWebHoldFlag = true;
                        break;
                    }
              }
        }           
			
		if((!YFCObject.isNull(willCallFlag) && willCallFlag.equalsIgnoreCase(XPXLiterals.BOOLEAN_FLAG_Y))
				|| (!YFCObject.isNull(orderUpdateFlag) && orderUpdateFlag.equalsIgnoreCase(XPXLiterals.BOOLEAN_FLAG_N))
					|| (setWebHoldFlag == true)) {
			webHoldFlag = XPXLiterals.BOOLEAN_FLAG_Y;
		}

		if(orderExtnElem != null) {		

			if(!YFCObject.isNull(webHoldFlag) && webHoldFlag.equalsIgnoreCase(XPXLiterals.BOOLEAN_FLAG_Y)) {
				
				orderExtnElem.setAttribute(XPXLiterals.A_EXTN_WEB_HOLD_FLAG,webHoldFlag);
				// To Set The Web Hold Reason.
				if((XPXLiterals.CONSTANT_CHAR_P).equalsIgnoreCase(willCallFlag) 
						&& !(XPXLiterals.BOOLEAN_FLAG_N).equalsIgnoreCase(orderUpdateFlag)) {					
					orderExtnElem.setAttribute(XPXLiterals.A_EXTN_WEB_HOLD_REASON, XPXLiterals.WEB_HOLD_FLAG_REASON_1);
				} else if ((XPXLiterals.BOOLEAN_FLAG_N).equalsIgnoreCase(orderUpdateFlag) 
						&& !(XPXLiterals.CONSTANT_CHAR_P).equalsIgnoreCase(willCallFlag)) {					
					orderExtnElem.setAttribute(XPXLiterals.A_EXTN_WEB_HOLD_REASON,XPXLiterals.WEB_HOLD_FLAG_REASON_2);
				} else if ((XPXLiterals.CONSTANT_CHAR_P).equalsIgnoreCase(willCallFlag) 
						&& (XPXLiterals.BOOLEAN_FLAG_N).equalsIgnoreCase(orderUpdateFlag)) {					
					orderExtnElem.setAttribute(XPXLiterals.A_EXTN_WEB_HOLD_REASON,XPXLiterals.WEB_HOLD_FLAG_REASON_1_AND_2);
				} else {
					// Do Nothing.
				}
			}
			
			orderExtnElem.setAttribute(XPXLiterals.A_EXTN_SOURCE_TYPE, sourceType);
			orderExtnElem.setAttribute(XPXLiterals.A_SAP_PARENT_NAME, masterCustomerName);
			orderExtnElem.setAttribute(XPXLiterals.A_EXTN_BILL_TO_NAME, billToName);
			orderExtnElem.setAttribute("ExtnOrigEnvironmentCode", customerEnvtId);
			orderExtnElem.setAttribute("ExtnOrderDivision", customerOrderingDivision);
			
			if(orderExtnElem.getAttribute(XPXLiterals.A_EXTN_CUSTOMER_NO) == null || orderExtnElem.getAttribute(XPXLiterals.A_EXTN_CUSTOMER_NO).trim().length() <= 0) {
				orderExtnElem.setAttribute(XPXLiterals.A_EXTN_CUSTOMER_NO, legacyCustomerNo);
			}
			
			if(orderExtnElem.getAttribute(XPXLiterals.A_EXTN_SHIP_TO_NAME) == null || orderExtnElem.getAttribute(XPXLiterals.A_EXTN_SHIP_TO_NAME).trim().length() <= 0) {
				orderExtnElem.setAttribute(XPXLiterals.A_EXTN_SHIP_TO_NAME, shipToName);
			}
			
			if(orderExtnElem.getAttribute(XPXLiterals.A_EXTN_BILL_TO_SUFFIX)==null || orderExtnElem.getAttribute(XPXLiterals.A_EXTN_BILL_TO_SUFFIX).trim().length()<=0) {
				orderExtnElem.setAttribute(XPXLiterals.A_EXTN_BILL_TO_SUFFIX, billToSuffix);
			}
			
			if(orderExtnElem.getAttribute(XPXLiterals.A_EXTN_SHIP_TO_SUFFIX)==null || orderExtnElem.getAttribute(XPXLiterals.A_EXTN_SHIP_TO_SUFFIX).trim().length()<=0)
			{
				orderExtnElem.setAttribute(XPXLiterals.A_EXTN_SHIP_TO_SUFFIX, shipToSuffix);
			}

			if(orderExtnElem.getAttribute("ExtnCustomerDivision")==null || orderExtnElem.getAttribute("ExtnCustomerDivision").trim().length()<=0) {
				orderExtnElem.setAttribute("ExtnCustomerDivision", customerDivision);
			}
			
			environment_id = orderExtnElem.getAttribute(XPXLiterals.A_EXTN_ENVT_ID);
			if(environment_id == null || environment_id.trim().length()<=0) {
				environment_id = envtCode;
				orderExtnElem.setAttribute(XPXLiterals.A_EXTN_ENVT_ID, environment_id);
			}
			
			company_code = orderExtnElem.getAttribute(XPXLiterals.A_EXTN_COMPANY_CODE);
			if(company_code == null || company_code.trim().length()<=0) {
				company_code = compCode;
				orderExtnElem.setAttribute(XPXLiterals.A_EXTN_COMPANY_CODE,company_code);				
			}
			
			orderedByName = getOrderedByNameFromEnv(env);
			if (orderedByName == null) {	
				if(!YFCObject.isNull(customerContactID) && !YFCObject.isVoid(customerContactID)) {	
					String extnOrderedByName = orderExtnElem.getAttribute("ExtnOrderedByName");				
					if(YFCObject.isNull(extnOrderedByName) || YFCObject.isVoid(extnOrderedByName)) {
					   Document getCustomerContactListInputDoc = YFCDocument.createDocument("CustomerContact").getDocument();
					   getCustomerContactListInputDoc.getDocumentElement().setAttribute("CustomerContactID", customerContactID);				   
					   Document getCustomerContactListOutputDoc = api.invoke(env, "getCustomerContactList", getCustomerContactListInputDoc);				   
					   if(getCustomerContactListOutputDoc.getDocumentElement().getElementsByTagName("CustomerContact").getLength() > 0) {
							Element customerContactElement = (Element) getCustomerContactListOutputDoc.getDocumentElement().getElementsByTagName("CustomerContact").item(0);
							String firstName = customerContactElement.getAttribute("FirstName");
							String lastName =  customerContactElement.getAttribute("LastName");
							orderedByName = firstName + "" + lastName;
							orderExtnElem.setAttribute("ExtnOrderedByName", orderedByName);
					    }
					}
				}
			}
		}		
			
		// Make complex query calls to ItemBranch, ItemCustXRef and to item catalog	
		boolean isSecondCall = false;
		Document XrefDoc = null;
	    Document ItemBranchDoc = null;
	    Document ItemDoc = null;
	    HashMap stockTypeMap = new HashMap();
	    
		if (!WebOrder) {	
			XrefDoc = invokeComplexQueryForXref(env,inputXML,legacyCustomerNo,environment_id,company_code,customer_branch);
			ItemBranchDoc = invokeComplexQueryForItemBranch(env,inputXML,legacyCustomerNo,environment_id,company_code,customer_branch);
			ItemDoc = invokeComplexQueryForItem(env,inputXML,customer_branch);
		} else {
			stockTypeMap = getStockTypeMapFromEnv(env);
		}
		
		YFCElement orderLinesElem = orderElem.getChildElement(XPXLiterals.E_ORDER_LINES);
		if (orderLinesElem != null) {
			YFCIterable<YFCElement> orderLineItr = orderLinesElem.getChildren(XPXLiterals.E_ORDER_LINE);
			while (orderLineItr.hasNext()) {
				// Iterate Through Every Line Of An Order.
				YFCElement orderLineElem = orderLineItr.next();
				String lineStatus = orderLineElem.getAttribute("Status");
				if (!YFCObject.isNull(lineStatus) && !"Cancelled".equalsIgnoreCase(lineStatus)) {
					String orderLineKey = orderLineElem.getAttribute(XPXLiterals.A_ORDER_LINE_KEY);
					String lineType = orderLineElem.getAttribute(XPXLiterals.A_LINE_TYPE);
					YFCElement orderLineExtnElem = orderLineElem.getChildElement("Extn");
					
					if (stockTypeMap != null) {
						// Web Channel.
						String stockType = (String) stockTypeMap.get(orderLineKey);
						if (!YFCObject.isNull(stockType) && !YFCObject.isVoid(stockType)) {
							orderLineExtnElem.setAttribute(XPXLiterals.A_EXTN_LINE_TYPE, stockType);
						} else {							
							// Below Condition Ensures XRef Call To Happen Only Once. 
							if (!isSecondCall) {
								XrefDoc = invokeComplexQueryForXref(env,inputXML,legacyCustomerNo,environment_id,company_code,customer_branch);
								ItemBranchDoc = invokeComplexQueryForItemBranch(env,inputXML,legacyCustomerNo,environment_id,company_code,customer_branch);
								ItemDoc = invokeComplexQueryForItem(env,inputXML,customer_branch);
								isSecondCall = true;
							}
							stampStockType(XrefDoc, ItemBranchDoc, ItemDoc, lineType, orderLineElem);
						}
					} else {
						// COM or Item Type Isn't Set From SWC.
						if (WebOrder && !isSecondCall) {
							XrefDoc = invokeComplexQueryForXref(env,inputXML,legacyCustomerNo,environment_id,company_code,customer_branch);
							ItemBranchDoc = invokeComplexQueryForItemBranch(env,inputXML,legacyCustomerNo,environment_id,company_code,customer_branch);
							ItemDoc = invokeComplexQueryForItem(env,inputXML,customer_branch);
							isSecondCall = true;
						}
						stampStockType(XrefDoc, ItemBranchDoc, ItemDoc, lineType, orderLineElem);						
					}	
				}
			}
		}

		if(log.isDebugEnabled()) {
			log.debug("XPXAddParametersAPI-OutXML: " + SCXmlUtil.getString(inputXML));
		}

		return inputXML;
	}


	private Document invokeComplexQueryForItemBranch(YFSEnvironment env, Document inputXML, String legacyCustomerNo, String environment_id,
			String company_code, String customer_branch) 
	{
		Document getComplexQueryOutputForItemBranch = null;
		 try
		 {	
			Document getComplexQueryInputForItemBranch = YFCDocument.createDocument(XPXLiterals.E_XPX_ITEM_EXTN).getDocument();
			getComplexQueryInputForItemBranch.getDocumentElement().setAttribute("EnvironmentID",environment_id);
			
			Element complexQuery = getComplexQueryInputForItemBranch.createElement("ComplexQuery");
			complexQuery.setAttribute("Operator", "AND");
			getComplexQueryInputForItemBranch.getDocumentElement().appendChild(complexQuery);
			
			Element primaryOrElement = getComplexQueryInputForItemBranch.createElement("Or");
			complexQuery.appendChild(primaryOrElement);
			
			Element inputDocRoot = inputXML.getDocumentElement();
			Element orderLines = (Element)inputDocRoot.getElementsByTagName(XPXLiterals.E_ORDER_LINES).item(0);

			NodeList orderLineList = orderLines.getElementsByTagName(XPXLiterals.E_ORDER_LINE);	
	        for(int i=0; i < orderLineList.getLength() ; i++)
			{
				Element orderLineElement = (Element) orderLineList.item(i);
				Element itemElement = (Element) orderLineElement.getElementsByTagName(XPXLiterals.E_ITEM).item(0);
				String itemID= itemElement.getAttribute(XPXLiterals.A_ITEM_ID);
	            String orderLineShipNode = orderLineElement.getAttribute(XPXLiterals.A_SHIP_NODE);
				
				if(orderLineShipNode == null || orderLineShipNode.trim().length()<=0)
				{
					orderLineShipNode = customer_branch;
				}

                 if(orderLineShipNode.contains("_"))
				{
					String[] splitArrayOnorderLineShipNode =  orderLineShipNode.split("_");		            
					if (splitArrayOnorderLineShipNode.length > 0) {	            
						orderLineShipNode = splitArrayOnorderLineShipNode[0];
		            }
				}
				Element secondaryOrElement = getComplexQueryInputForItemBranch.createElement("Or");
				primaryOrElement.appendChild(secondaryOrElement);
				
				Element secondaryAndElement = getComplexQueryInputForItemBranch.createElement("And");
				secondaryOrElement.appendChild(secondaryAndElement);
																
				Element customerDivisionExpElement = getComplexQueryInputForItemBranch.createElement("Exp");
				customerDivisionExpElement.setAttribute("Name", "XPXDivision");
				
				customerDivisionExpElement.setAttribute("Value", orderLineShipNode);
				secondaryAndElement.appendChild(customerDivisionExpElement);
				
				Element legacyItemIdExpElement = getComplexQueryInputForItemBranch.createElement("Exp");
				legacyItemIdExpElement.setAttribute("Name", XPXLiterals.A_ITEM_ID);
				
				legacyItemIdExpElement.setAttribute("Value", itemID);
				secondaryAndElement.appendChild(legacyItemIdExpElement);
			}
	    	if(log.isDebugEnabled()){
	        log.debug("The complex query input to getItemBranchListService is: "+SCXmlUtil.getString(getComplexQueryInputForItemBranch));
	    	}
	        getComplexQueryOutputForItemBranch = api.executeFlow(env, "getItemBranchListForOPService", getComplexQueryInputForItemBranch);
	    	if(log.isDebugEnabled()){
	        log.debug("The complex query output of getItemBranchListService is: "+SCXmlUtil.getString(getComplexQueryOutputForItemBranch));
	    	}
		  }
		  catch(Exception e)
		  {
			  com.xpedx.nextgen.common.cent.Error errorObject = new com.xpedx.nextgen.common.cent.Error();
				
		      errorObject.setTransType("OP");
		      errorObject.setErrorClass("Application");
		      errorObject.setInputDoc(inputXML);
		      		      
		      errorObject.setException(e);
		
		      ErrorLogger.log(errorObject, env);
		  }
			return getComplexQueryOutputForItemBranch;
	}


	private Document invokeComplexQueryForItem(YFSEnvironment env, Document inputXML, String customer_branch)
	{
		Document getComplexQueryOutputForItem = null;
		
		try
		{
			Document getComplexQueryInputForItem = YFCDocument.createDocument(XPXLiterals.E_ITEM).getDocument();
			getComplexQueryInputForItem.getDocumentElement().setAttribute("OrganizationCode",inputXML.getDocumentElement().getAttribute("EnterpriseCode"));
			
			Element complexQuery = getComplexQueryInputForItem.createElement("ComplexQuery");
			complexQuery.setAttribute("Operator", "AND");
			getComplexQueryInputForItem.getDocumentElement().appendChild(complexQuery);
			
			Element primaryAndElement = getComplexQueryInputForItem.createElement("And");
			complexQuery.appendChild(primaryAndElement);
			
			Element secondaryOrElement = getComplexQueryInputForItem.createElement("Or");
			primaryAndElement.appendChild(secondaryOrElement);
			
			Element inputDocRoot = inputXML.getDocumentElement();
			Element orderLines = (Element)inputDocRoot.getElementsByTagName(XPXLiterals.E_ORDER_LINES).item(0);

			NodeList orderLineList = orderLines.getElementsByTagName(XPXLiterals.E_ORDER_LINE);	
	        for(int i=0; i < orderLineList.getLength() ; i++)
			{
				Element orderLineElement = (Element) orderLineList.item(i);
				Element itemElement = (Element) orderLineElement.getElementsByTagName(XPXLiterals.E_ITEM).item(0);
				String itemID= itemElement.getAttribute(XPXLiterals.A_ITEM_ID);
				
				Element itemIdExpElement = getComplexQueryInputForItem.createElement("Exp");
				itemIdExpElement.setAttribute("Name", "ItemID");
				itemIdExpElement.setAttribute("Value", itemID);
				secondaryOrElement.appendChild(itemIdExpElement);
				
			}
	    	
	        env.setApiTemplate(XPXLiterals.GET_ITEM_LIST_API, template);
	        getComplexQueryOutputForItem = api.invoke(env, "getItemList", getComplexQueryInputForItem);
	        env.clearApiTemplate(XPXLiterals.GET_ITEM_LIST_API);
	    	if(log.isDebugEnabled()){
	        log.debug("The complex query output of getItemListService is: "+SCXmlUtil.getString(getComplexQueryOutputForItem));
	    	}
			
			
		}
		catch(Exception e)
		{
			   com.xpedx.nextgen.common.cent.Error errorObject = new com.xpedx.nextgen.common.cent.Error();
			
		      errorObject.setTransType("OP");
		      errorObject.setErrorClass("Application");
		      errorObject.setInputDoc(inputXML);
		      		      
		      errorObject.setException(e);
		
		      ErrorLogger.log(errorObject, env);
		}
		
		
		return getComplexQueryOutputForItem;
	}

	

	private Document invokeComplexQueryForXref(YFSEnvironment env, Document inputXML, String legacyCustomerNo, String environment_id,
			String company_code, String customer_branch) {
		
		Document getComplexQueryOutputForXref = null;
	    try
	    {	
		Document getComplexQueryInputForXref = YFCDocument.createDocument(XPXLiterals.E_XPX_ITEM_CUST_XREF).getDocument();
		getComplexQueryInputForXref.getDocumentElement().setAttribute(XPXLiterals.A_ENVT_CODE, environment_id);
		
		Element complexQuery = getComplexQueryInputForXref.createElement("ComplexQuery");
		complexQuery.setAttribute("Operator", "AND");
		getComplexQueryInputForXref.getDocumentElement().appendChild(complexQuery);
		
		Element primaryOrElement = getComplexQueryInputForXref.createElement("Or");
		complexQuery.appendChild(primaryOrElement);
		
		Element inputDocRoot = inputXML.getDocumentElement();
		Element orderLines = (Element)inputDocRoot.getElementsByTagName(XPXLiterals.E_ORDER_LINES).item(0);

		NodeList orderLineList = orderLines.getElementsByTagName(XPXLiterals.E_ORDER_LINE);	
        for(int i=0; i < orderLineList.getLength() ; i++)
		{
			Element orderLineElement = (Element) orderLineList.item(i);
			Element itemElement = (Element) orderLineElement.getElementsByTagName(XPXLiterals.E_ITEM).item(0);
			String itemID= itemElement.getAttribute(XPXLiterals.A_ITEM_ID);
            String orderLineShipNode = orderLineElement.getAttribute(XPXLiterals.A_SHIP_NODE);
            
			if(orderLineShipNode == null || orderLineShipNode.trim().length()<=0)
			{
				orderLineShipNode = customer_branch;
			}
			
			if(orderLineShipNode.contains("_"))
			{
				String[] splitArrayOnorderLineShipNode =  orderLineShipNode.split("_");
	            if (splitArrayOnorderLineShipNode.length > 0) {	            
	           		orderLineShipNode = splitArrayOnorderLineShipNode[0];	
	            }	           	 
			}
			
			Element secondaryOrElement = getComplexQueryInputForXref.createElement("Or");
			primaryOrElement.appendChild(secondaryOrElement);
			
			Element secondaryAndElement = getComplexQueryInputForXref.createElement("And");
			secondaryOrElement.appendChild(secondaryAndElement);
			
			Element customerNumberExpElement = getComplexQueryInputForXref.createElement("Exp");
			customerNumberExpElement.setAttribute("Name", XPXLiterals.A_CUSTOMER_NO);
			customerNumberExpElement.setAttribute("Value", legacyCustomerNo);
			secondaryAndElement.appendChild(customerNumberExpElement);
			
			Element customerDivisionExpElement = getComplexQueryInputForXref.createElement("Exp");
			customerDivisionExpElement.setAttribute("Name", "CustomerDivision");
			customerDivisionExpElement.setAttribute("Value", orderLineShipNode);
			secondaryAndElement.appendChild(customerDivisionExpElement);
			
			Element legacyItemIdExpElement = getComplexQueryInputForXref.createElement("Exp");
			legacyItemIdExpElement.setAttribute("Name", XPXLiterals.A_LEGACY_ITEM_NO);
			legacyItemIdExpElement.setAttribute("Value", itemID);
			secondaryAndElement.appendChild(legacyItemIdExpElement);
		}
        
    	
        getComplexQueryOutputForXref = api.executeFlow(env, "getItemCustXrefListService", getComplexQueryInputForXref);
    	if(log.isDebugEnabled()){
    		log.debug("getComplexQueryOutputForXref: "+SCXmlUtil.getString(getComplexQueryOutputForXref));
    	}
	  }
	  catch(Exception e)
	  {
		  com.xpedx.nextgen.common.cent.Error errorObject = new com.xpedx.nextgen.common.cent.Error();
			
	      errorObject.setTransType("OP");
	      errorObject.setErrorClass("Application");
	      errorObject.setInputDoc(inputXML);
	      		      
	      errorObject.setException(e);
	
	      ErrorLogger.log(errorObject, env);
	  }
		return getComplexQueryOutputForXref;
	}

	private Document createGetCustomerListInput(YFSEnvironment env, String customerId) {
		
		Document getCustomerListInputDoc = createDocument(XPXLiterals.E_CUSTOMER);
		getCustomerListInputDoc.getDocumentElement().setAttribute(XPXLiterals.A_CUSTOMER_ID, customerId);
          
        Document customerListTemplateDoc = createDocument(XPXLiterals.E_CUSTOMER_LIST);
        Element customerListElem = customerListTemplateDoc.getDocumentElement();
        Element customerElem = customerListTemplateDoc.createElement(XPXLiterals.E_CUSTOMER);
        customerElem.setAttribute(XPXLiterals.A_CUSTOMER_ID, "");
        Element custExtnElem = customerListTemplateDoc.createElement(XPXLiterals.E_EXTN);
        custExtnElem.setAttribute(XPXLiterals.A_EXTN_ORDER_UPDATE_FLAG, "");
        custExtnElem.setAttribute(XPXLiterals.A_EXTN_LEGACY_CUST_NO,"");
        custExtnElem.setAttribute(XPXLiterals.A_EXTN_SAP_PARENT_NAME,"");
        custExtnElem.setAttribute("ExtnCustOrderBranch","");
        custExtnElem.setAttribute("ExtnCustomerDivision","");
        custExtnElem.setAttribute("ExtnOrigEnvironmentCode","");
        Element buyerOrgElem = customerListTemplateDoc.createElement(XPXLiterals.E_BUYER_ORGANIZATION);
        buyerOrgElem.setAttribute(XPXLiterals.A_ORGANIZATION_NAME, "");
        
        customerElem.appendChild(custExtnElem);
        customerElem.appendChild(buyerOrgElem);
        
        Element parentCustElem = customerListTemplateDoc.createElement(XPXLiterals.E_PARENT_CUSTOMER);
        parentCustElem.setAttribute(XPXLiterals.A_CUSTOMER_ID, "");
        customerElem.appendChild(parentCustElem);
        customerListElem.appendChild(customerElem);
        
        env.setApiTemplate("getCustomerList", customerListTemplateDoc);
        	      
        return getCustomerListInputDoc;
	 }

	public static String generateWebLineNumber(String entryType,long uniqueSequenceNo,String envtCode) {
		String webLineNumber = "";
		String uniqueSequence = "";
		String formatted = "";

		int uniqueSequenceLength = 8;
		int keyLength = String.valueOf(uniqueSequenceNo).length();
		log.debug("KeyLength = "+keyLength);
		if(keyLength < 8)
		{
			formatted = String.format("%08d", uniqueSequenceNo); 

			log.debug("Number with leading zeros: " + formatted); 
		}
		else if(keyLength > 8)
		{

			int startIndex = keyLength-uniqueSequenceLength;
			formatted = String.valueOf(uniqueSequenceNo).substring(startIndex);

		}
		else
		{
			formatted = String.valueOf(uniqueSequenceNo);
		}
		
		/* Changes made to fix issue 926 
		IF order is placed from B2B,WEB or COM instead of appending Environment ID Changed it to constant('E') */
		if(entryType != null && (XPXLiterals.SOURCE_TYPE_B2B.equals(entryType) || XPXLiterals.SOURCE_WEB.equals(entryType) 
				|| XPXLiterals.SOURCE_COM.equals(entryType))){
			envtCode = XPXLiterals.SYSTEM_SPECIFIER_WEB;
		}				
		
		webLineNumber = envtCode+formatted;

		return webLineNumber;
	}

	private Document createDocument(String docElementTag) {

		Document doc = getNewDocument();
		Element ele = doc.createElement(docElementTag);
		doc.appendChild(ele);
		return doc;
	}

	public static Document getNewDocument() {
		return new DocumentImpl();
	}

	public static Document invokeAPI(YFSEnvironment env, String templateName,
			String apiName, Document inDoc) throws Exception {

		env.setApiTemplate(apiName, templateName);
		Document returnDoc = api.invoke(env, apiName, inDoc);
		env.clearApiTemplate(apiName);

		return returnDoc;
	}

	public static Element getFirstElementByName(Element ele, String tagName) {
		StringTokenizer st = new StringTokenizer(tagName, "/");
		Element curr = ele;
		Node node;
		String tag;

		while (st.hasMoreTokens()) {
			tag = st.nextToken();
			node = curr.getFirstChild();
			while (node != null) {
				if (node.getNodeType() == Node.ELEMENT_NODE
						&& tag.equals(node.getNodeName())) {
					break;
				}
				node = node.getNextSibling();
			}

			if (node != null)
				curr = (Element) node;
			else
				return null;
		}

		return curr;
	}
	
	private String getShipNodeFromCustomer(YFSEnvironment env, Document inputDoc) throws RemoteException {
		
		String shipNode = "";
		YFCDocument getCustomerListTemplate = YFCDocument.createDocument(XPXLiterals.E_CUSTOMER_LIST);
		YFCElement eCustomerListTemp = getCustomerListTemplate.getDocumentElement();
		YFCElement eCustomerTemp = getCustomerListTemplate.createElement(XPXLiterals.E_CUSTOMER);
		eCustomerListTemp.appendChild(eCustomerTemp);
		YFCElement eExtnTemp = getCustomerListTemplate.createElement(XPXLiterals.E_EXTN);
		eCustomerTemp.appendChild(eExtnTemp);
		eExtnTemp.setAttribute(XPXLiterals.A_EXTN_SERVICE_DIVISION, "");
		eExtnTemp.setAttribute(XPXLiterals.A_EXTN_CUSTOMER_ORDER_BRANCH, "");
		eExtnTemp.setAttribute(XPXLiterals.A_EXTN_ENVIRONMENT_CODE, "");
		eExtnTemp.setAttribute("ExtnShipFromBranch", "");
		
		String billToID = inputDoc.getDocumentElement().getAttribute(XPXLiterals.A_BUYER_ORGANIZATION_CODE);
		//form the input xml
		Document inputCustomerDoc = YFCDocument.createDocument(XPXLiterals.E_CUSTOMER).getDocument();
		Element inputCustomerElement = inputCustomerDoc.getDocumentElement();
		inputCustomerElement.setAttribute(XPXLiterals.A_CUSTOMER_ID, billToID);
		
		env.setApiTemplate(XPXLiterals.GET_CUSTOMER_LIST_API, getCustomerListTemplate.getDocument());
		Document outputCustomerListDoc = api.invoke(env, XPXLiterals.GET_CUSTOMER_LIST_API, inputCustomerDoc);
		env.clearApiTemplate(XPXLiterals.GET_CUSTOMER_LIST_API);
		Element outputCustomerListElement = outputCustomerListDoc.getDocumentElement();
		NodeList customerList = outputCustomerListElement.getElementsByTagName(XPXLiterals.E_CUSTOMER);
		int cLength = customerList.getLength();
		if(cLength > 0)
		{
			Element customerElement = (Element)customerList.item(0);
			shipNode = SCXmlUtil.getXpathAttribute(customerElement, "./Extn/@ExtnShipFromBranch");
			if(shipNode.equals(""))
			{
				shipNode = SCXmlUtil.getXpathAttribute(customerElement, "./Extn/@ExtnCustOrderBranch");
			}
			//String strEnvtId = SCXmlUtil.getXpathAttribute(customerElement, "./Extn/@ExtnEnvironmentCode");
			//shipNode = XPXUtils.updateNodeSyntax(strEnvtId, shipNode);
		}
		return shipNode;
	}
	
	private String getShipNodeFromCustomer(Document shipToCustProfileDoc) throws RemoteException {
		
		String shipNode = "";

		Element outputCustomerListElement = shipToCustProfileDoc.getDocumentElement();
		NodeList customerList = outputCustomerListElement.getElementsByTagName(XPXLiterals.E_CUSTOMER);
		if(customerList.getLength() > 0) {
			Element customerElement = (Element)customerList.item(0);
			shipNode = SCXmlUtil.getXpathAttribute(customerElement, "./Extn/@ExtnShipFromBranch");
			if(YFCObject.isNull(shipNode) || YFCObject.isVoid(shipNode)) {
				shipNode = SCXmlUtil.getXpathAttribute(customerElement, "./Extn/@ExtnCustOrderBranch");
				if (YFCObject.isNull(shipNode)) {
					shipNode = "";
				}
			}
		}
		return shipNode;
	}
	
	private void stampStockType(Document xrefDoc, Document itemBranchDoc, 
			Document itemDoc, String lineType, YFCElement orderLineElem) {
		
		String itemID = "";
	    String customerItem = "";
	    String inventoryIndicator = "";
	    
	    YFCElement orderLineExtnElem = orderLineElem.getChildElement("Extn");
	    
		// Get Item ID.
		YFCElement itemElement = orderLineElem.getChildElement(XPXLiterals.E_ITEM);
		if (itemElement != null) {
			itemID = itemElement.getAttribute(XPXLiterals.A_ITEM_ID);
		}
		
		// Replacing Customer Item Number.
		if (xrefDoc!=null) {	
			if (!"M".equalsIgnoreCase(lineType) && !"C".equalsIgnoreCase(lineType)) {
				YFCDocument xrefYFCDoc = YFCDocument.getDocumentFor(xrefDoc);
				YFCElement xrefRootElem = xrefYFCDoc.getDocumentElement();
				if (xrefRootElem != null) {
					YFCIterable<YFCElement> xrefItr = xrefRootElem.getChildren(XPXLiterals.E_XPX_ITEM_CUST_XREF);
					while (xrefItr.hasNext()) {
						YFCElement xRefElem = xrefItr.next();
						String legacyItemNumber = xRefElem.getAttribute("LegacyItemNumber");
						if(itemID.equalsIgnoreCase(legacyItemNumber)) {
						 customerItem = xRefElem.getAttribute("CustomerItemNumber");						
						 itemElement.setAttribute(XPXLiterals.A_CUSTOMER_ITEM, customerItem);
						 break;
					    }
					}
				}
			}
		}
		
		try {
			
			if (!"M".equalsIgnoreCase(lineType) && !"C".equalsIgnoreCase(lineType)) {
				
				YFCDocument itemBranchYFCDoc = YFCDocument.getDocumentFor(itemBranchDoc);
				YFCElement itemBranchRootElem = itemBranchYFCDoc.getDocumentElement();
				if (itemBranchRootElem != null) {
					YFCIterable<YFCElement> itemBranchItr = itemBranchRootElem.getChildren(XPXLiterals.E_XPX_ITEM_EXTN);
					
					boolean stockTypeIdentified = false;
					while (itemBranchItr.hasNext()) {
						YFCElement xpxItemExtnElem = itemBranchItr.next();
						String _itemID = xpxItemExtnElem.getAttribute(XPXLiterals.A_ITEM_ID);
						if (itemID.equalsIgnoreCase(_itemID)) {
							inventoryIndicator = xpxItemExtnElem.getAttribute(XPXLiterals.A_INVENTORY_INDICATOR);
							if("W".equalsIgnoreCase(inventoryIndicator) || "I".equalsIgnoreCase(inventoryIndicator)) {	
							   orderLineExtnElem.setAttribute(XPXLiterals.A_EXTN_LINE_TYPE, "STOCK");
							   stockTypeIdentified = true;
							   break;
						    } else if("M".equalsIgnoreCase(inventoryIndicator)) {
						       orderLineExtnElem.setAttribute(XPXLiterals.A_EXTN_LINE_TYPE, "DIRECT");
						       stockTypeIdentified = true;
							   break;
							} else {
								// Do Nothing.
							}
						}
					}
					// Check for Stock Type In Item Table.
					if (!stockTypeIdentified) {
						// Item Catalog Will Be Checked Even If The Inventory Indicator Is Empty.
						CheckItemDoc(YFCDocument.getDocumentFor(itemDoc), itemID, orderLineExtnElem);
					}
				}
			} else {
				// Special Charge Line.
				orderLineExtnElem.setAttribute(XPXLiterals.A_EXTN_LINE_TYPE, XPXLiterals.STOCK);	
			}
		} catch (Exception e) {				
			log.error("Exception Occured On Identifying Stock Type For An Item["+itemID+"]:" + e.getMessage());
		}
	}
	
	private String CheckItemDoc(YFCDocument itemDoc, String itemID, YFCElement orderLineExtnElem) throws Exception {
		
		String _itemID = "";
		boolean stockTypeIdentified = false;
		YFCElement rootElem = itemDoc.getDocumentElement();
		YFCIterable<YFCElement> itemItr = rootElem.getChildren(XPXLiterals.E_ITEM);
		while (itemItr.hasNext()) {
			YFCElement itemElement = itemItr.next();
			_itemID = itemElement.getAttribute("ItemID");
			if (_itemID.equalsIgnoreCase(itemID)) {
				if(log.isDebugEnabled()){
			    	 log.debug("Stock Type Of Item["+itemID+"] is: Direct");
			    }
				stockTypeIdentified = true;
				orderLineExtnElem.setAttribute(XPXLiterals.A_EXTN_LINE_TYPE, XPXLiterals.DIRECT);
			    break;
			}	
		}
		
		if (!stockTypeIdentified) {
			throw new Exception("Invalid Item To Determine Stock Type:" + itemID);
		}
		return null;
	}
	
	private String getBillToNameFromEnv (YFSEnvironment env) {
		
		String billToName = null;
		if (env instanceof ClientVersionSupport) {
			ClientVersionSupport clientVersionSupport = (ClientVersionSupport) env;
			HashMap envVariablesmap = clientVersionSupport.getClientProperties();
			if (envVariablesmap != null) {
				// Order Placed From Web. Ship To Customer Profile Has Been Set In Environment By WC Application.
				billToName = (String) envVariablesmap.get("BillToName");
				if (!YFCObject.isNull(billToName)) {
					envVariablesmap.remove("BillToName");
				}
			} 
		}
		return billToName;
	}
	
	private String getOrderedByNameFromEnv (YFSEnvironment env) {
		
		String orderedByName = null;
		if (env instanceof ClientVersionSupport) {
			ClientVersionSupport clientVersionSupport = (ClientVersionSupport) env;
			HashMap envVariablesmap = clientVersionSupport.getClientProperties();
			if (envVariablesmap != null) {
				// Order Placed From Web. Ship To Customer Profile Has Been Set In Environment By WC Application.
				orderedByName = (String) envVariablesmap.get("OrderedByName");
				if (!YFCObject.isNull(orderedByName)) {
					envVariablesmap.remove("OrderedByName");
				}
			} 
		}
		return orderedByName;
	}
	
	private HashMap getStockTypeMapFromEnv (YFSEnvironment env) {
		
		HashMap stockTypeMap = new HashMap();
		if (env instanceof ClientVersionSupport) {
			ClientVersionSupport clientVersionSupport = (ClientVersionSupport) env;
			HashMap envVariablesmap = clientVersionSupport.getClientProperties();
			if (envVariablesmap != null) {
				// Order Placed From Web. Ship To Customer Profile Has Been Set In Environment By WC Application.
				stockTypeMap = (HashMap) envVariablesmap.get("ItemTypeMap");
				if (!YFCObject.isNull(stockTypeMap)) {
					envVariablesmap.remove("ItemTypeMap");
				}
			} 
		}
		return stockTypeMap;
	}

	public void setProperties(Properties arg0) throws Exception {
		// TODO Auto-generated method stub

	}
}