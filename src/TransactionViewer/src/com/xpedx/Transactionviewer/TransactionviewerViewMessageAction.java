/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xpedx.Transactionviewer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.jtds.jdbc.JtdsResultSet;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;


/**
 *
 * @author vasudsam
 */



public class TransactionviewerViewMessageAction implements ServletRequestAware
{
	private String selectedTransType;
	private HttpServletRequest request;
	private HashMap <String, String> messageMap = new HashMap<String,String>();
	private String transType;
	private ArrayList<String> messageIdList = new ArrayList<String>();
	
	
	public String execute() {
		System.out.println("Selected TransactionType :" + request.getParameter("transType"));
		transType = request.getParameter("transType");
		//StringTokenizer messageTypes = new StringTokenizer(request.getParameter("msgTypes"), ",");
		String messageIds = request.getParameter("msgIds");
		StringTokenizer msgIdTokenizer = new StringTokenizer(messageIds, ",");
		while(msgIdTokenizer.hasMoreTokens()) {
			getMessageIdList().add(msgIdTokenizer.nextToken());
		}
		
        Statement stmt = null;
        Connection conn = null;
        JtdsResultSet resultSet = null;
        try
        {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            conn = DriverManager.getConnection(
                    "jdbc:jtds:sqlserver://s02asqlxstg02.na.ipaper.com:1433/xcomlogging", "xcomview", "xcomv13w");

            StringBuffer query = new StringBuffer("SELECT StepInSequence,Message from  xcomMessages where xcomMessageId in( ");
            query.append(messageIds + " )");
            
            stmt = conn.createStatement();
            System.out.println("Query : " + query.toString());
            resultSet = (net.sourceforge.jtds.jdbc.JtdsResultSet) stmt.executeQuery(query.toString());
            
            while (resultSet.next())
            {
            	String stepInSequence = resultSet.getString(1);
            	String message = resultSet.getString(2);
            	messageMap.put(stepInSequence, message);
            }                 
            
            
        } catch (SQLException sqe)
        { 
            sqe.printStackTrace();		
        } catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
        	try{
        		if(resultSet != null) {
		            resultSet.close();
        		} 
        		   stmt.close();
		           conn.close();
        	} catch (SQLException ex) {
        		System.out.println("Error in closing DB connection");
        	}
        }
		return "success";
	}


	public void setTransType(String selectedTransType) {
		this.transType = selectedTransType;
	}


	public String getTransType() {
		return transType;
	}


	public void setServletRequest(HttpServletRequest request) {
		// TODO Auto-generated method stub
		this.request = request;
		
	}


	public void setMessageIdList(ArrayList<String> messageIdList) {
		this.messageIdList = messageIdList;
	}


	public ArrayList<String> getMessageIdList() {
		return messageIdList;
	}
	
	public String getMessage(String stepInSequence) {
		return messageMap.get(stepInSequence);
	}
}
