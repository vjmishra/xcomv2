package com.reports.service;

import com.crystaldecisions.sdk.exception.SDKException;
import com.crystaldecisions.sdk.framework.CrystalEnterprise;
import com.crystaldecisions.sdk.framework.IEnterpriseSession;
import com.crystaldecisions.sdk.framework.ISessionMgr;

public class ReportConnection {
	public static IEnterpriseSession getEnterpriseSession(String userName, String userPass, String CMS, String auth) throws SDKException
	{
		
		final ISessionMgr sessionMgr = CrystalEnterprise.getSessionMgr();
		IEnterpriseSession enterpriseSession = null;
		enterpriseSession= sessionMgr.logon(userName, userPass, CMS, auth);
		return enterpriseSession;
	}
}
