<%@ include file="incStartpage.jsp" %>
<%@ page import="com.crystaldecisions.sdk.framework.CrystalEnterprise,
                 com.crystaldecisions.sdk.properties.IProperties,
 com.crystaldecisions.sdk.properties.IProperty, 
                 com.crystaldecisions.sdk.occa.infostore.*,
                 com.crystaldecisions.sdk.framework.ISessionMgr,
                 com.crystaldecisions.sdk.framework.IEnterpriseSession,
                 com.crystaldecisions.sdk.plugin.CeKind,
                 com.crystaldecisions.celib.properties.*,                 
                 com.crystaldecisions.sdk.exception.SDKException,
                 java.net.URLDecoder,                 
                 com.businessobjects.rebean.wi.DocumentInstance,
                 com.businessobjects.rebean.wi.ReportEngine,
                 java.util.*,
                 java.net.URLEncoder,
                 com.businessobjects.rebean.wi.PropertiesType,
                 com.businessobjects.sdk.plugin.desktop.webi.IWebi"
%>
<%
response.setDateHeader("expires", 0);
if (!isAlive) return;
try
{
_logger.info("-->incSave.jsp");
    String strEntry = requestWrapper.getQueryParameter("sEntry", true);
_logger.info("strEntry = " + strEntry );
String strDocID = requestWrapper.getQueryParameter("id", false);
_logger.info("strDocID = " + strDocID );
String sIsNew = requestWrapper.getQueryParameter("isNew", false ,"false");
boolean isNew = Boolean.valueOf(sIsNew).booleanValue();
_logger.info("sIsNew = " + sIsNew );
DocumentInstance doc = reportEngines.getDocumentFromStorageToken(strEntry);
StringBuffer sb = new StringBuffer();
sb.append("var docPropsForSave = newDocPropsForSave(");
    String[] spropsToGet = {PropertiesType.NAME, PropertiesType.DESCRIPTION, PropertiesType.KEYWORDS};
   Properties props = doc.getProperties();
for (int i = 0; i < spropsToGet.length; i++)
{
        String s = props.getProperty(spropsToGet[i]);
_logger.info("spropsToGet["+i+"] = " + s);
        s = (s == null)?"":s;
        sb.append( "\"" + ViewerTools.escapeQuotes(s) + "\"" );
        if (spropsToGet.length > 0)
        sb.append(",");
    }
String[] bpropsToGet = {PropertiesType.REFRESH_ON_OPEN};
for (int j = 0; j < bpropsToGet.length; j++)
{
        String s = props.getProperty(bpropsToGet[j]);
_logger.info("bpropsToGet["+j+"] = " + s);
        s = (s == null)?"":s;        
        sb.append(Boolean.valueOf(s));
        if (bpropsToGet.length > 0)
            sb.append(",");
    }
    if (doc.getFormattingLocale() == DocumentLocaleType.BASELOCALE)
    {
sb.append("true");
_logger.info("getFormattingLocale() = DocumentLocaleType.BASELOCALE");
} else {
sb.append("false");
_logger.info("getFormattingLocale() = DocumentLocaleType.LOCALE");
}
IInfoStore infoStore = (IInfoStore) entSession.getService("InfoStore");
int favoritesFolderID = infoStore.getMyFavoritesFolder().getID();
IInfoObject obj = null;
IInfoObjects objs = null;
String sFolderID = null;
if (isNew) {
sFolderID = String.valueOf(favoritesFolderID);
_logger.info("Is a new document, use Folder ID=" + favoritesFolderID);
} else {
long d = new java.util.Date().getTime();
System.out.println("sub date 1=" + ((new java.util.Date().getTime()) - d));
String query = "SELECT SI_PARENT_FOLDER, SI_PERSONAL_CATEGORIES,SI_CORPORATE_CATEGORIES FROM CI_INFOOBJECTS WHERE SI_ID=" + strDocID;
objs = infoStore.query(query);
System.out.println("sub date 2=" + ((new java.util.Date().getTime()) - d));
if ((objs == null) || (objs.size() <= 0)) {
throw new Exception();
}
obj = (IInfoObject) objs.get(0);
if (obj == null) {
throw new Exception();
}
sFolderID = String.valueOf(obj.getParentID());
_logger.info("parentFolderID=" + sFolderID);
System.out.println("sub date 3=" + ((new java.util.Date().getTime()) - d));
}
sb.append("," + sFolderID + ");\n");
out.print(sb.toString());
int publicFolderID = CeSecurityID.Folder.ROOT;
out.println("publicFolderID=" + publicFolderID + ";");
_logger.info("publicFolderID=" + publicFolderID);
out.println("favoritesFolderID=" + favoritesFolderID + ";");
_logger.info("favoritesFolderID=" + favoritesFolderID);
int personalCategoriesID = infoStore.getMyPersonalCategory().getID();
out.println("personalCategoriesID=" + personalCategoriesID + ";");
int corporateCategoriesID = CeSecurityID.Folder.CORPORATE_CATEGORIES;
out.println("corporateCategoriesID=" + corporateCategoriesID + ";");
int[] persCatIds = null;
int[] corpCatIds = null;
int persCatCount = 0;
int corpCatCount = 0;
if (!isNew) {
    persCatIds = getCategories(_logger, obj, true);
    persCatCount= persCatIds.length;    
    corpCatIds = getCategories(_logger, obj, false);
    corpCatCount = corpCatIds.length;
}
out.println("_persCatIds = new Array;");
out.println("_corpCatIds = new Array;");
    for (int i = 0; i < persCatCount; i++){
    String output = "_persCatIds[" + i + "]=" + persCatIds[i] + ";"; 
    _logger.info(output);
    out.println(output);        
    }
    for (int i = 0; i < corpCatCount; i++){
    String output = "_corpCatIds[" + i + "]=" + corpCatIds[i] + ";"; 
    _logger.info(output);
    out.println(output);       
    }    
out.println("var incSaveOK=true;");
_logger.info("<--incSave.jsp");
objs = null;
obj = null;
infoStore = null;
}
catch(Exception e)
{
objUtils.incErrorMsg(e, out);
}
%>
<%!
private int[] getCategories(DHTMLLogger _logger, IInfoObject obj, boolean isPersonnal)
throws SDKException
{
if (obj == null) return null;
Integer catType = isPersonnal?CePropertyID.SI_PERSONAL_CATEGORIES:CePropertyID.SI_CORPORATE_CATEGORIES;
Property prop = (Property) obj.properties().getProperty(catType);
_logger.info("catType:" +  catType);
PropertyBag propBag = (PropertyBag) prop.getPropertyBag();
_logger.info("Categories propBag:" +  propBag);
    int count = propBag.size() - 1; 
    _logger.info("count:" + count);
   int[] categories = new int[count];    
    for (int i = 0; i < count; i++) {
Integer propId = new Integer(i+1);
    _logger.info("propId:" + propId);
    int propValue = propBag.getInt(propId);
        _logger.info("propValue:" + propValue);
        if (propValue != 0) { 
        categories[i] = propValue;
        }
   }    
return categories;
} 
private int getUserFoldersID(DHTMLLogger _logger, IInfoStore iStore) 
throws SDKException
{
    String query = "SELECT SI_ID FROM CI_INFOOBJECTS WHERE SI_NAME='User Folders'";
    _logger.info("SQL: " + query);
    IInfoObjects objects = iStore.query(query);
    _logger.info("Found: "+objects.size()+" objects.");
    int usersFolder = -1;
    if (objects.size()>0) {
    usersFolder = ((IInfoObject) objects.get(0)).getID();
    }
return usersFolder;
}
private int getUserFolderID(DHTMLLogger _logger, IInfoStore iStore, String strUserName, int usersFolder) 
throws SDKException
{
     String query = "SELECT SI_ID FROM CI_INFOOBJECTS WHERE SI_NAME='" + strUserName + "' AND SI_PARENTID=" + usersFolder;    
 _logger.info("SQL: " + query);
 IInfoObjects objects = iStore.query(query);
     _logger.info("Found: "+objects.size()+" objects.");
int userFolderID = -1;
    if (objects.size()>0) {
    userFolderID = ((IInfoObject) objects.get(0)).getID();
    }        
return userFolderID;
}
%>