package com.sterlingcommerce.xpedx.webchannel.utilities;

import java.util.Comparator;
/**
 * @author Nimesh
 * The compare method that compares the alphanumeric strings
 */

public class XPEDXAlphanumericSorting implements Comparator {
	
	   
	    public int compare(Object firstObjToCompare, Object secondObjToCompare) {
	        String firstString = firstObjToCompare.toString();
	        String secondString = secondObjToCompare.toString();
	 
	        if (secondString == null || firstString == null) {
	            return 0;
	        }
	 
	        int lengthFirstStr = firstString.length();
	        int lengthSecondStr = secondString.length();
	 
	        int index1 = 0;
	        int index2 = 0;
	 
	        while (index1 < lengthFirstStr && index2 < lengthSecondStr) {
	            char ch1 = firstString.charAt(index1);
	            char ch2 = secondString.charAt(index2);
	 
	            char[] space1 = new char[lengthFirstStr];
	            char[] space2 = new char[lengthSecondStr];
	 
	            int loc1 = 0;
	            int loc2 = 0;
	 
	            do {
	                space1[loc1++] = ch1;
	                index1++;
	 
	                if (index1 < lengthFirstStr) {
	                    ch1 = firstString.charAt(index1);
	                } else {
	                    break;
	                }
	            } while (Character.isDigit(ch1) == Character.isDigit(space1[0]));
	 
	            do {
	                space2[loc2++] = ch2;
	                index2++;
	 
	                if (index2 < lengthSecondStr) {
	                    ch2 = secondString.charAt(index2);
	                } else {
	                    break;
	                }
	            } while (Character.isDigit(ch2) == Character.isDigit(space2[0]));
	 
	            String str1 = new String(space1);
	            String str2 = new String(space2);
	 
	            int result;
	 
	            if (Character.isDigit(space1[0]) && Character.isDigit(space2[0])) {
	                Double firstNumberToCompare = new Double(Double
	                        .parseDouble(str1.trim()));
	                Double secondNumberToCompare = new Double(Double
	                        .parseDouble(str2.trim()));
	                result = firstNumberToCompare.compareTo(secondNumberToCompare);
	            } else {
	                result = str1.compareTo(str2);
	            }
	 
	            if (result != 0) {
	                return result;
	            }
	        }
	        return lengthFirstStr - lengthSecondStr;
	    }

}
