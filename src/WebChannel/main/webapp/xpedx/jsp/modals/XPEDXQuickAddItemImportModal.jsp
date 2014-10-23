<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="swc" uri="swc"%>



<div style="display: none;">
	<div class="xpedx-light-box" id="dlgImportForm">
			<h1>Quick Add Import</h1>
			<s:form name="quickAddFileImport" id="quickAddFileImport" action="QuickAddImportPrepare"  namespace="/order" method="post" enctype="multipart/form-data">
				<span id="errorMsgForRequiredField" style="display:none;"></span>
				<div>
        		<p class="addmargintop0 addmarginbottom10">
        			Add to a cart by importing items from a comma separated value (CSV) file into Quick Add.
        			<br />
					Use xpedx item numbers or customer item numbers by selecting from the Select Item Type dropdown.
				</p>
				<div class="floatleft width120 textright gray italic">Example</div>
        		<div class="floatleft width120 textleft addmarginleft10 addmarginbottom10">
       	 			<ul class="bare">
              			<li>2001020,500,123456,44556</li>
              			<li>2001010,500,123456,44556</li>
             		 	<li>2001015,1000</li>
              			<li>2001008,500</li>   
        			</ul>  
        		</div>
        		<div class="clearfix"></div>
        		<p class="addmarginbottom10 small"><span class="bold">Please Note:</span> Quick Add cannot exceed 200 items. </p>
        		<hr class="shade"/>
       			<h4 class="addmargintop10 addmarginbottom10">Importing</h4>
       			<p class="addmarginbottom10">To import the file, locate the file on your hard drive by using the 'Browse' button. After the file is located, click 'Open' and then click the 'Import' button. </p>
       	
       			<s:file name="upload" size="50" id="File"/>
       	
       			<div id="errorMsgForBrowsePath" class="error error-msg-btm" style="display: none; width:120px;" >
					<s:text name="MSG.SWC.ITEM.LISTIMPORT.ERROR.FILEPATH" />
				</div>
       	
       			<p class="addmargintop10"> Items are imported in the same order they are listed in the file. </p>
       	
       			<div class="button-container addpadtop15">
					<input class="btn-gradient floatright addmarginright10" type="submit" value="Import" onclick="submitImportFile(); return false;" />
					<input class="btn-neutral floatright addmarginright10" type="submit" value="Cancel" onclick="$.fancybox.close(); return false;" />
				</div>
			
       			</div>
       	</s:form>
	</div> <%-- / dlgImportForm --%>
</div> <%-- / hidden div --%>
