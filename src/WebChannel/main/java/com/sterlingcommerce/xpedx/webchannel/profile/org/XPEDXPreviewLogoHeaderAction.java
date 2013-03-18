package com.sterlingcommerce.xpedx.webchannel.profile.org;

import java.io.File;
import java.net.MalformedURLException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.sterlingcommerce.ui.web.framework.context.SCUIContext;
import com.sterlingcommerce.webchannel.core.WCAction;
import com.sterlingcommerce.webchannel.core.WCAttributeScope;
import com.sterlingcommerce.webchannel.utilities.SSLSwitchingHelper;
import com.yantra.yfs.core.YFSSystem;

public class XPEDXPreviewLogoHeaderAction extends WCAction implements
		ServletRequestAware {

	public XPEDXPreviewLogoHeaderAction() {
		logoURL = null;
		custLogoUrl = null;
		request = null;
		previewFileName = null;
	}

	public String getPreviewFileName() {
		return previewFileName;
	}

	public void setPreviewFileName(String previewFileName) {
		this.previewFileName = previewFileName;
	}

	public String getCustLogoUrl() {
		return custLogoUrl;
	}

	public void setCustLogoUrl(String custLogoUrl) {
		this.custLogoUrl = custLogoUrl;
	}

	public String getLogoURL() {
		return logoURL;
	}

	public void setLogoURL(String logoURL) {
		this.logoURL = logoURL;
	}

	public String getSecureLoginURL() {
		return SSLSwitchingHelper.generateURL(wcContext, "https", "home",
				"loginFullPage", null);
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	private String getSFLogoDir() {
		String sfDir = "";
		if (wcContext != null)
			sfDir = wcContext.getStorefrontId();
		return sfDir;
	}

	private String getSFTheme() {
		String sfTheme = "";
		if (wcContext != null)
			sfTheme = wcContext.getStorefrontThemeId();
		return sfTheme;
	}

	private void setupLogoURL() {
		String logo = (String) wcContext.getWCAttribute("SF_THEME_LOGO",
				WCAttributeScope.LOCAL_SESSION);
		if (logo != null && logo.length() > 0) {
			LOG.debug((new StringBuilder())
					.append("Found Logo URL in the context=").append(logo)
					.toString());
			setLogoURL(logo);
			return;
		}
		ServletContext servletCtx = ServletActionContext.getServletContext();
		String baseLogoURL = (new StringBuilder())
				.append(request.getContextPath()).append("/swc/images/logo/")
				.append(getSFLogoDir()).toString();
		logo = (new StringBuilder()).append(baseLogoURL).append("/logo.gif")
				.toString();
		String theme = getSFTheme();
		if (servletCtx != null && theme != null && theme.length() > 0) {
			String themeLogoURL = (new StringBuilder())
					.append("/swc/images/logo/").append(getSFLogoDir())
					.append("/logo-").append(theme).append(".gif").toString();
			LOG.debug((new StringBuilder())
					.append("Checking for theme specific logo [")
					.append(themeLogoURL).append("]").toString());
			try {
				java.net.URL url = servletCtx.getResource(themeLogoURL);
				LOG.debug((new StringBuilder())
						.append("URL from servletContext=").append(url)
						.toString());
				if (url != null) {
					LOG.debug((new StringBuilder())
							.append("Using theme based LogoURL [").append(url)
							.append("]").toString());
					logo = (new StringBuilder()).append(baseLogoURL)
							.append("/logo-").append(theme).append(".gif")
							.toString();
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
		LOG.debug((new StringBuilder()).append("Logo URL =[").append(logo)
				.append("]").toString());
		setLogoURL(logo);
		wcContext.setWCAttribute("SF_THEME_LOGO", logo,
				WCAttributeScope.LOCAL_SESSION);
	}

	private void setupCustLogoURL() {
		String path = request.getContextPath() + relPreivewLogoDir;
		String customerId = getWCContext().getCustomerId();
		this.previewFileName = request.getParameter("custLogoFile");
		this.custLogoUrl = path + customerId + "_" + previewFileName;
	}

	public String deletePreviewLogo() {
		try {
			SCUIContext uictx = getWCContext().getSCUIContext();
			String filePath = uictx.getServletContext().getRealPath("/")
					+ File.separator + absPreivewLogoDir;
			String customerId = getWCContext().getCustomerId();
			String filename = request.getParameter("fileName");

			filePath = filePath + customerId + "_" + filename;
			File theFile = new File(filePath);

			FileUtils.forceDelete(theFile);

		} catch (Exception e) {

			addActionError(e.getMessage());

			return INPUT;

		}

		return SUCCESS;
	}

	@Override
	public String execute() {
		setupLogoURL();
		setupCustLogoURL();

		return "success";
	}

	static {
		String prevpath = YFSSystem.getProperty("previewlogo.path");
		if (prevpath == null) {
			prevpath = "swc/images/PreviewLogo";
		}
		prevpath = prevpath + File.separator;
		relPreivewLogoDir = File.separator + prevpath;
		absPreivewLogoDir = prevpath;
	}
	String logoURL;
	String custLogoUrl;
	String previewFileName;
	private HttpServletRequest request;
	private static final long serialVersionUID = 1L;
	private static String relPreivewLogoDir;
	private static String absPreivewLogoDir;

}
