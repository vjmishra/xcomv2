
package com.reports.service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.6 in JDK 6
 * Generated source version: 2.1
 * 
 */
@WebService(name = "ReportService", targetNamespace = "http://service.reports.com/")
@SOAPBinding(style = SOAPBinding.Style.RPC)
@XmlSeeAlso({
    ObjectFactory.class
})
public interface ReportService {


    /**
     * 
     * @return
     *     returns com.reports.service.ReportList
     */
    @WebMethod
    @WebResult(partName = "return")
    public ReportList getReports();

    /**
     * 
     * @param arg0
     * @return
     *     returns com.reports.service.ReportData
     */
    @WebMethod
    @WebResult(partName = "return")
    public ReportData executeReport(
        @WebParam(name = "arg0", partName = "arg0")
        ReportCriteria arg0);

}