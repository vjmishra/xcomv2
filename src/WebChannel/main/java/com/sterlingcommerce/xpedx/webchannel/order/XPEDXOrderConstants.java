/**
 * 
 */
package com.sterlingcommerce.xpedx.webchannel.order;

import java.util.HashMap;

/**
 * @author rugrani
 * History : 
 * 			Changed by @author reddypur on June/6/2012.
 * 			Added Order related named constants.
 */
public class XPEDXOrderConstants {
	public static final String XPEDX_ORDER_TYPE_CUSTOMER = "Customer";
	public static final String XPEDX_ORDER_STATUS_OPEN = "0";
	public static final String XPEDX_ORDER_STATUS_SHIPPED = "3700";
	public static final String XPEDX_ORDER_STATUS_BACK_ORDERED = "1300";
	public static final String XPEDX_COMPLETE_ORDER_DETAIL_MASHUP = "XPEDXOrderDetailMashup";

	
	/**
	 * Order Status related constants.
	 */
	
	//Submitted
	public static final String  ORDR_STATUS_TEXT_0100_SUBMITTED = "Submitted";
	public static final String  ORDR_STATUS_TEXT_0150_BACKORDER = "BackOrder";
	public static final String  ORDR_STATUS_TEXT_0200_SUBMITTED_PENDING_APPROVAL = "Submitted(Pending Approval)";
	public static final String  ORDR_STATUS_TEXT_0250_CANCELED = "Canceled";
	public static final String  ORDR_STATUS_TEXT_0240_SUBMITTED_REJECTED = "Submitted(Rejected)";
	public static final String  ORDR_STATUS_TEXT_0300_SUBMITTED_CSR_REVIEWING = "Submitted(CSR Reviewing)";
	
	//Open 
	public static final String  ORDR_STATUS_TEXT_0400_OPEN = "Open";
	
	public static final String  ORDR_STATUS_TEXT_0450_DIRECT_FROM_MANUFACTURER = "Direct From Manufacturer";
	
	//Holds
	public static final String  ORDR_STATUS_TEXT_0500_CUSTOMER_HOLD = "Customer Hold";
	public static final String  ORDR_STATUS_TEXT_0600_SYSTEM_HOLD = "System Hold";
	public static final String  ORDR_STATUS_TEXT_0650_WEB_HOLD = "Web Hold";
	
	//Released
	public static final String  ORDR_STATUS_TEXT_0700_RELEASED_FOR_FULLFILLMENT = "Released for Fullfillment";
	public static final String  ORDR_STATUS_TEXT_0800_SHIPPED = "Shipped";
	public static final String  ORDR_STATUS_TEXT_0900_INVOICED = "Invoiced";

	
	
	/**
	 * ORDER LIST NUMBERS.
	 * 
	 * @return
	 */
	
	//Submitted
	public static final String  ORDR_STATUS_NBR_1100 							= "1100";
	public static final String  ORDR_STATUS_NBR_SUBMITTED 						= "1100.0100";
	public static final String  ORDR_STATUS_NBR_BACKORDER 						= "1100.5100";
	
	public static final String  ORDR_STATUS_NBR_SUBMITTED_PENDING_APPROVAL 		= "1100.5200";
	//public static final String  ORDR_STATUS_NBR_CANCELED 						= "1100.WXYZ";
	public static final String  ORDR_STATUS_NBR_SUBMITTED_REJECTED 				= "1100.5240";
	public static final String  ORDR_STATUS_NBR_SUBMITTED_CSR_REVIEWING 		= "1100.5300";
	
	//Open 
	public static final String  ORDR_STATUS_NBR_OPEN 							= "1100.5250";
	
	//public static final String  ORDR_STATUS_NBR_DIRECT_FROM_MANUFACTURER 		= "1100.5300";
	
	//Holds
	public static final String  ORDR_STATUS_NBR_CUSTOMER_HOLD 					= "1100.5350";
	public static final String  ORDR_STATUS_NBR_SYSTEM_HOLD 					= "1100.5400";
	public static final String  ORDR_STATUS_NBR_WEB_HOLD 						= "1100.5450";
	
	//Released
	public static final String  ORDR_STATUS_NBR_RELEASED_FOR_FULLFILLMENT 		= "1100.5500";
	public static final String  ORDR_STATUS_NBR_SHIPPED 						= "1100.5550";
	public static final String  ORDR_STATUS_NBR_INVOICED 						= "1100.5700";

	

	
}
