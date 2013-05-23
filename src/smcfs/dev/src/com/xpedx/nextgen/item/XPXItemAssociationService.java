package com.xpedx.nextgen.item;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import au.com.bytecode.opencsv.CSVReader;

import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.interop.japi.YIFCustomApi;
import com.yantra.yfc.dom.YFCDocument;
import com.yantra.yfc.dom.YFCElement;
import com.yantra.yfc.dom.YFCNode;
import com.yantra.yfc.util.YFCCommon;
import com.yantra.yfs.japi.YFSEnvironment;

public class XPXItemAssociationService  implements YIFCustomApi {
	
	private static YIFApi api = null;
	private static Properties props;
	 String[] maxItemsIds;
	 private static String[] assoItemIds;
	/**
     * Reads text from a file line by line
     */
  public void setProperties(Properties arg0) throws Exception {
		// TODO Auto-generated method stub
		this.props = arg0;
	}
	
	
	public void generateAssociation(YFSEnvironment env, Document inputDoc){
		try
		{
			Element inputElem=inputDoc.getDocumentElement();
			String effectiveFrom=inputElem.getAttribute("EffectiveFrom");
			String effectiveTo=inputElem.getAttribute("EffectiveTo");
			String associationtype=inputElem.getAttribute("AssociationType");
			String organizationCode=inputElem.getAttribute("OrganizationCode");
			
			//File Read Code Start
			System.out.println("Xml Coming From API Tester"+SCXmlUtil.getString(inputDoc));
			
			//For calling  getCompleteItemList API inorder to get ItemKey of the items
			YFCDocument inputDocument = YFCDocument.createDocument("Item");
			YFCElement documentElement = inputDocument.getDocumentElement();
			documentElement.setAttribute("CallingOrganizationCode",organizationCode);
			YFCElement complexQueryElement = documentElement.createChild("ComplexQuery");
			YFCElement complexQueryOrElement = documentElement.createChild("Or");
			
			
				String fileName = inputElem.getAttribute("FilePath");//"D:/Kubra_Doc/Jira/White-Sprint/EB-317-DataloadScriptForACUItems/Book1.csv";
				CSVReader reader = new CSVReader(new FileReader(fileName));
				String[] nextLine;
				int i = -1;
				
				Map<String,String> itemMap=new HashMap<String,String>();
				while ((nextLine = reader.readNext()) != null) {
					System.out.println("In while loop");
					i++;
					if (i > 0){
						System.out.println("In IF Condn");
						if(!YFCCommon.isVoid(nextLine[1]))
						{
							itemMap.put(nextLine[0], nextLine[1]);
							YFCElement expElement = documentElement.createChild("Exp");
							expElement.setAttribute("Name", "ItemID");
							expElement.setAttribute("Value", nextLine[0]);							
							complexQueryOrElement.appendChild((YFCNode)expElement);
							
							YFCElement expElement1 = documentElement.createChild("Exp");
							expElement1.setAttribute("Name", "ItemID");
							expElement1.setAttribute("Value", nextLine[1]);							
							complexQueryOrElement.appendChild((YFCNode)expElement1);
						}
					}
				}//end of while
				complexQueryElement.appendChild(complexQueryOrElement);
				documentElement.appendChild(complexQueryElement);
				
			
			//File Read Code End

		api = YIFClientFactory.getInstance().getApi();
		env.setApiTemplate("getCompleteItemList", "<ItemList><Item ItemID='' ItemKey='' /> </ItemList>");
		Document outputDoc = api.invoke(env, "getCompleteItemList", inputDocument.getDocument());
		Element wElement = outputDoc.getDocumentElement();
		System.out.println(SCXmlUtil.getString(wElement));
		HashMap<String, String> itemKeyMap=  new HashMap<String, String>();
		if(wElement!= null){
			
			NodeList itemNodes=wElement.getElementsByTagName("Item");
			for(int ind=0;ind<itemNodes.getLength();ind++)
			{
				Element itemElem=(Element)itemNodes.item(ind);
				itemKeyMap.put(itemElem.getAttribute("ItemID"), itemElem.getAttribute("ItemKey"));
			}
		}
		
		
		Set<String> itemsSet=itemMap.keySet();
		/*<AssociationList ItemID="2001016" ItemKey="201107252310274927552" OrganizationCode="xpedx">
		<Association Action="Create" AssociationType="CrossSell" EffectiveFrom="13-05-20" EffectiveTo="13-06-30">
				<Item ItemID="2001018" ItemKey="201107220612341538531"/> 
				</Association>
		</AssociationList>*/
		//For Assosciation
		for(String item :itemsSet)
		{
			String associatedItem=itemMap.get(item);
			String itemKey=itemKeyMap.get(item);
			String associatedItemKey=itemKeyMap.get(associatedItem);
			YFCDocument associationinputDocument = YFCDocument.createDocument("AssociationList");
			YFCElement associationsinputElem=associationinputDocument.getDocumentElement();
			associationsinputElem.setAttribute("ItemID", item);
			associationsinputElem.setAttribute("ItemKey", itemKey);
			associationsinputElem.setAttribute("OrganizationCode", organizationCode);
			
			YFCElement associationinputElem=associationinputDocument.createElement("Association");
			associationinputElem.setAttribute("Action", "Create");
			associationinputElem.setAttribute("AssociationType", associationtype);
			associationinputElem.setAttribute("EffectiveFrom", effectiveFrom);
			associationinputElem.setAttribute("EffectiveTo", effectiveTo);
			associationsinputElem.appendChild(associationinputElem);
			YFCElement ItemElem=associationinputDocument.createElement("Item"); 
			ItemElem.setAttribute("ItemID", associatedItem);
			ItemElem.setAttribute("ItemKey", associatedItemKey);
			associationinputElem.appendChild(ItemElem);
			
			 api.invoke(env, "modifyItemAssociations", associationinputDocument.getDocument());
			System.out.println("!!!!!!!!!!!!We are done!!!!!!!!!!!!!!!!!!!");
		
		}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}

}
