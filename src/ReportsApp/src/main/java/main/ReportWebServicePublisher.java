package main;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.xml.ws.Endpoint;

import com.reports.service.ReportServiceImpl;

public class ReportWebServicePublisher {

	private String getUrl() {
		// Bring the value from properties file
		Properties properties = new Properties();
		InputStream is = this.getClass().getClassLoader()
				.getResourceAsStream("reports_app.properties");
		try {
			properties.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return properties.getProperty("deployedUrl");
	}

	public static void main(String[] args) {

		ReportWebServicePublisher p = new ReportWebServicePublisher();
		String urlLocation = p.getUrl();
		Endpoint.publish(urlLocation, new ReportServiceImpl());
	}

}
