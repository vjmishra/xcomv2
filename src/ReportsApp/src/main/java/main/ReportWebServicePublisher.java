package main;

import javax.xml.ws.Endpoint;

import com.reports.service.ReportServiceImpl;

public class ReportWebServicePublisher {
	
	public static void main(String[] args) {
		Endpoint.publish("http://localhost:9994/ws/ReportService",new ReportServiceImpl());		 
	}


}
