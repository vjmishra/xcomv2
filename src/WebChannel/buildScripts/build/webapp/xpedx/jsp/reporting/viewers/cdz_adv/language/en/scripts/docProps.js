/*
=============================================================
WebIntelligence(r) Report Panel
Copyright(c) 2001-2005 Business Objects S.A.
All rights reserved

Use and support of this software is governed by the terms
and conditions of the software license agreement and support
policy of Business Objects S.A. and/or its subsidiaries. 
The Business Objects products and technology are protected
by the US patent number 5,555,403 and 6,247,008

=============================================================
*/function newDocProps(name,auth,desc,keyw,lasRefD,lastRefDur,loc,title,ver,
prevVer,bEnhView,bRefOnOpen,bUseQuery,bMergeDim,bFormat)
{
var o=new Object
o.name=name
o.author=auth
o.description=desc
o.keywords=keyw
o.lastRefreshDate=lasRefD
o.lastRefreshDuration=lastRefDur
o.locale=loc
o.title=title
o.version=ver
o.previousVersion=prevVer
o.bEnhancedViewing=bEnhView
o.bRegionalFormatting=bFormat
o.bRefreshOnOpen=bRefOnOpen
o.bUseQueryDrill=bUseQuery
o.bMergeDimensions=bMergeDim
return o
}
