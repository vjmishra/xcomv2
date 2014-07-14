package com.xpedx.nextgen.om.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.yantra.interop.client.ClientVersionSupport;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.shared.omp.OMPFactory;
import com.yantra.shared.ycp.YFSContext;
import com.yantra.yfs.japi.YFSEnvironment;

public class XPXPriceChangeOrderOnSuccessAPI {

	private static final Logger log = Logger
	.getLogger(XPXPriceChangeOrderOnSuccessAPI.class);
	private static YIFApi api = null;
	
	public Document onSuccess(YFSEnvironment env, Document inXML)
	{
		log.debug("Inside Change Order On success");
		Document inputDoc=null;
		String ischangeOrderInprogress=null;
		String isDiscountCalculate = null;
		String isCouponApply=null;
		if (env instanceof ClientVersionSupport) {
			ClientVersionSupport clientVersionSupport = (ClientVersionSupport) env;
			HashMap map = clientVersionSupport.getClientProperties();
			if (map != null) {
				inputDoc=(Document) map.get("changeOrderDocument");
				ischangeOrderInprogress=(String) map.get("isChangeOrderInProgress");
				isDiscountCalculate=(String)map.get("isDiscountCalculate");
				isCouponApply=(String)map.get("isCouponApply");
			}
		}
		setProgressYFSEnvironmentVariables(env);
		if(ischangeOrderInprogress == null && inputDoc != null && 
				(isDiscountCalculate ==null  || "true".equals(isDiscountCalculate)))
		{
				try
				{
					String inputXML = SCXmlUtil.getString(inputDoc);
					log.debug("inputXML for changeOrder : " + inputXML);
				
					env
							.setApiTemplate(
									"changeOrder",
									SCXmlUtil
											.createFromString(""
													+ "<OrderList><Order><OrderLines><OrderLine>"
													+ "<Extn></Extn>"
													+ "</OrderLine></OrderLines></Order></OrderList>"));
				
					api = YIFClientFactory.getInstance().getApi();
					// if coupons are available then change order gets failed because of cache . so removing the cache
					// before calling change order and adding again after change order in cache. 
					if(isCouponApply !=null && "true".equals(isCouponApply))
					{
						Element orderElement = inputDoc.getDocumentElement();
						String orderHeaderKey=orderElement.getAttribute("OrderHeaderKey");
						List orderHeadrPromotionList=OMPFactory.getInstance().getOrderHeader(getContext(env),orderHeaderKey,"","","", "").getPromotionList();
						ArrayList promotionList=new ArrayList(orderHeadrPromotionList);
						orderHeadrPromotionList.clear();
						api.invoke(env, "changeOrder", inputDoc);
						orderHeadrPromotionList.addAll(promotionList);
					}
					else
					{
						api.invoke(env, "changeOrder", inputDoc);
					}
					
					setChangeOrderDocumentYFSEnvironmentVariables(env);
				}
				catch(Exception e)
				{
					log.debug("Error while updating the order "+e );
				}
				
		}
		return inXML;
	}
	
	private YFSContext  getContext(YFSEnvironment env) {
		if ( env instanceof YFSContext )
			return (YFSContext)env;
		else
			return null;
	}
	
	private void setProgressYFSEnvironmentVariables(YFSEnvironment env) {
		if (env instanceof ClientVersionSupport) {
			ClientVersionSupport clientVersionSupport = (ClientVersionSupport) env;
			HashMap envVariablesmap = clientVersionSupport.getClientProperties();
			if (envVariablesmap != null) {
				envVariablesmap.put("isChangeOrderInProgress", "true");
			}
			else
			{
				 envVariablesmap = new HashMap();
				 envVariablesmap.put("isChangeOrderInProgress", "true");
			}
			
			clientVersionSupport.setClientProperties(envVariablesmap);
		}
	}
	private void setChangeOrderDocumentYFSEnvironmentVariables(YFSEnvironment env) {
		if (env instanceof ClientVersionSupport) {
			ClientVersionSupport clientVersionSupport = (ClientVersionSupport) env;
			HashMap envVariablesmap = clientVersionSupport.getClientProperties();
			if (envVariablesmap != null) {
				envVariablesmap.put("isChangeOrderInProgress", "true");
			}
			else
			{
				 envVariablesmap = new HashMap();
				 envVariablesmap.put("isChangeOrderInProgress", "true");
				 envVariablesmap.put("changeOrderDocument", null);
			}
			
			clientVersionSupport.setClientProperties(envVariablesmap);
		}
	}
}
