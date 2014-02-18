
package com.reports.service;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for reportTypeEnum.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="reportTypeEnum">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="pdf"/>
 *     &lt;enumeration value="excel"/>
 *     &lt;enumeration value="html"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "reportTypeEnum")
@XmlEnum
public enum ReportTypeEnum {

    @XmlEnumValue("pdf")
    PDF("pdf"),
    @XmlEnumValue("excel")
    EXCEL("excel"),
    @XmlEnumValue("html")
    HTML("html");
    private final String value;

    ReportTypeEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ReportTypeEnum fromValue(String v) {
        for (ReportTypeEnum c: ReportTypeEnum.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
