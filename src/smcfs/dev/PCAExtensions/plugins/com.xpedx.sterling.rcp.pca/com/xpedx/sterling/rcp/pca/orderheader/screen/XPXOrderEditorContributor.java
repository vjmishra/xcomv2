package com.xpedx.sterling.rcp.pca.orderheader.screen;

import org.eclipse.swt.widgets.Composite;

import com.yantra.yfc.rcp.IYRCRelatedTasksExtensionContributor;
import com.yantra.yfc.rcp.YRCEditorInput;
import com.yantra.yfc.rcp.YRCRelatedTask;

/**
 * 
 * Author Manasa Mohapatro
 *
 */
public class XPXOrderEditorContributor implements
IYRCRelatedTasksExtensionContributor{

	public boolean acceptTask(YRCEditorInput yrceditorinput,
			YRCRelatedTask yrcrelatedtask) {
		String inputTaskId = yrcrelatedtask.getId();
System.out.println("MaNasa is here"+inputTaskId);
		//if the customer is a Master customer only then the Create customer contact link is displayed.
		//We cannot create customer contacts at any other level than MSAP level.
		if(inputTaskId.equals("YCD_ORDER_INVOICE"))
		{
			
				return false;
		}
		return true;
	}

	public boolean canExecuteNewTask(YRCEditorInput yrceditorinput,
			YRCRelatedTask yrcrelatedtask) {
		// TODO Auto-generated method stub
		return false;
	}

	public Composite createPartControl(Composite composite,
			YRCEditorInput yrceditorinput, YRCRelatedTask yrcrelatedtask) {
		// TODO Auto-generated method stub
		return null;
	}

}
