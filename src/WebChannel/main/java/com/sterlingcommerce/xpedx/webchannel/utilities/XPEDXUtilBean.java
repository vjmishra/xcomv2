/**
 * 
 */
package com.sterlingcommerce.xpedx.webchannel.utilities;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import com.sterlingcommerce.ui.web.framework.context.SCUIContext;
import com.sterlingcommerce.ui.web.framework.extensions.ISCUILocale;
import com.sterlingcommerce.webchannel.core.IWCContext;
import com.sterlingcommerce.webchannel.utilities.UtilBean;
import com.sterlingcommerce.webchannel.utilities.YfsUtils;
import com.yantra.yfc.date.YDate;
import com.yantra.yfc.util.YFCCommon;

/**
 * @author Administrator
 *
 */
public class XPEDXUtilBean extends UtilBean {

		/** Logger  */
	    private static final Logger log = Logger.getLogger(UtilBean.class);
	    
		private static int XPEDX_CURRENCY_PRECISION_TWO_PLACES = 2;
		private static int XPEDX_CURRENCY_PRECISION_FIVE_PLACES = 5;
		private static final String SHOW_CURRENCY_SYMBOL = "true";
		/**
		 * 
		 */
		public XPEDXUtilBean() {
			// TODO Auto-generated constructor stub
		}
		
		 /**
	     * Wrapper for Price and currency utility method for getting currency symbol with formatted price only.
	     * @param uiContext Web channel UI context
	     * @param currencyCode currency code.
	     * @param amount Amount to be formatted.
	     * @return String amount with currency symbol.
	     * @throws Exception
	     */

		public String formatPriceWithCurrencySymbol(SCUIContext ctx,String currencyCode, String amount) {
			   
			   		//Two decimal precision is default
				   return formatPriceWithCurrencySymbol(ctx, currencyCode, amount, SHOW_CURRENCY_SYMBOL);

		   }
		
		
	   public String formatPriceWithCurrencySymbol(SCUIContext ctx,String currencyCode, String amount, 
			   String showCurrencySymbol) {
		   
		   		//Two decimal precision is default
			   int decimalPrecisionString = getExtPriceDecimalPrecision ();

			   return formatPriceWithCurrancy(ctx, currencyCode, amount, showCurrencySymbol, decimalPrecisionString);

	   }
    
	    
	    /**
		    * Formats and returns a price string representing the given
		    * @currencyCode
		    * @amount
		    * @ctx
		    * @retrun string representing the price value
		    * @throws Exception
		    */
		   public String formatPriceWithCurrencySymbolWithPrecisionFive(SCUIContext ctx,String currencyCode, String amount,
		                               String showCurrencySymbol )   {
			   
			   int decimalPrecisionString = XPEDX_CURRENCY_PRECISION_FIVE_PLACES;
			   
		       return formatPriceWithCurrancy(ctx, currencyCode, amount,
					showCurrencySymbol, decimalPrecisionString);
		   }
		   
		   /**
		    * 
		    * @param uiContext
		    * @param currencyCode
		    * @param amount
		    * @return
		    * @throws Exception
		    */
		   public String formatPriceWithCurrencySymbolWithPrecisionFive(IWCContext uiContext, String currencyCode, String amount)
		        throws Exception{
			   //covert IWCContext to SCUIContext and call method
		        return formatPriceWithCurrencySymbolWithPrecisionFive(uiContext.getSCUIContext(), currencyCode, amount, "true");
		      }
		   
		 /**
		  *   
		  * @param ctx
		  * @param currencyCode
		  * @param amount
		  * @param showCurrencySymbol
		  * @param decimalPrecisionString
		  * @return
		  */
		private String formatPriceWithCurrancy(SCUIContext ctx,
				String currencyCode, String amount, String showCurrencySymbol, int decimalPrecisionString) {
			// If the currencyCodeString is null, then get the locale based default currency
		       String priceString = "";
		       if(currencyCode == null || currencyCode.length() < 1)
		       {
		           try
		           {
		               currencyCode = ctx.getUserPreferences().getLocale().getCurrency();
		           }
		           catch (Exception e)
		           {
		               log.error("Exception in getting default currency Code",e); /* TAG_NEW_LOGGING_CONVERTED from Global.logError */
		           }
		       }

		    	   
		       if(amount != null && amount.length() > 0)
		       {
		           double d = (Double.valueOf(amount)).doubleValue();
		           priceString = formatPrice(ctx,currencyCode, d, decimalPrecisionString, showCurrencySymbol);
		       }

		       return priceString;
		}
		
