
package com.reports.service;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for reportData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="reportData">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="binaryReport" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="boeErrorMessages" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="htmlReport" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="missingMandatoryPrompts" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "reportData", propOrder = {
    "binaryReport",
    "boeErrorMessages",
    "htmlReport",
    "missingMandatoryPrompts"
})
public class ReportData {

    protected byte[] binaryReport;
    @XmlElement(nillable = true)
    protected List<String> boeErrorMessages;
    protected String htmlReport;
    @XmlElement(nillable = true)
    protected List<String> missingMandatoryPrompts;

    /**
     * Gets the value of the binaryReport property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getBinaryReport() {
        return binaryReport;
    }

    /**
     * Sets the value of the binaryReport property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setBinaryReport(byte[] value) {
        this.binaryReport = ((byte[]) value);
    }

    /**
     * Gets the value of the boeErrorMessages property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the boeErrorMessages property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBoeErrorMessages().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getBoeErrorMessages() {
        if (boeErrorMessages == null) {
            boeErrorMessages = new ArrayList<String>();
        }
        return this.boeErrorMessages;
    }

    /**
     * Gets the value of the htmlReport property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHtmlReport() {
        return htmlReport;
    }

    /**
     * Sets the value of the htmlReport property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHtmlReport(String value) {
        this.htmlReport = value;
    }

    /**
     * Gets the value of the missingMandatoryPrompts property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the missingMandatoryPrompts property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMissingMandatoryPrompts().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getMissingMandatoryPrompts() {
        if (missingMandatoryPrompts == null) {
            missingMandatoryPrompts = new ArrayList<String>();
        }
        return this.missingMandatoryPrompts;
    }

}
