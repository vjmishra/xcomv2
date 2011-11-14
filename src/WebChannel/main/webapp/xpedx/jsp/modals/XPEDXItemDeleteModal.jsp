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
	<!-- <h2>Delete List</h2> -->
	<h2> <s:text name="MSG.SWC.MIL.DELMIL.GENERIC.DLGTITLE" /> </h2>
	<!-- <p>Are you sure you would like to delete this list?</p> -->
	<p><s:text name="MSG.SWC.MIL.DELMIL.GENERIC.CONFIRM" /></p>
	<br />
	<ul id="tool-bar" class="tool-bar-bottom">
		<li><a class="grey-ui-btn" href="javascript:$.fancybox.close()"><span>No</span></a></li>
		<li style="float: right;"><a class="green-ui-btn float-right"
			href="javascript:submitForm();"><span>Yes</span></a></li>
	</ul>
</div></s:div>
</div>