		/**
		 * 
		 * Overriding this method from YfsUtils since it wasn't having capability
		 * of rounding after given precision. JIRA # 2394
		 * 
		 * @param ctx
		 * @param currencyCode
		 * @param amount
		 * @param precision
		 * @param showCurrencySymbol
		 * @return
		 */
	    public String formatPrice(SCUIContext ctx, String currencyCode, double amount, int precision, String showCurrencySymbol)
	    {
	        java.util.Locale currLocale = ctx.getUserPreferences().getLocale().getJLocale();

	        DecimalFormat currencyFormatter = (DecimalFormat)DecimalFormat.getCurrencyInstance(currLocale);
	        if(precision >= 0)
	        {
	        	currencyFormatter.setRoundingMode(RoundingMode.HALF_UP);
	            currencyFormatter.setMinimumFractionDigits(precision);
	            currencyFormatter.setMaximumFractionDigits(precision);
	        }
	        if((showCurrencySymbol == null || !showCurrencySymbol.equalsIgnoreCase("false")) && currencyCode != null)
	        {
	            String currencySymbol[] = {
	                "", ""
	            };
	            try
	            {
	                currencySymbol = YfsUtils.getCurrencySymbol(currencyCode, ctx);
	            }
	            catch(Exception e)
	            {
	                DecimalFormatSymbols dfs = new DecimalFormatSymbols(currLocale);
	                currencySymbol[0] = dfs.getCurrencySymbol();
	            }
	            DecimalFormatSymbols dfs = new DecimalFormatSymbols(currLocale);
	            if(null != currencySymbol[0])
	                dfs.setCurrencySymbol(currencySymbol[0]);
	            else
	            if(null != currencySymbol[1])
	                dfs.setCurrencySymbol(currencySymbol[1]);
	            currencyFormatter.setDecimalFormatSymbols(dfs);
	        }
	        String formattedAmount = currencyFormatter.format(amount);
	        return formattedAmount;
	    }
		
		/**
	     * Returns the number of digits after decimal point.  Value is obtained
	     * from Business Rule if possible, otherwise the DEFAULT_DECIMAL_PRECISION
	     * is returned.
	     *
	     * @return default or the value set in the Business Rule.
	     */
	    public static int getExtPriceDecimalPrecision()
	    {
	       return XPEDX_CURRENCY_PRECISION_TWO_PLACES ;
	    }
	    
	    
	    /**
	     * Returns the number of digits after decimal point.  
	     *
	     * @return default or the value set in the Business Rule.
	     */
	    public static int getBracketPriceDecimalPrecision()
	    {
	       return XPEDX_CURRENCY_PRECISION_FIVE_PLACES ;
	    }
	    
	    
	    /**
	     * Returns the DayPhone.
	     * 
	     * Need to format like 905 595-4351 instead of 9055954351.
	     * If input String has more than 10 chars return same String.
	     */
    public  String getFormattedPhone(String phoneNumber)
	    {
    	String areaCode = "";
    	String middleThree = "";
    	String lastFour = "";
    	
    	//Return same String if condition is not met.
    	String fmtPhoneNumber = phoneNumber;
    	if(phoneNumber!=null){
    		phoneNumber = phoneNumber.replaceAll(" ", "");
    		phoneNumber = phoneNumber.replaceAll("-", "");
    	}
    	if (phoneNumber != null && phoneNumber.trim().length() == 10)
    	{
    		 areaCode  = phoneNumber.substring(0, 3);
     		 middleThree  = phoneNumber.substring(3, 6);
     		 lastFour  = phoneNumber.substring(6, 10);
     		 fmtPhoneNumber = areaCode+ " " + middleThree + "-" + lastFour;
    	}
    	
    	return fmtPhoneNumber;
    	
	    }
	    
	    
	    /*
	     * Strip Decimals for Ordered Quantity
	     */
	    
	    public String formatQuantityWithTrimmedDecimals(IWCContext webcontext, String quantityString)
	        throws Exception
	     {
		    String qtyValue = super.formatQuantity( webcontext,  quantityString);
		    String decimalStrippedQty  = qtyValue ;
		    
		    try {

		    int decimalStartIndex = qtyValue.indexOf(".");

		     decimalStrippedQty = qtyValue.substring(0, decimalStartIndex);
		     return  decimalStrippedQty ;
		    
	     } catch (Exception e) {
	    	 	log.debug("Exception in Stripping decimals in quantity",e);
				return decimalStrippedQty ;
			}
	    }
	    
