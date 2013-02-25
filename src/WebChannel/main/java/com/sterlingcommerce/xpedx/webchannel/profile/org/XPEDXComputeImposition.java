package com.sterlingcommerce.xpedx.webchannel.profile.org;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XPEDXComputeImposition {

    // Declare Local Variables for Calculations
    private static double lnParWid;
    private static double lnParLen;
    private static double lnChldWid;
    private static double lnChldLen;
    private static double lnPlong;
    private static double lnPshort;
    private static double lnWlong;
    private static double lnflong;
    private static double lnfshort;
    private static double lnPArea;
    private static double lnFarea;
    private static double lnGripper;
    private static double lnColorb;
    private static double lnGutter;
    private static double lnSGuide;
    private static double nrPieces4;
    private static int nDown3;
    private static int nAcross3;
    private static int nDown2;
    private static int nSUnits = 0;
    private static int nAcross2;
    private static double nrAcross3;
    private static int nDown1;
    private static int nAcross4;
    private static int nDown4;
    private static double nSuTshort;
    private static int nAcross1;
    private static double nrDown3;
    private static double nrPieces3;
    private static double nSuVlong;
    private static double nSuVShort;
    private static double nrAcross4;
    private static double nrDown4;
    private static double lnrPieces;
    private static double nSuTLong;
    private static double lncolWaste;
    private static double lnrProws;
    private static double lnrPCols;
    private static final long lnVlong = 300;

    public static List<Map<String, String>> calcPage(String sheetSizeW,
	    String sheetSizeH, String trimSizeW, String trimSizeH,
	    String gripperWidth, String colorBarWidth, String sideGuide,
	    String gutter) {

	// Assign Parent Sheet from Object Call
	lnParWid = Double.parseDouble(sheetSizeH); // 2
	lnParLen = Double.parseDouble(sheetSizeW); // 1

	// Assign Child Size from Object Call
	lnChldWid = Double.parseDouble(trimSizeH); // 4
	lnChldLen = Double.parseDouble(trimSizeW); // 3

	// Assign additional factors from Object Call
	lnGripper = Double.parseDouble(gripperWidth); // 5
	lnColorb = Double.parseDouble(colorBarWidth); // 6
	lnGutter = Double.parseDouble(gutter); // 8
	lnSGuide = Double.parseDouble(sideGuide); // 7
	// Determine Parent Long and Short sides
	if (lnParWid >= lnParLen) {
	    lnPlong = lnParWid; // 2
	    lnPshort = lnParLen; // 1
	} else {
	    lnPlong = lnParLen;
	    lnPshort = lnParWid;
	}

	// Determine Child Long and Short sides
	if (lnChldWid >= lnChldLen) {
	    lnflong = lnChldWid; // 4
	    lnfshort = lnChldLen; // 3
	} else {
	    lnflong = lnChldLen;
	    lnfshort = lnChldWid;
	}

	// Calculate Areas
	lnPArea = lnPlong * lnPshort;
	lnFarea = lnflong * lnfshort;
	lnWlong = lnPlong;
	lnPshort = lnPshort - lnGripper - lnColorb; // 1 - 5 - 6 = -10
	lnPlong = lnPlong - lnGutter - lnSGuide; // 2 - 8 - 7 = -13

	// Calculate 1st Imposition
	int nNumout1 = (int) (Math.floor(lnPshort / lnfshort) * Math
		.floor(lnPlong / lnflong));
	int nWaste1 = (int) Math.floor(((lnPArea - (lnFarea * nNumout1))
		/ lnPArea * 100));
	System.out.println("lnPArea = " + lnPArea);

	nAcross1 = (int) Math.floor((lnPlong / lnflong));
	nDown1 = (int) Math.floor((lnPshort / lnfshort));

	// Calculate 2nd Imposition
	int nNumout2 = (int) (Math.floor(lnPshort / lnflong) * Math
		.floor(lnPlong / lnfshort));
	int nWaste2 = (int) Math.floor(((lnPArea - (lnFarea * nNumout2))
		/ lnPArea * 100));
	nAcross2 = (int) Math.floor((lnPlong / lnfshort));
	nDown2 = (int) Math.floor((lnPshort / lnflong));

	// Calculate 3rd Imposition
	nAcross3 = (int) Math.floor((lnPlong / lnflong));
	nDown3 = (int) Math.floor((lnPshort / lnfshort));
	double nRLong3 = lnPshort;
	double nRShort3 = lnPlong - (nAcross3 * lnflong);

	if (nRShort3 == 0 || nRShort3 < lnfshort) {
	    lnrPieces = 0;
	    lnrProws = 0;
	    lnrPCols = 0;
	} else {
	    lnrPieces = (int) Math.floor((nRLong3 / lnflong));
	    lncolWaste = (lnPlong - (lnflong * (int) Math
		    .floor((lnPlong / lnflong))));

	    lnrProws = (int) Math.floor((nRShort3 / lnfshort));
	    lnrPCols = (int) Math.floor((nRLong3 / lnflong));
	}

	nrPieces3 = lnrPieces;
	nrAcross3 = lnrProws;
	nrDown3 = lnrPCols;
	int nNumout3 = nNumout1 + (int) Math.floor((lnrProws * lnrPCols));
	int nWaste3 = (int) Math.floor(((lnPArea - (lnFarea * nNumout3))
		/ lnPArea * 100));

	// Calculate 4th Imposition
	nAcross4 = (int) Math.floor((lnPlong / lnfshort));
	nDown4 = (int) Math.floor((lnPshort / lnflong));
	double nRLong4 = lnPlong;
	double nRShort4 = lnPshort - (nDown4 * lnflong);

	if (nRShort4 == 0 || nRShort4 < lnfshort) {
	    lnrPieces = 0;
	    lnrPCols = 0;
	    lnrProws = 0;
	} else {
	    lnrPieces = (int) Math.floor((nRLong4 / lnflong));
	    lncolWaste = (lnPshort - (lnflong * (int) Math
		    .floor((lnPshort / lnflong))));
	    lnrProws = (int) Math.floor((nRLong4 / lnflong));
	    lnrPCols = (int) Math.floor((nRShort4 / lnfshort));
	}

	nrPieces4 = lnrPieces;
	nrAcross4 = lnrProws;
	nrDown4 = lnrPCols;
	int nNumout4 = nNumout2
		+ ((int) Math.floor((lnrProws)) * (int) Math.floor((lnrPCols)));
	int nWaste4 = (int) Math.floor(((lnPArea - (lnFarea * nNumout4))
		/ lnPArea * 100));

	if ((int) Math.floor(lnPlong) == 0) {
	    nSUnits = 0;
	} else {
	    // Scaled areas preformatted for HTML table width and height
	    nSUnits = (int) Math
		    .floor((lnVlong / lnPlong + lnSGuide + lnGutter));
	}

	// Long side of Parent Sheet
	nSuVlong = nSUnits * (lnPlong + lnSGuide + lnGutter);

	// Short Side of Parent Sheet
	nSuVShort = nSUnits * (lnPshort + lnGripper + lnColorb);

	// Length for Trim Sheet
	nSuTLong = nSUnits * lnflong;

	// Short Side of Trim Sheet
	nSuTshort = nSUnits * lnfshort;

	Map<String, String> numWaste1 = new HashMap<String, String>();
	numWaste1.put(Integer.toString(nNumout1), Integer.toString(nWaste1));

	Map<String, String> numWaste2 = new HashMap<String, String>();
	numWaste2.put(Integer.toString(nNumout2), Integer.toString(nWaste2));

	Map<String, String> numWaste3 = new HashMap<String, String>();
	numWaste3.put(Integer.toString(nNumout3), Integer.toString(nWaste3));

	Map<String, String> numWaste4 = new HashMap<String, String>();
	numWaste4.put(Integer.toString(nNumout4), Integer.toString(nWaste4));

	List<Map<String, String>> numWasteList = new ArrayList<Map<String, String>>();
	numWasteList.add(numWaste1);
	numWasteList.add(numWaste2);
	numWasteList.add(numWaste3);
	numWasteList.add(numWaste4);

	return numWasteList;
    }

    public static String drawPage(String option) {

	String cPassBack = null;
	int nAddin = 0;

	// Outer table draw and directional labels
	cPassBack = "<div id=\"requestform2\"> <TABLE Border=2 class=\"form\"><TR><TD style=\"border:none; padding:0px;\">";
	cPassBack = cPassBack + "<TABLE><TR><TD>&nbsp;</TD>";
	cPassBack = cPassBack
		+ "<TD ALIGN='CENTER'><IMG SRC='/swc/xpedx/images/pls.gif' WIDTH=200 HEIGHT=12 BORDER=0 ALT='Sheet Long Side'></TD>";
	cPassBack = cPassBack + "<TD></TD></TR><TR>";
	cPassBack = cPassBack
		+ "<TD><IMG SRC='/swc/xpedx/images/pss.gif' WIDTH=12 HEIGHT=200 BORDER=0 ALT='Sheet Short Side'></TD>";
	cPassBack = cPassBack
		+ "<TD><TABLE CELLSPACING=0 CELLPADDING=0 BORDER=1><TR>";

	// Add Side Guide if Exists
	if (lnSGuide > 0) {
	    cPassBack = cPassBack
		    + "<TD BGCOLOR='BLUE' WIDTH="
		    + (lnSGuide * nSUnits)
		    + "> <IMG SRC='/swc/xpedx/images/bpixel.gif' WIDTH=2 HEIGHT=2 BORDER=0 ALT=''></TD>";
	}

	// Add Gutter if Exists
	if (lnGutter > 0) {
	    cPassBack = cPassBack
		    + "<TD BGCOLOR='SILVER' WIDTH="
		    + (lnGutter * nSUnits)
		    + "> <IMG SRC='/swc/xpedx/images/bpixel.gif' WIDTH=2 HEIGHT=2 BORDER=0 ALT=''></TD>";
	}

	// Build Main Table
	switch (Integer.parseInt(option)) {
	// Layout Style 1
	case 1: // Draw Inner Parent Sheet
	    cPassBack = cPassBack + "<TD VALIGN='TOP' WIDTH=" + nSuVlong;
	    cPassBack = cPassBack + " HEIGHT=" + nSuVShort + " >";
	    cPassBack = cPassBack
		    + "<TABLE CELLSPACING=0 CELLPADDING=0 BORDER=1>";

	    for (int i = 1; i <= nDown1; i++) {
		cPassBack = cPassBack + "<TR>";
		for (int j = 1; j <= nAcross1; j++) {
		    cPassBack = cPassBack + "<TD BGCOLOR='FFFFD7' WIDTH="
			    + (int) Math.floor(nSuTLong);
		    cPassBack = cPassBack + " HEIGHT="
			    + (int) Math.floor(nSuTshort);
		    cPassBack = cPassBack
			    + "> <IMG SRC='/swc/xpedx/images/bpixel.gif' WIDTH=2 HEIGHT=2 BORDER=0 ALT=''></TD>";
		}
		cPassBack = cPassBack + "</TR>";
	    }

	    break;

	// Layout Style 2
	case 2:
	    // Draw Inner Parent Sheet
	    cPassBack = cPassBack + "<TD VALIGN='TOP' WIDTH=" + nSuVlong;
	    cPassBack = cPassBack + " HEIGHT=" + nSuVShort + " >";
	    cPassBack = cPassBack
		    + "<TABLE CELLSPACING=0 CELLPADDING=0 BORDER=1>";

	    for (int i = 1; i <= nDown2; i++) {
		cPassBack = cPassBack + "<TR>";
		for (int j = 1; j <= nAcross2; j++) {
		    cPassBack = cPassBack + "<TD BGCOLOR='FFFFD7' WIDTH="
			    + (int) Math.floor(nSuTshort);
		    cPassBack = cPassBack + " HEIGHT="
			    + (int) Math.floor(nSuTLong);
		    cPassBack = cPassBack
			    + "> <IMG SRC='/swc/xpedx/images/bpixel.gif' WIDTH=2 HEIGHT=2 BORDER=0 ALT=''></TD>";
		}
		cPassBack = cPassBack + "</TR>";
	    }

	    break;

	// Layout Style 3
	case 3:

	    // Draw Inner Parent Sheet
	    if ((int) Math.floor((lnWlong * nSUnits))
		    - (nAcross3 * nSUnits * nSuTLong)
		    - (nSUnits * nSuTshort * nrAcross3) < 8) {
		nAddin = 8;
	    } else {
		nAddin = 0;
	    }

	    cPassBack = cPassBack + "<TD VALIGN='TOP' WIDTH="
		    + (nSuVlong + nAddin);
	    cPassBack = cPassBack + " HEIGHT=" + nSuVShort + " >";
	    cPassBack = cPassBack
		    + "<TABLE CELLSPACING=0 CELLPADDING=0 BORDER=1 ALIGN='LEFT' VALIGN='TOP'>";

	    for (int i = 1; i <= nDown3; i++) {
		cPassBack = cPassBack + "<TR>";
		for (int j = 1; j <= nAcross3; j++) {
		    cPassBack = cPassBack + "<TD BGCOLOR='FFFFD7' WIDTH="
			    + (int) Math.floor(nSuTLong);
		    cPassBack = cPassBack + " HEIGHT="
			    + (int) Math.floor(nSuTshort);
		    cPassBack = cPassBack
			    + "> <IMG SRC='/swc/xpedx/images/bpixel.gif' WIDTH=2 HEIGHT=2 BORDER=0 ALT=''></TD>";
		}
		cPassBack = cPassBack + "</TR>";
	    }

	    if (nrPieces3 > 0) {
		cPassBack = cPassBack + "</TABLE>";
		cPassBack = cPassBack
			+ "<TABLE BORDER=1 CELLSPACING=0 CELLPADDING=0 ALIGN='LEFT' VALIGN='TOP'>";
		for (int k = 1; k <= (int) nrDown3; k++) {
		    cPassBack = cPassBack + "<TR>";
		    for (int l = 1; l <= (int) nrAcross3; l++) {
			cPassBack = cPassBack + "<TD BGCOLOR='FFFFD7' WIDTH="
				+ (int) Math.floor(nSuTshort);
			cPassBack = cPassBack + " HEIGHT="
				+ (int) Math.floor(nSuTLong);
			cPassBack = cPassBack
				+ "> <IMG SRC='/swc/xpedx/images/bpixel.gif' WIDTH=2 HEIGHT=2 BORDER=0 ALT=''></TD>";
		    }
		    cPassBack = cPassBack + "</TR>";
		}

	    }

	    break;

	// Layout Style 4
	case 4:
	    // Draw Inner Parent Sheet
	    cPassBack = cPassBack + "<TD VALIGN='TOP' WIDTH=" + nSuVlong;
	    cPassBack = cPassBack + " HEIGHT=" + nSuVShort + " >";
	    cPassBack = cPassBack
		    + "<TABLE CELLSPACING=0 CELLPADDING=0 BORDER=1>";

	    for (int i = 1; i <= nDown4; i++) {
		cPassBack = cPassBack + "<TR>";
		for (int j = 1; j <= nAcross4; j++) {
		    cPassBack = cPassBack + "<TD BGCOLOR='FFFFD7' WIDTH="
			    + (int) Math.floor(nSuTshort);
		    cPassBack = cPassBack + " HEIGHT="
			    + (int) Math.floor(nSuTLong);
		    cPassBack = cPassBack
			    + "> <IMG SRC='/swc/xpedx/images/bpixel.gif' WIDTH=2 HEIGHT=2 BORDER=0 ALT=''></TD>";
		}
		cPassBack = cPassBack + "</TR>";
	    }

	    if ((int) nrPieces4 > 0) {
		cPassBack = cPassBack + "</TABLE>";
		cPassBack = cPassBack
			+ "<TABLE BORDER=1 CELLSPACING=0 CELLPADDING=0 ALIGN='LEFT' VALIGN='TOP'>";
		for (int k = 1; k <= (int) nrDown4; k++) {
		    cPassBack = cPassBack + "<TR>";
		    for (int l = 1; l <= (int) nrAcross4; l++) {
			cPassBack = cPassBack + "<TD BGCOLOR='FFFFD7' WIDTH="
				+ (int) Math.floor(nSuTLong);
			cPassBack = cPassBack + " HEIGHT="
				+ (int) Math.floor(nSuTshort);
			cPassBack = cPassBack
				+ "> <IMG SRC='/swc/xpedx/images/bpixel.gif' WIDTH=2 HEIGHT=2 BORDER=0 ALT=''></TD>";
		    }
		    cPassBack = cPassBack + "</TR>";
		}
	    }

	    break;
	}

	cPassBack = cPassBack + "</TABLE></TD></TR>";

	// Calculate COLSPAN
	int nColSpan = 1;
	if (lnGutter > 0) {
	    nColSpan = nColSpan + 1;
	}
	if (lnSGuide > 0) {
	    nColSpan = nColSpan + 1;
	}

	// Build Color Bar if Exists
	if (lnColorb > 0) {
	    cPassBack = cPassBack
		    + "<TR><TD BGCOLOR='#00FF00' COLSPAN="
		    + nColSpan
		    + " HEIGHT="
		    + (int) Math.floor((lnColorb * nSUnits))
		    + "> <IMG SRC='/swc/xpedx/images/bpixel.gif' WIDTH=2 HEIGHT=2 BORDER=0 ALT=''></TD></TR>";
	}

	// Build Gripper if Exists
	if (lnGripper > 0) {
	    cPassBack = cPassBack
		    + "<TR><TD BGCOLOR='#FF0000' COLSPAN="
		    + nColSpan
		    + " HEIGHT="
		    + (int) Math.floor((lnGripper * nSUnits))
		    + "> <IMG SRC='/swc/xpedx/images/bpixel.gif' WIDTH=2 HEIGHT=2 BORDER=0 ALT=''></TD></TR>";
	}

	// Close up Table
	cPassBack = cPassBack + "</TABLE></TD><TD>&nbsp;</TD></TR>";
	cPassBack = cPassBack
		+ "<TR><TD>&nbsp;</TD><TD>&nbsp;</TD><TD>&nbsp;</TD></TR></TABLE></TD></TR></TABLE></div>";

	return cPassBack;

    }

}
