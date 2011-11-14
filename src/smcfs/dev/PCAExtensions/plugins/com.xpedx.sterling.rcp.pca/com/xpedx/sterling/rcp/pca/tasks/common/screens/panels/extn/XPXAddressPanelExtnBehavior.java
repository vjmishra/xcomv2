package com.xpedx.sterling.rcp.pca.tasks.common.screens.panels.extn;

import com.yantra.yfc.rcp.YRCExtentionBehavior;
import com.yantra.yfc.rcp.YRCTextBindingData;

public class XPXAddressPanelExtnBehavior extends YRCExtentionBehavior {

	public Object getBindingData(String fieldName) {
		if(fieldName.equalsIgnoreCase("extn_CustomAddress")){
			YRCTextBindingData txtAddressDisplay1bd = new YRCTextBindingData();
			txtAddressDisplay1bd.setName("txtAddressDisplay1");
			txtAddressDisplay1bd.setSourceBinding("address:/PersonInfo/@AddressLine1;address:/PersonInfo/@AddressLine2;address:/PersonInfo/@AddressLine3;address:/PersonInfo/@City;address:/PersonInfo/@State;address:/PersonInfo/@ZipCode;address:/PersonInfo/@Country");
			txtAddressDisplay1bd.setKey("xpedx_BillTo_address_key");
			return txtAddressDisplay1bd;
		}
		return super.getBindingData(fieldName);
	}
	
	@Override
	public void postSetModel(String s) {
		// TODO Auto-generated method stub
		super.postSetModel(s);
	}
}
