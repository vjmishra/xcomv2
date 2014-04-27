package com.sterlingcommerce.xpedx.webchannel.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DownloadSampleServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doGet (HttpServletRequest request,HttpServletResponse response) 
			throws ServletException,IOException  {
		try{   
			PrintWriter out = response.getWriter();
			String filename = getServletContext().getRealPath("/sample/SampleImport.csv");

			   // set the http content type to "APPLICATION/OCTET-STREAM
			   response.setContentType("APPLICATION/OCTET-STREAM");

			   // initialize the http content-disposition header to
			   // indicate a file attachment with the default filename
			   // "SampleImport.csv"
			   String disHeader = "Attachment; Filename=\"SampleImport.csv\"";
			   response.setHeader("Content-Disposition", disHeader);

			   // transfer the file byte-by-byte to the response object
			   File fileToDownload = new File(filename);
			   FileInputStream fileInputStream = new FileInputStream(fileToDownload);
			   int i;
			   while ((i=fileInputStream.read())!=-1)
			   {
			      out.write(i);
			   }
			   fileInputStream.close();
			   out.close();
			}catch(Exception e) // file IO errors
			{
			e.printStackTrace();
			} 
	}

}
