
package com.reports.service;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for reportList complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="reportList">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="custReportList" type="{http://service.reports.com/}report" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="stdReportList" type="{http://service.reports.com/}report" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "reportList", propOrder = {
    "custReportList",
    "stdReportList"
})
public class ReportList {

    @XmlElement(nillable = true)
    protected List<Report> custReportList;
    @XmlElement(nillable = true)
    protected List<Report> stdReportList;

    /**
     * Gets the value of the custReportList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the custReportList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCustReportList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Report }
     * 
     * 
     */
    public List<Report> getCustReportList() {
        if (custReportList == null) {
            custReportList = new ArrayList<Report>();
        }
        return this.custReportList;
    }

    /**
     * Gets the value of the stdReportList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the stdReportList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStdReportList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Report }
     * 
     * 
     */
    public List<Report> getStdReportList() {
        if (stdReportList == null) {
            stdReportList = new ArrayList<Report>();
        }
        return this.stdReportList;
    }

}
