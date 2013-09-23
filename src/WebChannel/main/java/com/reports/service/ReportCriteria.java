
package com.reports.service;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for reportCriteria complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="reportCriteria">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="reportId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="reportPromptNameValue" type="{http://service.reports.com/}reportPromptNameValue" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="reportType" type="{http://service.reports.com/}reportTypeEnum" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "reportCriteria", propOrder = {
    "reportId",
    "reportPromptNameValue",
    "reportType"
})
public class ReportCriteria {

    protected int reportId;
    @XmlElement(nillable = true)
    protected List<ReportPromptNameValue> reportPromptNameValue;
    protected ReportTypeEnum reportType;

    /**
     * Gets the value of the reportId property.
     * 
     */
    public int getReportId() {
        return reportId;
    }

    /**
     * Sets the value of the reportId property.
     * 
     */
    public void setReportId(int value) {
        this.reportId = value;
    }

    /**
     * Gets the value of the reportPromptNameValue property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the reportPromptNameValue property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getReportPromptNameValue().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ReportPromptNameValue }
     * 
     * 
     */
    public List<ReportPromptNameValue> getReportPromptNameValue() {
        if (reportPromptNameValue == null) {
            reportPromptNameValue = new ArrayList<ReportPromptNameValue>();
        }
        return this.reportPromptNameValue;
    }
    
    public void setReportPromptNameValue(List<ReportPromptNameValue> reportPromptNameValue) {
    	this.reportPromptNameValue = reportPromptNameValue;
        
    }

    /**
     * Gets the value of the reportType property.
     * 
     * @return
     *     possible object is
     *     {@link ReportTypeEnum }
     *     
     */
    public ReportTypeEnum getReportType() {
        return reportType;
    }

    /**
     * Sets the value of the reportType property.
     * 
     * @param value
     *     allowed object is
     *     {@link ReportTypeEnum }
     *     
     */
    public void setReportType(ReportTypeEnum value) {
        this.reportType = value;
    }

}
