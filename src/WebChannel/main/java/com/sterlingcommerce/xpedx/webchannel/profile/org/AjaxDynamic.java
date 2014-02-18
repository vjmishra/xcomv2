package com.sterlingcommerce.xpedx.webchannel.profile.org;

import org.apache.struts2.ServletActionContext;
import org.json.JSONException;
import org.json.JSONObject;

import com.sterlingcommerce.webchannel.core.WCMashupAction;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper.CannotBuildInputException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import com.sterlingcommerce.xpedx.webchannel.profile.org.DynamicQueryAction;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper;
import com.sterlingcommerce.webchannel.utilities.WCMashupHelper.CannotBuildInputException;



public class AjaxDynamic extends WCMashupAction {


	private static final long serialVersionUID = 7756731378709085664L;
	DynamicQueryAction DaQueryAction =null;
	private String PrntChildComb = null;
	public String execute(){
		DaQueryAction = new DynamicQueryAction();
		HttpServletRequest request = ServletActionContext.getRequest();
	
		System.out.println("sapid 123 ::::" + request.getParameter("ParentCustomerId"));
		String sParentCustomerId = request.getParameter("ParentCustomerId");
		String name =request.getParameter("name");
		System.out.println("name"+name);
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/text;charset=utf-8");
		response.setHeader("cache-control", "no-cache");
		PrintWriter out;
		try {

			//DaQueryAction.getAuthorizedLocation(wcContext);
			PrntChildComb =	DaQueryAction.getChildCustomers(wcContext,sParentCustomerId);
			out = response.getWriter();
			out.println(PrntChildComb);
			//out.println(json.toString());
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CannotBuildInputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;

	}




	public void setPrntChildComb(String p_sPrntChildComb) {

		this.PrntChildComb = p_sPrntChildComb;
	}

	public String getPrntChildComb() {

		return PrntChildComb;
	}





}
