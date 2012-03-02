package com.xpedx.Transactionviewer;

import java.sql.Connection;
import java.sql.DriverManager;
import net.sourceforge.jtds.jdbc.JtdsResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.opensymphony.xwork2.ActionSupport;


public class TransactionviewerAction extends ActionSupport
{
	
	    private String WebconfTextboxName = ""; // default value
	    private String TransactionTypeDropdownName = ""; // default value
	    private String TimeRangeDropDownName = "";
	    private String DateTextBoxName = "";
	    private JtdsResultSet resultSet = null;
        private Statement stmt = null;
        private Connection conn = null;	    
        private HashMap<String,XPEDXTransaction> xcomMessageIdMap ;
	   
	    public void setConn(Connection conn) {
			this.conn = conn;
		}
		public Connection getConn() {
			return conn;
		}
		public void setStmt(Statement stmt) {
			this.stmt = stmt;
		}
		public Statement getStmt() {
			return stmt;
		}
		public void setResultSet(JtdsResultSet resultSet)
	    {
	        this.resultSet = resultSet;
	    }
	    public JtdsResultSet getResultSet()
	    {
	        return this.resultSet;
	    }
	    
	     public void setWebconfTextboxName(String value)
	    {
	        this.WebconfTextboxName = value;
	        System.out.println("Web conf num in form = " + value);
	    }
	    public String getWebconfTextboxName()
	    {
	        return this.WebconfTextboxName;
	    }
	    
	    public void setTransactionTypeDropdownName(String value)
	    {
	        this.TransactionTypeDropdownName = value;
	    }
	    public String getTransactionTypeDropdownName()
	    {
	        return this.TransactionTypeDropdownName;
	    }   
	    
	    public void setTimeRangeDropDownName(String value)
	    {
	        this.TimeRangeDropDownName = value;
	    }
	    
	    public String getTimeRangeDropDownName()
	    {
	        return this.TimeRangeDropDownName;
	    }

	    public void setDateTextBoxName(String value)
	    {
	        this.DateTextBoxName = value;
	    }
	    public String getDateTextBoxName()
	    {
	        return this.DateTextBoxName;
	    }
	    
	public String execute() {
		System.out.println("Inside the action");
		System.out.println("Web conf number = " + getWebconfTextboxName());
		
		
		System.out.println("From SubmitAction111");
        //SubmitForm sform = (SubmitForm) form;
        String webconfnumber = getWebconfTextboxName();
        System.out.println("webconfnumber is " + webconfnumber);
        //JtdsResultSet resultSet = null;
        Statement stmt = null;
        Connection conn = null;
        try
        {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            conn = DriverManager.getConnection(
                    "jdbc:jtds:sqlserver://s02asqlxstg02.na.ipaper.com:1433/xcomlogging", "xcomview", "xcomv13w");

            StringBuffer query = new StringBuffer("SELECT xcomMessageId, TransactionName, BusinessIdentifier,"
                    + " Business_Transaction_Timestamp, StepInSequence, Message FROM xcomMessages WHERE ");
            query.append(" BusinessIdentifier like" + "'%" + webconfnumber + "%'");
            
            String transactionDate = getDateTextBoxName();
            if(!(transactionDate == null || transactionDate.trim().length() == 0))
            {
                query.append(" and CONVERT(varchar, CONVERT(datetime, left(business_Transaction_Timestamp,10))"+
                    ", 101) = convert(datetime, ' " + getDateTextBoxName() + "', 101)");
            }

            String transactionName = getTransactionTypeDropdownName();
            if (!transactionName.equalsIgnoreCase("--All Transactions--"))
            {
                query.append(" and TransactionName = '" + transactionName + "'");
            }
            
            String timerange = getTimeRangeDropDownName();
            if (!timerange.equalsIgnoreCase("--All Times--"))
            {
                query.append(" and SUBSTRING(business_Transaction_Timestamp,12,13)like" + "'%" + getTimeRangeDropDownName() + "%'");
  
               /* query.append(Business_Transaction_Timestamp > 'sform.getCtl00$MainContent$DateTextBox sform.getCtl00$MainContent$TimeRangeDropDown' )*/
                
            }

            stmt = conn.createStatement();
            System.out.println("Query : " + query.toString());
            resultSet = (net.sourceforge.jtds.jdbc.JtdsResultSet) stmt.executeQuery(query.toString());
            System.out.println("resultSet111 is " + resultSet + " " + resultSet.getRow());

            String businessTransactionTimestamp = "";
            setXcomMessageIdMap(new HashMap<String,XPEDXTransaction>());
            LinkedHashMap stepInSequenceMap = new LinkedHashMap();
            List transactionTypeList = new ArrayList();
            List webConfirmationNumberList = new ArrayList();
            while (resultSet.next())
            {
            	XPEDXTransaction objTransaction = null;
                businessTransactionTimestamp = resultSet.getString(4);
                System.out.println("BT : "+ businessTransactionTimestamp);
                if(getXcomMessageIdMap().containsKey(businessTransactionTimestamp))
                {
                        //String oldValue = (String)xcomMessageIdMap.get(businessTransactionTimestamp);
                        //xcomMessageIdMap.put(businessTransactionTimestamp, oldValue + "," + resultSet.getString(1));
                	objTransaction = getXcomMessageIdMap().get(businessTransactionTimestamp);
                }
                else
                {
                		objTransaction = new XPEDXTransaction();
                		System.out.println("Creating new object");
                		objTransaction.setBusinessTimestamp(businessTransactionTimestamp);
                		objTransaction.setWebConfNumber(resultSet.getString(3));
                		objTransaction.setTransactionType(resultSet.getString(2));
                        getXcomMessageIdMap().put(businessTransactionTimestamp, objTransaction);
                        //transactionTypeList.add(resultSet.getString(2)); /*transactionType*/
                        //webConfirmationNumberList.add(resultSet.getString(3)); /*webConfirmationNumber*/
                }
                objTransaction.addMessage(resultSet.getString(5), resultSet.getString(1));
            }     
            

        }
        catch (ClassNotFoundException cnfe)
        {
            cnfe.printStackTrace();
        } catch (SQLException sqe)
        { 
            sqe.printStackTrace();		
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
	public void setXcomMessageIdMap(HashMap<String,XPEDXTransaction> xcomMessageIdMap) {
		this.xcomMessageIdMap = xcomMessageIdMap;
	}
	public HashMap<String,XPEDXTransaction> getXcomMessageIdMap() {
		return xcomMessageIdMap;
	}
	
}