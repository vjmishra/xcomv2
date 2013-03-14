package com.sterlingcommerce.xpedx.webchannel.profile.org;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.sterlingcommerce.ui.web.framework.context.SCUIContext;
import com.sterlingcommerce.webchannel.core.WCMashupAction;
import com.yantra.yfs.core.YFSSystem;

public class XPEDXCustomerLogoAction extends WCMashupAction {

	/**
     * 
     */
	private static final long serialVersionUID = -4731751794963371357L;
	/**
	 * Logger for this class
	 */
	private static final Logger LOG = Logger
			.getLogger(XPEDXCustomerLogoAction.class);
	private File upload;// The actual file
	private String uploadFileName; // The uploaded file name
	private String filePath;
	private String absfilePath;
	private String previewFilePath;
	private String custLogoUrl;
	private static String absCustLogoDir;
	private static String relCustLogoDir;
	private static String absPreivewLogoDir;
	static {
		String custpath = YFSSystem.getProperty("customerlogo.path");
		if (custpath == null) {
			custpath = "swc/images/CustomerLogo";
		}
		custpath = custpath + File.separator;
		String prevpath = YFSSystem.getProperty("previewlogo.path");
		if (prevpath == null) {
			prevpath = "swc/images/PreviewLogo";
		}
		prevpath = prevpath + File.separator;

		absCustLogoDir = custpath;
		relCustLogoDir = File.separator + custpath;
		absPreivewLogoDir = prevpath;
	}

	public String uploadLogo() {
		try {

			SCUIContext uictx = getWCContext().getSCUIContext();
			absfilePath = uictx.getServletContext().getRealPath("/")
					+ File.separator + absCustLogoDir;
			String ctxtpath = request.getContextPath();
			filePath = ctxtpath + relCustLogoDir
					+ request.getParameter("customerId") + "_" + uploadFileName;
			absfilePath = absfilePath + request.getParameter("customerId")
					+ "_" + uploadFileName;
			File theFile = new File(absfilePath);

			FileUtils.copyFile(upload, theFile);

			prepareAndInvokeMashups();

		} catch (Exception e) {

			addActionError(e.getMessage());

			return ERROR;

		}
		return SUCCESS;

	}

	public String deleteLogo() {
		try {
			String relLogoUrl = request.getParameter("logoUrl");
			int index = relLogoUrl.lastIndexOf(File.separator);
			String fname = relLogoUrl.substring(index + 1);

			SCUIContext uictx = getWCContext().getSCUIContext();
			absfilePath = uictx.getServletContext().getRealPath("/")
					+ absCustLogoDir;
			absfilePath = absfilePath + fname;
			File theFile = new File(absfilePath);
			FileUtils.forceDelete(theFile);

			filePath = "";
			prepareAndInvokeMashups();
		} catch (Exception e) {
			addActionError(e.getMessage());

			return ERROR;

		}

		return SUCCESS;
	}

	public String previewLogo() {
		try {

			SCUIContext uictx = getWCContext().getSCUIContext();
			filePath = uictx.getServletContext().getRealPath("/")
					+ File.separator + absPreivewLogoDir;

			previewFilePath = filePath + request.getParameter("customerId")
					+ "_" + uploadFileName;

			request.getContextPath();

			File theFile = new File(previewFilePath);

			FileUtils.copyFile(upload, theFile);

			// theFile.delete();

		} catch (Exception e) {

			addActionError(e.getMessage());

			return ERROR;

		}

		return SUCCESS;

	}

	public String viewLogo() {
		try {
			custLogoUrl = request.getParameter("logoUrl");

		} catch (Exception e) {

			addActionError(e.getMessage());

			return ERROR;

		}

		return SUCCESS;
	}

	public String getCustLogoUrl() {
		return custLogoUrl;
	}

	public void setCustLogoUrl(String custLogoUrl) {
		this.custLogoUrl = custLogoUrl;
	}

	public File getUpload() {
		return upload;
	}

	public void setUpload(File upload) {
		this.upload = upload;
	}

	public String getPreviewFilePath() {
		return previewFilePath;
	}

	public void setPreviewFilePath(String previewFilePath) {
		this.previewFilePath = previewFilePath;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getUploadFileName() {
		return uploadFileName;
	}

	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}

}
