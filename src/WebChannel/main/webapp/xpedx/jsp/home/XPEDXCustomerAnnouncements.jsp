<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="swc" uri="/WEB-INF/swc.tld" %>



<s:set name='_action' value='[0]' />
<s:set name="xutil" value="XMLUtils" />
<s:bean name='com.sterlingcommerce.webchannel.utilities.UtilBean' id='dateUtilBean'/>
<s:bean name='com.sterlingcommerce.xpedx.webchannel.utilities.XPEDXWCUtils' id='util' />

   <!-- <h2>My Company News</h2> -->
   <h2> <s:text name="MSG.SWC.NEWSARTL.MYCOMPANYNEWS.GENERIC.PGTITLE" /> </h2>
   <div class="news-scrollable">
	   <s:set name="iter" value="articleLines"/>
	   <ul class="news-links">
           <s:iterator value='articleLines' id='articleLine' status="articleLineCount">
				<s:set name="StartDate" value='%{#dateUtilBean.formatDate(#xutil.getAttribute(#articleLine,"StartDate"),wCContext)}' />
				<s:set name="EndDate" value='%{#dateUtilBean.formatDate(#xutil.getAttribute(#articleLine,"EndDate"),wCContext)}' />
				<s:set name="ArticleName" value='#xutil.getAttribute(#articleLine,"ArticleName")' />
				<s:set name="ArticleKey" value='#xutil.getAttribute(#articleLine,"ArticleKey")' />
				<s:set name="Article" value='#xutil.getAttribute(#articleLine,"Article")' />
				<s:set name="Forced" value='#xutil.getAttribute(#articleLine,"ForcedMessage")' />
				<s:set name="ModifiedUserId" value='#xutil.getAttribute(#articleLine,"Modifyuserid")' />	
					<li>	
			        	<a href="<s:url namespace='/profile/user' action='xpedxReadArticle'><s:param name="articleKey" 
                			value='%{#ArticleKey}' /></s:url>" class="underlink">
			        		<s:property value='%{#ArticleName}'/>
			        	</a>
			        </li>
					
					<%--Hemantha  
			        <div id="articleLine_<s:property value='#articleLineCount.count'/>" style="display: none;" >
				        <!-- p><s:property value='%{#Article}'/></p -->
				        <p><a href="<s:url namespace='/home' action='xpedxLearnMoreArticle'/>" class="hp-learn-more">Learn More »</a></p>			        
				        <div style="height:25px; margin-right:10px; float:right; margin-left:65px; width:75px;">
			            	<img style="padding-right:10px;" class="float-left" src="<s:property value='#util.staticFileLocation' />/xpedx/images/home/hp-announce-left-arrow.gif" 
			            		width="13" height="14" alt="Previous" onclick="javascript:showPrevious('<s:property value='#articleLineCount.count'/>','<s:property value='articleLines.size'/>')"/>
				            <span class="checkboxtxt"><s:property value='#articleLineCount.count'/> of <s:property value='articleLines.size'/></span> 
				            <img class="float-right"  src="<s:property value='#util.staticFileLocation' />/xpedx/images/home/hp-announce-right-arrow.gif" 
				            	width="13" height="14" alt="Next" onclick="javascript:showNext('<s:property value='#articleLineCount.count'/>','<s:property value='articleLines.size'/>')"/> </div>
			        </div>
			        --%>
           </s:iterator>
		</ul>
	</div>

<script type="text/javascript">
	
	/*
	function onLoad(){
		var articleSize = '<s:property value="articleLines.size"/>';
		if(articleSize != null && articleSize != '0')
			{
			var firstArticle = document.getElementById('articleLine_1');
			if(firstArticle.style.display == 'none')
				firstArticle.style.display = 'block';
			}
		}

	function showPrevious(presentArticle, articlesNo){
		if(presentArticle != '1')
		{
			var prevArticle = presentArticle-1;
			var presArticleDiv = document.getElementById('articleLine_'+presentArticle);
			presArticleDiv.style.display = 'none';
			var prevArticleDiv = document.getElementById('articleLine_'+prevArticle);
			prevArticleDiv.style.display ='block';
		}
		
		}
	function showNext(presentArticle, articlesNo){
		if(presentArticle != articlesNo )
			{
				var presArticle = presentArticle;
				var nextArticle = ++presArticle;
				var presArticleDiv = document.getElementById('articleLine_'+presentArticle);
				presArticleDiv.style.display = 'none';
				var nextArticleDiv = document.getElementById('articleLine_'+nextArticle);
				nextArticleDiv.style.display ='block';
			}
		}

	onLoad();
	*/
</script>
