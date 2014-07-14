package com.xpedx.sterling.rcp.pca.referenceorder.screen;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.PlatformUI;
import org.w3c.dom.Element;

import com.yantra.yfc.rcp.YRCBehavior;
import com.yantra.yfc.rcp.YRCEditorInput;
import com.yantra.yfc.rcp.YRCPlatformUI;
import com.yantra.yfc.rcp.YRCXmlUtils;

public class XPXRefOrderHeaderPanelBehavior extends YRCBehavior {

	private XPXRefOrderHeaderPanel page;
	private Element inputElement;
	private Element eleOrderDetails;

	public XPXRefOrderHeaderPanelBehavior(Composite ownerComposite, String formId, Object inputObject, Element eleOrderDetails) {
        super(ownerComposite, formId, inputObject);
        this.page = (XPXRefOrderHeaderPanel) ownerComposite;
        this.eleOrderDetails = eleOrderDetails;
        this.inputElement = ((YRCEditorInput) inputObject).getXml();
        setModel("OrderDetails",eleOrderDetails);
        Element eleCustList = page.getOrderLinesPanel().getPageBehavior().getCustomerInfo();
        Element eleCustInfo = YRCXmlUtils.getChildElement(eleCustList, "Customer");
        setModel("CustomerDetails", eleCustInfo);

        setDirty(false);
    }
	public Element getInputElement(){
		return this.inputElement;
	}
	
	public void openDetailEditor(String linkName)
    {
        Element newCustomerElement = prepareEditorInput(linkName);;
        boolean isCustomerEditorFound = false;
        IEditorReference editorReferences[] = PlatformUI.getWorkbench ().getActiveWorkbenchWindow().getActivePage().getEditorReferences();
        for(int i = 0; i < editorReferences.length; i++)
        {
            try
            {
                org.eclipse.ui.IEditorInput tEditorInput = editorReferences [i].getEditorInput();
                org.eclipse.ui.IEditorPart tEditor = editorReferences [i].getEditor(false);
                
                if(tEditor == null || !("com.yantra.pca.ycd.rcp.editors.YCDCustomerEditor".equals(tEditor.getClass().getName())))
                    continue;
                Element customerEditorInput = ((YRCEditorInput)tEditorInput).getXml();
                if(YRCPlatformUI.isVoid(customerEditorInput)|| !YRCPlatformUI.equals(newCustomerElement.getAttribute("CustomerKey"),customerEditorInput.getAttribute("CustomerKey")))
                    continue;
                YRCPlatformUI.openEditor ("com.yantra.pca.ycd.rcp.editors.YCDCustomerEditor",(YRCEditorInput)tEditorInput);
                isCustomerEditorFound = true;
                break;
            }
            catch(Exception e)
            {
                YRCPlatformUI.trace("Exception occured while opening Customer Details window.:", e);
            }
        }

        if(!isCustomerEditorFound)
            YRCPlatformUI.openEditor(
            		"com.yantra.pca.ycd.rcp.editors.YCDCustomerEditor", 
            		new YRCEditorInput (newCustomerElement, new String[]{"CustomerKey"}, "YCD_TASK_CUSTOMER_DETAILS"));
	
	}
	
	private Element prepareEditorInput(String linkName)
	{
		Element eleCustomerElement = getModel("CustomerDetails");
		Element eleInput = YRCXmlUtils.getCopy(eleCustomerElement, false);
		eleInput.setAttribute("CustomerType","01");
	    return eleInput;
	}
	
	}