		/*
	     * Converts to Comma formatted string.
	     * This handles the decimal part and NonDecimal part.
	     */
		public static String formatQuantityForCommas (String strOriginal ) {
			String nonDecimalQtyPart = strOriginal;
			String onlyDecimalQtyPart = "";
			
			if(strOriginal != null && strOriginal.length() > 0 )
			{
				strOriginal = strOriginal.trim();
			}
			else
			{
				strOriginal = "0";
			}
				
		  int decimalStartIndex = strOriginal.indexOf(".");
		  if (decimalStartIndex > 0 )
		  	{
			  nonDecimalQtyPart = strOriginal.substring(0, decimalStartIndex);
			  onlyDecimalQtyPart = strOriginal.substring( decimalStartIndex, strOriginal.length());
		  	}
		  else
		  	{ 
			  nonDecimalQtyPart = strOriginal;
			  onlyDecimalQtyPart = "";
		  	}
				  
					  
			//Format only Qty before decimal '.' then append decimal part.
			String formattedNonDecimalPart  = formatQtyForCommasWithoutDecimals(nonDecimalQtyPart) ;
			//System.out.println("formattedNonDecimalPart " + formattedNonDecimalPart);
			
			return formattedNonDecimalPart + onlyDecimalQtyPart;
		}

		/*
		 * This will not handle the decimal part.
		 */
		private static String formatQtyForCommasWithoutDecimals(String strOriginal) {
			try {
				char data[] = new char[2*strOriginal.length()]; //Needs some extra spaces for commas
					for(int k = 0; k < data.length; k++ )
						data[k]=' ';
				 
				for(int i = strOriginal.length()-1,j=0 ; i >= 0; i-- )
				{
					data[j++] = (strOriginal.charAt(i)) ;
					if( (j+1)%4 == 0 && i>0)
						data[j++] = ',' ;
				}
				
				String commaFmtString =  new StringBuffer(new String(data)).reverse().toString();
				
				return commaFmtString.trim() ;
			} catch (Exception e) {
				return strOriginal ;
			}
		}

		public String formatDate(String inputDate,  String inputFormat, String outputFormat)
	    {
	        
	        if(YFCCommon.isVoid(inputFormat))
	            inputFormat = "yyyy-MM-dd'T'HH:mm:ss";
	        if(YFCCommon.isVoid(outputFormat))
	            outputFormat = "yyyy-MM-dd'T'HH:mm:ss";
	        //YDate yDate = new YDate(inputDate, inputFormat, true);
	        SimpleDateFormat dateFormat=new SimpleDateFormat(outputFormat);
	        String date[]=inputDate.split("-");
	       // Date d=new Date(inputDate);
	       // dateFormat.format(d);
	       // String returnValue = yDate.getString(outputFormat);
	        
	        return date[1]+"/"+date[2]+"/"+date[0];
	    }
		public String formatDate(String inputDate,IWCContext wcContext,  String inputFormat, String outputFormat)
	    {
	    
			 ISCUILocale locale = wcContext.getSCUIContext().getUserPreferences().getLocale();
		        if(YFCCommon.isVoid(inputFormat))
		        {
		            inputFormat = DEFAULT_XML_DATE_FORMAT;
		        }

		        if(YFCCommon.isVoid(outputFormat))
		        {
		            outputFormat = locale.getDateFormat();
		        }

		        YDate yDate = new YDate(inputDate, inputFormat, true);
		        String dateTimes[]=inputDate.split("T");
		        if(dateTimes!=null && dateTimes.length >1)
		        {
		        	String timesStr=dateTimes[1];
		        	if(timesStr != null)
		        	{
		        		String times[]=dateTimes[1].split(":");
		        		if(times[0] != null)
		        			yDate.setHours(Integer.parseInt(times[0]));
		        		if(times[1] != null)
		        			yDate.setMinutes(Integer.parseInt(times[1]));
		        		if(times[2] != null)
		        		{
		        			String seconds[]=times[2].split("-");
		        			if(seconds !=null && seconds[0] !=null)
		        			yDate.setSeconds(Integer.parseInt(seconds[0]));
		        		}
		        	}
		        }
		        String returnValue = yDate.getString(outputFormat, locale.getJLocale());

		        return returnValue;
	    }
}
