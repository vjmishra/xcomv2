<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<s:set name='isProcurementUser' value='wCContext.isProcurementUser()'/>
<s:if test='!#isProcurementUser'>
    <table >
      <tr>
        <td class="first"><a href="#"  tabindex="2501"><s:text name="link.copyRight"/></a></td>
        <td><a href="#"  tabindex="2502"><s:text name="link.about"/></a></td>
        <td><a href="#"  tabindex="2503"><s:text name="link.location"/></a></td>
        <td><a href="#"  tabindex="2504"><s:text name="link.term"/></a></td>
        <td><a href="#"  tabindex="2505"><s:text name="link.privacy"/></a></td>
        <td><a href="#"  tabindex="2505"><s:text name="link.siteMap"/></a></td>
        <td><a href="#"  tabindex="2507"><s:text name="link.feedback"/></a></td>
      </tr>
    </table>
</s:if>

