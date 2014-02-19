<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="swc"%>

<script type="text/javascript">
	function closeWindow(){
		$.fancybox.close();
	}
	
	function submitForm(){	
		var form = Ext.get("doAction_delete_item_list");
		form.dom.submit();
		$.fancybox.close();
		return;
	}
</script>

<style>
	.share-modal { width:399px!important; height:370px;;}         
	.indent-tree { margin-left:15px; }       
	.indent-tree-act { margin-left:25px; } 
	   
	.xpedx-light-box h2 {
	    border-bottom: 1px solid #CCCCCC;
	    color: #002F75;
	    font-size: 16px;
	    font-weight: normal;
	    margin: 0 0 0px;
	    padding: 0 0 0px;
	}
</style>

<div style="display: none;">
	<s:div id="delete_my_item_list">
		<div class="xpedx-light-box" style="width:250px; height:150px;">
			<h2> <s:text name="MSG.SWC.MIL.DELMIL.GENERIC.DLGTITLE" /> </h2>
			<p><s:text name="MSG.SWC.MIL.DELMIL.GENERIC.CONFIRM" /></p>
			
			<div class="button-container addpadtop15">
				<input class="btn-gradient floatright addmarginright10" type="submit" value="Yes" onclick="submitForm(); return false;" />
				<input class="btn-neutral floatright addmarginright10" type="submit" value="No" onclick="$.fancybox.close(); return false;" />
			</div>
		</div> <%-- / xpedx-light-box --%>
	</s:div> <%-- / delete_my_item_list --%>
</div> <%-- / hidden div --%>
